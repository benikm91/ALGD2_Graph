package com.hambbe.graph.sample;

import com.hambbe.graph.Graph;
import com.hambbe.graph.Graph.Vertex;
import com.hambbe.graph.Graphs;
import com.hambbe.graph.Graphs.Link;
import com.hambbe.graph.UndirectedGraph;

/**
 * Created by Benjamin on 12.01.2016.
 */
public class Sample {

    public static void main(String[] args) {
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

        Vertex from = jeru;
        Vertex to = newYork;
        System.out.println("Note: Costs have switching cost added in Train and Airport class.");
        System.out.println("How to get from " + graph.getValue(from) + " to " + graph.getValue(to) + ": ");
        int i = 0;
        for (Link link : Graphs.<String, Transport>bellmanFord(graph, from, to)) {
            Transport edge = graph.getEdgeValue(link.getEdge());
            System.out.println(++i + ") " + graph.getValue(link.getFrom()) + "--" + edge.toString() + "-->" + graph.getValue(link.getTo()));
        }


    }

}
