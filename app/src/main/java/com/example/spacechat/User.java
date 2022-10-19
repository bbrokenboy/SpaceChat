package com.example.spacechat;

public class User {
    private String username;
    private String email;
    private String nome;
    private String dtaNascimento;
    private String number;
    private String profilePicture;

    public User(){

    }
    public User(String username, String email, String nome, String dtaNascimento, String number, String profilePicture) {
        this.email = email;
        this.nome = nome;
        this.dtaNascimento = dtaNascimento;
        this.number = number;
        this.profilePicture = profilePicture;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDtaNascimento() {
        return dtaNascimento;
    }

    public void setDtaNascimento(String dtaNascimento) {
        this.dtaNascimento = dtaNascimento;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
