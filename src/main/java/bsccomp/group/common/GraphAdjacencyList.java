package bsccomp.group.common;

import java.util.HashSet;

public interface GraphAdjacencyList<T> {
    public void addEdge(T node);
    public void addNodeAndWeightConnected(T vertex,T destinationNode, int weight);
    public HashSet findAllDestinationNodeAndWeightsToAVertex(T node);
    public void printAdjacencyList();
}
