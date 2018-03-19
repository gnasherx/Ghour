package com.example.ganesh.ghour;

/**
 * Created by Ganesh on 05-Oct-17.
 */

public class Chat {
    private String name;
    private String chat;

    public Chat() {

    }

    public Chat(String name, String chat) {
        this.name = name;
        this.chat = chat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
