package com.example.h9;

import java.util.ArrayList;

public class Theaters{

    private static Theaters single_instance = null;
    public ArrayList<MovieTheater> TheaterList = new ArrayList<MovieTheater>();



    private Theaters(){
    }

    public static Theaters getInstance(){
        if (single_instance == null){
            single_instance = new Theaters();
        }
        return single_instance;
    }

    public void addTheater(int id, String name){
        TheaterList.add(new MovieTheater(id, name));
    }

    public int getTheaterAmount(){
        return TheaterList.size();
    }

    public int getTheaterIdWithName(String name){
        int correctId = -1;
        for (MovieTheater object: TheaterList){
            if(object.getName().equals(name)){
                correctId = object.getId();
            }
        }
        return correctId;
    }

    public String getTheaterNameWithListId(int id){
        return TheaterList.get(id).getName();
    }
}
