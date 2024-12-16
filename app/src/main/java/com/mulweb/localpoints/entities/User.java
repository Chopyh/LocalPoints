package com.mulweb.localpoints.entities;

import java.time.LocalDate;

public class User {
    private String userName;
    private String fullName;
    private String email;
    private String birthDate;

    public User(String userName, String fullName, String email, String birthDate) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.birthDate = birthDate;
    }

    public User() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
