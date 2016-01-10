package com.hambbe.graph;

import java.util.function.Function;

/***
 * Gives a general DirectedGraph-structure for extension and default implementations for specific search algorithms.
 * Sub-classes may override search algorithms and improve them.
 *
 * @param <V> Type for vertexes.
 * @param <E> Type for edges.
 */
public class DirectedGraph<V, E> extends AbstractGraph<V, E> {

    protected final Function<E, Double> edgeToWeight;

    public DirectedGraph(Function<E, Double> edgeToWeight) {
        if (edgeToWeight == null) throw new IllegalArgumentException("edgeToWeight can't be null");
        this.edgeToWeight = edgeToWeight;
    }

    @Override
    public void connect(Vertex pFrom, Vertex pTo, E edgeValue) {
        checkMembership(pFrom, pTo);
        final VertexImpl from = (VertexImpl) pFrom;
        from.connect(new GenericEdge(edgeValue, from, (VertexImpl) pTo, this));
    }

    @Override
    public E getEdgeValue(Edge pEdge) {
        checkMembership(pEdge);
        GenericEdge edge = (GenericEdge) pEdge;
        return edge.value;
    }

    protected class GenericEdge extends AbstractEdge {
        final E value;

        protected GenericEdge(E value, VertexImpl from, VertexImpl to, DirectedGraph graph) {
            super(from, to, graph);
            this.value = value;
        }

        @Override
        public double getWeight() {
            return edgeToWeight.apply(this.value);
        }
    }

}
