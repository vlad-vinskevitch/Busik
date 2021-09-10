package com.sharkit.busik.Entity;

import java.util.List;

public class OwnName {
    private String ownName;
    private List<String> listName;

    public OwnName(String ownName, List<String> listName) {
        this.ownName = ownName;
        this.listName = listName;
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }


    public List<String> getListName() {
        return listName;
    }

    public void setListName(List<String> listName) {
        this.listName = listName;
    }
}
