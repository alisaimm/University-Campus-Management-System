package com.campus.interfaces;

import java.io.Serializable;

public interface Notifiable extends Serializable {
    public void sendNotification(String message);
}
