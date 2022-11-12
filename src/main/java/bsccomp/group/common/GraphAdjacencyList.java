package bsccomp.group.common;

import java.util.HashMap;
import java.util.HashSet;

public interface GraphAdjacencyList<T, U> {
    void addEdge(T node);
    void addNodeAndWeightConnected(T vertex,T destinationNode, U weight);
    HashSet<?> findAllDestinationNodeAndWeightsToAVertex(T node);
    void printAdjacencyList();
    HashMap<T, HashMap<T, U>> returnList();
}
