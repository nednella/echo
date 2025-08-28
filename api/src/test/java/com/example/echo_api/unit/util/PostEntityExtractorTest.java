package com.example.echo_api.unit.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.persistence.model.post.entity.PostEntity;
import com.example.echo_api.persistence.model.post.entity.PostEntityType;
import com.example.echo_api.util.PostEntityExtractor;

/**
 * Unit test class for {@link PostEntityExtractor}.
 */
@ExtendWith(MockitoExtension.class)
class PostEntityExtractorTest {

    private static final UUID mockPostId = UUID.randomUUID();

    @Test
    void extract_ThrowsIllegalArgument_WhenTextIsNull() {
        // arrange
        String text = null;

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> PostEntityExtractor.extract(mockPostId, text));
    }

    @Test
    void extract_IgnoresCashtags_WhenTextCotainsCashtags() {
        // arrange
        String text = "$btc!      $cshtag  $AAPL, $etc";

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        System.out.println(entities);

        // assert
        assertNotNull(entities);
        assertTrue(entities.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "          ", // only whitespace chars
        "\n\n", // newline chars
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vitae ex quis purus pharetra.", // basic text
        "#Yo# #Invalid#Hashtags #$ #^   #*  #  ##  ###", // invalid hashtags
        "@# @john_doe@john_doe! @|$ername @^st @invalid@ @  @@  @@@", // invalid mentions
        "$www.google.com. u$ername.com d|scord.*gg @^st @invalid@ @  @@  @@@" // invalid urls
    })
    void extract_ReturnsNoEntities_WhenTextDoesNotContainAnyValidEntities(String text) {
        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertTrue(entities.isEmpty());
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsMultipleValidEntities() {
        // arrange
        String text = "Hi @john_doe, @admin(:D) and @test! Cool #Java #SpringBoot application! #dev #test GH link github.com/abc";

        var expectedHashtags = List.of(
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 41, 46, "Java"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 47, 58, "SpringBoot"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 72, 76, "dev"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 77, 82, "test"));

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.MENTION, 3, 12, "john_doe"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 14, 20, "admin"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 29, 34, "test"));

        var expectedUrls = List.of(
            new PostEntity(mockPostId, PostEntityType.URL, 91, 105, "github.com/abc"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(8, entities.size());
        assertTrue(entities.containsAll(expectedHashtags));
        assertTrue(entities.containsAll(expectedMentions));
        assertTrue(entities.containsAll(expectedUrls));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidHashtagAtStart() {
        // arrange
        String text = "#Hello!      whitespace #valid, but#invalid.";

        var expectedHashtags = List.of(
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 0, 6, "Hello"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 24, 30, "valid"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedHashtags));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidMentionAtStart() {
        // arrange
        String text = "@Hello!      whitespace @valid, but@invalid.";

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.MENTION, 0, 6, "Hello"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 24, 30, "valid"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedMentions));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidUrlAtStart() {
        // arrange
        String text = "https://www.google.com      and my github github.com, but@invalid.com";

        var expectedUrls = List.of(
            new PostEntity(mockPostId, PostEntityType.URL, 0, 22, "https://www.google.com"),
            new PostEntity(mockPostId, PostEntityType.URL, 42, 52, "github.com"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedUrls));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidHashtagAtEnd() {
        // arrange
        String text = "This is a test string with a valid hashtag at the #end";

        var expectedHashtags = List.of(
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 50, 54, "end"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertTrue(entities.containsAll(expectedHashtags));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidMentionAtEnd() {
        // arrange
        String text = "This is a test string with a valid mention at the @end";

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.MENTION, 50, 54, "end"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertTrue(entities.containsAll(expectedMentions));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidUrlAtEnd() {
        // arrange
        String text = "This is a test string with a valid url at the end.gg";

        var expectedUrls = List.of(
            new PostEntity(mockPostId, PostEntityType.URL, 46, 52, "end.gg"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertTrue(entities.containsAll(expectedUrls));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidHashtagAfterLineBreak() {
        // arrange
        String text = "#Hello! \n#NewLineValidHashtag, testing...";

        var expectedHashtags = List.of(
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 0, 6, "Hello"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 9, 29, "NewLineValidHashtag"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        System.out.println(entities);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedHashtags));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidMentionAfterLineBreak() {
        // arrange
        String text = "@admin! \n@valid_user1, testing...";

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.MENTION, 0, 6, "admin"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 9, 21, "valid_user1"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedMentions));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidUrlAfterLineBreak() {
        // arrange
        String text = "mysite.au! \ndiscord.gg, testing...";

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.URL, 0, 9, "mysite.au"),
            new PostEntity(mockPostId, PostEntityType.URL, 12, 22, "discord.gg"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedMentions));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidHashtagsBeforeAndAfterPunctuation() {
        // arrange
        String text = "#HashtagFollowedByComma, .#AnotherValidHashtag and, #ValidHashtag.";

        var expectedHashtags = List.of(
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 0, 23, "HashtagFollowedByComma"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 26, 46, "AnotherValidHashtag"),
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 52, 65, "ValidHashtag"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(3, entities.size());
        assertTrue(entities.containsAll(expectedHashtags));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidMentionsBeforeAndAfterPunctuation() {
        // arrange
        String text = "Mention followed by a @Comma, .@ThisIsAValidMention_ and, @Also_Valid1Mention.";

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.MENTION, 22, 28, "Comma"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 31, 52, "ThisIsAValidMention_"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 58, 77, "Also_Valid1Mention"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(3, entities.size());
        assertTrue(entities.containsAll(expectedMentions));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsValidUrlsBeforeAndAfterPunctuation() {
        // arrange
        String text = "Url followed by a https://comma.co.uk, ThisIsAValidUrl.gg and, www.Also3ValidUrl.co.";

        var expectedUrls = List.of(
            new PostEntity(mockPostId, PostEntityType.URL, 18, 37, "https://comma.co.uk"),
            new PostEntity(mockPostId, PostEntityType.URL, 39, 57, "ThisIsAValidUrl.gg"),
            new PostEntity(mockPostId, PostEntityType.URL, 63, 83, "www.Also3ValidUrl.co"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(3, entities.size());
        assertTrue(entities.containsAll(expectedUrls));
    }

    @Test
    void extract_ReturnsEntities_WhenTextContainsExtremelyLongValidHashtag() {
        // arrange
        String text = "This text contains a #VeryVeryVeryLongHashtagWith_SomeAdditionalWord_Characters_And_Numbers_123_But_No_Non_Word_Characters_";

        var expectedHashtags = List.of(
            new PostEntity(mockPostId, PostEntityType.HASHTAG, 21, 123,
                "VeryVeryVeryLongHashtagWith_SomeAdditionalWord_Characters_And_Numbers_123_But_No_Non_Word_Characters_"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertEquals(1, entities.size());
        assertTrue(entities.containsAll(expectedHashtags));
    }

    @Test
    void extract_CutsMentions_WhenTextContainsAMentionThatExceedsMaxAllowedLength() {
        // arrange
        String text = "Hello @valid_len_12345, and @max_length_valid_mention_that_gets_chopped_at_20th_char";

        var expectedMentions = List.of(
            new PostEntity(mockPostId, PostEntityType.MENTION, 6, 22, "valid_len_12345"),
            new PostEntity(mockPostId, PostEntityType.MENTION, 28, 49, "max_length_valid_men"));

        // act
        List<PostEntity> entities = PostEntityExtractor.extract(mockPostId, text);

        // assert
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.containsAll(expectedMentions));
    }

}
