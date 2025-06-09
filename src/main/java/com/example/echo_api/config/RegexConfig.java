package com.example.echo_api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConfig {

    /**
     * Username regex pattern
     * 
     * <ul>
     * <li>Contains only word characters (alphanumerics and underscores)
     * <li>Contains between 3 and 15 characters
     * </ul>
     */
    public static final String USERNAME = "^\\w{3,15}$";

    /**
     * Password regex pattern
     * 
     * <ul>
     * <li>Contains at least one letter
     * <li>Contains at least one number
     * <li>Contain at least 6 non-whitespace characters (excludes space, tabs and
     * line breaks)
     * </ul>
     */
    public static final String PASSWORD = "^(?=.*[a-zA-Z])(?=.*\\d)[\\S]{6,}$";

}
