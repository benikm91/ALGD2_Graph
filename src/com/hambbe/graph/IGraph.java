package com.hambbe.graph;

/**
 * Interface for all Graph implementation of the hambbe.graph library.
 * @param <V> Type of value in vertex.
 */
public interface IGraph<V> {

    /**
     * Add a vertex to the graph.
     * @param value The value of the vertex to create.
     * @return An item reference to the vertex after the ... principle.
     */
    Item addVertex(V value);

    /***
     * Public Item for referencing vertexes in the Graph implementations after the ... principle.
     * Graph implementations have to implement this interface into there vertex inner class,
     * so a vertex can be referenced from outside the graph.
     */
    interface Item { }

}
