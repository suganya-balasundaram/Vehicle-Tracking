package com.lic.myearth;

public class User {
    private int id;
    private String user_id, image_path, username, email, gender, designation;

    public User(int id, String image_path, String user_id, String username, String email, String gender, String designation) {
        this.id = id;
        this.user_id = user_id;
        this.image_path = image_path;
        this.username = username;
        this.email = email;

        this.gender = gender;
        this.designation = designation;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    public String getImage_path() {
        return image_path;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }



    public String getDesignation() {
        return designation;
    }


}
