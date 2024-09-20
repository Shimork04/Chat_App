package services;

import controllers.ClientHandler;
import models.ChatRoom;
import network.ChatServer;

public class MessageService {

    // Broadcast message to all users in a chat room
    public static void broadcastMessage(ChatRoom room, String sender, String message) {
        String formattedMessage = sender + ": " + message;
        room.addMessage(formattedMessage);

        for (ClientHandler client : ChatServer.getClients()) {
            client.sendMessage(formattedMessage);
        }
    }

    // Send private message to a specific user
    public static void sendPrivateMessage(ClientHandler sender, String recipientUsername, String message) {
        String formattedMessage = "[Private] " + sender.getUsername() + ": " + message;

        // Find the recipient client and send message
        for (ClientHandler client : ChatServer.getClients()) {
            if (client.getUsername().equals(recipientUsername)) {
                client.sendMessage(formattedMessage);
                sender.sendMessage("Message sent to " + recipientUsername);
                return;
            }
        }

        sender.sendMessage("User " + recipientUsername + " not found.");
    }
}
