package pers.spl.chatserver;

import pers.spl.chatserver.datasource.DbcpConnectionPool;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static DbcpConnectionPool pool = new DbcpConnectionPool();
    public static Map<String, Socket> socketMap = new HashMap<>();

    public static void main(String[] args) {
        int port = 2981;
        int maxClients = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(maxClients);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                //为客户端连接创建新线程
                executorService.submit(new ClientHandler(socket));
            }

        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}

