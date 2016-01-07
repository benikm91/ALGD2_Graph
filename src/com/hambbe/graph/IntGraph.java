package com.hambbe.graph;

/**
 *
 * {@inheritdoc}
 *
 * Graph with int as edge weight.
 *
 * It is more memory efficient than a {@link GenericGraph}, because it uses a value type for its edge weights.
 *
 * @param <V> Type of value in vertex
 *
 * @author Benjamin Meyer
 * @see Graph
 */
public class IntGraph<V> extends Graph<V> {

    @Override
    protected double getValue(AbstractEdge e) {
        assert (e instanceof IntGraph.IntEdge) : "Illegal edge " + e;
        return ((IntEdge) e).weight;
    }

    /**
     * Edge implementation with int as weight.
     */
    protected class IntEdge extends AbstractEdge {
        protected final int weight;

        protected IntEdge(int weight, Vertex goal) {
            super(goal);
            this.weight = weight;
        }
    }

    /**
     * Connect two {@link com.hambbe.graph.IGraph.Item} together with an edge.
     * @param from .
     * @param to .
     * @param weight The edge weight.
     */
    public void connect(Item from, Item to, int weight) {
        checkMembership(from, to);
        ((Vertex) from).connect(new IntEdge(weight, (Vertex) to));
    }

}
