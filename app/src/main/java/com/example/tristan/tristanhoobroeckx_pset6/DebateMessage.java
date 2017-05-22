package com.example.tristan.tristanhoobroeckx_pset6;

import java.util.Date;

/**
 * Created by Tristan on 22/05/2017.
 */

public class DebateMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public DebateMessage(String messageText, String messageUser){
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Current time
        messageTime = new Date().getTime();
    }

    public DebateMessage(){

    }

    // Setters
    public void setMessageText(String newmessageText){
        this.messageText = newmessageText;
    }

    public void setMessageUser(String newmessageUser){
        this.messageUser = newmessageUser;
    }

    public void setMessageTime(long newmessagetime){
        this.messageTime = newmessagetime;
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
}
