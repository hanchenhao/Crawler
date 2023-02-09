package com.github.hcsp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    @SuppressFBWarnings({"DMI_CONSTANT_DB_PASSWORD", "ODR_OPEN_DATABASE_RESOURCE"})
    public static void main(String[] args) {
        final String user = "root";
        final String password = "123456";
        String jdbcUrl = "jdbc:h2:file:/Users/hanchenhao/Desktop/Java学习/Crawler/target";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);) {
            DushuJdbcCrawler crawler = new DushuJdbcCrawler(connection);
            for (int i = 1; i < 100; i++) {
                crawler.insertAllOriginalNewsLinks("85_" + i);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
