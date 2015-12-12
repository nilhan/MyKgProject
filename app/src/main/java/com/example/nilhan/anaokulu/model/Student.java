package com.example.nilhan.anaokulu.model;

/**
 * Created by nilhan on 12.12.2015.
 */
public class Student {
    Beacon beacon;
    String name;

    public Student(Beacon beacon, String name) {
        this.beacon = beacon;
        this.name = name;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
