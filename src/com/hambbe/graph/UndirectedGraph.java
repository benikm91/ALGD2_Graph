package com.hambbe.graph;

import java.util.function.Function;

@Deprecated
public class UndirectedGraph<V, E> extends GraphDecorator<V, E> {

    public UndirectedGraph(Function<E, Double> f) {
        super(new DirectedGraph<>(f));
    }

    public UndirectedGraph(UnweightedGraph<V, E> graph) {
        super(graph);
    }

    @Override
    public Edge connect(Vertex from, Vertex to, E weight) {
        graph.connect(from, to, weight);
        graph.connect(to, from, weight);
        return null;
    }

    @Override
    public boolean disconnect(Vertex from, Vertex to) {
        return graph.disconnect(from, to)
               && graph.disconnect(to, from);
    }

    /**
     * Due to time issues an optimal way of getEdges was not implemented for the UndirectedGraph.
     * We had no solution idea yet to describe here.
     * @return throws UnsupportedOperationException.
     */
    @Override
    public Iterable<? extends Edge> getEdges() {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
