package pers.spl.chatserver.service;

import pers.spl.chatserver.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static pers.spl.chatserver.Server.pool;

public class MessageService {
    public boolean saveMessage(Message message){
        String sql = "INSERT INTO message (sender, receiver, content,timestamp) VALUES (?, ?, ?,?)";
        try (Connection conn = pool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getReceiver());
            pstmt.setString(3, message.getContent());
            pstmt.setTimestamp(4, message.getTimestamp());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<Message> getLatestMessages(String username) {
        List<Message> latestMessages = new ArrayList<>();
        String sql = "SELECT m1.* FROM message m1 JOIN (SELECT sender, MAX(timestamp) AS latest FROM message WHERE receiver = ? GROUP BY sender) m2 ON m1.sender = m2.sender AND m1.timestamp = m2.latest WHERE m1.receiver = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Message message = createMessageFromResultSet(rs);
                latestMessages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestMessages;
    }


    public List<Message> getChatHistory(String username1, String username2) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp ASC";
        try (Connection conn = pool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username1);
            pstmt.setString(2, username2);
            pstmt.setString(3, username2);
            pstmt.setString(4, username1);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Message message = createMessageFromResultSet(rs);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private Message createMessageFromResultSet(ResultSet rs) throws SQLException {
        // 创建并返回一个Message对象
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setSender(rs.getString("sender"));
        message.setReceiver(rs.getString("receiver"));
        message.setContent(rs.getString("content"));
        message.setTimestamp(rs.getTimestamp("timestamp"));
        return message;
    }
}
