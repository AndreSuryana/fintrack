package com.andresuryana.fintrack.data.model;

public class User {

    private String userId;
    private String name;
    private String email;
    private String password;
    private String token;
    private String imageUrl;

    public User() {
        // Default constructor
    }

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User createForLogin(String email, String password) {
        return new User(null, null, email, password);
    }

    public static User createForRegister(String name, String email, String password) {
        return new User(null, name, email, password);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
