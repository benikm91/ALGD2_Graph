package com.hambbe.graph;

/***
 * Public vertex for referencing vertexes in a <ll>Graph</ll> implementations after the item principle.
 * Graph implementations have to implement this interface for there hidden vertex inner class.
 */
public interface Vertex {

    /**
     * @return All edges of this vertex.
     */
    Iterable<? extends Edge> getEdges();

    /**
     * @return True, if vertex has been marked. False, otherwise.
     */
    boolean isMarked();

    /**
     * @return Specific mark value of this vertex.
     */
    byte getMarkedValue();

    /**
     * Remove mark from vertex.
     */
    void demark();

    /**
     * Mark value with 1.
     */
    void mark();


}

