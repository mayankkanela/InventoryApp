package com.mayank.inventory;

public class Vendor {
    private String name;
    private String address;
    private String id;

    public Vendor()
    {
        name=new String();
        address=new String();
        id=new String();
    }
    public Vendor(String name, String id, String address) {
        this.name = name;
        this.address = address;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
