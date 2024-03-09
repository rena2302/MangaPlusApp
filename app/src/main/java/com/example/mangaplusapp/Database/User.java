package com.example.mangaplusapp.Database;

public class User {
    private String idUser;
    private String emailUser;
    public User() {
        // Constructor mặc định để phù hợp với Firebase
    }

    public User(String idUser,String emailUser) {
        this.idUser=idUser;
        this.emailUser = emailUser;
    }

    // Getters và setters
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String id) {
        this.idUser = id;
    }
    public String getUserEmail(){return emailUser;}
    public void  setUserEmail(String emailUser){this.emailUser=emailUser;}
}
