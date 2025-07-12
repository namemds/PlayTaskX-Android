package com.cdut.playtask.network;

import java.util.List;

public class ChatResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }
}
