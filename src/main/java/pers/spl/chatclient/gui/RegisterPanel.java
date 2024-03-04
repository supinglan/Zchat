package pers.spl.chatclient.gui;


import pers.spl.chatclient.Client;
import pers.spl.chatserver.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static pers.spl.chatclient.Client.*;

public class RegisterPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JPasswordField confirmPasswordField;
    private JButton loginButton;
    private JButton registerButton;

    private CardLayout cardLayout;

    private JPanel mainPanel;

    public RegisterPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        // 设置布局
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(Color.WHITE); // 设置背景颜色为白色
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon("src/main/resources/logo.jpg"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc);

        // 创建并添加标题
        JLabel titleLabel = new JLabel("欢迎注册ZChat");
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

        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE; // 重置填充方式
        gbc.weightx = 0; // 重置水平权重
        gbc.anchor = GridBagConstraints.LINE_END;
        add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        add(confirmPasswordField, gbc);

        // 注册按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        // 登录按钮
        registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        buttonPanel.add(registerButton);

        // 注册按钮
        loginButton = new JButton("<html><u>返回登录</u></html>");
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        buttonPanel.add(loginButton);

        // 添加按钮面板到主面板
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;  // 占据两列
        gbc.fill = GridBagConstraints.NONE;  // 不填充
        gbc.anchor = GridBagConstraints.CENTER; // 中心对齐
        add(buttonPanel, gbc);
        addActions();
    }

    // 添加登录按钮和注册按钮的事件处理方法
    private void addActions() {
        // 登录按钮的事件处理
        loginButton.addActionListener(e -> {
            // 切换到注册界面
            cardLayout.show(mainPanel, "login");
        });
        // 注册按钮的事件处理
        registerButton.addActionListener(e -> register(usernameField.getText(), new String(passwordField.getPassword()), new String(confirmPasswordField.getPassword())));
    }

    private void register(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名或密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 使用线程池来发送数据
        executorService.submit(() -> {
            try {
                out.println("register"); // "login" 或 "register"
                User user = new User(username, password);
                String json = gson.toJson(user);
                out.println(json);

                // 读取服务器响应
                String response = in.readLine();
                System.out.println("Server response: " + response);

                //注册成功的特定逻辑
                if (response.equals("注册成功")) {
                    JOptionPane.showMessageDialog(this, "注册成功,请返回登录界面登录", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, response, "错误", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }


}
