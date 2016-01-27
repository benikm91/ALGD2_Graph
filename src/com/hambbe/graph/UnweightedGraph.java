package com.hambbe.graph;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * General implementation of a unweighted graph.
 *
 * @param <V> Type of value in vertex
 * @param <E> Type for edges.
 */
public class UnweightedGraph<V, E> implements Graph<V, E> {

    /** Vertexes of the graph. */
    protected final LinkedList<VertexImpl> vertexes = new LinkedList<>();

    @Override
    public Vertex addVertex(final V value) {
        VertexImpl v = new VertexImpl(value, this);
        vertexes.addLast(v);
        return v;
    }

    @Override
    public Iterable<? extends Vertex> getVertexes() {
        return (Iterable<Vertex>)this.vertexes.clone();
    }

    @Override
    public Iterable<? extends Edge> getEdges() {
        return vertexes.stream().flatMap(v -> v.edges.stream()).collect(Collectors.toList());
    }

    /**
     * Check if edge is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param pEdge Edge to check
     */
    protected void checkMembership(final Edge pEdge) {
        if (pEdge == null) throw new IllegalArgumentException("Null is not a member of this graph.");
        if (!(pEdge instanceof UnweightedGraph.UnweightedEdge)) throw new IllegalArgumentException("Supplied Edge is not a UnweightedEdge");
        final UnweightedEdge edge = (UnweightedEdge) pEdge;
        if (edge.graph != this) throw new IllegalArgumentException("Supplied Edge not part of DirectedGraph");
    }

    /**
     * Check if vertex is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param pVertex Vertex to check
     */
    protected void checkMembership(final Vertex pVertex) {
        if (pVertex == null) throw new IllegalArgumentException("Null is not a member of this graph.");
        if (!(pVertex instanceof UnweightedGraph.VertexImpl)) throw new IllegalArgumentException("Supplied Vertex is not a VertexImpl");
        final VertexImpl vertex = (VertexImpl) pVertex;
        if (vertex.graph != this) throw new IllegalArgumentException("Supplied VertexImpl not part of DirectedGraph");
    }

    /**
     * Calls {@link #checkMembership(Edge)} for all edges.
     * @param edges Edges to check.
     */
    protected final void checkMembership(final Edge... edges) {
        for (Edge edge : edges) {
            checkMembership(edge);
        }
    }

    /**
     * Calls {@link #checkMembership(Vertex)} for all vertexes.
     * @param vertexes Items to check.
     */
    protected final void checkMembership(final Vertex... vertexes) {
        for (Vertex vertex : vertexes) {
            checkMembership(vertex);
        }
    }

    @Override
    public Edge connect(Vertex pFrom, Vertex pTo, E edgeValue) {
        checkMembership(pFrom, pTo);
        final VertexImpl from = (VertexImpl) pFrom;
        final VertexImpl to = (VertexImpl) pTo;
        UnweightedEdge edge = new UnweightedEdge(from, to, this);
        from.connect(edge);
        return edge;
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
    public E getEdgeValue(Edge edge) {
        return null;
    }

    @Override
    public boolean adjacent(final Vertex from, final Vertex to) {
        checkMembership(from, to);
        for (UnweightedEdge e : ((VertexImpl) from).edges) {
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
    public int degree(Vertex vertex) {
        checkMembership(vertex);
        return ((VertexImpl) vertex).edges.size();
    }

    @Override
    public void removeVertex(final Vertex pToRemove) {
        checkMembership(pToRemove);
        final VertexImpl toRemove = (VertexImpl) pToRemove;
        for (VertexImpl vertex : this.vertexes) {
            for (UnweightedEdge edge : vertex.edges) {
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

    protected class UnweightedEdge implements Edge {

        /** Graph this edge belongs to. */
        protected final UnweightedGraph graph;

        /** Vertex where the edge points from. */
        protected final Vertex from;

        /** Vertex where the edge points to. */
        protected final Vertex to;

        @Override
        /**
         * Edges have no weights. So that search algorithms still work we return 1.
         * So every edge costs 1.
         */
        public double getWeight() {
            return 1;
        }

        @Override
        public Vertex getTo() {
            return this.to;
        }

        @Override
        public Vertex getFrom() {
            return this.from;
        }

        /**
         * @param from Field value.
         * @param to Field value.
         * @param graph Field value.
         */
        protected UnweightedEdge(final Vertex from, final Vertex to, final UnweightedGraph graph) {
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

        /** Graph this vertex belongs to. */
        protected final UnweightedGraph graph;

        /** Value of this vertex. */
        protected V value;

        /**
         * Helper variable for marking the vertex with extra info (like visited).
         * Byte was chosen, so a range of info can be stored.
         * In Java boolean needs 1 byte storage anyways.
         */
        protected byte marked = 0;

        /** Edges outgoing form this vertex. */
        protected LinkedList<UnweightedEdge> edges = new LinkedList<>();

        /**
         * @param value Field value.
         * @param graph Field value.
         */
        protected VertexImpl(V value, UnweightedGraph graph) {
            this.value = value;
            this.graph = graph;
        }

        /**
         * Adds edge to edges.
         * @param edge Edge pointing from this vertex to a vertex of this graph.
         */
        protected void connect(UnweightedEdge edge) {
            assert edge.from == this;
            this.edges.addLast(edge);
        }

        /**
         * Remove all edges which point to vertex.
         * @param vertex .
         * @return True, if something got deleted. False, otherwise.
         */
        protected boolean disconnect(VertexImpl vertex) {
            return edges.removeIf(e -> e.to == vertex);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public int compare(VertexImpl v1, VertexImpl v2) {
            return v2.edges.size() - v1.edges.size();
        }

        @Override
        public boolean isMarked() {
            return this.marked != 0;
        }

        @Override
        public byte getMarkedValue() {
            return this.marked;
        }

        @Override
        public void demark() {
            this.setMarked((byte) 0);
        }

        @Override
        public void mark() {
            this.setMarked((byte) 1);
        }

        /**
         * Mark vertex with a value.
         * @param marked Mark value.
         */
        protected void setMarked(byte marked) {
            this.marked = marked;
        }

        @Override
        public Iterable<? extends Edge> getEdges() {
            return edges;
        }

    }

}
