package models;

import java.util.HashMap;

public class ChatRoomManager {
    private static ChatRoomManager instance;
    private HashMap<String, ChatRoom> chatRooms;

    private ChatRoomManager() {
        chatRooms = new HashMap<>();
    }

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    public ChatRoom createRoom(String roomId) {
        if (!chatRooms.containsKey(roomId)) {
            ChatRoom chatRoom = new ChatRoom(roomId);
            chatRooms.put(roomId, chatRoom);
        }
        return chatRooms.get(roomId);
    }
}
