package com.hambbe.graph.sample.genericedge;

import com.hambbe.graph.Graph;
import com.hambbe.graph.Graphs;
import com.hambbe.graph.Graphs.Link;
import com.hambbe.graph.UndirectedGraph;
import com.hambbe.graph.Vertex;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Benjamin on 12.01.2016.
 */
public class Sample {

    public static void main(String[] args) {
        /**
         * Sample program with following steps:
         * - createing and connection graph.
         * - Finding shortest path of from and to with an algorithm of Graphs (feel free to change for testing).
         * - Echo all shortest costs from dijkstra & bellmanFord (should be equal).
         */
        // Createing and connecting graph (Connections are alphabetic ordered).
        Graph<String, Transport> graph = new UndirectedGraph<String, Transport>(t -> (double) t.getCost());
        Vertex newYork = graph.addVertex("New York");
        Vertex berlin = graph.addVertex("Berlin");
        Vertex paris = graph.addVertex("Paris");
        Vertex moskau = graph.addVertex("Moskau");
        Vertex basel = graph.addVertex("Basel");
        Vertex tokyo = graph.addVertex("Tokyo");
        Vertex jeru = graph.addVertex("Jerusalem");
        Vertex rome = graph.addVertex("Rome");
        Vertex braz = graph.addVertex("Brazilia");
        // basel
        graph.connect(basel, berlin, new Train(100));
        graph.connect(basel, paris, new Train(100));
        graph.connect(basel, rome, new Train(100));
        graph.connect(basel, jeru, new Train(150));
        graph.connect(basel, tokyo, new Airplane(500));
        graph.connect(basel, moskau, new Train(200));
        // berlin
        graph.connect(berlin, newYork, new Airplane(500));
        graph.connect(berlin, moskau, new Train(300));
        graph.connect(berlin, paris, new Train(200));
        // braz
        graph.connect(braz, newYork, new Airplane(200));
        graph.connect(braz, rome, new Airplane(450));
        // jeru
        // new york
        graph.connect(newYork, tokyo, new Airplane(350));
        // moskau
        // paris
        // rome
        // tokyo

        // Find & print fastest path from from to to.
        final Vertex from = jeru;
        final Vertex to = newYork;
        System.out.println("Note: Costs have switching cost added in Train and Airport class.");
        System.out.println("How to get from " + graph.getValue(from) + " to " + graph.getValue(to) + ": ");
        int i = 0;
        for (Link link : Graphs.bellmanFord(graph, from, to)) {
            Transport edge = graph.getEdgeValue(link.getEdge());
            System.out.println(++i + ") " + graph.getValue(link.getFrom()) + "--" + edge.toString() + "-->" + graph.getValue(link.getTo()));
        }

        System.out.println("---");

        // Find & print all costs from from to all other vertexes.
        System.out.println("Find shortest routes to all other cities from " + graph.getValue(from) + " (dijkstra)");
        Graphs.dijkstra(graph, from).values().forEach(
                route -> {
                    if (!route.isEmpty()) {
                        Link last = route.getLast();
                        System.out.println(graph.getValue(last.getTo()) + ": " + last.getTotalCost());
                    }
                }
        );

        System.out.println("---");

        System.out.println("Find shortest routes to all other cities from " + graph.getValue(from) + " (bellmanFord)");
        HashMap<Vertex, LinkedList<Link>> routes = Graphs.bellmanFord(graph, from);
        if (routes == null) System.out.println("bellman returned null");
        else routes.values().forEach(
                route -> {
                    if (!route.isEmpty()) {
                        Link last = route.getLast();
                        System.out.println(graph.getValue(last.getTo()) + ": " + last.getTotalCost());
                    }
                }
        );
    }

}
