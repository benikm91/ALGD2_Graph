package com.hambbe.graph;

import java.util.List;

/**
 * Interface for all graph implementation of the hambbe.graph library.
 *
 * @param <V> Type of value in vertex.
 * @param <K> Type for edges.
 */
public interface Graph<V, K> {

    /**
     * Connect two {@link Graph.Item} together with an edge.
     *
     * @param from .
     * @param to .
     * @param edgeValue The edge value.
     */
    void connect(Graph.Item from, Graph.Item to, K edgeValue);

    /**
     * Add a vertex to the graph.
     * @param value The value of the vertex to create.
     * @return An item reference to the vertex after the ... principle.
     */
    Item addVertex(V value);

    /**
     * Check if there is a connection between from and to.
     * @param from From item
     * @param to To item.
     * @return True if there is a connection. False, otherwise.
     */
    boolean adjacent(Item from, Item to);

    /**
     * @param from Item to get neighbors from.
     * @return All neighbors of item.
     */
    List<Item> neighbors(Item from);

    /**
     * Remove a vertex and all outgoing and ingoing edges.
     * @param item Item to remove.
     */
    void removeVertex(Item item);

    /**
     * Get the value from an Item.
     * @param item Item to get value from.
     * @return Value of item.
     */
    V getValue(Item item);

    /**
     * Change the value for item.
     * @param item Item to change.
     * @param newValue New value for item.
     */
    void setValue(Item item, V newValue);

    /***
     * Public Item for referencing vertexes in the AbstractGraph implementations after the ... principle.
     * AbstractGraph implementations have to implement this interface into there vertex inner class,
     * so a vertex can be referenced from outside the graph.
     */
    interface Item { }

}
