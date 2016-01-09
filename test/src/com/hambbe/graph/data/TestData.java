package com.hambbe.graph.data;

import com.hambbe.graph.Graph;
import com.hambbe.graph.IntGraph;

import java.util.function.Function;
import java.util.stream.IntStream;

public class TestData {

    public static class AStarTestData {
        public final IntGraph<String> graph;
        public final Graph.Item from;
        public final Graph.Item to;
        public final Function<String, Double> heuristic;

        public AStarTestData(final IntGraph<String> graph, final Graph.Item from, final Graph.Item to, final Function<String, Double> heuristic) {
            this.graph = graph;
            this.from = from;
            this.to = to;
            this.heuristic = heuristic;
        }

    }

    private static Graph.Item generateNext(IntGraph<String> graph, String current, int remaining) {
        if (remaining == 0) return null;
        Graph.Item parent = graph.addVertex(current);
        for (char c = 'A'; c < 'Z'; c++) {
            Graph.Item child = generateNext(graph, current + c, remaining - 1);
            if (child != null) {
                graph.connect(parent, child, 1);
            }
        }
        return parent;
    }

    public static AStarTestData getTestIntGraph() {
        final int toSum = 'A' + 'A' + 'B';
        final Function<String, Double> heuristic = (s) -> (double) (toSum) - IntStream.range(0, s.length()).map(i -> s.charAt(i)).sum();
        final IntGraph<String> graph = new IntGraph<>();

        generateNext(graph, "A", 2);

        final Graph.Item from = graph.getItem("A");
        final Graph.Item to = graph.getItem("AA");
        assert from != null;
        assert to != null;
        return new AStarTestData(graph, from, to, heuristic);
    }

}
