package com.hambbe.graph;

import com.hambbe.graph.AbstractGraph.Step;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class Graphs {

    /**
     * Helper function for different graph search implementations.
     * @param pFrom Vertex we are starting at.
     * @param pq Initialized priority queue.
     * @param pTo Vertex we are looking for.
     * @return Route to vertex, if exists. Null, otherwise.
     */
    protected static <V, K> List<Link> graphSearch(final AbstractGraph<V, K> graph, Graph.Item pFrom, PriorityQueue<Step> pq, Graph.Item pTo) {
        final AbstractGraph<V, K>.Vertex from = (AbstractGraph<V, K>.Vertex) pFrom;
        final AbstractGraph<V, K>.Vertex to = (AbstractGraph<V, K>.Vertex) pTo;
        if (from == to) return new LinkedList<>();
        from.edges.forEach(e -> pq.add(new Step(null, e, e.getWeight())));
        Step result = null;
        while (!pq.isEmpty() && result == null) {
            final Step p = pq.poll();
            final AbstractGraph<V, K>.Vertex next = (AbstractGraph<V, K>.Vertex) p.step.goal;
            if (next.isMarked()) continue;
            if (next == to) {
                result = p;
            } else {
                next.mark();
                next.edges.forEach(e -> pq.add(new Step(p, e, e.getWeight())));
            }
        }
        graph.vertexes.forEach(AbstractGraph.Vertex::demark);
        if (result == null) return null;

        // Build route between start and goal item
        LinkedList<Link> route = new LinkedList<>();
        for (Step prev = result; prev != null; prev = prev.prev) {
            route.addFirst(new Link(from, to, prev.step, prev.totalCost));
        }
        return route;
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
    public static <V, K> List<Link> aStar(final AbstractGraph<V, K> graph, final Graph.Item from, final Graph.Item to, final Function<V, Double> heuristic) {
        graph.checkMembership(from, to);
        final Function<Step, Double> assumedTotalCost = (p) ->
                p.totalCost
                + ((p.getCurrent() == null) ? 0 : heuristic.apply(((AbstractGraph<V, K>.Vertex) p.getCurrent()).value));
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
     * @param <K> Generic edge type
     * @return Route to item, if exists and there was no cycle with negative edges on the way. Null, otherwise.
     */
    public static <V, K> List<Link> bellmanFord(final AbstractGraph<V, K> graph, final Graph.Item pFrom, final Graph.Item pTo) {
        //graph.checkMembership(pFrom, pTo);
        HashMap<Graph.Item, BellmanFordNode> V = bellmanFord(graph, pFrom);

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
            route.addFirst(new Link(curr.from.value, curr.value, curr.via, curr.totalCost));
        }
        return route;
    }

    /**
     *
     * @param graph
     * @param pFrom
     * @param <V>
     * @param <K>
     * @return
     */
    protected static <V, K> HashMap<Graph.Item, BellmanFordNode> bellmanFord(final AbstractGraph<V, K> graph, final Graph.Item pFrom) {
        //graph.checkMembership(pFrom);
        final AbstractGraph.Vertex from = (AbstractGraph.Vertex) pFrom;

        HashMap<Graph.Item, BellmanFordNode> V = new HashMap<>();
        graph.vertexes.forEach(v -> V.put(v, new BellmanFordNode(null, null, null, (from == v) ? 0 : Double.MAX_VALUE)));

        for (int i = 0; i < graph.vertexes.size() - 1; i++) {
            for (AbstractGraph<V,K>.Vertex vx : graph.vertexes) {
                for (AbstractGraph.AbstractEdge e : vx.edges) {
                    BellmanFordNode u = V.get(vx);
                    BellmanFordNode v = V.get(e.goal);
                    if (u.totalCost + graph.getValue(e) < v.totalCost) {
                        V.put(e.goal, new BellmanFordNode(e.goal, u, e, graph.getValue(e)));
                    }
                }
            }
        }

        for (AbstractGraph<V,K>.Vertex vx : graph.vertexes) {
            for (AbstractGraph.AbstractEdge e : vx.edges) {
                BellmanFordNode u = V.get(vx);
                BellmanFordNode v = V.get(e.goal);
                if (u.totalCost + graph.getValue(e) < v.totalCost) {
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
        Graph.Item value;
        BellmanFordNode from;
        AbstractGraph.AbstractEdge via;
        double totalCost;
        public BellmanFordNode(AbstractGraph.Item value, BellmanFordNode from, AbstractGraph.AbstractEdge via, double totalCost) {
            this.value = value;
            this.from = from;
            this.via = via;
            this.totalCost = totalCost;
        }
    }

    public static class Link {
        public final Graph.Item from;
        public final Graph.Item to;
        public final Graph.AbstractEdge via;
        public final double totalCost;

        public Link(Graph.Item from, Graph.Item to, Graph.AbstractEdge via, double totalCost) {
            this.from = from;
            this.to = to;
            this.via = via;
            this.totalCost = totalCost;
        }
    }
}
