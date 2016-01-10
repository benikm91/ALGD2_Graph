package com.hambbe.graph;

/**
 *
 * AbstractGraph with value type <tt>int</tt> as edge weight.
 *
 * It is more memory efficient than a {@link DirectedGraph}, because it uses a value type for its edge weights.
 *
 * @param <V> Type of value in vertex
 *
 * @author Benjamin Meyer
 * @see AbstractGraph
 */
public class IntGraph<V> extends AbstractGraph<V, Integer> {

    @Override
    public void connect(final Vertex pFrom, final Vertex pTo, final Integer edgeValue) {
        checkMembership(pFrom, pTo);
        final VertexImpl from = (VertexImpl) pFrom;
        from.connect(new IntEdge(edgeValue, from, (VertexImpl) pTo, this));
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
    protected class IntEdge extends AbstractEdge {
        protected final int weight;

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
