package com.hambbe.graph;

import java.util.List;

/**
 * Interface for graph implementation.
 *
 * When implementing there have to be hidden inner vertex and edge classes.
 * For pointing to vertexes and edges from a graph implementation use following the interfaces:
 * <ul>
 * <li>{@link com.hambbe.graph.Graph.Edge}
 * <li>{@link com.hambbe.graph.Graph.Vertex}
 * </ul>
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
     *
     * @return Connecting edge.
     */
    Edge connect(Vertex from, Vertex to, E edgeValue);

    /**
     * Remove an edge.
     * @param edge Remove edge.
     */
    void disconnect(Edge edge);

    /**
     * Disconnect two directly connected {@link Vertex}.
     * Removes all  connection found from <tt>from</tt> to <tt>to</tt>.
     *
     * Direct connection : Reachable with one step.
     *
     * @param from .
     * @param to .
     * @return True, if successfully removed edge(s). False, otherwise (e.g. Edge not found).
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
     * @param edge Edge to get value from.
     * @return Value of edge.
     */
    E getEdgeValue(Edge edge);

    /**
     * @return Number of vertexes in Graph.
     */
    int getVertexCount();

    /**
     * Change the value for vertex.
     * @param vertex Vertex to change.
     * @param newValue New value for vertex.
     */
    void setValue(Vertex vertex, V newValue);

    /**
     * Degree represents the number of outgoing edges from a vertex.
     * @param vertex Vertex to get degree from.
     * @return The degree
     */
    int degree(Vertex vertex);

    /**
     * Get all vertexes stored in this graph.
     * @return Iterable of vertexes
     */
    Iterable<? extends Vertex> getVertexes();

    /**
     * Get all edges stored in this graph.
     * @return Iterable of edges
     */
    Iterable<? extends Edge> getEdges();

    /***
     * Public vertex for referencing vertexes in a <ll>Graph</ll> implementations after the item principle.
     * Graph implementations have to implement this interface for there hidden vertex inner class.
     */
    interface Vertex {

        /**
         * @return All edges of this vertex.
         */
        Iterable<? extends Edge> getEdges();

    }

    /***
     * Public edge for referencing edges in a <ll>Graph</ll> implementations after the item principle.
     * Graph implementations have to implement this interface for there hidden edge inner class.
     */
    interface Edge {

        /**
         * @return Graph vertex the edge points from.
         */
        Vertex getFrom();

        /**
         * @return Graph vertex the edge points to.
         */
        Vertex getTo();

        /**
         * @return Weight of the edge.
         */
        double getWeight();

    }

}
