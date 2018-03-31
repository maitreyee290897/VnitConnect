package com.example.anany.vnit_connect.models;

import java.util.ArrayList;

/**
 * Created by shivali on 26/3/18.
 */

public class User {

    private String username, email;
    private ArrayList<String> interests = new ArrayList<>();

    public User(){}

    public User(String username, String email, ArrayList<String> interests){
        this.username = username;
        this.email = email;
        this.interests = interests;
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

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

}
