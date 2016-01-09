package com.hambbe.graph;

import java.util.List;

public abstract class GraphDecorator<V, K> implements Graph<V, K> {

    protected final Graph<V, K> graph;

    public GraphDecorator(Graph<V, K> graph) {
        this.graph = graph;
    }

    @Override
    public void connect(Item from, Item to, K edgeValue) {
        graph.connect(from, to, edgeValue);
    }

    @Override
    public Item addVertex(V value) {
        return graph.addVertex(value);
    }

    @Override
    public boolean adjacent(Item from, Item to) {
        return graph.adjacent(from, to);
    }

    @Override
    public List<Item> neighbors(Item from) {
        return graph.neighbors(from);
    }

    @Override
    public void removeVertex(Item item) {
        removeVertex(item);
    }

    @Override
    public V getValue(Item item) {
        return getValue(item);
    }

    @Override
    public void setValue(Item item, V newValue) {
        setValue(item, newValue);
    }
}
