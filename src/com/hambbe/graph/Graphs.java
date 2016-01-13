package com.hambbe.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

/**
 * Contains implementations for known graph search algorithms.
 * Is not Thread save.
 */
public class Graphs {

    /**
     * Helper function for different graph search implementations.
     * Uses helper class {@link com.hambbe.graph.Graphs.Step} for finding path,
     * but returns List of Link values for the user.
     * Implementation is searching from pFrom only.
     *
     * @param graph Graph to search in.
     * @param pFrom VertexImpl we are starting at.
     * @param pq Initialized priority queue according to current search algorithm.
     * @param pTo VertexImpl we are looking for.
     * @param <V> Value type of vertex in graph.
     * @param <E> Value type of edge in graph.
     * @return Route of vertexes, if exists. Empty, if to == from. Null, otherwise.
     */
    protected static <V, E> List<Link> graphSearch(final Graph<V, E> graph, final Graph.Vertex pFrom, final PriorityQueue<Step> pq, final Graph.Vertex pTo) {
        final AbstractGraph<V, E>.VertexImpl from = (AbstractGraph<V, E>.VertexImpl) pFrom;
        final AbstractGraph<V, E>.VertexImpl to = (AbstractGraph<V, E>.VertexImpl) pTo;
        if (from == to) return new LinkedList<>(); // nothing must be do, to reach to.
        // Add init values (neighbours of from).
        from.edges.forEach(e -> pq.add(new Step(null, e, e.getWeight())));
        Step result = null;
        while (!pq.isEmpty() && result == null) {
            // Get best candidate for search.
            final Step currentStep = pq.poll();
            final AbstractGraph<V, E>.VertexImpl next = (AbstractGraph<V, E>.VertexImpl) currentStep.edge.getTo();
            if (next.isMarked()) continue;
            if (next == to) {
                result = currentStep;
            } else {
                next.mark();
                // add children of currentStep to PriorityQueue.
                next.edges.forEach(e -> pq.add(new Step(currentStep, e, e.getWeight())));
            }
        }
        // Clean up - Delete marking on vertexes.
        graph.getVertexes().forEach(Graph.Vertex::demark);
        if (result == null) return null; // Nothing found.

        // Build route between start and to item
        LinkedList<Link> route = new LinkedList<>();
        // result is actually last step of the hole path, so we go back and add all steps at the front, therefore last step automatically moves to the end of the list.
        for (Step prev = result; prev != null; prev = prev.prev) {
            route.addFirst(new Link(prev.edge, prev.totalCost));
        }
        return route;
    }

