package bsccomp.group.common;

import bsccomp.group.common.models.Edge;

import java.util.HashMap;
import java.util.HashSet;

public interface GraphAdjacencyList<T> {
    void addEdge(T node);
    void addNodeAndWeightConnected(T vertex,T destinationNode, Edge weight);
    HashSet<?> findAllDestinationNodeAndWeightsToAVertex(T node);
    void printAdjacencyList();
    HashMap<T, HashMap<T, Edge>> returnList();
}
