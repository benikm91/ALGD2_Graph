package com.hambbe.graph.sample;

/**
 * Created by Benjamin on 23.12.2015.
 */
public class City {

    public final String name;

    public City(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
