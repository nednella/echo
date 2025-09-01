package com.example.echo_api.testing.support;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;

@TestComponent
public class DatabaseCleaner {

    private static final List<String> PROFILE_INTERACTION_TABLES = List.of("follow");

    private static final List<String> ALL_POST_TABLES = List.of("post", "post_like", "post_entity");
    private static final List<String> POST_INTERACTION_TABLES = List.of("post_like");

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * Join table names into a single string, each wrapped with quotation marks, and
     * separated with a single comma.
     * 
     * @param tableNames the list of table names to join
     * @return the joined string of table names
     */
    private static String joinTableNames(List<String> tableNames) {
        return tableNames.stream()
            .map(name -> "\"" + name + "\"")
            .collect(Collectors.joining(", "));
    }

    /**
     * Empty a table or set of tables.
     * 
     * <p>
     * See: https://www.postgresql.org/docs/current/sql-truncate.html
     * 
     * @param tables stringified list of table(s)
     */
    private void clean(String tables) {
        jdbc.execute("TRUNCATE TABLE " + tables + " RESTART IDENTITY CASCADE");
    }

    public void cleanProfileInteractions() {
        String tables = joinTableNames(PROFILE_INTERACTION_TABLES);
        clean(tables);
    }

    public void cleanPosts() {
        String tables = joinTableNames(ALL_POST_TABLES);
        clean(tables);
    }

    public void cleanPostInteractions() {
        String tables = joinTableNames(POST_INTERACTION_TABLES);
        clean(tables);
    }

}
