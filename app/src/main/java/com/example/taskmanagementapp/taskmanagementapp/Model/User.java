package com.example.taskmanagementapp.taskmanagementapp.Model;

public class User {

    private String name;
    private String lastName;
    private String email;
    private String image;
    private String id;

    public User() {
    }

    public User(String name, String lastName, String email,String image ,String id) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.image=image;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
