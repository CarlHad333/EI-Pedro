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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class JdbcMysqlJ8IntegrationTests {

    private static final String CONNECTION_NAME = "ei-pedro-sante:europe-west9:ei-pedro-sql"; // nom complet de l'instance de BDD chez Google
    private static final String DB_NAME = "EI-Pedro"; // Nom de la BDD
    private static final String DB_USER = "Pedro"; // utilisateur
    private static final String DB_PASSWORD = "Pedro"; // password

    private String tableName;
    private HikariDataSource connectionPool;

    JdbcMysqlJ8IntegrationTests() throws SQLException {
        setUpPool();
    }

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
        this.tableName = "Data";

        // Create table
        try (Connection conn = connectionPool.getConnection()) {
            String stmt ="CREATE TABLE IF NOT EXISTS Data (" +
                    "id_data int not null auto_increment," +
                    "gilevel float not null," +
                    "time float not null," +
                    "insulin float not null," +
                    "primary key(id_data))";
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
        this.tableName = "Data";

        // Create table
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = "CREATE TABLE IF NOT EXISTS Data (" +
                    "id_data int not null auto_increment," +
                    "gilevel float not null," +
                    "time float not null," +
                    "insulin float not null," +
                    "primary key(id_data))";
            try (PreparedStatement createTableStatement = conn.prepareStatement(stmt)) {
                createTableStatement.execute();
            }
        }
    }


    public void pooledConnectionTest(String data, float time) throws SQLException {
        String[] parts = data.split(",");
        String gilevel = parts[0];
        String insulin = parts[1];
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("INSERT INTO %s (gilevel,insulin,time) VALUES (?, ?, ?)", this.tableName);
            try (PreparedStatement insertStmt = conn.prepareStatement(stmt)) {
                insertStmt.setFloat(1, Float.parseFloat(gilevel));
                insertStmt.setFloat(2, Float.parseFloat(insulin));
                insertStmt.setFloat(3,time);
  //              insertStmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
                insertStmt.execute();
            }
        }
    }

    public String getGILevel() throws SQLException {
        List<Float> gilevellist = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("SELECT gilevel FROM %s", this.tableName);
            try (PreparedStatement selectStmt = conn.prepareStatement(stmt)) {
//                selectStmt.setQueryTimeout(10); // 10s
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                    gilevellist.add(rs.getFloat("gilevel"));
                }
            }
        }
        return gilevellist.toString();
    }

    public String getTime() throws SQLException {
        List<Float> timelist = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("SELECT time FROM %s", this.tableName);
            try (PreparedStatement selectStmt = conn.prepareStatement(stmt)) {
//                selectStmt.setQueryTimeout(10); // 10s
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                    timelist.add(rs.getFloat("time"));
                }
            }
        }
        return timelist.toString();
    }

    public String getInsulin() throws SQLException {
        List<Float> insulinlist = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection()) {
            String stmt = String.format("SELECT insulin FROM %s", this.tableName);
            try (PreparedStatement selectStmt = conn.prepareStatement(stmt)) {
//                selectStmt.setQueryTimeout(10); // 10s
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                    insulinlist.add(rs.getFloat("insulin"));
                }
            }
        }
        return insulinlist.toString();
    }
}

