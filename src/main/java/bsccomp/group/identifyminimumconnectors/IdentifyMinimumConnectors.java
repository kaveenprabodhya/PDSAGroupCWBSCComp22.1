package bsccomp.group.identifyminimumconnectors;

import bsccomp.group.common.Base;
import bsccomp.group.common.GraphAdjacencyList;
import bsccomp.group.common.Vertex;

public class IdentifyMinimumConnectors extends Base {
    private final GraphAdjacencyList<Vertex> adjacencyList;

    public IdentifyMinimumConnectors(GraphAdjacencyList<Vertex> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public void findMinimumConnectors(){
        // create a graph for find minimum connectors
        this.createGraph(adjacencyList);
        this.adjacencyList.printAdjacencyList();
        // select the shortest edge connected to the vertex

        // select the shortest edge connect to any vertex already connected
        // Repeat until all vertices have been connected
    }
}
