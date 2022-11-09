package bsccomp.group.common;

import java.util.HashMap;
import java.util.HashSet;

public interface GraphAdjacencyList<T> {
    void addEdge(T node);
    void addNodeAndWeightConnected(T vertex,T destinationNode, int weight);
    HashSet<?> findAllDestinationNodeAndWeightsToAVertex(T node);
    void printAdjacencyList();
    HashMap<T, HashMap<T, Integer>> get();
}
