package com.b00tc4mp.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataImpl implements Data {

    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/b00tc4mp?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("RootPass123*");           // change this obviously
        config.setMaximumPoolSize(10);
        ds = new HikariDataSource(config);
    }

    private static final DataImpl instance = new DataImpl();

    public static Data get() {
        return instance;
    }

    private DataImpl() {
    }

    @Override
    public void addUser(UserData user) {
        String sql = "INSERT IGNORE INTO users (id, name, username, password) VALUES (?, ?, ?, ?) ";

        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB error adding user", e);
        }
    }

    @Override
    public List<UserData> getUsers() {
        List<UserData> list = new ArrayList<>();
        String sql = "SELECT id, name, username, password FROM users";

        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new UserData(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error", e);
        }
        return list;
    }

    @Override
    public UserData findUserByUsername(String username) {
        String sql = "SELECT id, name, username, password FROM users WHERE username = ?";

        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error", e);
        }
        return null;
    }

    @Override
    public UserData findUserById(String id) {
        // same as above, just WHERE id = ?
        // copy-paste and change the WHERE clause
        String sql = "SELECT id, name, username, password FROM users WHERE id = ?";
        // ... same code, return null if not found
        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error", e);
        }
        return null;
    }

    @Override
    public void removeUsers() {
        try (Connection conn = ds.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            throw new RuntimeException("DB error", e);
        }
    }
}
