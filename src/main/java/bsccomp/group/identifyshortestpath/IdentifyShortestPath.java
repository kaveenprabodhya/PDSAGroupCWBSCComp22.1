package bsccomp.group.identifyshortestpath;

import bsccomp.group.common.Base;
import bsccomp.group.common.GraphAdjacencyList;
import bsccomp.group.common.models.Vertex;

public class IdentifyShortestPath extends Base {
    private final GraphAdjacencyList<Vertex> adjacencyList;

    public IdentifyShortestPath(GraphAdjacencyList<Vertex> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
}
