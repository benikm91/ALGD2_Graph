package com.hambbe.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class Graphs {

    /**
     * Helper function for different graph search implementations.
     * @param pFrom VertexImpl we are starting at.
     * @param pq Initialized priority queue.
     * @param pTo VertexImpl we are looking for.
     * @return Route to vertex, if exists. Null, otherwise.
     */
    protected static <V, E> List<Link> graphSearch(final AbstractGraph<V, E> graph, Graph.Vertex pFrom, PriorityQueue<Step> pq, Graph.Vertex pTo) {
        final AbstractGraph<V, E>.VertexImpl from = (AbstractGraph<V, E>.VertexImpl) pFrom;
        final AbstractGraph<V, E>.VertexImpl to = (AbstractGraph<V, E>.VertexImpl) pTo;
        if (from == to) return new LinkedList<>();
        from.edges.forEach(e -> pq.add(new Step(null, e, e.getWeight())));
        Step result = null;
        while (!pq.isEmpty() && result == null) {
            final Step p = pq.poll();
            final AbstractGraph<V, E>.VertexImpl next = (AbstractGraph<V, E>.VertexImpl) p.step.to;
            if (next.isMarked()) continue;
            if (next == to) {
                result = p;
            } else {
                next.mark();
                next.edges.forEach(e -> pq.add(new Step(p, e, e.getWeight())));
            }
        }
        graph.vertexes.forEach(AbstractGraph.VertexImpl::demark);
        if (result == null) return null;

        // Build route between start and to item
        LinkedList<Link> route = new LinkedList<>();
        for (Step prev = result; prev != null; prev = prev.prev) {
            route.addFirst(new Link(prev.step, prev.totalCost));
        }
        return route;
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
    public static <V, E> List<Link> greedySearch(final AbstractGraph<V, E> graph, final Graph.Vertex from, final Graph.Vertex to, final Function<V, Double> heuristic) {
        graph.checkMembership(from, to);
        final Function<Step, Double> greedy = (p) -> heuristic.apply(((AbstractGraph<V, E>.VertexImpl) p.getCurrent()).value);
        final PriorityQueue<Step> pq = new PriorityQueue<>(
                (p1, p2) -> Double.compare(greedy.apply(p1), greedy.apply(p2)));
        return graphSearch(graph, from, pq, to);
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
    public static <V, E> List<Link> dijkstra(final AbstractGraph<V, E> graph, final Graph.Vertex from, final Graph.Vertex to) {
        graph.checkMembership(from, to);
        final PriorityQueue<Step> pq = new PriorityQueue<>((p1, p2) -> Double.compare(p1.totalCost, p2.totalCost));
        return graphSearch(graph, from, pq, to);
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
    public static <V, E> List<Link> aStar(final AbstractGraph<V, E> graph, final Graph.Vertex from, final Graph.Vertex to, final Function<V, Double> heuristic) {
        graph.checkMembership(from, to);
        final Function<Step, Double> assumedTotalCost = (p) ->
                p.totalCost
                + ((p.getCurrent() == null) ? 0 : heuristic.apply(((AbstractGraph<V, E>.VertexImpl) p.getCurrent()).value));
        final PriorityQueue<Step> pq = new PriorityQueue<>(
                (p1, p2) -> Double.compare(assumedTotalCost.apply(p1), assumedTotalCost.apply(p2)));
        return graphSearch(graph, from, pq, to);
    }


    /**
     * Bellman Ford algorithm implementation
     *
     * Finds the shortest path between two vertices, if there is one.
     * Other then the Dijkstra algorithm, Bellman Ford also works with negative
     * edge values. The trade-off is a runtime complexity of O(|V|*|E|).
     * The algorithm fails if there is no path between the two vertices or
     * a cycle with negative total values has been found.
     *
     * @param graph The graph
     * @param pFrom Starting vertex
     * @param pTo Goal vertex
     * @param <V> Generic vertex type
     * @param <E> Generic edge type
     * @return Route to item, if exists and there was no cycle with negative edges on the way. Null, otherwise.
     */
    public static <V, E> List<Link> bellmanFord(final Graph<V, E> graph, final Graph.Vertex pFrom, final Graph.Vertex pTo) {
        HashMap<Graph.Vertex, BellmanFordNode> V = bellmanFordSearch(graph, pFrom);

        // Return null if the bellman-ford algorithm wasn't successful
        if (V == null) {
            return null;
        }

        // Return null if the goal is unreachable from the given start item
        if (V.get(pTo).totalCost == Double.MAX_VALUE) {
            return null;
        }

        // Build route between start and goal item
        LinkedList<Link> route = new LinkedList<>();
        for (BellmanFordNode curr = V.get(pTo); curr != null; curr = curr.from) {
            route.addFirst(new Link(curr.via, curr.totalCost));
        }
        return route;
    }

    /**
     * Bellman Ford algorithm implementation
     *
     * Finds the shortest path between a vertex and all other edges.
     * Other then the Dijkstra algorithm, Bellman Ford also works with negative
     * edge values. The trade-off is a runtime complexity of O(|V|*|E|).
     * The algorithm fails if there is a cycle with negative total values has been found.
     *
     * @param graph The graph
     * @param pFrom Starting vertex
     * @param <V> Generic vertex type
     * @param <K> Generic edge type
     * @return List routes to all reachable items, if there was no cycle with negative edges on the way. Null, otherwise.
     */
    public static <V, K> List<List<Link>> bellmanFord(final Graph<V, K> graph, final Graph.Vertex pFrom) {
        HashMap<Graph.Vertex, BellmanFordNode> V = bellmanFordSearch(graph, pFrom);

        // Return null if the bellman-ford algorithm wasn't successful
        if (V == null) {
            return null;
        }

        List<List<Link>> routes = new LinkedList<>();

        for (Graph.Vertex vertex : graph.getVertexes()) {
            // Skip if the goal is unreachable from the given start item
            if (V.get(vertex).totalCost == Double.MAX_VALUE || vertex.equals(pFrom)) {
                continue;
            }

            // Build route between start and goal item
            LinkedList<Link> route = new LinkedList<>();
            for (BellmanFordNode curr = V.get(vertex); curr != null; curr = curr.from) {
                route.addFirst(new Link(curr.via, curr.totalCost));
            }
            routes.add(route);
        }

        return routes;
    }

    /**
     *
     * @param graph
     * @param pFrom
     * @param <V>
     * @param <E>
     * @return
     */
    protected static <V, E> HashMap<Graph.Vertex, BellmanFordNode> bellmanFordSearch(final Graph<V, E> graph, final Graph.Vertex pFrom) {
        HashMap<Graph.Vertex, BellmanFordNode> V = new HashMap<>();
        graph.getVertexes().forEach(v -> V.put(v, new BellmanFordNode(null, null, null, (pFrom == v) ? 0 : Double.MAX_VALUE)));

        for (int i = 0; i < graph.getVertexes().size() - 1; i++) {
            for (Graph.Vertex vertex : graph.getVertexes()) {
                for (Graph.Edge e : vertex.getEdges()) {
                    BellmanFordNode u = V.get(vertex);
                    BellmanFordNode v = V.get(e.getTo());
                    if (u.totalCost + e.getWeight() < v.totalCost) {
                        V.put(e.getTo(), new BellmanFordNode(e.getTo(), u, e, u.totalCost + e.getWeight()));
                    }
                }
            }
        }

        for (Graph.Vertex vertex : graph.getVertexes()) {
            for (Graph.Edge e : vertex.getEdges()) {
                BellmanFordNode u = V.get(vertex);
                BellmanFordNode v = V.get(e.getTo());
                if (u.totalCost + e.getWeight() < v.totalCost) {
                    return null;
                }
            }
        }
        return V;
    }

    /**"
     *
     */
    protected static class BellmanFordNode {
        Graph.Vertex value;
        BellmanFordNode from;
        Graph.Edge via;
        double totalCost;
        public BellmanFordNode(Graph.Vertex value, BellmanFordNode from, Graph.Edge via, double totalCost) {
            this.value = value;
            this.from = from;
            this.via = via;
            this.totalCost = totalCost;
        }
    }

    public static class Link {
        public final Graph.Edge via;
        public final double totalCost;

        public Link(Graph.Edge via, double totalCost) {
            assert via != null : "Edge can't be null.";
            this.via = via;
            this.totalCost = totalCost;
        }

        /**
         * @return Get Item from where the link starts.
         */
        public Graph.Vertex getFrom() {
            return this.via.getFrom();
        }

        /**
         * @return Get Item where the link ends.
         */
        public Graph.Vertex getTo() {
            return this.via.getTo();
        }

        /**
         * @return Get the connection {@link #getFrom()} and {@link #getTo()} have.
         */
        public Graph.Edge getVia() {
            return via;
        }

        /**
         * @return Total costs from the start to {@link #getFrom()}
         */
        public double getTotalCost() {
            return this.totalCost;
        }

        /**
         * @return Get costs from {@link #getFrom()} to {@link #getTo()})
         */
        public double getCost() {
            return this.via.getWeight();
        }

    }


    // TODO implement PathNode. Iterator for path.
    public static class Step {
        public final Step prev;
        public final AbstractGraph.AbstractEdge step;
        public final double totalCost;

        public Step(Step prev, AbstractGraph.AbstractEdge step, double cost) {
            this.prev = prev;
            this.step = step;
            this.totalCost = ((prev == null) ? 0 : prev.totalCost) + cost; //TODO: i was confused :) really really confused - until I found this line :)
        }

        public Graph.Vertex getCurrent() {
            return (prev == null) ? null : prev.getGoal();
        }

        public Graph.Vertex getGoal() {
            return (step == null) ? null : step.to;
        }
    }

}
