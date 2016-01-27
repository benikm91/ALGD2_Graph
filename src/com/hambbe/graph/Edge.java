package com.hambbe.graph;

/***
 * Public edge for referencing edges in a <ll>Graph</ll> implementations after the item principle.
 * Graph implementations have to implement this interface for there hidden edge inner class.
 */
public interface Edge {

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
