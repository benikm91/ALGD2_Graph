package com.hambbe.graph;

public class UndirectedGraph<V, K> extends GraphDecorator<V, K> {

    public UndirectedGraph(Graph<V, K> graph) {
        super(graph);
    }

    @Override
    public void connect(Graph.Item from, Graph.Item to, K weight) {
        graph.connect(from, to, weight);
        graph.connect(to, from, weight);
    }

}
