package models;

import java.io.*;
import java.util.ArrayList;

public class ChatRoom {
    private String roomId;
    private ArrayList<String> activeUsers;
    private ArrayList<String> messageHistory;
    private File logFile;

    public ChatRoom(String roomId) {
        this.roomId = roomId;
        this.activeUsers = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
        this.logFile = new File(roomId + "_chat_log.txt");  // Create a unique log file for the room
        loadMessageHistory();  // Load message history when chat room is created
    }

    public void addUser(String username) {
        activeUsers.add(username);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
    }

    public void addMessage(String message) {
        messageHistory.add(message);
        appendMessageToFile(message);
    }

    // Load past messages from the log file when the chat room is opened
    private void loadMessageHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messageHistory.add(line);
            }
        } catch (IOException e) {
            System.out.println("No previous chat history found.");
        }
    }

    // Append each new message to the log file
    private void appendMessageToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getMessageHistory() {
        return messageHistory;
    }
}
