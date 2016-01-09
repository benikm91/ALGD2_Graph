package com.hambbe.graph;

import java.util.List;

/**
 * General abstract class for all graph decorators.
 * This class only forwards all method class to the {@link #graph}, so not all methods have to be overriden for every new decorator.
 *
 * It is abstract, because creating an object of this only forwarding decorator doesn't make sense.
 *
 * @param <V> Type of value in vertex.
 * @param <K> Type for edges.
 */
public abstract class GraphDecorator<V, K> implements Graph<V, K> {

    /** Graph to decorate. */
    protected final Graph<V, K> graph;

    /**
     * @param graph Graph to decorate.
     */
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
