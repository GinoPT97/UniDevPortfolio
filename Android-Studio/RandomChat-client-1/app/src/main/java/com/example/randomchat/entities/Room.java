package com.example.randomchat.entities;

import java.io.Serializable;

public class Room implements Serializable {

    private final String name;
    private final int number;
    private final String description;
    private int size;


    /**
     * CONSTRUCTOR
     */

    public Room(String name, int number, String description) {
        this.name = name;
        this.number = number;
        this.description = description;
    }

    /**
     * GETTERS
     * & SETTERS
     */

    public String getName() {
        return name;
    }


    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

}
