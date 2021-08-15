package com.sharkit.busik.Entity;

public class StaticUser {
    private static String role, name, last_name, country, city, phone, email, password;

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        StaticUser.role = role;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        StaticUser.name = name;
    }

    public static String getLast_name() {
        return last_name;
    }

    public static void setLast_name(String last_name) {
        StaticUser.last_name = last_name;
    }

    public static String getCountry() {
        return country;
    }

    public static void setCountry(String country) {
        StaticUser.country = country;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        StaticUser.city = city;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        StaticUser.phone = phone;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        StaticUser.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        StaticUser.password = password;
    }
}