    /**
     * Greedy Best-first search algorithm implementation.
     *
     * It finds an existing path.
     * It does not guarantee the optimal path.
     *
     * @param graph Graph to search in.
     * @param from Start item.
     * @param to Goal item.
     * @param heuristic Heuristic function for prioritizing items.
     * @return Route to item, if exists. Null, otherwise.
     */
    public static <V, E> List<Link> bestFirstSearch(final Graph<V, E> graph, final Graph.Vertex from, final Graph.Vertex to, final Function<V, Double> heuristic) {
        final Function<Step, Double> greedy = (p) -> heuristic.apply(((AbstractGraph<V, E>.VertexImpl) p.getFrom()).value);
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
     * @param graph Graph to search in.
     * @param from Start item.
     * @param to Goal item.
     * @return Route to item, if exists. Null, otherwise.
     */
    public static <V, E> List<Link> dijkstra(final Graph<V, E> graph, final Graph.Vertex from, final Graph.Vertex to) {
        final PriorityQueue<Step> pq = new PriorityQueue<>((p1, p2) -> Double.compare(p1.totalCost, p2.totalCost));
        return graphSearch(graph, from, pq, to);
    }

    /**
     *
     * @param graph Graph to search in.
     * @param pFrom Vertex to search from.
     * @param <V> Type of vertex in graph
     * @param <E> Type of edges in graph.
     * @return
     *  HashMap with all shortest paths from pFrom to all vertexes in the graph. Vertex is the key to get to the path.
     *  If a vertex is not reachable there is no path to it.
     */
    public static <V, E> HashMap<Graph.Vertex, LinkedList<Link>> dijkstra(final Graph<V, E> graph, final Graph.Vertex pFrom) {
        final HashMap<Graph.Vertex, Step> shortestPaths = new HashMap<>();
        final PriorityQueue<Step> pq = new PriorityQueue<>((p1, p2) -> Double.compare(p1.totalCost, p2.totalCost));

        final Step init = new Step(null, null, 0);
        shortestPaths.put(pFrom, init);

        // add neighbours from pFrom
        pFrom.getEdges().forEach(e -> pq.add(new Step(init, e, e.getWeight())));

        // Search all shortest paths and store them into V.
        while (!pq.isEmpty()) {
            // Get best candidate for search.
            final Step currentStep = pq.poll();
            final Graph.Vertex currentStepTo = currentStep.edge.getTo();
            Step oldStep = shortestPaths.get(currentStepTo);
            if (oldStep == null || currentStep.totalCost < oldStep.totalCost) {
                // override oldStep with currentStep.
                shortestPaths.put(currentStepTo, currentStep);
                // add children of currentStep to PriorityQueue.
                currentStepTo.getEdges().forEach(e -> pq.add(new Step(currentStep, e, e.getWeight())));
            }
        }

        // Prepare result.
        HashMap<Graph.Vertex, LinkedList<Link>> result = new HashMap<>();
        graph.getVertexes().forEach(vertex -> {
            // Build route between start and vertex
            LinkedList<Link> route = new LinkedList<>();
            for (Step step = shortestPaths.get(vertex); step.prev != null; step = step.prev) {
                route.addFirst(new Link(step.edge, step.totalCost));
            }
            result.put(vertex, route);
        });
        return result;
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
    public static <V, E> List<Link> aStar(final Graph<V, E> graph, final Graph.Vertex from, final Graph.Vertex to, final Function<V, Double> heuristic) {
        final Function<Step, Double> assumedTotalCost = (p) ->
                p.totalCost
                + ((p.getFrom() == null) ? 0 : heuristic.apply(graph.getValue(p.getFrom())));
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
        HashMap<Graph.Vertex, Step> V = bellmanFordSearch(graph, pFrom);

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
        for (Step curr = V.get(pTo); curr.prev != null; curr = curr.prev) {
            route.addFirst(new Link(curr.edge, curr.totalCost));
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
    public static <V, K> HashMap<Graph.Vertex, LinkedList<Link>> bellmanFord(final Graph<V, K> graph, final Graph.Vertex pFrom) {
        HashMap<Graph.Vertex, Step> V = bellmanFordSearch(graph, pFrom);

        // Return null if the bellman-ford algorithm wasn't successful
        if (V == null) {
            return null;
        }

        HashMap<Graph.Vertex, LinkedList<Link>> routes = new HashMap<>();

        for (Graph.Vertex vertex : graph.getVertexes()) {
            // Skip if the goal is unreachable from the given start item
            if (V.get(vertex).totalCost == Double.MAX_VALUE || vertex.equals(pFrom)) {
                continue;
            }

            // Build route between start and goal item
            LinkedList<Link> route = new LinkedList<>();
            for (Step curr = V.get(vertex); curr.prev != null; curr = curr.prev) {
                route.addFirst(new Link(curr.edge, curr.totalCost));
            }
            routes.put(vertex, route);
        }

        return routes;
    }

    /**
     * @param graph The graph
     * @param pFrom Starting vertex
     * @param <V> Generic vertex type
     * @param <E> Generic edge type
     * @return HashMap with all shortest paths from pFrom to all vertexes in the graph. Vertex is the key to get to the path.
     */
    private static <V, E> HashMap<Graph.Vertex, Step> bellmanFordSearch(final Graph<V, E> graph, final Graph.Vertex pFrom) {
        HashMap<Graph.Vertex, Step> V = new HashMap<>();
        graph.getVertexes().forEach(v -> V.put(v, new Step(null, null, (pFrom == v) ? 0 : Double.MAX_VALUE)));

        for (int i = 0; i < graph.getVertexCount() - 1; i++) {
            for (Graph.Vertex vertex : graph.getVertexes()) {
                for (Graph.Edge e : vertex.getEdges()) {
                    Step u = V.get(vertex);
                    Step v = V.get(e.getTo());
                    if (u.totalCost + e.getWeight() < v.totalCost) {
                        V.put(e.getTo(), new Step(u, e, e.getWeight()));
                    }
                }
            }
        }

        for (Graph.Vertex vertex : graph.getVertexes()) {
            for (Graph.Edge e : vertex.getEdges()) {
                Step u = V.get(vertex);
                Step v = V.get(e.getTo());
                if (u.totalCost + e.getWeight() < v.totalCost) {
                    return null;
                }
            }
        }
        return V;
    }

    /**
     * Link is used to give a user all the data he needs after a graph search algorithms terminates.
     *
     * Link represents a step in the graph from one vertex to another using a edge as well as a cost.
     * A link also know the cost previous links had to get to this link.
     */
    public static class Link {
        /** Edge used. */
        private final Graph.Edge edge;
        /** Total costs including current link. */
        private final double totalCost;

        /**
         * @param edge Field value.
         * @param totalCost Field value.
         */
        protected Link(final Graph.Edge edge, final double totalCost) {
            assert edge != null : "Edge can't be null.";
            this.edge = edge;
            this.totalCost = totalCost;
        }

        /**
         * @return Get Item from where the link starts.
         */
        public Graph.Vertex getFrom() {
            return this.edge.getFrom();
        }

        /**
         * @return Get Item where the link ends.
         */
        public Graph.Vertex getTo() {
            return this.edge.getTo();
        }

        /**
         * @return Get the connection {@link #getFrom()} and {@link #getTo()} have.
         */
        public Graph.Edge getEdge() {
            return edge;
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
            return this.edge.getWeight();
        }

    }

    /**
     * Helper class for several graph searches.
     */
    protected static class Step {

        /** Previous step. How did we end up here! */
        public final Step prev;

        /** Current step to use. */
        public final Graph.Edge edge;

        /** Total costs including current step. */
        public final double totalCost;

        /**
         * @param prev Previous path.
         * @param edge Current edge.
         * @param cost Cost of this step.
         */
        public Step(final Step prev, final Graph.Edge edge, final double cost) {
            this.prev = prev;
            this.edge = edge;
            this.totalCost = ((prev == null) ? 0 : prev.totalCost) + cost;
        }

        public Graph.Vertex getFrom() {
            return (edge == null) ? null : edge.getFrom();
        }

        public Graph.Vertex getTo() {
            return (edge == null) ? null : edge.getTo();
        }
    }

}
