package com.sharkit.busik.Entity;

public class Flight {
    private String startCountry, finishCountry, startCity, finishCity, note, priceCargo, pricePassenger;
    private long startDate, finishDate;

    public String getStartCountry() {
        return startCountry;
    }

    public String getPriceCargo() {
        return priceCargo;
    }

    public void setPriceCargo(String priceCargo) {
        this.priceCargo = priceCargo;
    }

    public String getPricePassenger() {
        return pricePassenger;
    }

    public void setPricePassenger(String pricePassenger) {
        this.pricePassenger = pricePassenger;
    }

    public void setStartCountry(String startCountry) {
        this.startCountry = startCountry;
    }

    public String getFinishCountry() {
        return finishCountry;
    }

    public void setFinishCountry(String finishCountry) {
        this.finishCountry = finishCountry;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getFinishCity() {
        return finishCity;
    }

    public void setFinishCity(String finishCity) {
        this.finishCity = finishCity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }

}
