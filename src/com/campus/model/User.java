package com.campus.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String fullName;
    private String role;

    public User(){

    }

    public User(String username, String password, String fullName, String role){
        setUsername(username);
        setPassword(password);
        setFullName(fullName);
        setRole(role);
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()){
            this.username = username;
        } else {
            System.out.println("Username cannot be empty.");
        }
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()){
            this.password = password;
        } else {
            System.out.println("Password cannot be empty.");
        }
    }

    public void setFullName(String fullName) {
        if (fullName != null && !fullName.isEmpty()){
            this.fullName = fullName;
        } else {
            System.out.println("Name cannot be empty.");
        }
    }

    protected void setRole(String role) {
        if (role == null || role.isEmpty()){
            System.out.println("Role cannot be empty.");
            return;
        }

        if (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("STUDENT") || role.equalsIgnoreCase("TEACHER")){
            this.role = role;
        } else {
            System.out.println("Role is invalid.");
        }
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean authenticate(String inputPassword) {
        if (this.password == null) return false;
        if (this.password.equals(inputPassword)) return true;
        return false;
    }

    @Override
    public String toString() {
        return String.format("Username: %s%nFull Name: %s%nRole: %s%n", username, fullName, role);
    }
}
