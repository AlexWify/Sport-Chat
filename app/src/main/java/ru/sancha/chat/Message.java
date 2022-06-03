package ru.sancha.chat;

import java.util.Date;

public class Message {
    public String userName;
    public String textMessage;
    public String category;
    private long messageTime;

    // создаем конструктор
    public Message() {}

    public Message(String userName, String textMessage, String category){
        this.userName = userName;
        this.textMessage = textMessage;
        this.category = category;
        this.messageTime = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

}
