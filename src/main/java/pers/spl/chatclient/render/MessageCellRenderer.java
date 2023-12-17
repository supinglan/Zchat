package pers.spl.chatclient.render;

import pers.spl.chatserver.model.Message;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.sql.Timestamp;

public class MessageCellRenderer extends JPanel implements ListCellRenderer<Message> {
    private JLabel lblSender = new JLabel();
    private JLabel lblContent = new JLabel();
    private JLabel lblTime = new JLabel();
    private JPanel leftPanel = new JPanel(new BorderLayout());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MessageCellRenderer() {
        setLayout(new BorderLayout());
        lblSender.setFont(new Font("微软雅黑", Font.BOLD, 12));
        lblContent.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        lblSender.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        lblContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        lblSender.setOpaque(true);
        lblContent.setOpaque(true);
        leftPanel.add(lblSender, BorderLayout.NORTH);
        leftPanel.add(lblContent, BorderLayout.CENTER);
        this.setBackground(Color.WHITE);
        lblTime.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        lblTime.setHorizontalAlignment(JLabel.RIGHT);
        lblTime.setOpaque(true);
        leftPanel.setOpaque(true);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblTime.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(leftPanel, BorderLayout.CENTER);
        add(lblTime, BorderLayout.EAST);
    }

    private boolean isToday(Timestamp timestamp) {
        Calendar now = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timestamp.getTime());
        return now.get(Calendar.YEAR) == time.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index, boolean isSelected, boolean cellHasFocus) {
        lblSender.setText(value.getSender());
        lblContent.setText(value.getContent());
        String formattedTime = isToday(value.getTimestamp()) ?
                dateFormat.format(value.getTimestamp()) :
                dateOnlyFormat.format(value.getTimestamp());
        lblTime.setText(formattedTime);
        Color lightGrayColor = new Color(220, 220, 220);
        // 设置背景和前景颜色
        if (isSelected) {
            // 选中时的背景颜色为浅灰色
            setBackground(lightGrayColor);
            lblSender.setForeground(Color.BLACK);
            lblContent.setForeground(Color.BLACK);
            lblTime.setForeground(Color.BLACK);
            leftPanel.setBackground(lightGrayColor);
            lblTime.setBackground(lightGrayColor);
            lblSender.setBackground(lightGrayColor);
            lblContent.setBackground(lightGrayColor);
        } else {
            // 未选中时的背景颜色为白色
            setBackground(Color.WHITE);
            lblSender.setForeground(Color.BLACK);
            lblContent.setForeground(Color.BLACK);
            lblTime.setForeground(Color.BLACK);
            leftPanel.setBackground(Color.WHITE);
            lblTime.setBackground(Color.WHITE);
            lblSender.setBackground(Color.WHITE);
            lblContent.setBackground(Color.WHITE);
        }
        return this;
    }


}

