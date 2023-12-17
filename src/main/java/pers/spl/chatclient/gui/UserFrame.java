package pers.spl.chatclient.gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import static pers.spl.chatclient.Client.frames;

public class UserFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public UserFrame() {
        setTitle("ZChat");
        setSize(400, 800); // 调整窗体大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗体居中显示
        FlatLightLaf.install();
        try {
            Image icon = ImageIO.read(new File("src/main/resources/logo.png")); // 替换为实际图标路径
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserPanel userPanel = new UserPanel();// 创建用户面板
        // 设置主面板和布局
        mainPanel.add(userPanel, "UserPanel");
        // 添加主面板到窗体
        add(mainPanel);
        // 设置界面可见性
        setVisible(true);
    }

    public void refresh() {
        mainPanel.removeAll();
        UserPanel userPanel = new UserPanel();// 创建用户面板
        // 设置主面板和布局
        mainPanel.add(userPanel, "UserPanel");
        // 添加主面板到窗体
        add(mainPanel);
        // 设置界面可见性
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new UserFrame();
    }
}
