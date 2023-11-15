package com.example.ei;
/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.collect.ImmutableList;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class JdbcMysqlJ8IntegrationTests {

    private static final String CONNECTION_NAME = "ei-pedro-sante:europe-west9:ei-pedro-sql"; // nom complet de l'instance de BDD chez Google
    private static final String DB_NAME = "EI-Pedro"; // Nom de la BDD
    private static final String DB_USER = "Pedro"; // utilisateur
    private static final String DB_PASSWORD = "Pedro"; // password

    private String tableName;
    private HikariDataSource connectionPool;

    public void setUpPool() throws SQLException {
        // Set up URL parameters
        String jdbcURL = String.format("jdbc:mysql:///%s", DB_NAME);
        Properties connProps = new Properties();
        connProps.setProperty("user", DB_USER);
        connProps.setProperty("password", DB_PASSWORD);
        connProps.setProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        connProps.setProperty("cloudSqlInstance", CONNECTION_NAME);

        // Initialize connection pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcURL);
        config.setDataSourceProperties(connProps);
        config.setConnectionTimeout(10000); // 10s

        this.connectionPool = new HikariDataSource(config);
        this.tableName = String.format("books_%s", UUID.randomUUID().toString().replace("-", ""));

        // Create table
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("CREATE TABLE %s (", this.tableName)
                    + "  ID CHAR(20) NOT NULL,"
                    + "  TITLE TEXT NOT NULL"
                    + ");";
            try (PreparedStatement createTableStatement = conn.prepareStatement(stmt)) {
                createTableStatement.execute();
            }
        }
    }

    public void dropTableIfPresent() throws SQLException {
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("DROP TABLE %s;", this.tableName);
            try (PreparedStatement dropTableStatement = conn.prepareStatement(stmt)) {
                dropTableStatement.execute();
            }
        }
    }


    public String pooledConnectionTest() throws SQLException {
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("INSERT INTO %s (ID, TITLE) VALUES (?, ?)", this.tableName);
            try (PreparedStatement insertStmt = conn.prepareStatement(stmt)) {
                insertStmt.setQueryTimeout(10);
                insertStmt.setString(1, "book1");
                insertStmt.setString(2, "Book One");
                insertStmt.execute();
                insertStmt.setString(1, "book2");
                insertStmt.setString(2, "Book Two");
                insertStmt.execute();
            }
        }

        List<String> bookList = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("SELECT TITLE FROM %s ORDER BY ID", this.tableName);
            try (PreparedStatement selectStmt = conn.prepareStatement(stmt)) {
                selectStmt.setQueryTimeout(10); // 10s
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                    bookList.add(rs.getString("TITLE"));
                }
            }
        }
        assertThat(bookList).containsExactly("Book One", "Book Two");

        return bookList.toString();
    }
}

