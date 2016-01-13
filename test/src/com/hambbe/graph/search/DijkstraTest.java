package com.hambbe.graph.search;

import com.hambbe.graph.DirectedGraph;
import com.hambbe.graph.Graph;
import com.hambbe.graph.Graphs;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Christian on 13.01.2016.
 */
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
        DirectedGraph<Test, Integer> graph = new DirectedGraph<>(e -> e.doubleValue());

        Graph.Vertex H = graph.addVertex(new Test(12, "Hambbe"));
        Graph.Vertex I = graph.addVertex(new Test(43, "ist"));
        Graph.Vertex J = graph.addVertex(new Test(34, "Jaja"));
        Graph.Vertex D = graph.addVertex(new Test(56, "der"));
        Graph.Vertex E = graph.addVertex(new Test(34, "Wie bitte?"));
        Graph.Vertex W = graph.addVertex(new Test(45, "wahre"));
        Graph.Vertex K = graph.addVertex(new Test(42, "König"));

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
}