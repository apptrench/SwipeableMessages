package com.ganesh.learn.android.swipeablemessages;

import java.util.Date;

/**
 * Created by Ganesh on 09-05-2015.
 */
public class SmsHolder {
    private String address;
    private String message;
    private Date date;

    public SmsHolder(String address, String message, Date date) {
        this.address = address;
        this.message = message;
        this.date = date;
    }

    public String getAddress() {
        return address;
    }


    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
