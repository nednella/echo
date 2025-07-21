package com.example.echo_api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConfig {

    /**
     * Username regex pattern
     * 
     * <ul>
     * <li>Contains between 3 and 15 word characters
     * </ul>
     */
    public static final String USERNAME = "^\\w{3,15}$";

    /**
     * Password regex pattern
     * 
     * <ul>
     * <li>Contains at least one letter
     * <li>Contains at least one number
     * <li>Contain at least 6 non-whitespace characters
     * </ul>
     */
    public static final String PASSWORD = "^(?=.*[a-zA-Z])(?=.*\\d)[\\S]{6,}$";

    /**
     * Username mention regex pattern
     * 
     * <ul>
     * <li>Preceded by the start of a string OR a whitespace character
     * <li>Starts with "@"
     * <li>Contains between 3 and 15 word characters
     * <li>Not followed by "@"
     * <li>Followed by a word boundary character
     * </ul>
     */
    // public static final String USERNAME_MENTION =
    // "(?<=^|\\s)@\\w{3,15}(?![@])\\b";

    /**
     * Hashtag regex pattern
     * 
     * <ul>
     * <li>Preceded by the start of a string OR a whitespace character
     * <li>Starts with "#"
     * <li>Contains any word characters, matched 1+ times
     * <li>Not followed by "#"
     * <li>Followed by a word boundary character
     * </ul>
     */
    // public static final String HASHTAG =
    // "(?<=^|\\s)#\\w+(?![#])\\b";

}
