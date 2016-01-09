package com.hambbe.graph;

import java.util.function.Function;

public class UndirectedGraph<V, K> extends GraphDecorator<V, K> {

    public UndirectedGraph(Function<K, Double> f) {
        super(new DirectedGraph<>(f));
    }

    public UndirectedGraph(Graph<V, K> graph) {
        super(graph);
    }

    @Override
    public void connect(Graph.Item from, Graph.Item to, K weight) {
        graph.connect(from, to, weight);
        graph.connect(to, from, weight);
    }

    @Override
    public boolean disconnect(Graph.Item from, Graph.Item to) {
        return graph.disconnect(from, to)
               && graph.disconnect(to, from);
    }

}
