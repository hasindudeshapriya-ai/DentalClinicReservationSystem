package com.dentalclinic.dao;

import com.dentalclinic.model.User;
import com.dentalclinic.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    public User login(String username, String password) {
        User user = null;
        String sql = "SELECT user_id, username, password, full_name, role, dentist_id "
                   + "FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int dentistIdValue = rs.getInt("dentist_id");
                    Integer dentistId = rs.wasNull() ? null : dentistIdValue;
                    user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        dentistId
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        String sql = "SELECT user_id, username, full_name, role, created_at "
                   + "FROM users ORDER BY user_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("userId", rs.getInt("user_id"));
                user.put("username", rs.getString("username"));
                user.put("fullName", rs.getString("full_name"));
                user.put("email", "");
                user.put("role", rs.getString("role"));
                user.put("status", "ACTIVE");
                user.put("createdAt", rs.getObject("created_at"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a login account. If the new account is a dentist, the method
     * also creates the matching dentists record and links both records.
     */
    public boolean registerUserAndProfile(String username,
                                           String password,
                                           String fullName,
                                           String email,
                                           String role,
                                           String specialization,
                                           String phone) {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String insertUser = "INSERT INTO users "
                    + "(username, password, full_name, role, dentist_id) "
                    + "VALUES (?, ?, ?, ?, NULL)";

            int userId;
            try (PreparedStatement statement = connection.prepareStatement(
                    insertUser, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, fullName);
                statement.setString(4, role);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        connection.rollback();
                        return false;
                    }
                    userId = keys.getInt(1);
                }
            }

            if ("DENTIST".equalsIgnoreCase(role)) {
                String insertDentist = "INSERT INTO dentists "
                        + "(dentist_name, specialization, phone, email, user_id) "
                        + "VALUES (?, ?, ?, ?, ?)";

                int dentistId;
                try (PreparedStatement statement = connection.prepareStatement(
                        insertDentist, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, fullName);
                    statement.setString(2, specialization);
                    statement.setString(3, phone);
                    statement.setString(4, email);
                    statement.setInt(5, userId);
                    statement.executeUpdate();

                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (!keys.next()) {
                            connection.rollback();
                            return false;
                        }
                        dentistId = keys.getInt(1);
                    }
                }

                String linkUser = "UPDATE users SET dentist_id = ? WHERE user_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(linkUser)) {
                    statement.setInt(1, dentistId);
                    statement.setInt(2, userId);
                    statement.executeUpdate();
                }
            }

            connection.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception rollbackError) {
                rollbackError.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public boolean addUser(String username, String password, String fullName,
                           String email, String role, String status) {
        // email/status are UI fields; the current project users table does not contain them.
        String sql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, role);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(int userId, String username, String fullName,
                              String email, String role, String status) {
        String sql = "UPDATE users SET username = ?, full_name = ?, role = ? WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, fullName);
            statement.setString(3, role);
            statement.setInt(4, userId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
