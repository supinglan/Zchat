package pers.spl.chatclient.gui;

import com.google.gson.reflect.TypeToken;
import pers.spl.chatclient.Client;
import pers.spl.chatclient.render.MessageCellRenderer;
import pers.spl.chatserver.model.Message;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static pers.spl.chatclient.Client.*;

public class UserPanel extends JPanel {
    private JLabel welcomeLabel;
    private JList<Message> messageList;
    private JLabel logoLabel;

    private JTextField receiverTextField;
    private JButton startChatButton;
    private DefaultListModel<Message> listModel;

    public UserPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 创建一个面板来放置欢迎语、Logo和发起聊天模块
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        // 创建并设置欢迎标签
        welcomeLabel = new JLabel("你好，" + Client.username + "！");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 水平居中对齐
        topPanel.add(welcomeLabel);

        // 加载并添加Logo
        logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/main/resources/logo.jpg");
        logoLabel.setIcon(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 水平居中对齐
        topPanel.add(logoLabel);

        // 创建并设置发起聊天模块
        JLabel receiverLabel = new JLabel("对方用户名:");
        receiverTextField = new JTextField(10); // 设置合适的大小
        startChatButton = new JButton("发起聊天");
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(receiverLabel);
        inputPanel.add(receiverTextField);
        inputPanel.add(startChatButton);
        topPanel.add(inputPanel);

        // 将组合面板添加到顶部
        add(topPanel, BorderLayout.NORTH);

        // 创建消息列表模块
        listModel = new DefaultListModel<>();
        getLatestMessages(listModel);
        messageList = new JList<>(listModel);
        messageList.setBackground(Color.WHITE);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 设置只能选中一项
        messageList.setCellRenderer(new MessageCellRenderer());
        JScrollPane scrollPane = new JScrollPane(messageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置滚动面板与边缘的间距
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        addActions();
    }

    private void addActions() {
        startChatButton.addActionListener(e -> openChatWindow(receiverTextField.getText()));
        messageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 双击事件
                    int selectedIndex = messageList.getSelectedIndex();
                    if (selectedIndex != -1) { // 如果有选中的消息
                        Message selectedMessage = listModel.getElementAt(selectedIndex);
                        String sender = selectedMessage.getSender();
                        openChatWindow(sender);
                    }
                }
            }
        });

    }

    private void getLatestMessages(DefaultListModel<Message> listModel) {
        executorService.submit(() -> {
            try {
                out.println("getLatestMessages"); // 发送请求
                out.println(username);
                String response = in.readLine(); // 读取响应
                // 使用Gson解析JSON字符串
                List<Message> messages = gson.fromJson(response, new TypeToken<List<Message>>() {
                }.getType());
                // 将解析出来的消息添加到列表模型
                SwingUtilities.invokeLater(() -> {
                    for (Message msg : messages) {
                        listModel.addElement(msg);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void openChatWindow(String receiver) {
        if (receiver.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入对方用户名", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (receiver.equals(username)) {
            JOptionPane.showMessageDialog(this, "不能和自己聊天", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        executorService.submit(() -> {
            try {
                out.println("chat"); // 发送请求
                out.println(receiver);
                String response = in.readLine(); // 读取响应
                if (!response.equals("发起成功")) {
                    JOptionPane.showMessageDialog(this, "对方用户不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 打开聊天窗口
                SwingUtilities.invokeLater(() -> {
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frames.add(new ChatFrame(username, receiver)); // 打开新的聊天窗口
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
