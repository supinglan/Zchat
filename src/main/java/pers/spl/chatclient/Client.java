package pers.spl.chatclient;

import com.google.gson.Gson;
import pers.spl.chatclient.gui.ChatFrame;
import pers.spl.chatclient.gui.LoginFrame;
import pers.spl.chatclient.gui.UserFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static Socket socket;
    public static List<ChatFrame> frames = new ArrayList<>();
    public static UserFrame userFrame;
    public static PrintWriter out;
    public static BufferedReader in;
    public static Gson gson = new Gson();
    public static ExecutorService executorService = Executors.newFixedThreadPool(10); // 创建线程池
    public static String username = "Zjuer";

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 2981;
        try {
            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader filterIn= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PipedOutputStream pipedOut = new PipedOutputStream();
            PrintWriter filterOut = new PrintWriter(pipedOut, true);
            executorService.submit(() -> {
                while (true) {
                    String line = filterIn.readLine();
                    System.out.println(line);
                    while (line.equals("refresh")) {
                        for (ChatFrame frame : frames) {
                            frame.refresh();
                        }
                        userFrame.refresh();
                        line = filterIn.readLine();
                    }
                    filterOut.println(line);
                }
            });
            in = new BufferedReader(new InputStreamReader(new PipedInputStream(pipedOut)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(Client::gui);
    }

    public static void gui() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
    }


    public static void shutdown() {
        executorService.shutdown();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}