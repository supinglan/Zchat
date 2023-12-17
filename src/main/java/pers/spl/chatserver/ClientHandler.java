package pers.spl.chatserver;

import com.google.gson.Gson;
import pers.spl.chatserver.model.Message;
import pers.spl.chatserver.model.User;
import pers.spl.chatserver.service.MessageService;
import pers.spl.chatserver.service.UserService;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static pers.spl.chatserver.Server.socketMap;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Gson gson;
    private BufferedReader reader;
    private PrintWriter writer;
    static UserService userService = new UserService();
    static MessageService messageService = new MessageService();


    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.gson = new Gson();
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            String line;
            // 循环读取并处理客户端发送的消息
            while ((line = reader.readLine()) != null) {
                System.out.println("Received from client: " + line);
                // 根据接收到的指令执行相应操作
                String response = processRequest(line);
                writer.println(response);
            }

        }catch (SocketException ex) {
            System.out.println("Client disconnected");
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("I/O error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String processRequest(String requestType) {
        return switch (requestType) {
            case "login" -> login();
            case "register" -> register();
            case "send" -> send();
            case "getLatestMessages" -> getLatestMessages();
            case "chat" -> startChat();
            case "getChatHistory" -> getChatHistory();
            default -> "error";
        };
    }
    private String login() {
        String response = "服务器错误";
        try {
            String content = reader.readLine();
            System.out.println("Received from client: " + content);
            User user = gson.fromJson(content, User.class);
            System.out.println("User: " + user);
            response = userService.validateUser(user);
            if(response.equals("登录成功")) {
                socketMap.put(user.getUsername(), socket);
                System.out.println(user.getUsername() + "登录成功");
            }
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }
    private String register() {
        try {
            String content = reader.readLine();
            System.out.println("Received from client: " + content);
            User user = gson.fromJson(content, User.class);
            System.out.println("User: " + user);
            return userService.createUser(user);
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "服务器错误";
    }
    private String startChat() {
        try {
            String content = reader.readLine();
            System.out.println("Received from client: " + content);
            String receiver = content;
            if(userService.checkUser(receiver) ) {
                return "发起成功";
            }
            else {
                return "发起失败";
            }
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "服务器错误";
    }
    private String send() {
        try {
            String content = reader.readLine();
            System.out.println("Received from client: " + content);
            Message message = gson.fromJson(content, Message.class);
            System.out.println("Message: " + message);
            if(!messageService.saveMessage(message)) {
                return "发送失败";
            }
            String receiver = message.getReceiver();
            Socket des = socketMap.get(receiver);
            if(des != null) {
                PrintWriter desWriter = new PrintWriter(des.getOutputStream(), true);
                desWriter.println("refresh");
            }
            return "发送成功";
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "服务器错误";
    }
    private String getLatestMessages() {
        try {
            String content = reader.readLine();
            return gson.toJson(messageService.getLatestMessages(content));
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "服务器错误";
    }
    private String getChatHistory() {
        try {
            String username1 = reader.readLine();
            String username2 = reader.readLine();
            return gson.toJson(messageService.getChatHistory(username1, username2));
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "服务器错误";
    }
}



    
