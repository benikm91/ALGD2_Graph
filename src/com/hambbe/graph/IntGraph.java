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
    public void connect(Item from, Item to, Integer edgeValue) {
        checkMembership(from, to);
        ((Vertex) from).connect(new IntEdge(edgeValue, (Vertex) to));
    }

    /**
     * Edge implementation with value type <tt>int</tt> as weight.
     */
    protected class IntEdge extends Edge {
        protected final int weight;

        protected IntEdge(int weight, Vertex goal) {
            super(goal);
            this.weight = weight;
        }

        @Override
        protected double getWeight() {
            return weight;
        }
    }

}
