package com.schwarcz.samuel.trempeasy.model;


public class User {

    private String uid;
    private String username;
    private String email;
    private String telephone;
    private String fullName;


    public User() { }

    public User(String uid, String username ,String fullName,String Email , String telephone) {
        this.uid = uid;
        this.username = username;
        this.fullName=fullName;
        this.email=Email;
        this.telephone=telephone;


    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }

    public String getTelephone() {
        return telephone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}