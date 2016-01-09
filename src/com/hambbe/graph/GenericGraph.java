package com.hambbe.graph;

import java.util.function.Function;

/***
 * Gives a general GenericGraph-structure for extension and default implementations for specific search algorithms.
 * Sub-classes may override search algorithms and improve them.
 *
 * @param <V> Type for vertexes.
 * @param <K> Type for edges.
 */
public abstract class GenericGraph<V, K> extends Graph<V> {

    protected final Function<K, Double> edgeToWeight; // TODO: Edge instead of generic type K as edge?

    public GenericGraph(Function<K, Double> edgeToWeight) {
        if (edgeToWeight == null) throw new IllegalArgumentException("edgeToWeight can't be null");
        this.edgeToWeight = edgeToWeight;
    }

    @Override
    protected double getValue(AbstractEdge e) {
        return edgeToWeight.apply(((Edge) e).value);
    }

    public abstract void connect(Item v1, Item v2, K edge);

    protected class Edge extends AbstractEdge {
        final K value;

        protected Edge(K value, Vertex goal) {
            super(goal);
            this.value = value;
        }
    }

}
