package com.sharkit.busik.Entity;

import java.util.ArrayList;
import java.util.List;

public class OwnName {
    private String ownName;
    private ArrayList<String> listName;

    public OwnName(String ownName, ArrayList<String> listName) {
        this.ownName = ownName;
        this.listName = listName;
    }

    public OwnName() {
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

    public ArrayList<String> getListName() {
        return listName;
    }

    public void setListName(ArrayList<String> listName) {
        this.listName = listName;
    }
}
