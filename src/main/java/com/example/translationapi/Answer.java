package com.example.translationapi;

public class Answer {
    private String protokol = "http";
    private int statusCode = 200;
    private String message = "";

    public void SetAnswer(int code, String msg) {
        if (statusCode == 200) { // если в ответе ещё не лежит сообщение об ошибке
            if (code == 200) {
                if (message == "")
                    message = msg;
                else
                    message = message.concat(" "+msg);
            } else {
                statusCode = code;
                message = msg;
            }
        }
    }

    public String GetMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", protokol, statusCode, message);
    }
}
