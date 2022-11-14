package bsccomp.group.identifyminimumconnectors;

import bsccomp.group.common.Base;
import bsccomp.group.common.GraphAdjacencyList;
import bsccomp.group.common.models.Edge;
import bsccomp.group.common.models.Pair;
import bsccomp.group.common.models.Vertex;

import java.util.*;

public class IdentifyMinimumConnectors extends Base {
    private final GraphAdjacencyList<Vertex, Edge> adjacencyList;
    private final List<Pair<Vertex, List<Pair<Vertex, Edge>>>> answerList;
    private final List<Vertex> visitedVertexList;

    public IdentifyMinimumConnectors(GraphAdjacencyList<Vertex, Edge> adjacencyList) {
        this.adjacencyList = adjacencyList;
        this.answerList = new LinkedList<>();
        visitedVertexList = new LinkedList<>();
    }

    public void findMinimumConnectors() {
        // create a graph for find minimum connectors
        this.createGraph(adjacencyList);
        this.displayGraph(this.adjacencyList);
        // select any vertex
        Map.Entry<Vertex, HashMap<Vertex, Edge>> mapEntry = this.adjacencyList.returnList().entrySet().iterator().next();
        Vertex startVertex = mapEntry.getKey();
        if (this.answerList.isEmpty()) {
            startVertex.setVisited(true);
            visitedVertexList.add(startVertex);
            // select shortest edge connected to the vertex
            this.findShortestEdgeOfStaterVertex(startVertex);
        }
        // select the shortest edge connected to any vertex already connected
        while (isDisconnected()) {
            this.findNextShortestEdge();
        }
        System.out.println();
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> v : this.answerList) {
            for (Pair<Vertex, Edge> vE : v.getValue()) {
                System.out.println(v.getKey().getName() + " " +
                        v.getKey().isVisited() + " " +
                        vE.getKey().getName() + " " +
                        vE.getKey().isVisited() + " " +
                        vE.getValue().getWeight() + " " +
                        vE.getValue().isIncluded());
            }
        }
    }

    private void findNextShortestEdge() {
        List<Pair<Vertex, Edge>> collectionOfLinkedVertices = new LinkedList<>();
        List<Pair<Vertex, Edge>> temps = new LinkedList<>();

        for (Vertex vertex : visitedVertexList) {
            for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : this.adjacencyList.returnList().entrySet()) {
                if (hashMapEntry.getKey().equals(vertex)) {
                    // get connected vertex and edges for given vertex
                    this.getConnectedVerticesForGiven(vertex, collectionOfLinkedVertices);
                    // get other links for given vertex
                    this.getOtherLinkedVerticesForGiven(vertex, collectionOfLinkedVertices);
                }
            }
        }
        System.out.println("\nFilter only non included edges");
        for (Pair<Vertex, Edge> vertexEdgeEntry : collectionOfLinkedVertices) {
            if (!vertexEdgeEntry.getValue().isIncluded()) {
                System.out.println(
                        vertexEdgeEntry.getKey().getName() + " " +
                                vertexEdgeEntry.getKey().isVisited() + " " +
                                vertexEdgeEntry.getValue().getWeight() + " " +
                                vertexEdgeEntry.getValue().isIncluded());
                temps.put(vertexEdgeEntry.getKey(), vertexEdgeEntry.getValue());

            }
        }
        // find the min clear the temps
        Map.Entry<Vertex, Edge> minKeyVertexWithEdge = this.findMinimumKey(temps);
        Vertex key = minKeyVertexWithEdge.getKey();
        Edge edge = minKeyVertexWithEdge.getValue();
        // find the parent matching key and edge
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : this.adjacencyList.returnList().entrySet()) {
            for (Map.Entry<Vertex, Edge> vertexEdgeEntry : hashMapEntry.getValue().entrySet()) {
                if (vertexEdgeEntry.getKey().getName().equalsIgnoreCase(key.getName())
                        && (vertexEdgeEntry.getValue().getWeight() == edge.getWeight())) {
                    key.setVisited(true);
                    edge.setIncluded(true);
                    List<Pair<Vertex, Edge>> pairList = new LinkedList<>();
                    pairList.add(new Pair<>(key, edge));
                    System.out.println(
                            "City of Top " + hashMapEntry.getKey().getName() +
                                    " Bottom city " + vertexEdgeEntry.getKey().getName() +
                                    " Bottom Edge " + vertexEdgeEntry.getValue().getWeight()
                    );
                    answerList.add(new Pair<>(hashMapEntry.getKey(), pairList));
                    visitedVertexList.add(key);
                }
                if (hashMapEntry.getKey().getName().equalsIgnoreCase(key.getName())
                        && (vertexEdgeEntry.getValue().getWeight() == edge.getWeight())) {
                    hashMapEntry.getKey().setVisited(true);
                    edge.setIncluded(true);
                    List<Pair<Vertex, Edge>> pairList = new LinkedList<>();
                    pairList.add(new Pair<>(key, edge));
                    System.out.println(
                            "Top city " + vertexEdgeEntry.getKey().getName() +
                                    " Bottom city " + key.getName() +
                                    " Bottom Edge " + vertexEdgeEntry.getValue().getWeight()
                    );
                    answerList.add(new Pair<>(vertexEdgeEntry.getKey(), pairList));
                    visitedVertexList.add(hashMapEntry.getKey());
                }
            }
        }
        System.out.println("Found next minimum key: " + key.getName() +
                " key selected: " + key.isVisited() +
                " edge: " + edge.getWeight() +
                " edge included " + edge.isIncluded()
        );
        temps.clear();
    }

    private void findShortestEdgeOfStaterVertex(Vertex startVertex) {
        this.run(startVertex);
    }

    private void run(Vertex vertex) {
        HashMap<Vertex, Edge> mapOfLinkedVerticesForGiven = new LinkedHashMap<>();
        // get connected vertex and edges for given vertex
        this.getConnectedVerticesForGiven(vertex, mapOfLinkedVerticesForGiven);
        // get other links for given vertex
        this.getOtherLinkedVerticesForGiven(vertex, mapOfLinkedVerticesForGiven);
        // find min edge get its key
        Map.Entry<Vertex, Edge> minKeyVertexWithEdge = this.findMinimumKey(mapOfLinkedVerticesForGiven);
        System.out.println("Found minimum key: " + minKeyVertexWithEdge.getKey().getName());
        this.findAndUpdateDirectlyConnectedOrLinkedVertex(vertex, minKeyVertexWithEdge.getKey());
    }

    //find minimum key
    private Map.Entry<Vertex, Edge> findMinimumKey(HashMap<Vertex, Edge> map) {
        Map.Entry<Vertex, Edge> key = Collections.min(map.entrySet(),
                Comparator.comparingInt(value -> value.getValue().getWeight()));
        List<String> listOfEqualsWeights = new LinkedList<>();
        // find if map holding same weight keys if true get the minimum based on alphabetical order
        for (Map.Entry<Vertex, Edge> vertexEdgeEntry : map.entrySet()) {
            if (key.getValue().getWeight() == vertexEdgeEntry.getValue().getWeight()) {
                listOfEqualsWeights.add(vertexEdgeEntry.getKey().getName());
            }
        }
        if (listOfEqualsWeights.size() > 1) {
            Collections.sort(listOfEqualsWeights);
            String keyName = listOfEqualsWeights.get(0);
            for (Map.Entry<Vertex, Edge> vertexEdgeEntry : map.entrySet()) {
                if (keyName.equalsIgnoreCase(vertexEdgeEntry.getKey().getName())) {
                    key = vertexEdgeEntry;
                }
            }
        }
        return key;
    }

    private void findAndUpdateDirectlyConnectedOrLinkedVertex(Vertex vertex, Vertex key) {
        // one of the keys are not directly connected in adjList, have to find the link
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : this.adjacencyList.returnList().entrySet()) {
            if (hashMapEntry.getKey().equals(vertex)) {
                for (Map.Entry<Vertex, Edge> vertexEdgeEntry : hashMapEntry.getValue().entrySet()) {
                    if (key.equals(vertexEdgeEntry.getKey())) {
                        Edge edge = this.adjacencyList.returnList().get(hashMapEntry.getKey()).get(key);
                        edge.setIncluded(true);
                        key.setVisited(true);
                        HashMap<Vertex, Edge> hashMap = new HashMap<>();
                        hashMap.put(key, edge);
                        answerList.put(hashMapEntry.getKey(), hashMap);
                        visitedVertexList.add(key);
                    }
                }
            }
            if (key.equals(hashMapEntry.getKey())) {
                for (Map.Entry<Vertex, Edge> vertexEdgeEntry : hashMapEntry.getValue().entrySet()) {
                    if (vertex.equals(vertexEdgeEntry.getKey())) {
                        Edge edge = this.adjacencyList.returnList().get(hashMapEntry.getKey()).get(vertex);
                        edge.setIncluded(true);
                        key.setVisited(true);
                        HashMap<Vertex, Edge> hashMap = new HashMap<>();
                        hashMap.put(key, edge);
                        System.out.println(vertexEdgeEntry.getKey().getName() + " -- " + vertexEdgeEntry.getKey().isVisited());
                        answerList.put(vertexEdgeEntry.getKey(), hashMap);
                        visitedVertexList.add(key);
                    }
                }
            }
        }
    }

    // get subset of linked vertices
    private void getOtherLinkedVerticesForGiven(Vertex vertex, List<Pair<Vertex, Edge>> temps) {
        Set<Map.Entry<Vertex, Edge>> entries = this.checkForOtherLinks(vertex).entrySet();
        if (!entries.isEmpty()) {
            for (Map.Entry<Vertex, Edge> entry : entries) {
                temps.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }
    }

    // get subset of directly connected vertices
    private void getConnectedVerticesForGiven(Vertex vertex, List<Pair<Vertex, Edge>> temps) {
        Set<Map.Entry<Vertex, Edge>> entries = this.adjacencyList.returnList().get(vertex).entrySet();
        if (!entries.isEmpty()) {
            for (Map.Entry<Vertex, Edge> entry : entries) {
                temps.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }
    }

    // check for other vertices that have links to a given vertex
    private HashMap<Vertex, Edge> checkForOtherLinks(Vertex vertex) {
        HashMap<Vertex, Edge> temps = new LinkedHashMap<>();
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : this.adjacencyList.returnList().entrySet()) {
            System.out.println("Top level City Name()- " + hashMapEntry.getKey().getName());
            if (!hashMapEntry.getKey().getName().equalsIgnoreCase(vertex.getName())) {
                for (Map.Entry<Vertex, Edge> vertexEdgeEntry : hashMapEntry.getValue().entrySet()) {
                    if (vertexEdgeEntry.getKey().getName().equalsIgnoreCase(vertex.getName())) {
                        System.out.println("Bottom Level City Name()- " +
                                vertexEdgeEntry.getKey().getName() +
                                " Bottom level city isVisited()- " +
                                vertexEdgeEntry.getKey().isVisited() +
                                "  vertex.getName()- " + vertex.getName() +
                                "  Top level city Name()- " + hashMapEntry.getKey().getName() +
                                "  Top level city isVisited()- " + hashMapEntry.getKey().isVisited() +
                                " edge- " + vertexEdgeEntry.getValue().getWeight() +
                                " isSelected- " + vertexEdgeEntry.getValue().isIncluded());
                        temps.put(hashMapEntry.getKey(), vertexEdgeEntry.getValue());
                    }
                }
            }
        }
        System.out.println();
        System.out.println("Other Links");
        for (Map.Entry<Vertex, Edge> vertexEdgeEntry : temps.entrySet()) {
            System.out.println("Key: " + vertexEdgeEntry.getKey().getName() +
                    " Val: " + vertexEdgeEntry.getValue().getWeight());
        }
        return temps;
    }

    // check until all vertices get visited
    private boolean isDisconnected() {
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : this.adjacencyList.returnList().entrySet()) {
            if (!hashMapEntry.getKey().isVisited()) {
                return true;
            }
        }
        return false;
    }
}
