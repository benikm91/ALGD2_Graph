package com.hambbe.graph;

import java.util.function.Function;

/***
 *
 * Implementation of a directed graph with generic value and generic edge.
 * For the generic edge, a edgeToWeight function is needed in the constructor.
 * This function helps calculating distances from vertex to vertex for several algorithms.
 *
 * @param <V> Type for vertexes.
 * @param <E> Type for edges.
 */
public class DirectedGraph<V, E> extends AbstractGraph<V, E> {

    /** User defined function for getting weight of a generic edge. */
    protected final Function<E, Double> edgeToWeight;

    /**
     * @param edgeToWeight Field value.
     */
    public DirectedGraph(final Function<E, Double> edgeToWeight) {
        if (edgeToWeight == null) throw new IllegalArgumentException("edgeToWeight can't be null");
        this.edgeToWeight = edgeToWeight;
    }

    @Override
    public Edge connect(final Vertex pFrom, final Vertex pTo, final E edgeValue) {
        checkMembership(pFrom, pTo);
        final VertexImpl from = (VertexImpl) pFrom;
        AbstractEdge e = new GenericEdge(edgeValue, from, (VertexImpl) pTo, this);
        from.connect(e);
        return e;
    }

    @Override
    public E getEdgeValue(final Edge pEdge) {
        checkMembership(pEdge);
        GenericEdge edge = (GenericEdge) pEdge;
        return edge.value;
    }

    /**
     * Edge with generic value.
     */
    protected class GenericEdge extends AbstractEdge {

        /** value of edge. */
        protected final E value;

        /**
         * @param value Field value.
         * @param from Field value.
         * @param to Field value.
         * @param graph Field value.
         */
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
