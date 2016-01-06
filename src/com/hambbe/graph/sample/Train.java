package com.hambbe.graph.sample;

/**
 * Created by Benjamin on 23.12.2015.
 */
public class Train implements Transport {

    private static final int switchingCost = 5;
    private final int cost;

    public Train(int cost) {
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return switchingCost + cost;
    }

    @Override
    public String toString() {
        return "Train(" + cost + ")";
    }

}
