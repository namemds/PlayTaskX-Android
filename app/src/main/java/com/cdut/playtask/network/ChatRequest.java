package com.cdut.playtask.network;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class ChatRequest {
    public String model = "glm-4-flash-250414";
    public List<Message> messages;

    public ChatRequest(List<Message> messages) {
        this.messages = messages;
    }
}



