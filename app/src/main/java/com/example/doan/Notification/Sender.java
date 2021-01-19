package com.example.doan.Notification;

public class Sender {
    private Data data;
    private String to;

    public Sender(Data Data, String To){
        this.setData(Data);
        this.setTo(To);
    }

    public Sender(){}

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
