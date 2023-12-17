package pers.spl.chatserver.model;

import java.sql.Timestamp;

public class Message {
    private int id; // 对应表中的id字段
    private String sender; // 对应表中的sender字段
    private String receiver; // 对应表中的receiver字段
    private String content; // 对应表中的content字段
    private Timestamp timestamp; // 对应表中的timestamp字段

    public Message() {
    }

    public Message(int id, String sender, String receiver, String content, Timestamp timestamp) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
