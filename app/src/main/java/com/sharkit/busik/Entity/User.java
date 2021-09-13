package com.sharkit.busik.Entity;

import java.util.ArrayList;

public class User {
    private String role, name, last_name, country, city, phone, email, password;
    private float rating;
    private String flight;
    private ArrayList<String> tagCountry;
    private ArrayList<String> tagCity;

    public ArrayList<String> getTagCountry() {
        return tagCountry;
    }

    public void setTagCountry(ArrayList<String> tagCountry) {
        this.tagCountry = tagCountry;
    }

    public ArrayList<String> getTagCity() {
        return tagCity;
    }

    public void setTagCity(ArrayList<String> tagCity) {
        this.tagCity = tagCity;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
