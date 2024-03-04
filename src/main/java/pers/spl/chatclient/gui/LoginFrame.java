package pers.spl.chatclient.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import com.formdev.flatlaf.FlatLightLaf;


public class LoginFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private LoginPanel loginPanel;

    private RegisterPanel registerPanel;

    public LoginFrame() {
        // 设置窗体标题和大小
        setTitle("ZChat");
        setSize(425, 500); // 调整窗体大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗体居中显示
        FlatLightLaf.install();
        try {
            Image icon = ImageIO.read(new File("src/main/resources/logo.png"));
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginPanel = new LoginPanel(cardLayout,mainPanel); // 创建登录面板
        registerPanel = new RegisterPanel(cardLayout,mainPanel); // 创建注册面板
        // 设置主面板和布局
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        // 添加主面板到窗体
        add(mainPanel);
        // 设置界面可见性
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new LoginFrame();
    }
}
