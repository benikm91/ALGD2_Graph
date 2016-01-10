package com.hambbe.graph;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * General abstract class for different Graph implementations in the hambbe.graph library.
 *
 * @param <V> Type of value in vertex
 * @param <E> Type for edges.
 */
public abstract class AbstractGraph<V, E> implements Graph<V, E> {

    /** Vertexes of the graph. */
    protected final LinkedList<VertexImpl> vertexes = new LinkedList<>();

    @Override
    public Vertex addVertex(V value) {
        VertexImpl v = new VertexImpl(value, this);
        vertexes.addLast(v);
        return v;
    }

    @Override
    public Iterable<? extends Vertex> getVertexes() {
        return this.vertexes;
    }

    /**
     * Check if edge is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param pEdge Edge to check
     */
    protected void checkMembership(final Edge pEdge) {
        if (pEdge == null) throw new IllegalArgumentException("Null is not a member of this graph.");
        if (!(pEdge instanceof AbstractGraph.VertexImpl)) throw new IllegalArgumentException("Supplied Vertex is not a VertexImpl");
        final AbstractEdge edge = (AbstractEdge) pEdge;
        if (edge.graph != this) throw new IllegalArgumentException("Supplied VertexImpl not part of DirectedGraph");

    }

    /**
     * Check if vertex is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param pVertex Vertex to check
     */
    protected void checkMembership(final Vertex pVertex) {
        if (pVertex == null) throw new IllegalArgumentException("Null is not a member of this graph.");
        if (!(pVertex instanceof AbstractGraph.VertexImpl)) throw new IllegalArgumentException("Supplied Vertex is not a VertexImpl");
        final VertexImpl vertex = (VertexImpl) pVertex;
        if (vertex.graph != this) throw new IllegalArgumentException("Supplied VertexImpl not part of DirectedGraph");
    }

    /**
     * Calls {@link #checkMembership(Edge)} for all edges.
     * @param edges Edges to check.
     */
    protected void checkMembership(final Edge... edges) {
        for (Edge edge : edges) {
            checkMembership(edge);
        }
    }

    /**
     * Calls {@link #checkMembership(Vertex)} for all vertexes.
     * @param vertexes Items to check.
     */
    protected void checkMembership(final Vertex... vertexes) {
        for (Vertex vertex : vertexes) {
            checkMembership(vertex);
        }
    }

    @Override
    public void disconnect(final Edge edge) {
        checkMembership(edge);
        final VertexImpl from = (VertexImpl) edge.getFrom();
        from.edges.removeIf(e -> e == edge);
    }

    @Override
    public boolean disconnect(final Vertex from, final Vertex to) {
        if (from == null || to == null) return false;
        checkMembership(from, to);
        return ((VertexImpl) from).disconnect((VertexImpl) to);
    }

    @Override
    public V getValue(final Vertex vertex) {
        checkMembership(vertex);
        return ((VertexImpl) vertex).value;
    }

    @Override
    public boolean adjacent(final Vertex from, final Vertex to) {
        checkMembership(from, to);
        for (AbstractEdge e : ((VertexImpl) from).edges) {
            if (e.to == to) return true;
        }
        return false;
    }

    @Override
    public List<Vertex> neighbors(final Vertex from) {
        checkMembership(from);
        return ((VertexImpl) from).edges.stream().map(e -> e.to).collect(Collectors.toList());
    }

    @Override
    public void removeVertex(final Vertex pToRemove) {
        checkMembership(pToRemove);
        final VertexImpl toRemove = (VertexImpl) pToRemove;
        for (VertexImpl vertex : this.vertexes) {
            for (AbstractEdge edge : vertex.edges) {
                if (edge.getTo() == toRemove) {
                    this.disconnect(edge);
                }
            }
        }
        vertexes.remove(toRemove);
    }

    @Override
    public void setValue(final Vertex vertex, final V newValue) {
        checkMembership(vertex);
        ((VertexImpl) vertex).value = newValue;
    }

    protected abstract class AbstractEdge implements Edge {
        public final AbstractGraph graph;

        public final Vertex from;
        public final Vertex to;

        public abstract double getWeight();

        @Override
        public Vertex getTo() {
            return this.to;
        }

        @Override
        public Vertex getFrom() {
            return this.from;
        }

        protected AbstractEdge(Vertex from, Vertex to, AbstractGraph graph) {
            this.from = from;
            this.to = to;
            this.graph = graph;
        }
    }

    @Override
    public int getVertexCount() {
        return this.vertexes.size();
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
        protected LinkedList<AbstractEdge> edges = new LinkedList<>();

        protected VertexImpl(V value, AbstractGraph graph) {
            this.value = value;
            this.graph = graph;
        }

        protected void connect(AbstractEdge edge) {
            this.edges.addLast(edge);
        }

        protected boolean disconnect(VertexImpl v) {
            return edges.removeIf(e -> e.to == v);
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

        @Override
        public Iterable<? extends Edge> getEdges() {
            return edges;
        }

    }

}
