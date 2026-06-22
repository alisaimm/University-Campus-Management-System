package com.campus.model;

import com.campus.interfaces.Notifiable;

import com.campus.repository.DataManager;

public class AdminUser extends User implements Notifiable {
    private static final long serialVersionUID = 1L;

    public AdminUser() {
        super();
    }

    public AdminUser(String username, String password, String fullName) {
        super(username, password, fullName, "ADMIN");
    }

    public void sendNotification(String message) {
        System.out.println("ADMIN ALERT ~ " + message);
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
