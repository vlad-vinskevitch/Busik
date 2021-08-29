package com.sharkit.busik.Entity;

import java.util.ArrayList;

public class Flight {
    private String startCountry, finishCountry, startCity, finishCity, note, status, owner, name;
    private long startDate, finishDate;
    private float  priceCargo, pricePassenger;
    private ArrayList<String> passengers;

    public ArrayList<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<String> passengers) {
        this.passengers = passengers;
    }

    public String getOwner() {
        return owner;
    }

    public float getPriceCargo() {
        return priceCargo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriceCargo(float priceCargo) {
        this.priceCargo = priceCargo;
    }

    public float getPricePassenger() {
        return pricePassenger;
    }

    public void setPricePassenger(float pricePassenger) {
        this.pricePassenger = pricePassenger;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartCountry() {
        return startCountry;
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
