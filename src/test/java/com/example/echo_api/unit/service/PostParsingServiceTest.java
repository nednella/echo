package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.persistence.model.post.entity.PostHashtag;
import com.example.echo_api.persistence.model.post.entity.PostMention;
import com.example.echo_api.service.post.util.PostParsingService;
import com.example.echo_api.service.post.util.PostParsingServiceImpl;

/**
 * Unit test class for {@link PostParsingService}.
 */
@ExtendWith(MockitoExtension.class)
class PostParsingServiceTest {

    @InjectMocks
    private PostParsingServiceImpl postParsingService;

    private static final UUID postId = UUID.randomUUID();

    @Test
    void PostParsingService_Parse_ThrowsIllegalArgumentException() {
        // arrange
        String text = null;

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> postParsingService.parse(postId, text));
    }

    @Test
    void PostParsingService_Parse_ContainsOnlyWhitespace() {
        // arrange
        String text = "          ";

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(0, entities.get("hashtags").size());
        assertEquals(0, entities.get("mentions").size());
    }

    @Test
    void PostParsingService_Parse_ContainsOnlyLineBreak() {
        // arrange
        String text = "\n";

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(0, entities.get("hashtags").size());
        assertEquals(0, entities.get("mentions").size());
    }

    @Test
    void PostParsingService_Parse_ContainsNoEntities() {
        // arrange
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vitae ex quis purus pharetra porttitor sed eu massa. Nunc vel diam at arcu pulvinar porttitor eget at felis. Pellentesque sagittis magna id sem accumsan malesuada. Maecenas porta blandit posuere.";

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertTrue(entities.get("hashtags").isEmpty());
        assertTrue(entities.get("mentions").isEmpty());
    }

    @Test
    void PostParsingService_Parse_ContainsMultipleValidEntities() {
        // arrange
        String text = "Hi @john_doe, @admin(:D) and @test! Cool #Java #SpringBoot application! #dev #test";

        var expectedHashtags = List.of(
            new PostHashtag(postId, 41, 46, "#Java"),
            new PostHashtag(postId, 47, 58, "#SpringBoot"),
            new PostHashtag(postId, 72, 76, "#dev"),
            new PostHashtag(postId, 77, 82, "#test"));

        var expectedMentions = List.of(
            new PostMention(postId, 3, 12, "@john_doe"),
            new PostMention(postId, 14, 20, "@admin"),
            new PostMention(postId, 29, 34, "@test"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(4, entities.get("hashtags").size());
        assertTrue(entities.get("hashtags").containsAll(expectedHashtags));
        assertEquals(3, entities.get("mentions").size());
        assertTrue(entities.get("mentions").containsAll(expectedMentions));
    }

    @Test
    void PostParsingService_Parse_ContainsValidHashtagAtStart() {
        // arrange
        String text = "#Hello!      whitespace #valid, but#invalid.";

        var expectedHashtags = List.of(
            new PostHashtag(postId, 0, 6, "#Hello"),
            new PostHashtag(postId, 24, 30, "#valid"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(2, entities.get("hashtags").size());
        assertTrue(entities.get("hashtags").containsAll(expectedHashtags));
    }

    @Test
    void PostParsingService_Parse_ContainsValidMentionAtStart() {
        // arrange
        String text = "@Hello!      whitespace @valid, but@invalid.";

        var expectedMentions = List.of(
            new PostMention(postId, 0, 6, "@Hello"),
            new PostMention(postId, 24, 30, "@valid"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(2, entities.get("mentions").size());
        assertTrue(entities.get("mentions").containsAll(expectedMentions));
    }

    @Test
    void PostParsingService_Parse_ContainsValidHashtagAtEnd() {
        // arrange
        String text = "This is a test string with a valid hashtag at the #end";

        var expectedHashtags = List.of(new PostHashtag(postId, 50, 54, "#end"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(1, entities.get("hashtags").size());
        assertTrue(entities.get("hashtags").containsAll(expectedHashtags));
    }

    @Test
    void PostParsingService_Parse_ContainsValidMentionAtEnd() {
        // arrange
        String text = "This is a test string with a valid mention at the @end";

        var expectedMentions = List.of(new PostMention(postId, 50, 54, "@end"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(1, entities.get("mentions").size());
        assertTrue(entities.get("mentions").containsAll(expectedMentions));
    }

    @Test
    void PostParsingService_Parse_ContainsValidHashtagAfterLineBreak() {
        // arrange
        String text = "#Hello! \n#NewLineValidHashtag, testing...";

        var expectedHashtags = List.of(
            new PostHashtag(postId, 0, 6, "#Hello"),
            new PostHashtag(postId, 9, 29, "#NewLineValidHashtag"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(2, entities.get("hashtags").size());
        assertTrue(entities.get("hashtags").containsAll(expectedHashtags));
    }

    @Test
    void PostParsingService_Parse_ContainsValidMentionAfterLineBreak() {
        // arrange
        String text = "@admin! \n@valid_user1, testing...";

        var expectedMentions = List.of(
            new PostMention(postId, 0, 6, "@admin"),
            new PostMention(postId, 9, 21, "@valid_user1"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(2, entities.get("mentions").size());
        assertTrue(entities.get("mentions").containsAll(expectedMentions));
    }

    @Test
    void PostParsingService_Parse_ContainsValidHashtagsBeforeAndAfterPunctuation() {
        // arrange
        String text = "#HashtagFollowedByComma, .#InvalidHashtag but, #ValidHashtag.";

        var expectedHashtags = List.of(
            new PostHashtag(postId, 0, 23, "#HashtagFollowedByComma"),
            new PostHashtag(postId, 47, 60, "#ValidHashtag"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(2, entities.get("hashtags").size());
        assertTrue(entities.get("hashtags").containsAll(expectedHashtags));
    }

    @Test
    void PostParsingService_Parse_ContainsValidMentionsBeforeAndAfterPunctuation() {
        // arrange
        String text = "Mention followed by a @Comma, .@InvalidMention but, @ValidMention.";

        var expectedMentions = List.of(
            new PostMention(postId, 22, 28, "@Comma"),
            new PostMention(postId, 52, 65, "@ValidMention"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(2, entities.get("mentions").size());
        assertTrue(entities.get("mentions").containsAll(expectedMentions));
    }

    @Test
    void PostParsingService_Parse_ContainsExtremelyLongValidHashtag() {
        // arrange
        String text = "This text contains a #VeryVeryVeryLongHashtagWith_SomeAdditionalWord_Characters_And_Numbers_123_But_No_Non_Word_Characters_";

        var expectedHashtags = List.of(
            new PostHashtag(postId, 21, 123,
                "#VeryVeryVeryLongHashtagWith_SomeAdditionalWord_Characters_And_Numbers_123_But_No_Non_Word_Characters_"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(1, entities.get("hashtags").size());
        assertTrue(entities.get("hashtags").containsAll(expectedHashtags));
    }

    @Test
    void PostParsingService_Parse_MaxLengthValidMention() {
        // arrange
        String text = "Hello @valid_len_12345, and @invalid_len_1234";

        var expectedMentions = List.of(new PostMention(postId, 6, 22, "@valid_len_12345"));

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(1, entities.get("mentions").size());
        assertTrue(entities.get("mentions").containsAll(expectedMentions));
    }

    @Test
    void PostParsingService_Parse_ContainsOnlyInvalidHashtags() {
        // arrange
        String text = "@Hi @john_doe@john_doe! @username_too_long_ @u$ername @te^st @invalid@ @  @@  @@@";

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(0, entities.get("hashtags").size());
    }

    @Test
    void PostParsingService_Parse_ContainsOnlyInvalidMentions() {
        // arrange
        String text = "#Yo# #Invalid#Hashtags #$ #^   #*  #  ##  ###";

        // act
        var entities = postParsingService.parse(postId, text);

        // assert
        assertEquals(0, entities.get("mentions").size());
    }

}
