package com.hambbe.graph.test;

import com.hambbe.graph.IGraph;
import com.hambbe.graph.IntGraph;

import java.util.function.Function;
import java.util.stream.IntStream;

public class TestData {

    public static class AStarTestData {
        public final IntGraph<String> graph;
        public final IGraph.Item from;
        public final IGraph.Item to;
        public final Function<String, Double> heuristic;

        public AStarTestData(final IntGraph<String> graph, final IGraph.Item from, final IGraph.Item to, final Function<String, Double> heuristic) {
            this.graph = graph;
            this.from = from;
            this.to = to;
            this.heuristic = heuristic;
        }

    }

    private static IGraph.Item generateNext(IntGraph<String> graph, String current, int remaining) {
        if (remaining == 0) return null;
        IGraph.Item parent = graph.addVertex(current);
        for (char c = 'A'; c < 'Z'; c++) {
            IGraph.Item child = generateNext(graph, current + c, remaining - 1);
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

        final IGraph.Item from = graph.getItem("A");
        final IGraph.Item to = graph.getItem("AA");
        assert from != null;
        assert to != null;
        return new AStarTestData(graph, from, to, heuristic);
    }

}
