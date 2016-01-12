package com.hambbe.graph.sample;

public abstract class Transport {

    private int cost;

    public Transport(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return this.cost;
    }

}
