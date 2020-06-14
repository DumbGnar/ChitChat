package com.example.demo.model;

public class MyResponse {

    private boolean ok;
    private String description;

    public MyResponse(boolean ok, String description) {
        this.ok = ok;
        this.description = description;
    }

    @Override
    public String toString() {
        return ok + ": " + description;
    }

}