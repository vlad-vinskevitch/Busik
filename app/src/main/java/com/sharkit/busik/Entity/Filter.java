package com.sharkit.busik.Entity;

public class Filter {

    private static String startCountry, finishCountry, startCity, finishCity;
    private static float minPricePassenger, maxPricePassenger, maxPriceCargo, minPriceCargo;
    private static long startDateDo, startDateAfter, finishDateDo, finishDateAfter;

    public static String getStartCountry() {
        return startCountry;
    }

    public static void setStartCountry(String startCountry) {
        Filter.startCountry = startCountry;
    }

    public static String getFinishCountry() {
        return finishCountry;
    }

    public static void setFinishCountry(String finishCountry) {
        Filter.finishCountry = finishCountry;
    }

    public static String getStartCity() {
        return startCity;
    }

    public static void setStartCity(String startCity) {
        Filter.startCity = startCity;
    }

    public static String getFinishCity() {
        return finishCity;
    }

    public static void setFinishCity(String finishCity) {
        Filter.finishCity = finishCity;
    }

    public static float getMinPricePassenger() {
        return minPricePassenger;
    }

    public static void setMinPricePassenger(float minPricePassenger) {
        Filter.minPricePassenger = minPricePassenger;
    }

    public static float getMaxPricePassenger() {
        return maxPricePassenger;
    }

    public static void setMaxPricePassenger(float maxPricePassenger) {
        Filter.maxPricePassenger = maxPricePassenger;
    }

    public static float getMaxPriceCargo() {
        return maxPriceCargo;
    }

    public static void setMaxPriceCargo(float maxPriceCargo) {
        Filter.maxPriceCargo = maxPriceCargo;
    }

    public static float getMinPriceCargo() {
        return minPriceCargo;
    }

    public static void setMinPriceCargo(float minPriceCargo) {
        Filter.minPriceCargo = minPriceCargo;
    }

    public static long getStartDateDo() {
        return startDateDo;
    }

    public static void setStartDateDo(long startDateDo) {
        Filter.startDateDo = startDateDo;
    }

    public static long getStartDateAfter() {
        return startDateAfter;
    }

    public static void setStartDateAfter(long startDateAfter) {
        Filter.startDateAfter = startDateAfter;
    }

    public static long getFinishDateDo() {
        return finishDateDo;
    }

    public static void setFinishDateDo(long finishDateDo) {
        Filter.finishDateDo = finishDateDo;
    }

    public static long getFinishDateAfter() {
        return finishDateAfter;
    }

    public static void setFinishDateAfter(long finishDateAfter) {
        Filter.finishDateAfter = finishDateAfter;
    }
}
