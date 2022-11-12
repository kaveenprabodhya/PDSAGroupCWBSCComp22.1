package bsccomp.group.identifyshortestpath;

import bsccomp.group.common.Base;
import bsccomp.group.common.GraphAdjacencyList;
import bsccomp.group.common.models.Edge;
import bsccomp.group.common.models.Vertex;

public class IdentifyShortestPath extends Base {
    private final GraphAdjacencyList<Vertex, Edge> adjacencyList;

    public IdentifyShortestPath(GraphAdjacencyList<Vertex, Edge> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
}
