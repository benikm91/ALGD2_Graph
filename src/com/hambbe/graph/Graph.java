package com.hambbe.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

/**
 * General abstract implementation for different Graph implementations in the hambbe.graph library.
 * @param <V> Type of value in vertex
 */
public abstract class Graph<V> implements IGraph<V> {

    protected final List<Vertex> vertexes = new ArrayList<>(); //TODO Priority Queue with highest degree.

    protected abstract double getValue(AbstractEdge e);

    /**
     * Helper function for different graph search implementations.
     * @param from Vertex we are starting at.
     * @param pq Initialized priority queue.
     * @param to Vertex we are looking for.
     * @return Path to vertex, if exists. Null, otherwise.
     */
    protected Path graphSearch(Vertex from, PriorityQueue<Path> pq, Vertex to) {
        from.edges.forEach(e -> pq.add(new Path(null, e, getValue(e))));
        Path result = null;
        while (!pq.isEmpty() && result == null) {
            final Path p = pq.poll();
            Vertex next = (Vertex) p.step.goal;
            if (next.isMarked()) continue;
            if (next == to) result = p;
            else {
                next.mark();
                next.edges.forEach(e -> new Path(p, e, getValue(e)));
            }
        }
        this.vertexes.forEach(Vertex::demark);
        while (result.pre != null) {
            result = result.pre;
        }
        return result;
    }

    /**
     *
     * @param pFrom Item we are starting at.
     * @param pTo Item we are looking for.
     * @return
     */
    public AbstractEdge[] bellman(final Item pFrom, final Item pTo) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        return null; //TODO implement.
    }

    /**
     * Greedy search algorithm implementation.
     *
     * It finds an existing path.
     * It does not guarantee the optimal path.
     *
     * @param pFrom Item we are starting at.
     * @param pTo Item we are looking for.
     * @param heuristic Heuristic function for prioritizing items.
     * @return Path to item, if exists. Null, otherwise.
     */
    public Path greedySearch(final Item pFrom, final Item pTo, final Function<V, Double> heuristic) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        final Function<Path, Double> greedy = (p) -> heuristic.apply(((Vertex) p.getCurrent()).value);
        final PriorityQueue<Path> pq = new PriorityQueue<>(
                (p1, p2) -> Double.compare(greedy.apply(p1), greedy.apply(p2)));
        return graphSearch(from, pq, to);
    }

    /**
     * A* search algorithm implementation.
     *
     * It finds an existing path.
     * It finds the optimal path (if rules below are full filled).
     * It won't look at any path which costs are higher than the optimal one (if rules below are full filled).
     *
     *
     * Rules to work:
     * <ul>
     * <li>All step costs have to be positive.
     * <li>Heuristic must not overestimate costs.
     * </ul>
     *
     * @param pFrom Item we are starting at.
     * @param pTo Item we are looking for.
     * @param heuristic Heuristic function for helping to prioritize items.
     * @return Path to item, if exists. Null, otherwise.
     */
    public Path aStar(final Item pFrom, final Item pTo, final Function<V, Double> heuristic) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        final Function<Path, Double> assumedTotalCost = (p) -> p.totalCost + heuristic.apply(((Vertex) p.getCurrent()).value);
        final PriorityQueue<Path> pq = new PriorityQueue<>(
                (p1, p2) -> Double.compare(assumedTotalCost.apply(p1), assumedTotalCost.apply(p2)));
        return graphSearch(from, pq, to);
    }

    /**
     * Dijkstra algorithm implementation.
     *
     * It finds an existing path.
     * It finds the optimal path (if rules below are full filled).
     *
     * Rules to work:
     * <ul>
     * <li>All step costs have to be positive.
     * </ul>
     *
     * @param pFrom Item we are starting at.
     * @param pTo Item we are looking for.
     * @return Path to item, if exists. Null, otherwise.
     */
    public Path dijkstra(final Item pFrom, final Item pTo) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        final PriorityQueue<Path> pq = new PriorityQueue<>((p1, p2) -> Double.compare(p1.totalCost, p2.totalCost));
        return graphSearch(from, pq, to);
    }

    @Override
    public Item addVertex(V value) {
        Vertex v = new Vertex(value, this);
        vertexes.add(v);
        return v;
    }

    /**
     * Check if item is element of this graph. Throws an {@link IllegalArgumentException} if not.
     * @param item Item to check
     */
    protected void checkMembership(Item item) {
        if (!(item instanceof GenericGraph.Vertex)) throw new IllegalArgumentException("Supplied Item is not a Vertex");
        Vertex v = (Vertex) item;
        if (v.graph != this) throw new IllegalArgumentException("Supplied Vertex not part of GenericGraph");

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

    public static class Path {
        public final Path pre;
        public final AbstractEdge step;
        public final double totalCost;

        public Path(Path pre, AbstractEdge step, double cost) {
            this.pre = pre;
            this.step = step;
            this.totalCost = ((pre == null) ? 0 : pre.totalCost) + cost;
        }

        public Item getCurrent() {
            return pre.step.goal;
        }
    }

    public static abstract class AbstractEdge {
        protected final Item goal;

        public AbstractEdge(Item goal) {
            this.goal = goal;
        }
    }

    protected class Vertex implements Item, Comparator<Vertex> {

        protected final Graph graph;
        protected final V value;

        /**
         * Helper variable for marking the vertex with extra info (like visited).
         * Byte was chosen, so different info can be stored and it needs also 1 byte of storage (same as boolean).
         */
        protected byte marked = 0;

        /** adjacency list */
        protected Set<AbstractEdge> edges = new HashSet<>(); //TODO check if HashMap best?

        protected Vertex(V value, Graph graph) {
            this.value = value; //TODO check if has to clone
            this.graph = graph;
        }

        protected void connect(AbstractEdge edge) {
            this.edges.add(edge);
        }

        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public int compare(Vertex v1, Vertex v2) {
            return v2.edges.size() - v1.edges.size();
        }

        public boolean isMarked() {
            return this.marked != 0;
        }

        public byte getMarkedValue() {
            return this.marked;
        }

        public void demark() {
            this.setMarked((byte) 0);
        }

        public void mark() {
            this.setMarked((byte) 1);
        }

        public void setMarked(byte marked) {
            this.marked = marked;
        }

    }

}
