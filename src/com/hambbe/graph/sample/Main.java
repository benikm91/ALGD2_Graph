package com.hambbe.graph.sample;

import com.hambbe.graph.DiGenericGraph;
import com.hambbe.graph.GenericGraph;
import com.hambbe.graph.Item;

public class Main {

    public static void main(String[] args) {
        DiGenericGraph<City, Transport> graph = new DiGenericGraph<>(Transport::getCost);
        Item basel = graph.addVertex(new City("Basel"));
        Item zuerich = graph.addVertex(new City("Zuerich"));
        Item berlin = graph.addVertex(new City("Berlin"));
        Item rom = graph.addVertex(new City("Rom"));
        graph.connect(basel, zuerich, new Train(94));
        graph.connect(zuerich, rom, new Train(132));
        graph.connect(basel, berlin, new Train(189));
        graph.connect(berlin, rom, new Train(356));
        GenericGraph.Path p = graph.dijkstraSearch(basel, rom);
        while(p.prev != null) {
            System.out.print(p.vertex.getValue() + " <--" + p.edge + "-- ");
            p = p.prev;
        }
        System.out.print(basel.getValue());
    }

}
