package com.example.doan;

public class User {
    private String id;
    private String username;
    private String imageURL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public User(String Id, String Username, String ImageURL){
        this.id = Id;
        this.username = Username;
        this.imageURL = ImageURL;
    }
}
