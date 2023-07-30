package com.example.mahanews.Models;

import java.io.Serializable;

public class Source implements Serializable {
    String id = "";
    String name = "";

    //getter and setter for above variables
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
