package com.hambbe.graph.sample.genericedge;

/**
 * Created by Benjamin on 23.12.2015.
 */
public class Airplane extends Transport {

    private static final int switchingCost = 50;

    public Airplane(int cost) {
        super(cost);
    }

    @Override
    public int getCost() {
        return switchingCost + super.getCost();
    }

    public String toString() { return "Airplane(" + getCost() + ")"; }
}
