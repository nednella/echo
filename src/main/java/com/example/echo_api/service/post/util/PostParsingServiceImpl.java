package com.example.echo_api.service.post.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.echo_api.config.RegexConfig;
import com.example.echo_api.persistence.model.post.entity.PostEntity;
import com.example.echo_api.persistence.model.post.entity.PostHashtag;
import com.example.echo_api.persistence.model.post.entity.PostMention;

/**
 * Utility service implementation for handling string parsing as part of the
 * post creation process.
 */
@Service
public class PostParsingServiceImpl implements PostParsingService {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile(RegexConfig.HASHTAG);
    private static final Pattern MENTION_PATTERN = Pattern.compile(RegexConfig.USERNAME_MENTION);

    @Override
    public Map<String, List<PostEntity>> parse(UUID postId, String text) {
        Map<String, List<PostEntity>> map = new HashMap<>();
        map.put("hashtags", parseHashtags(postId, text));
        map.put("mentions", parseMentions(postId, text));
        return map;
    }

    private List<PostEntity> parseHashtags(UUID postId, String text) {
        if (text == null) {
            throw new IllegalArgumentException("Parser input cannot be null.");
        }

        List<PostEntity> hashtags = new ArrayList<>();

        Matcher matcher = HASHTAG_PATTERN.matcher(text);
        while (matcher.find()) {
            String match = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            hashtags.add(new PostHashtag(postId, start, end, match));
        }

        return hashtags;
    }

    private List<PostEntity> parseMentions(UUID postId, String text) {
        List<PostEntity> mentions = new ArrayList<>();

        Matcher matcher = MENTION_PATTERN.matcher(text);
        while (matcher.find()) {
            String match = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            mentions.add(new PostMention(postId, start, end, match));
        }

        return mentions;
    }

}
