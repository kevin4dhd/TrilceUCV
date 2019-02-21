package com.company.proyect.kevinpiazzoli.trilceucv.TrilceChat;

/**
 * Created by KevinPiazzoli on 15/11/2016.
 */

public class ChatMessage {
    String id;
    int TipoDeMensaje;
    String message;
    String dateTime;

    public ChatMessage(String id, String message,int TipoDeMensaje, String dateTime) {
        this.id = id;
        this.TipoDeMensaje = TipoDeMensaje;
        this.message = message;
        this.dateTime = dateTime;
    }
}
