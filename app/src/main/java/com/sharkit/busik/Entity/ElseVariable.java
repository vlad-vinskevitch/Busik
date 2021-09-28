package com.sharkit.busik.Entity;

public class ElseVariable {
    private static String profile, nameFlight, statusPassenger;

    public static String getStatusPassenger() {
        return statusPassenger;
    }

    public static void setStatusPassenger(String statusPassenger) {
        ElseVariable.statusPassenger = statusPassenger;
    }

    public static String getNameFlight() {
        return nameFlight;
    }

    public static void setNameFlight(String nameFlight) {
        ElseVariable.nameFlight = nameFlight;
    }

    public static String getProfile() {
        return profile;
    }

    public static void setProfile(String profile) {
        ElseVariable.profile = profile;
    }

}
