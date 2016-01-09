package com.hambbe.graph;

import com.hambbe.graph.AbstractGraph.Step;

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
    protected static <V, K> List<Step> graphSearch(final AbstractGraph<V, K> graph, Graph.Item pFrom, PriorityQueue<Step> pq, Graph.Item pTo) {
        final AbstractGraph<V, K>.Vertex from = (AbstractGraph<V, K>.Vertex) pFrom;
        final AbstractGraph<V, K>.Vertex to = (AbstractGraph<V, K>.Vertex) pTo;
        if (from == to) return new LinkedList<>();
        (from.edges).forEach(e -> pq.add(new Step(null, e, graph.getValue(e))));
        Step result = null;
        while (!pq.isEmpty() && result == null) {
            final Step p = pq.poll();
            AbstractGraph<V, K>.Vertex next = (AbstractGraph<V, K>.Vertex) p.step.goal;
            if (next.isMarked()) continue;
            if (next == to) {
                result = p;
            } else {
                next.mark();
                (next.edges).forEach(e -> pq.add(new Step(p, e, graph.getValue(e))));
            }
        }
        graph.vertexes.forEach(AbstractGraph.Vertex::demark);
        if (result == null) return null;

        // Build route between start and goal item
        LinkedList<Step> route = new LinkedList<>();
        for (Step prev = result; prev != null; prev = prev.prev) {
            route.addFirst(prev);
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
    public static <V, K> List<Step> aStar(final AbstractGraph<V, K> graph, final Graph.Item from, final Graph.Item to, final Function<V, Double> heuristic) {
        graph.checkMembership(from, to);
        final Function<Step, Double> assumedTotalCost = (p) ->
                p.totalCost
                + ((p.getCurrent() == null) ? 0 : heuristic.apply(((AbstractGraph<V, K>.Vertex) p.getCurrent()).value));
        final PriorityQueue<Step> pq = new PriorityQueue<>(
                (p1, p2) -> Double.compare(assumedTotalCost.apply(p1), assumedTotalCost.apply(p2)));
        return graphSearch(graph, from, pq, to);
    }


}
