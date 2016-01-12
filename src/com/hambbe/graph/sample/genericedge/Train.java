package com.hambbe.graph.sample.genericedge;

/**
 * Created by Benjamin on 23.12.2015.
 */
public class Train extends Transport {

    private static final int switchingCost = 5;

    public Train(int cost) {
        super(cost);
    }

    @Override
    public int getCost() {
        return switchingCost + super.getCost();
    }

    public String toString() { return "Train(" + getCost() + ")"; }

}
