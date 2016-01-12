package com.hambbe.graph;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
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
        return new EdgeIterable();
    }

    /**
     * Check if edge is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param pEdge Edge to check
     */
    protected void checkMembership(final Edge pEdge) {
        if (pEdge == null) throw new IllegalArgumentException("Null is not a member of this graph.");
        if (!(pEdge instanceof AbstractGraph.AbstractEdge)) throw new IllegalArgumentException("Supplied Vertex is not a AbstractEdge");
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
    public int degree(Vertex vertex) {
        checkMembership(vertex);
        return ((VertexImpl) vertex).edges.size();
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

        /** Graph this edge belongs to. */
        protected final AbstractGraph graph;

        /** Vertex where the edge points from. */
        protected final Vertex from;

        /** Vertex where the edge points to. */
        protected final Vertex to;

        @Override
        public abstract double getWeight();

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
        protected AbstractEdge(final Vertex from, final Vertex to, final AbstractGraph graph) {
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
        protected final AbstractGraph graph;

        /** Value of this vertex. */
        protected V value;

        /**
         * Helper variable for marking the vertex with extra info (like visited).
         * Byte was chosen, so a range of info can be stored.
         * In Java boolean needs 1 byte storage anyways.
         */
        protected byte marked = 0;

        /** Edges outgoing form this vertex. */
        protected LinkedList<AbstractEdge> edges = new LinkedList<>();

        /**
         * @param value Field value.
         * @param graph Field value.
         */
        protected VertexImpl(V value, AbstractGraph graph) {
            this.value = value;
            this.graph = graph;
        }

        /**
         * Adds edge to edges.
         * @param edge Edge pointing from this vertex to a vertex of this graph.
         */
        protected void connect(AbstractEdge edge) {
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

        /**
         * @return True, if vertex has been marked. False, otherwise.
         */
        protected boolean isMarked() {
            return this.marked != 0;
        }

        /**
         * @return Specific mark value of this vertex.
         */
        protected byte getMarkedValue() {
            return this.marked;
        }

        /**
         * Remove mark from vertex.
         */
        protected void demark() {
            this.setMarked((byte) 0);
        }

        /**
         * Mark value with 1.
         */
        protected void mark() {
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

    /**
     * Own Edge iterator implementation.
     *
     * <b>Idea</b>
     * Behind the scenes iterate over all vertexes and for each vertex over his edges.
     *
     * <b>Note</b>
     * To archive a iteration over all edges in O(e).
     * If we would instead connect all vertex::edges together, we get O(v + e).
     *
     * Iterator.remove was not implemented. Remove edges via {@link #disconnect(Edge)}.
     */
    class EdgeIterable implements Iterable<Edge> {

        /** Internal vertex iterator. */
        private Iterator<VertexImpl> vertexIterator;

        /** Internal edge iterator for current vertex. */
        private Iterator<AbstractEdge> edgesIterator;

        /**
         * Inits vertexIterator and edgesIterator with first value.
         */
        private EdgeIterable() {
            this.vertexIterator = vertexes.iterator();
            this.edgesIterator = vertexes.getFirst().edges.iterator();
        }

        @Override
        public Iterator<Edge> iterator() {
            return new EdgeIterator();
        }

        @Override
        public void forEach(final Consumer<? super Edge> action) {
            Iterator<Edge> it = iterator();
            while (it.hasNext()) {
                action.accept(it.next());
            }
        }

        @Override
        public Spliterator<Edge> spliterator() {
            throw new UnsupportedOperationException();
        }

        class EdgeIterator implements Iterator<Edge> {

            @Override
            public boolean hasNext() {
                if (!edgesIterator.hasNext()) {
                    if (vertexIterator.hasNext()) {
                        VertexImpl v = vertexIterator.next();
                        edgesIterator = v.edges.iterator();
                        return hasNext();
                    }
                    return false;
                }
                return true;
            }

            @Override
            public Edge next() {
                if (!edgesIterator.hasNext()) {
                    if (!vertexIterator.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    VertexImpl v = vertexIterator.next();
                    edgesIterator = v.edges.iterator();
                    return next();
                }
                return edgesIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
