package com.hambbe.graph;

/**
 * Created by Benjamin on 06.01.2016.
 */
public interface IGraph<V> {

    Item addVertex(V value);

    interface Item { }

}
