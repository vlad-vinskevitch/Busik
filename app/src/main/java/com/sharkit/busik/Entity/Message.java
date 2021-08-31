package com.sharkit.busik.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Message {
    private String flight, message, status;
    private long date;
    private List<Object> recipient;

    public long getDate() {
        return date;
    }

    public List<Object> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<Object> recipient) {
        this.recipient = recipient;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
