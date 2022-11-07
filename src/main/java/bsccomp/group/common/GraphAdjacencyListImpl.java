package bsccomp.group.common;

import java.util.HashMap;
import java.util.HashSet;

public class GraphAdjacencyListImpl implements GraphAdjacencyList<String> {
    protected HashMap<Object, HashMap<Object, Integer>> adjacencyList;

    public GraphAdjacencyListImpl() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void addEdge(String node) {
        adjacencyList.put(node, new HashMap<>());
    }

    @Override
    public void addNodeAndWeightConnected(String vertex, String destinationNode, int weight) {
        adjacencyList.get(vertex).put(destinationNode, weight);
    }

    @Override
    public HashSet<?> findAllDestinationNodeAndWeightsToAVertex(String node) {
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.containsKey(node)) {
                return new HashSet<>(adjacencyList.values());
            }
        }
        return null;
    }

    @Override
    public void printAdjacencyList() {
        for (Object key: this.adjacencyList.keySet()) {
            System.out.println("\nNode " + key
                    + " makes an edge with ");
            this.adjacencyList.get(key).entrySet().forEach(
                        e -> System.out.println(
                                "\tNode " + e.getKey()
                                        + " with edge weight "
                                        + e.getValue() + " "));
        }
    }
}
