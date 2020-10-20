package com.liqun.power.entity;

public class Person {

    public String name;

    public String address;

    public Person(String _name, String _address) {
        this.name = _name;
        this.address = _address;
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
}
