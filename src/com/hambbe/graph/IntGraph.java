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
    public void connect(Vertex from, Vertex to, Integer edgeValue) {
        checkMembership(from, to);
        ((VertexImpl) from).connect(new IntEdge(edgeValue, (VertexImpl) to));
    }

    /**
     * Edge implementation with value type <tt>int</tt> as weight.
     */
    protected class IntEdge extends AbstractEdge {
        protected final int weight;

        protected IntEdge(int weight, VertexImpl goal) {
            super(goal);
            this.weight = weight;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }

}
