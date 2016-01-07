package com.hambbe.graph;

import java.util.*;
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
     * @param pFrom Vertex we are starting at.
     * @param pq Initialized priority queue.
     * @param pTo Vertex we are looking for.
     * @return Route to vertex, if exists. Null, otherwise.
     */
    protected List<Step> graphSearch(Item pFrom, PriorityQueue<Step> pq, Item pTo) {
        final Vertex from = (Vertex) pFrom;
        final Vertex to = (Vertex) pTo;
        if (from == to) return new LinkedList<>();
        from.edges.forEach(e -> pq.add(new Step(null, e, getValue(e))));
        Step result = null;
        while (!pq.isEmpty() && result == null) {
            final Step p = pq.poll();
            Vertex next = (Vertex) p.step.goal;
            if (next.isMarked()) continue;
            if (next == to) {
                result = p;
            } else {
                next.mark();
                next.edges.forEach(e -> pq.add(new Step(p, e, getValue(e))));
            }
        }
        this.vertexes.forEach(Vertex::demark);
        if (result == null) return null;

        // Build route between start and goal item
        LinkedList<Step> route = new LinkedList<>();
        for (Step prev = result; prev != null; prev = prev.prev) {
            route.addFirst(prev);
        }
        return route;
    }

    /**
     *
     * @param pFrom Stating item
     * @param pTo Goal Item
     * @return
     */
    public List<Step> bellmanFord(final Item pFrom, final Item pTo)
    {
        checkMembership(pFrom, pTo);
        HashMap<Item, Step> V = bellmanFord(pFrom);

        // Return null if the bellman-ford algorithm wasn't successful
        if (V == null) {
            return null;
        }

        // Return null if the goal is unreachable from the given start item
        if (V.get(pTo).totalCost == Double.MAX_VALUE) {
            return null;
        }

        // Build route between start and goal item
        LinkedList<Step> route = new LinkedList<>();
        for (Step curr = V.get(pTo); curr != null; curr = V.get(curr).prev) { //TODO V.get(curr) = curr?
            route.addFirst(curr);
        }
        return route;
    }

    /**
     * Runtime complexity: O(|V|*|E|)
     *
     * @param pFrom Start item.
     * @return
     */
    public HashMap<Item, Step> bellmanFord(final Item pFrom) {
        checkMembership(pFrom);
        final Vertex from = (Vertex) pFrom;

        HashMap<Item, Step> V = new HashMap<>();
        vertexes.forEach(v -> V.put(v, new Step(null, null, (from == v) ? 0 : Double.MAX_VALUE)));

        for (int i = 0; i < vertexes.size() - 1; i++) {
            for (Vertex vx : vertexes) {
                for (AbstractEdge e : vx.edges) {
                    Step u = V.get(vx);
                    Step v = V.get(e.goal);
                    if (u.totalCost + getValue(e) < v.totalCost) {
                        V.put(e.goal, new Step(u, e, u.totalCost + getValue(e)));
                    }
                }
            }
        }

        for (Vertex vx : vertexes) {
            for (AbstractEdge e : vx.edges) {
                Step u = V.get(vx);
                Step v = V.get(e.goal);
                if (u.totalCost + getValue(e) < v.totalCost) {
                    return null;
                }
            }
        }

        return V;
    }


    /**
     * Greedy search algorithm implementation. //TODO check name.
     *
     * It finds an existing path.
     * It does not guarantee the optimal path.
     *
     * @param from Start item.
     * @param to Goal item.
     * @param heuristic Heuristic function for prioritizing items.
     * @return Route to item, if exists. Null, otherwise.
     */
    public List<Step> greedySearch(final Item from, final Item to, final Function<V, Double> heuristic) {
        checkMembership(from, to);
        final Function<Step, Double> greedy = (p) -> heuristic.apply(((Vertex) p.getCurrent()).value);
        final PriorityQueue<Step> pq = new PriorityQueue<>(
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
     * @param from Start item.
     * @param to Goal item.
     * @param heuristic Heuristic function for helping to prioritize items.
     * @return Route to item, if exists. Null, otherwise.
     */
    public List<Step> aStar(final Item from, final Item to, final Function<V, Double> heuristic) {
        checkMembership(from, to);
        final Function<Step, Double> assumedTotalCost = (p) ->
             p.totalCost
             + ((p.getCurrent() == null) ? 0 : heuristic.apply(((Vertex) p.getCurrent()).value));
        final PriorityQueue<Step> pq = new PriorityQueue<>(
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
     * @param from Start item.
     * @param to Goal item.
     * @return Route to item, if exists. Null, otherwise.
     */
    public List<Step> dijkstra(final Item from, final Item to) {
        checkMembership(from, to);
        final PriorityQueue<Step> pq = new PriorityQueue<>((p1, p2) -> Double.compare(p1.totalCost, p2.totalCost));
        return graphSearch(from, pq, to);
    }

    @Override
    public Item addVertex(V value) {
        Vertex v = new Vertex(value, this);
        vertexes.add(v);
        return v;
    }

    /**
     * @param value Value of item to get.
     * @return First item found with value. Null, if no item was found.
     */
    public Item getItem(V value) {
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
        assert item != null;
        if (!(item instanceof Graph.Vertex)) throw new IllegalArgumentException("Supplied Item is not a Vertex");
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

    // TODO implement PathNode. Iterator for path.
    public static class Step {
        public final Step prev;
        public final AbstractEdge step;
        public final double totalCost;

        public Step(Step prev, AbstractEdge step, double cost) {
            this.prev = prev;
            this.step = step;
            this.totalCost = ((prev == null) ? 0 : prev.totalCost) + cost;
        }

        public Item getCurrent() {
            return (prev == null) ? null : prev.step.goal;
        }
    }

    public static abstract class AbstractEdge {
        protected final Item goal; // TODO: type Vertex not better here?

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
