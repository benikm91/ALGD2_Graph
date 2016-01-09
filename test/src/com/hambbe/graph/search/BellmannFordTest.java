package com.hambbe.graph.search;

import com.hambbe.graph.DiGenericGraph;
import com.hambbe.graph.Graph;
import com.hambbe.graph.IGraph;
import junit.framework.TestCase;

import org.junit.Test;

import java.util.List;

public class BellmannFordTest extends TestCase {

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
    public void testBellmanFord() throws Exception {
        DiGenericGraph<Test, Integer> graph = new DiGenericGraph<>(e -> e.doubleValue());

        IGraph.Item H = graph.addVertex(new Test(12, "Hambbe"));
        IGraph.Item I = graph.addVertex(new Test(786, "ist"));
        IGraph.Item J = graph.addVertex(new Test(34, "Jaja"));
        IGraph.Item D = graph.addVertex(new Test(56, "der"));
        IGraph.Item E = graph.addVertex(new Test(34, "Wie bitte?"));
        IGraph.Item W = graph.addVertex(new Test(45, "wahre"));
        IGraph.Item K = graph.addVertex(new Test(42, "König"));

        graph.connect(H,I,1);
        graph.connect(I,D,5);
        graph.connect(D,W,3);
        graph.connect(W,K,-10);
        graph.connect(H,J,1);
        graph.connect(J,K,7);
        graph.connect(I,E,1);
        graph.connect(E,K,1);

        List<Graph.Step> route = graph.bellmanFord(H,K);

        assertNotNull("No route found!", route);

        String res = route.stream().map(s -> graph.getValue(s.getCurrentGoal()).name).reduce((s,a) -> s + " " + a).get();

        assertEquals("Not expected result!", "Hambbe ist der wahre König", res);

    }

    @org.junit.Test
    public void testBellmanFord1() throws Exception {
        // TODO: test :)
    }

}