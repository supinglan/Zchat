package pers.spl.chatclient.gui;

import pers.spl.chatclient.Client;
import pers.spl.chatserver.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static pers.spl.chatclient.Client.*;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel() {
        // 设置布局
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(Color.WHITE); // 设置背景颜色为白色
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon("src/main/resources/logo.jpg")); // 替换为实际的Logo路径
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc);

        // 创建并添加标题
        JLabel titleLabel = new JLabel("欢迎登录ZChat");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridy = 1;
        add(titleLabel, gbc);

        // 用户名标签和文本框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 设置填充方式
        gbc.weightx = 0.5; // 设置水平权重
        add(usernameField, gbc);

        // 密码标签和文本框
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE; // 重置填充方式
        gbc.weightx = 0; // 重置水平权重
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 设置填充方式
        gbc.weightx = 0.5; // 设置水平权重
        add(passwordField, gbc);

        // 登录按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 流布局，居中对齐，水平间隔10像素，垂直间隔0像素
        buttonPanel.setBackground(Color.WHITE); // 设置背景颜色为白色
        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        buttonPanel.add(loginButton);

        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        buttonPanel.add(registerButton);

        // 添加按钮面板到主面板
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;  // 占据两列
        gbc.fill = GridBagConstraints.NONE;  // 不填充
        gbc.anchor = GridBagConstraints.CENTER; // 中心对齐
        add(buttonPanel, gbc);
        addActions();
    }

    // 添加登录按钮和注册按钮的事件处理方法
    private void addActions() {
        // 登录按钮的事件处理
        loginButton.addActionListener(e -> processUserAction("login", usernameField.getText(), new String(passwordField.getPassword())));

        // 注册按钮的事件处理
        registerButton.addActionListener(e -> processUserAction("register", usernameField.getText(), new String(passwordField.getPassword())));
    }

    private void processUserAction(String actionType, String username, String password) {
        // 使用线程池来发送数据
        executorService.submit(() -> {
            try {
                out.println(actionType); // "login" 或 "register"
                User user = new User(username, password);
                String json = gson.toJson(user);
                out.println(json);

                // 读取服务器响应
                String response = in.readLine();
                System.out.println("Server response: " + response);

                // 登录成功的特定逻辑
                if (actionType.equals("login") && response.equals("登录成功")) {
                    // 在AWT事件线程中关闭当前窗口并打开用户界面
                    SwingUtilities.invokeLater(() -> {
                        Client.username = username;
                        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        currentFrame.dispose(); // 关闭当前窗口
                        userFrame = new UserFrame(); // 打开新的用户界面窗口

                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }


}
