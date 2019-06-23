package com.example.faione.epshelper;

public class AddressItem {
    private int id;
    private String addressname;
    private String addressdot;
    private String addresstime;

    public AddressItem() {
        this.addressname = "";
        this.addressdot = "";
        this.addresstime = "";
    }

    public AddressItem(String addressname, String addressdot, String addresstime) {
        this.addressname = addressname;
        this.addressdot = addressdot;
        this.addresstime = addresstime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressname() {
        return addressname;
    }

    public void setAddressname(String addressname) {
        this.addressname = addressname;
    }

    public String getAddressdot() {
        return addressdot;
    }

    public void setAddressdot(String addressdot) {
        this.addressdot = addressdot;
    }

    public String getAddresstime() {
        return addresstime;
    }

    public void setAddresstime(String addresstime) {
        this.addresstime = addresstime;
    }
}

