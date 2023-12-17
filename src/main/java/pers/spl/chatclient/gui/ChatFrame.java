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

public class ChatFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private String username = "Zjuer";
    private String receiver = "Receiver";

    public ChatFrame(String username, String receiver) {
        this.username = username;
        this.receiver = receiver;
        setTitle("ZChat");
        setSize(700, 500); // 调整窗体大小
        setLocationRelativeTo(null); // 窗体居中显示
        FlatLightLaf.install();
        try {
            Image icon = ImageIO.read(new File("src/main/resources/logo.png")); // 替换为实际图标路径
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatPanel chatPanel = new ChatPanel(username, receiver); // 创建登录面板
        // 设置主面板和布局
        mainPanel.add(chatPanel, "ChatPanel");
        // 添加主面板到窗体
        add(mainPanel);
        // 设置界面可见性
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frames.remove(this);
                super.windowClosing(e);
            }
        });
    }

    public void refresh() {
        mainPanel.removeAll();
        ChatPanel chatPanel = new ChatPanel(username, receiver);// 创建用户面板
        // 设置主面板和布局
        mainPanel.add(chatPanel, "ChatPanel");
        // 添加主面板到窗体
        add(mainPanel);
        // 设置界面可见性
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new ChatFrame("Zjuer", "Sender1");
    }
}
