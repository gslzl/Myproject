package com.example.myproject.bean;


public class ResultMessage {
    private int code;
    private String message;
    public static final int SUCCESS = 1;
    public static final int ERROR = 0;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
