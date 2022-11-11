package bsccomp.group.common;

import bsccomp.group.common.models.Edge;
import bsccomp.group.common.models.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class GraphAdjacencyListImpl implements GraphAdjacencyList<Vertex> {
    protected HashMap<Vertex, HashMap<Vertex, Edge>> adjacencyList;

    public GraphAdjacencyListImpl() {
        adjacencyList = new LinkedHashMap<>();
    }

    @Override
    public void addEdge(Vertex node) {
        adjacencyList.put(node, new LinkedHashMap<>());
    }

    @Override
    public void addNodeAndWeightConnected(Vertex vertex, Vertex destinationNode, Edge weight) {
        adjacencyList.get(vertex).put(destinationNode, weight);
    }

    @Override
    public HashSet<?> findAllDestinationNodeAndWeightsToAVertex(Vertex node) {
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.containsKey(node)) {
                return new HashSet<>(adjacencyList.values());
            }
        }
        return null;
    }

    @Override
    public void printAdjacencyList() {
        for (Vertex key: this.adjacencyList.keySet()) {
            System.out.println("\nNode " + key.getName()
                    + " makes an edge with ");
            this.adjacencyList.get(key).forEach((key1, value) -> System.out.println(
                    "\tNode " + key1.getName()
                            + " with edge weight "
                            + value.getWeight() + " "));
        }
    }

    @Override
    public HashMap<Vertex, HashMap<Vertex, Edge>> returnList() {
        return adjacencyList;
    }
}
