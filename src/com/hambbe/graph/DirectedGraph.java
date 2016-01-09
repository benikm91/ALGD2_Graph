package com.hambbe.graph;

import java.util.function.Function;

/***
 * Gives a general DirectedGraph-structure for extension and default implementations for specific search algorithms.
 * Sub-classes may override search algorithms and improve them.
 *
 * @param <V> Type for vertexes.
 * @param <K> Type for edges.
 */
public class DirectedGraph<V, K> extends AbstractGraph<V, K> {

    protected final Function<K, Double> edgeToWeight; // TODO: Edge instead of generic type K as edge?

    public DirectedGraph(Function<K, Double> edgeToWeight) {
        if (edgeToWeight == null) throw new IllegalArgumentException("edgeToWeight can't be null");
        this.edgeToWeight = edgeToWeight;
    }

    @Override
    protected double getValue(AbstractEdge e) {
        return edgeToWeight.apply(((GenericEdge) e).value);
    }

    @Override
    public void connect(Item from, Item to, K edgeValue) {
        checkMembership(from, to);
        ((Vertex) from).connect(new GenericEdge(edgeValue, (Vertex) to));
    }

    protected class GenericEdge extends AbstractEdge {
        final K value;

        protected GenericEdge(K value, Vertex goal) {
            super(goal);
            this.value = value;
        }
    }

}
