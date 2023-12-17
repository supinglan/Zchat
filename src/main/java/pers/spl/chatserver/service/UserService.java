package pers.spl.chatserver.service;

import pers.spl.chatserver.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pers.spl.chatserver.Server.pool;

public class UserService {
    public String validateUser(User user) {
        try (Connection connection = pool.getConnection()) {
            String sql = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUsername());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String dbPassword = resultSet.getString("password");
                        if (dbPassword.equals(user.getPassword())) {
                            return "登录成功";
                        } else {
                            return "密码错误";
                        }
                    } else {
                        return "用户名不存在";
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
        return "服务器错误";
    }

    public String createUser(User user) {
        try (Connection conn = pool.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE username = ?");
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)")) {
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "用户名已被注册";
            }
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, user.getPassword());
            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows > 0) {
                return "注册成功";
            }
        } catch (SQLException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
        return "注册失败";
    }

    public boolean checkUser(String username) {
        try (Connection conn = pool.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
