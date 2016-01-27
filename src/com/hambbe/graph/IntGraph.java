package com.hambbe.graph;

/**
 *
 * UnweightedGraph with value type <tt>int</tt> as edge weight.
 *
 * It is more memory efficient than a {@link DirectedGraph}, because it uses a value type for its edge weights.
 *
 * If using an Integer for edge weight, always this implementation must be used for efficiency.
 *
 * @param <V> Type of value in vertex
 *
 * @author Benjamin Meyer
 * @see UnweightedGraph
 */
public class IntGraph<V> extends UnweightedGraph<V, Integer> {

    @Override
    public Edge connect(final Vertex pFrom, final Vertex pTo, final Integer edgeValue) {
        checkMembership(pFrom, pTo);
        final VertexImpl from = (VertexImpl) pFrom;
        IntEdge edge = new IntEdge(edgeValue, from, (VertexImpl) pTo, this);
        from.connect(edge);
        return edge;
    }

    @Override
    public Integer getEdgeValue(Edge pEdge) {
        checkMembership(pEdge);
        IntEdge edge = (IntEdge) pEdge;
        return edge.weight;
    }

    /**
     * Edge implementation with value type <tt>int</tt> as weight.
     */
    protected class IntEdge extends UnweightedEdge {

        /** Edge weight. */
        protected final int weight;

        /**
         * @param weight Field value.
         * @param from Field value.
         * @param to Field value.
         * @param graph Field value.
         */
        protected IntEdge(final int weight, final VertexImpl from, final VertexImpl to, final IntGraph<V> graph) {
            super(from, to, graph);
            this.weight = weight;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }

}
