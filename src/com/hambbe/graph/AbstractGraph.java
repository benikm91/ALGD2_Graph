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
 * @param <K> Type for edges.
 */
public abstract class AbstractGraph<V, K> implements Graph<V, K> {

    protected final List<Vertex> vertexes = new ArrayList<>(); //TODO Priority Queue with highest degree.

    @Override
    public Item addVertex(V value) {
        Vertex v = new Vertex(value, this);
        vertexes.add(v);
        return v;
    }

    @Override
    public Item getItem(V value) { //TODO if this.vertex is a Tree we could have O(log n)
        for (Vertex vertex : this.vertexes) {
            if (vertex.value.equals(value)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Check if item is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param item Item to check
     */
    protected void checkMembership(Item item) {
        assert item != null; // TODO throw exception if item == null?
        if (!(item instanceof AbstractGraph.Vertex)) throw new IllegalArgumentException("Supplied Item is not a Vertex");
        Vertex v = (Vertex) item;
        if (v.graph != this) throw new IllegalArgumentException("Supplied Vertex not part of DirectedGraph");

    }

    /**
     * Calls {@link #checkMembership(Item)} for all items.
     * @param items Items to check.
     */
    protected void checkMembership(Item... items) {
        for (Item item : items) {
            checkMembership(item);
        }
    }

    @Override
    public boolean disconnect(Item from, Item to) {
        if (from == null || to == null) return false;
        checkMembership(from, to);
        return ((Vertex) from).disconnect((Vertex) to);
    }

    @Override
    public V getValue(Item item) {
        checkMembership(item);
        return ((Vertex) item).value;
    }

    @Override
    public boolean adjacent(Item from, Item to) {
        checkMembership(from, to);
        for (AbstractEdge e : ((Vertex) from).edges) {
            if (e.goal == to) return true;
        }
        return false;
    }

    @Override
    public List<Item> neighbors(Item from) {
        checkMembership(from);
        return ((Vertex) from).edges.stream().map(e -> e.goal).collect(Collectors.toList());
    }

    @Override
    public void removeVertex(Item item) {
        throw new UnsupportedOperationException("Operation is unsupported in this graph implementation because of bad run time.");
    }

    @Override
    public void setValue(Item item, V newValue) {
        checkMembership(item);
        ((Vertex) item).value = newValue;
    }

    abstract class AbstractEdge implements Edge {
        public final Item goal; // TODO: type Vertex not better here?

        public abstract double getWeight();

        @Override
        public Item getGoal() {
            return goal;
        }

        protected AbstractEdge(Item goal) {
            this.goal = goal;
        }
    }

    protected class Vertex implements Item, Comparator<Vertex> {

        protected final AbstractGraph graph;
        protected V value;

        /**
         * Helper variable for marking the vertex with extra info (like visited).
         * Byte was chosen, so different info can be stored and it needs also 1 byte of storage (same as boolean).
         */
        protected byte marked = 0;

        /** adjacency list */
        protected Set<AbstractEdge> edges = new HashSet<>(); //TODO check if HashMap best?

        protected Vertex(V value, AbstractGraph graph) {
            this.value = value; //TODO check if has to clone
            this.graph = graph;
        }

        protected void connect(AbstractEdge edge) {
            this.edges.add(edge);
        }

        protected boolean disconnect(Vertex v) {
            return edges.removeIf(e -> e.goal == v);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public int compare(Vertex v1, Vertex v2) {
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
