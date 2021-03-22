package com.example.h9;

public class MovieTheater {
    private String name;
    private int id;

    public MovieTheater(int ID, String theaterName){
        name = theaterName;
        id = ID;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}
