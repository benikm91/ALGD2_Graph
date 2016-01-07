package com.hambbe.graph;

/**
 * Created by Benjamin on 06.01.2016.
 */
public class IntGraph<V> extends Graph<V> {

    @Override
    protected double getValue(AbstractEdge e) {
        assert (e instanceof IntGraph.IntEdge) : "Illegal edge " + e;
        return ((IntEdge) e).value;
    }

    protected class IntEdge extends AbstractEdge {
        protected final int value;

        protected IntEdge(int value, Vertex goal) {
            super(goal);
            this.value = value;
        }
    }

    public void connect(Item v1, Item v2, int value) {
        checkMembership(v1, v2);
        ((Vertex) v1).connect(new IntEdge(value, (Vertex) v2));
    }

}
