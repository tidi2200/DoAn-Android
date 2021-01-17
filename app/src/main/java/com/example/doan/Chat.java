package com.example.doan;

public class Chat {
    public String sender; //user hiện hành
    public String receiver; //user đang được nhắn tin
    public String message;
    public boolean isseen;

    public Chat(String sender, String receiver, String message, boolean Isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = Isseen;
    }

    public Chat() {
    }
    public boolean getIsseen() {
        return isseen;
    }

    public boolean setIsseen() {
        return isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
