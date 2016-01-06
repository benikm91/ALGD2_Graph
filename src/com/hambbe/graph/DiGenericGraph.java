package com.hambbe.graph;

import java.util.function.Function;

public class DiGenericGraph<V, K> extends GenericGraph<V, K> {

    public DiGenericGraph(Function<K, Integer> edgeToWeight) { super(edgeToWeight); }

    public void connect(Item v1, Item v2, K edgeValue) {
        checkMembership(v1, v2);
        ((Vertex) v1).connect(new Edge(edgeValue, (Vertex) v2));
    }

}
