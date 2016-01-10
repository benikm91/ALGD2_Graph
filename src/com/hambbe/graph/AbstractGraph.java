package com.hambbe.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * General abstract class for different Graph implementations in the hambbe.graph library.
 *
 * @param <V> Type of value in vertex
 * @param <E> Type for edges.
 */
public abstract class AbstractGraph<V, E> implements Graph<V, E> {

    protected final List<VertexImpl> vertexes = new ArrayList<>(); //TODO Priority Queue with highest degree.

    @Override
    public Vertex addVertex(V value) {
        VertexImpl v = new VertexImpl(value, this);
        vertexes.add(v);
        return v;
    }

    @Override
    public Vertex getItem(V value) { //TODO if this.vertex is a Tree we could have O(log n)
        for (VertexImpl vertex : this.vertexes) {
            if (vertex.value.equals(value)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Check if vertex is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param vertex Vertex to check
     */
    protected void checkMembership(Vertex vertex) {
        assert vertex != null; // TODO throw exception if vertex == null?
        if (!(vertex instanceof AbstractGraph.VertexImpl)) throw new IllegalArgumentException("Supplied Vertex is not a VertexImpl");
        VertexImpl v = (VertexImpl) vertex;
        if (v.graph != this) throw new IllegalArgumentException("Supplied VertexImpl not part of DirectedGraph");

    }

    /**
     * Calls {@link #checkMembership(Vertex)} for all vertexes.
     * @param vertexes Items to check.
     */
    protected void checkMembership(Vertex... vertexes) {
        for (Vertex vertex : vertexes) {
            checkMembership(vertex);
        }
    }

    @Override
    public boolean disconnect(Vertex from, Vertex to) {
        if (from == null || to == null) return false;
        checkMembership(from, to);
        return ((VertexImpl) from).disconnect((VertexImpl) to);
    }

    @Override
    public V getValue(Vertex vertex) {
        checkMembership(vertex);
        return ((VertexImpl) vertex).value;
    }

    @Override
    public boolean adjacent(Vertex from, Vertex to) {
        checkMembership(from, to);
        for (AbstractEdge e : ((VertexImpl) from).edges) {
            if (e.goal == to) return true;
        }
        return false;
    }

    @Override
    public List<Vertex> neighbors(Vertex from) {
        checkMembership(from);
        return ((VertexImpl) from).edges.stream().map(e -> e.goal).collect(Collectors.toList());
    }

    @Override
    public void removeVertex(Vertex vertex) {
        throw new UnsupportedOperationException("Operation is unsupported in this graph implementation because of bad run time.");
    }

    @Override
    public void setValue(Vertex vertex, V newValue) {
        checkMembership(vertex);
        ((VertexImpl) vertex).value = newValue;
    }

    abstract class AbstractEdge implements Edge {
        public final Vertex goal; // TODO: type VertexImpl not better here?

        public abstract double getWeight();

        @Override
        public Vertex getGoal() {
            return goal;
        }

        protected AbstractEdge(Vertex goal) {
            this.goal = goal;
        }
    }

    protected class VertexImpl implements Vertex, Comparator<VertexImpl> {

        protected final AbstractGraph graph;
        protected V value;

        /**
         * Helper variable for marking the vertex with extra info (like visited).
         * Byte was chosen, so different info can be stored and it needs also 1 byte of storage (same as boolean).
         */
        protected byte marked = 0;

        /** adjacency list */
        protected Set<AbstractEdge> edges = new HashSet<>(); //TODO check if HashMap best?

        protected VertexImpl(V value, AbstractGraph graph) {
            this.value = value; //TODO check if has to clone
            this.graph = graph;
        }

        protected void connect(AbstractEdge edge) {
            this.edges.add(edge);
        }

        protected boolean disconnect(VertexImpl v) {
            return edges.removeIf(e -> e.goal == v);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public int compare(VertexImpl v1, VertexImpl v2) {
            return v2.edges.size() - v1.edges.size();
        }

        protected boolean isMarked() {
            return this.marked != 0;
        }

        protected byte getMarkedValue() {
            return this.marked;
        }

        protected void demark() {
            this.setMarked((byte) 0);
        }

        protected void mark() {
            this.setMarked((byte) 1);
        }

        protected void setMarked(byte marked) {
            this.marked = marked;
        }

    }

}
