package controllers;

import models.ChatRoom;
import models.ChatRoomManager;
import services.MessageService;
import services.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private ChatRoom room;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // User authentication or registration
            UserManager userManager = UserManager.getInstance();

            while (true) {
                out.println("Enter username:");
                username = in.readLine();
                out.println("Enter password:");
                String password = in.readLine();

                // Check if the user exists
                if (userManager.login(username, password)) {
                    out.println("Login successful!");
                    break;
                } else {
                    // Offer user to register
                    out.println("Username not found. Would you like to register with this username? (yes/no)");
                    String response = in.readLine();

                    if (response.equalsIgnoreCase("yes")) {
                        boolean registered = userManager.register(username, password);
                        if (registered) {
                            out.println("Registration successful! You are now logged in.");
                            break;
                        } else {
                            out.println("Registration failed, please try again.");
                        }
                    } else {
                        out.println("Try again or register with a different username.");
                    }
                }
            }

            // Join or create a chat room
            out.println("Enter room ID to join or create:");
            String roomId = in.readLine();
            room = ChatRoomManager.getInstance().createRoom(roomId);

            // Show chat history
            out.println("Chat history:");
            for (String message : room.getMessageHistory()) {
                out.println(message);
            }

            // Main chat loop
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    break;
                } else if (message.startsWith("/msg")) {
                    String[] parts = message.split(" ", 3);
                    if (parts.length == 3) {
                        String recipient = parts[1];
                        String privateMessage = parts[2];
                        MessageService.sendPrivateMessage(this, recipient, privateMessage);
                    } else {
                        out.println("Incorrect format. Use: /msg <username> <message>");
                    }
                } else {
                    MessageService.broadcastMessage(room, username, message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
