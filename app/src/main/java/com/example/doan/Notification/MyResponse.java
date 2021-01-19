package com.example.doan.Notification;

public class MyResponse {
    private int success;

    public MyResponse(int Success){
        this.setSuccess(Success);
    }

    public MyResponse(){}


    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
