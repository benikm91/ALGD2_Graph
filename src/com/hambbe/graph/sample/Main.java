package com.hambbe.graph.sample;

import com.hambbe.graph.Graph;
import com.hambbe.graph.Graphs;
import com.hambbe.graph.IntGraph;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        IntGraph<String> graph = new IntGraph<>();
        Graph.Vertex basel = graph.addVertex("Basel");
        Graph.Vertex berlin = graph.addVertex("Berlin");
        Graph.Vertex rome = graph.addVertex("Rome");
        Graph.Vertex jerusalem = graph.addVertex("Jerusalem");
        Graph.Vertex london = graph.addVertex("London");
        Graph.Vertex tokyo = graph.addVertex("Tokyo");
        Graph.Vertex newyork = graph.addVertex("New York");

        graph.connect(tokyo, berlin, 100);
        graph.connect(jerusalem, tokyo, 50);
        graph.connect(newyork, tokyo, 100);
        graph.connect(tokyo, newyork, 100);
        graph.connect(rome, tokyo, 25);
        graph.connect(london, berlin, 20);
        graph.connect(berlin, london, 22);
        graph.connect(basel, berlin, 20);
        graph.connect(berlin, basel, 10);
        graph.connect(basel, newyork, 100);
        graph.connect(newyork, berlin, 40);
        graph.connect(berlin, newyork, 45);

        List<Graphs.Link> route = Graphs.dijkstra(graph, basel, tokyo);
        System.out.print(graph.getValue(basel));
        double lastCost = 0;
        for (Graphs.Link s : route) {
            System.out.print(" -" + (s.totalCost - lastCost) + "-> " + graph.getValue(s.getTo()));
            lastCost = s.totalCost;
        }
        System.out.print(" ( = " + lastCost + " )" );

    }

}
