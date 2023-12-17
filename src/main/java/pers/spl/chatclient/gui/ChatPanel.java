package pers.spl.chatclient.gui;

import com.google.gson.reflect.TypeToken;
import pers.spl.chatserver.model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static pers.spl.chatclient.Client.*;

public class ChatPanel extends JPanel {
    private JTextArea chatTextArea;
    private JTextArea messageTextArea; // Changed from JTextField to JTextArea
    private JButton sendButton;
    private JScrollPane messageScrollPane; // Scroll pane for the message input

    private String username;
    private String receiver;

    private JLabel titleLabel;

    public ChatPanel(String username, String receiver) {
        this.username = username;
        this.receiver = receiver;
        setBackground(Color.WHITE);

        // 设置布局
        setLayout(new BorderLayout());
        titleLabel = new JLabel(receiver);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setPreferredSize(new Dimension(0, 30));
        titleLabel.setBackground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // 创建聊天文本区域
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false); // 不允许编辑
        chatTextArea.setBackground(Color.WHITE); // 设置背景为白色
        getChatHistory(); // 获取聊天记录
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        add(chatScrollPane, BorderLayout.CENTER);

        // 创建消息输入框和发送按钮
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageTextArea = new JTextArea();
        messageTextArea.setLineWrap(true); // 自动换行
        messageTextArea.setWrapStyleWord(true); // 断行不断字
        messageScrollPane = new JScrollPane(messageTextArea); // 添加滚动条
        messageScrollPane.setPreferredSize(new Dimension(0, 100)); // 设置更大的首选大小

        sendButton = new JButton("发送");
        sendButton.setPreferredSize(new Dimension(80, 30)); // 设置小巧的按钮大小
        JPanel sendButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 右对齐的按钮面板
        sendButtonPanel.setBackground(Color.WHITE);
        sendButtonPanel.add(sendButton);
        inputPanel.add(messageScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButtonPanel, BorderLayout.SOUTH); // 放置按钮面板在底部右侧

        add(inputPanel, BorderLayout.SOUTH);

        // 添加发送按钮的事件处理
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageTextArea.getText();
                if (!message.isEmpty()) {
                    Sendmessage(message);
                }
            }
        });
    }

    private void Sendmessage(String content) {
        executorService.submit(() -> {
            try {
                // 发送请求以获取消息记录
                out.println("send");
                Message message = new Message(0, username, receiver, content, Timestamp.valueOf(LocalDateTime.now()));
                String json = gson.toJson(message);
                out.println(json);

                // 读取服务器响应
                String response = in.readLine();

                if (response != null) {
                    // 在AWT事件线程中将消息显示在聊天界面上
                    SwingUtilities.invokeLater(() -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String timeString = formatter.format(java.time.LocalDateTime.now());
                        chatTextArea.append(username + " " + timeString + "\n");
                        chatTextArea.append(content + "\n");
                        chatTextArea.append("\n");
                        messageTextArea.setText("");
                        scrollToBottom(chatTextArea);
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void scrollToBottom(JTextArea textArea) {
        JScrollBar verticalBar = messageScrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

    private void getChatHistory() {
        executorService.submit(() -> {
            try {
                // 发送请求以获取消息记录
                out.println("getChatHistory");
                out.println(username);
                out.println(receiver);

                // 读取服务器响应
                String response = in.readLine();

                // 处理从服务器返回的消息记录
                if (response != null) {

                    // 将 JSON 转换为List<Message>
                    java.util.List<Message> messages = gson.fromJson(response, new TypeToken<List<Message>>() {
                    }.getType());

                    // 在AWT事件线程中将消息显示在聊天界面上
                    SwingUtilities.invokeLater(() -> {
                        for (Message message : messages) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String timeString = formatter.format(message.getTimestamp().toLocalDateTime());
                            chatTextArea.append(message.getSender() + " " + timeString + "\n");
                            chatTextArea.append(message.getContent() + "\n");
                            chatTextArea.append("\n");
                        }
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
