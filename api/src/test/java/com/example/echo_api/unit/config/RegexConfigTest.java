package com.example.echo_api.unit.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.echo_api.config.RegexConfig;

/**
 * Unit test class for {@link RegexConfig} expressions.
 */
public class RegexConfigTest {

    private static List<String> validUsernames;
    private static List<String> validPasswords;
    private static List<String> invalidUsernames;
    private static List<String> invalidPasswords;

    @BeforeAll
    public static void readFiles() throws Exception {
        validUsernames = readFileFromResources("data/valid_usernames.txt");
        validPasswords = readFileFromResources("data/valid_passwords.txt");
        invalidUsernames = readFileFromResources("data/invalid_usernames.txt");
        invalidPasswords = readFileFromResources("data/invalid_passwords.txt");
    }

    /**
     * Test ensures that the {@link RegexConfig} USERNAME expression returns a match
     * for all 100 items present in {@code validUsernames}.
     */
    @Test
    void RegexConfig_Username_ShouldPass() {
        for (String username : validUsernames) {
            assertTrue(username.matches(RegexConfig.USERNAME),
                "Expected username to match regex: " + username);
        }
    }

    /**
     * Test ensures that the {@link RegexConfig} USERNAME expression does not return
     * a match for all 100 items present in {@code invalidUsernames}.
     */
    @Test
    void RegexConfig_Username_ShouldFail() {
        for (String username : invalidUsernames) {
            assertFalse(username.matches(RegexConfig.USERNAME),
                "Expected username to match regex: " + username);
        }
    }

    /**
     * Test ensures that the {@link RegexConfig} PASSWORD expression returns a match
     * for all 100 items present in {@code validPasswords}.
     */
    @Test
    void RegexConfig_Password_ShouldPass() {
        for (String password : validPasswords) {
            assertTrue(password.matches(RegexConfig.PASSWORD),
                "Expected password to match regex: " + password);
        }
    }

    /**
     * Test ensures that the {@link RegexConfig} PASSWORD expression does not return
     * a match for all 100 items present in {@code invalidPasswords}.
     */
    @Test
    void RegexConfig_Password_ShouldFail() {
        for (String password : invalidPasswords) {
            assertFalse(password.matches(RegexConfig.PASSWORD),
                "Expected password to match regex: " + password);
        }
    }

    /**
     * Reads a file from the resources directory and returns its content as a list
     * of strings.
     *
     * @param fileName name of the file to read
     * @return a list of strings representing the contents
     * @throws Exception if the file is not found or an error occurs while reading
     */
    private static List<String> readFileFromResources(String fileName) throws Exception {
        InputStream inputStream = RegexConfigTest.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileNotFoundException("File not found in resources: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().toList();
        }
    }

}
