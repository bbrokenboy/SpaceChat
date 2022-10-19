package com.example.spacechat;

public class Message {

    private String sender;
    private String receiver;
    private String conteudo;

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

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Message(String sender, String receiver, String conteudo) {
        this.sender = sender;
        this.receiver = receiver;
        this.conteudo = conteudo;
    }

    public Message() {
    }
}
