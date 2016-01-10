package com.hambbe.graph;

import java.util.List;

/**
 * Interface for all graph implementation of the hambbe.graph library.
 *
 * @param <V> Type of value in vertex.
 * @param <E> Type for edges.
 */
public interface Graph<V, E> {

    /**
     * Connect two {@link Vertex} together with an edge.
     *
     * @param from .
     * @param to .
     * @param edgeValue The edge value.
     */
    void connect(Vertex from, Vertex to, E edgeValue);

    /**
     * Disconnect two directly connected {@link Vertex}.
     * Removes first direct connection found from <tt>from</tt> to <tt>to</tt>.
     *
     * Direct connection : Reachable with one step.
     *
     * @param from .
     * @param to .
     * @return True, if successfully removed. False, otherwise (e.g. Edge not found).
     */
    boolean disconnect(Vertex from, Vertex to);

    /**
     * Add a vertex to the graph.
     * @param value The value of the vertex to create.
     * @return An item reference to the vertex after the ... principle.
     */
    Vertex addVertex(V value);

    /**
     * Check if there is a connection between from and to.
     * @param from From item
     * @param to To item.
     * @return True if there is a connection. False, otherwise.
     */
    boolean adjacent(Vertex from, Vertex to);

    /**
     * @param from Vertex to get neighbors from.
     * @return All neighbors of item.
     */
    List<Vertex> neighbors(Vertex from);

    /**
     * Remove a vertex and all outgoing and ingoing edges.
     * @param vertex Vertex to remove.
     */
    void removeVertex(Vertex vertex);

    /**
     * Get the value from an Vertex.
     * @param vertex Vertex to get value from.
     * @return Value of vertex.
     */
    V getValue(Vertex vertex);

    /**
     * @param value Value of item to get.
     * @return First item found with value. Null, if no item was found.
     */
    Vertex getItem(V value);

    /**
     * Change the value for vertex.
     * @param vertex Vertex to change.
     * @param newValue New value for vertex.
     */
    void setValue(Vertex vertex, V newValue);

    /***
     * Public Vertex for referencing vertexes in the AbstractGraph implementations after the ... principle.
     * AbstractGraph implementations have to implement this interface into there vertex inner class,
     * so a vertex can be referenced from outside the graph.
     */
    interface Vertex { }

    interface Edge {

        Vertex getGoal();
        double getWeight();

    }

}
