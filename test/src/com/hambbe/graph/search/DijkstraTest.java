package com.hambbe.graph.search;

import com.hambbe.graph.DirectedGraph;
import com.hambbe.graph.Graphs;
import com.hambbe.graph.Vertex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DijkstraTest {

    class Test {
        int number;
        String name;

        public Test(int number, String name) {
            this.number = number;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @org.junit.Test
    public void testDijkstra() throws Exception {
        DirectedGraph<Test, Integer> graph = new DirectedGraph<>(Integer::doubleValue);

        Vertex H = graph.addVertex(new Test(12, "Hambbe"));
        Vertex I = graph.addVertex(new Test(43, "ist"));
        Vertex J = graph.addVertex(new Test(34, "Jaja"));
        Vertex D = graph.addVertex(new Test(56, "der"));
        Vertex E = graph.addVertex(new Test(34, "Wie bitte?"));
        Vertex W = graph.addVertex(new Test(45, "wahre"));
        Vertex K = graph.addVertex(new Test(42, "König"));

        graph.connect(H,I,1);
        graph.connect(I,D,1);
        graph.connect(D,W,3);
        graph.connect(W,K,1);
        graph.connect(H,J,1);
        graph.connect(J,K,7);
        graph.connect(I,E,6);
        graph.connect(E,K,1);

        List<Graphs.Link> route = Graphs.dijkstra(graph, H, K);

        assertNotNull(route);

        String result = graph.getValue(route.get(0).getFrom()).name + " " + route.stream().map(s -> graph.getValue(s.getTo()).name).reduce((s,a) -> s + " " + a).get();

        assertEquals("Hambbe ist der wahre König", result);
    }

    @org.junit.Test
    public void testDijkstra1() throws Exception {
        DirectedGraph<Test, Integer> graph = new DirectedGraph<>(Integer::doubleValue);

        Vertex H = graph.addVertex(new Test(12, "Hambbe"));
        Vertex I = graph.addVertex(new Test(786, "ist"));
        Vertex J = graph.addVertex(new Test(34, "Jaja"));
        Vertex D = graph.addVertex(new Test(56, "der"));
        Vertex E = graph.addVertex(new Test(34, "Wie bitte?"));
        Vertex W = graph.addVertex(new Test(45, "wahre"));
        Vertex K = graph.addVertex(new Test(42, "König"));

        graph.connect(H,I,1);
        graph.connect(I,D,5);
        graph.connect(D,W,3);
        graph.connect(W,K,1);
        graph.connect(H,J,1);
        graph.connect(J,K,7);
        graph.connect(I,E,1);
        graph.connect(E,K,1);

        HashMap<Vertex, LinkedList<Graphs.Link>> routes = Graphs.dijkstra(graph, H);

        assertNotNull(routes);

        HashMap<Vertex, Double> expectedTotalValue = new HashMap<>();
        expectedTotalValue.put(I, 1.0);
        expectedTotalValue.put(J, 1.0);
        expectedTotalValue.put(K, 3.0);
        expectedTotalValue.put(E, 2.0);
        expectedTotalValue.put(D, 6.0);
        expectedTotalValue.put(W, 9.0);

        Iterator<Map.Entry<Vertex, LinkedList<Graphs.Link>>> it = routes.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<Vertex, LinkedList<Graphs.Link>> pair = it.next();
            if (pair.getKey() != H) {
                assertTrue(Math.abs(pair.getValue().getLast().getTotalCost() - expectedTotalValue.get(pair.getKey())) < 0.0000000001);
            } else {
                // if starting vertex: the path should be empty!
                assertTrue(pair.getValue().isEmpty());
            }
        }
    }
}