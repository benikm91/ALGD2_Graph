package com.hambbe.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

public abstract class Graph<V> implements IGraph<V> {

    protected final List<Vertex> vertexes = new ArrayList<>(); //TODO Priority Queue with highest degree.

    protected abstract double getValue(AbstractEdge e);

    public AbstractEdge[] bellman(final Item pFrom, final Item pTo) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        return null; //TODO implement.
    }

    protected Path graphSearch(PriorityQueue<Path> pq, Vertex goal) {
        Path result = null;
        while (!pq.isEmpty() && result == null) {
            final Path p = pq.poll();
            Vertex next = (Vertex) p.step.goal;
            if (next.isMarked()) continue;
            if (next == goal) result = p;
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

    public Path aStar(final Item pFrom, final Item pTo, final Function<V, Double> heuristic) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        final Function<Path, Double> assumedTotalCost = (p) -> p.totalCost + heuristic.apply(((Vertex) p.getCurrent()).value);
        final PriorityQueue<Path> pq = new PriorityQueue<>(
                (p1, p2) -> Double.compare(assumedTotalCost.apply(p1), assumedTotalCost.apply(p2)));
        from.edges.forEach(e -> pq.add(new Path(null, e, getValue(e))));
        return graphSearch(pq, to);
    }

    public Path dijkstra(final Item pFrom, final Item pTo) {
        checkMembership(pFrom, pTo);
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        final PriorityQueue<Path> pq = new PriorityQueue<>((p1, p2) -> Double.compare(p1.totalCost, p2.totalCost));
        from.edges.forEach(e -> pq.add(new Path(null, e, getValue(e))));
        return graphSearch(pq, to);
    }

    @Override
    public Item addVertex(V value) {
        Vertex v = new Vertex(value, this);
        vertexes.add(v);
        return v;
    }

    protected void checkMembership(Item item) {
        if (!(item instanceof GenericGraph.Vertex)) throw new IllegalArgumentException("Supplied Item is not a Vertex");
        Vertex v = (Vertex) item;
        if (v.graph != this) throw new IllegalArgumentException("Supplied Vertex not part of GenericGraph");

    }

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
