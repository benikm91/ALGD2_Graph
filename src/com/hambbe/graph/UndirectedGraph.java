package com.hambbe.graph;

import java.util.function.Function;

public class UndirectedGraph<V, E> extends GraphDecorator<V, E> {

    public UndirectedGraph(Function<E, Double> f) {
        super(new DirectedGraph<>(f));
    }

    public UndirectedGraph(Graph<V, E> graph) {
        super(graph);
    }

    @Override
    public void connect(Vertex from, Vertex to, E weight) {
        graph.connect(from, to, weight);
        graph.connect(to, from, weight);
    }

    @Override
    public boolean disconnect(Vertex from, Vertex to) {
        return graph.disconnect(from, to)
               && graph.disconnect(to, from);
    }

}
