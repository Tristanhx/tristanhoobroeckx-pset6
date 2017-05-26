package com.example.tristan.tristanhoobroeckx_pset6;

import java.util.Date;

/**
 * Created by Tristan on 22/05/2017.
 * This is essentially a message-object with all attributes and means to get them.
 */

public class DebateMessage {
    private String messageText, messageUser, messageTeam;
    private long messageTime;

    public DebateMessage(String messageText, String messageUser, String messageTeam){
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTeam = messageTeam;

        // Current time
        messageTime = new Date().getTime();
    }

    // Getters
    public String getMessageText(){
        return this.messageText;
    }

    public String getMessageUser(){
        return this.messageUser;
    }

    public long getMessageTime(){
        return this.messageTime;
    }

    public String getMessageTeam(){
        return this.messageTeam;
    }
}
