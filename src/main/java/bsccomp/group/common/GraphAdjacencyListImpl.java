package bsccomp.group.common;

import java.util.HashMap;
import java.util.HashSet;

public class GraphAdjacencyListImpl implements GraphAdjacencyList<Vertex> {
    protected HashMap<Vertex, HashMap<Vertex, Integer>> adjacencyList;

    public GraphAdjacencyListImpl() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void addEdge(Vertex node) {
        adjacencyList.put(node, new HashMap<>());
    }

    @Override
    public void addNodeAndWeightConnected(Vertex vertex, Vertex destinationNode, int weight) {
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
                            + value + " "));
        }
    }

    @Override
    public HashMap<Vertex, HashMap<Vertex, Integer>> get() {
        return adjacencyList;
    }
}
