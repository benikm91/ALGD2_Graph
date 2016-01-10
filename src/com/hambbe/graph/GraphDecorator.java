package com.hambbe.graph;

import java.util.List;

/**
 * General abstract class for all graph decorators.
 * This class only forwards all method class to the {@link #graph}, so not all methods have to be overriden for every new decorator.
 *
 * It is abstract, because creating an object of this only forwarding decorator doesn't make sense.
 *
 * @param <V> Type of value in vertex.
 * @param <E> Type for edges.
 */
public abstract class GraphDecorator<V, E> implements Graph<V, E> {

    /** Graph to decorate. */
    protected final Graph<V, E> graph;

    /**
     * @param graph Graph to decorate.
     */
    public GraphDecorator(Graph<V, E> graph) {
        this.graph = graph;
    }

    @Override
    public void connect(Vertex from, Vertex to, E edgeValue) {
        graph.connect(from, to, edgeValue);
    }

    @Override
    public boolean disconnect(Vertex from, Vertex to) {
        return graph.disconnect(from, to);
    }

    @Override
    public Vertex addVertex(V value) {
        return graph.addVertex(value);
    }

    @Override
    public boolean adjacent(Vertex from, Vertex to) {
        return graph.adjacent(from, to);
    }

    @Override
    public List<Vertex> neighbors(Vertex from) {
        return graph.neighbors(from);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        removeVertex(vertex);
    }

    @Override
    public V getValue(Vertex vertex) {
        return getValue(vertex);
    }

    @Override
    public Vertex getItem(V value) {
        return graph.getItem(value);
    }

    @Override
    public void setValue(Vertex vertex, V newValue) {
        setValue(vertex, newValue);
    }
}
