package org.example.quizwebapp.Model;

import java.util.Objects;

public class User {

    private final String username;
    private String password;

    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object other) { //TODO possibly problematic
        if (this == other) return true;
        if (!(other instanceof User)) return false;

        User a = (User)other;
        return Objects.equals(a.username, this.username);
    }
}