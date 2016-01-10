package com.hambbe.graph.search;

import com.hambbe.graph.Graph;
import com.hambbe.graph.Graphs;
import com.hambbe.graph.IntGraph;
import com.hambbe.graph.data.TestData;

import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AStarTest {

    @Test
    public void testAStar() {
        final String fromValue = "A";
        final String toValue = "ACBX";
        final Function<String, Double> heuristic = TestData.ABCGraphOptimalHeuristic(toValue);
        IntGraph<String> graph = TestData.ABCintGraph(4);
        final Graph.Vertex from = graph.getItem(fromValue);
        final Graph.Vertex to = graph.getItem(toValue);
        List<Graphs.Link> route = Graphs.aStar(graph, from, to, heuristic);
        // Has found a path?
        assertNotNull("Expected: Reachable value gets found, Actual: Value not found.", route);
        assertTrue("Expected: At least 1 step, Actual: 0 steps.", route.size() > 0);
        // Is path optimal?
        assertTrue("Expected: Optimal path, Actual: Not optimal path.", route.size() == toValue.length() - fromValue.length());
    }

    @Test
    public void testNoPath() {
        final String fromValue = "A";
        final String toValue = "ACBX";
        final Function<String, Double> heuristic = TestData.ABCGraphOptimalHeuristic(toValue);
        IntGraph<String> graph = TestData.ABCintGraph(4);
        final Graph.Vertex from = graph.getItem(fromValue);
        final Graph.Vertex to = graph.getItem(toValue);
        // remove needed connection.
        final Graph.Vertex ac = graph.getItem("AC");
        graph.disconnect(from, ac);
        // No path found?
        List<Graphs.Link> route = Graphs.aStar(graph, from, to, heuristic);
        assertNull("Expected: No route found, Actual: route found.", route);
    }

}
