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
    private Vertex startNode;

    public IdentifyMinimumConnectors(GraphAdjacencyList<Vertex, Edge> adjacencyList) {
        this.adjacencyList = adjacencyList;
        answerList = new LinkedList<>();
        visitedVertexList = new LinkedList<>();
    }

    public void findMinimumConnectors() {
        // create a graph for find minimum connectors
        createGraph(adjacencyList);
        displayGraph(adjacencyList, true);
        // select any vertex
        Map.Entry<Vertex, HashMap<Vertex, Edge>> mapEntry = adjacencyList.returnList().entrySet().iterator().next();
        Vertex startVertex = mapEntry.getKey();
        if (answerList.isEmpty()) {
            startVertex.setVisited(true);
            this.setStartNode(startVertex);
            visitedVertexList.add(startVertex);
        }
        // select the shortest edge connected to any vertex already connected
        while (isDisconnected()) {
            this.findNextShortestEdge();
        }
        System.out.println();
//        int count = 0;
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> v : answerList) {
            for (Pair<Vertex, Edge> vE : v.getValue()) {
                System.out.println(
                        v.getKey().getName() + ", " +
//                                v.getKey().isVisited() + " " +
                                vE.getKey().getName() + " ----> " +
//                                vE.getKey().isVisited() + " " +
                                vE.getValue().getWeight() + " "
//                                + vE.getValue().isIncluded()
                );
//                count++;
            }
        }
//        System.out.println("answers: " + count);
    }

    public Vertex getStartNode() {
        return startNode;
    }

    public void setStartNode(Vertex startNode) {
        this.startNode = startNode;
    }

    // calculate Minimum distance
    public int calculateMinimumDistance() {
        int distance = 0;
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : answerList) {
            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                distance += vertexEdgePair.getValue().getWeight();
            }
        }
        return distance;
    }

    private void findNextShortestEdge() {
        List<Pair<Vertex, List<Pair<Vertex, Edge>>>> collectionOfLinkedVertices = new LinkedList<>();
        List<Pair<Vertex, List<Pair<Vertex, Edge>>>> temps = new LinkedList<>();

        for (Vertex vertex : visitedVertexList) {
//            for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : adjacencyList.returnList().entrySet()) {
//                if (hashMapEntry.getKey().equals(vertex)) {
                    // get connected vertex and edges for given vertex
                    this.getConnectedVerticesForGiven(vertex, collectionOfLinkedVertices, adjacencyList);
                    // get other links for given vertex
                    this.getOtherLinkedVerticesForGiven(vertex, collectionOfLinkedVertices, adjacencyList);
//                }
//            }
        }
        this.getNonIncludedEdges(collectionOfLinkedVertices, temps);

//        System.out.println("\nafter remove found minimum values= " + temps.size());
//        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : temps) {
//            System.out.println("vertexListPair.getValue().size(): " + vertexListPair.getValue().size());
//            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
//                System.out.println(
//                        "Visited vertex " + vertexListPair.getKey().getName() + " " +
//                                vertexListPair.getKey().isVisited() + " of " +
//                                vertexEdgePair.getKey().getName() + " visited " +
//                                vertexEdgePair.getKey().isVisited() + " of weight " +
//                                vertexEdgePair.getValue().getWeight() + " include " +
//                                vertexEdgePair.getValue().isIncluded()
//                );
//            }
//        }
        // find the min clear the temps
//        System.out.println("\nFind minimum");
        Pair<Vertex, Pair<Vertex, Edge>> minPair = this.findMinimumKey(temps);

//        System.out.println(
//                "\nFound minimum of visited vertex " + minPair.getKey().getName() +
//                        " selected " + minPair.getKey().isVisited() +
//                        " Found next minimum " + minPair.getValue().getKey().getName() +
//                        " and key selected " + minPair.getValue().getKey().isVisited() +
//                        " edge " + minPair.getValue().getValue().getWeight() +
//                        " edge included " + minPair.getValue().getValue().isIncluded()
//        );
//        System.out.println("\nmark the vertex as visited and edge included");
        // find the parent matching key and edge add to answer list
        this.findMatchingParentNodeAndAddToAnswerList(minPair);
        temps.clear();
    }

    private void filterEdgesWhichHasBothVisitedVertices(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> temps) {
//        System.out.println("\nFilter out edges has both vertices visited");

//        System.out.println("\nEdges that has both visited nodes");
//        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : temps) {
//            System.out.println("temps getValue().size(): " + vertexListPair.getValue().size());
//            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
//                System.out.println(
//                        "Visited vertex " + vertexListPair.getKey().getName() + " " +
//                                vertexListPair.getKey().isVisited() + " of " +
//                                vertexEdgePair.getKey().getName() + " visited " +
//                                vertexEdgePair.getKey().isVisited() + " of weight " +
//                                vertexEdgePair.getValue().getWeight() + " include " +
//                                vertexEdgePair.getValue().isIncluded()
//                );
//            }
//        }

        List<Pair<Vertex, Edge>> foundPairsOfVisited = new LinkedList<>();
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : temps) {
            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                if (vertexListPair.getKey().isVisited() && vertexEdgePair.getKey().isVisited()) {
//                    System.out.println("Removed -> Opposite city " + vertexListPair.getKey().getName() +
//                            " for city " + vertexEdgePair.getKey().getName() +
//                            " and edge " + vertexEdgePair.getValue().getWeight() + " pair."
//                    );
                    vertexEdgePair.getValue().setIncluded(true);
                    foundPairsOfVisited.add(vertexEdgePair);
                }
            }
        }

        if (!foundPairsOfVisited.isEmpty()) {
            for (Pair<Vertex, Edge> foundEdgePair : foundPairsOfVisited) {
                for (Pair<Vertex, List<Pair<Vertex, Edge>>> temp : temps) {
                    temp.getValue().remove(foundEdgePair);
                }
            }
        }

        // remove visited vertex holding empty pair list
        temps.removeIf(vertexListPair -> vertexListPair.getValue().isEmpty());
        // if temps got empty
        if (temps.isEmpty()) {
            this.findMinimumConnectors();
        }
    }

    private void getNonIncludedEdges(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> collectionOfLinkedVertices,
                                     List<Pair<Vertex, List<Pair<Vertex, Edge>>>> temps) {
//        System.out.println("\nFilter only non included edges");
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : collectionOfLinkedVertices) {
            List<Pair<Vertex, Edge>> pairList = new LinkedList<>();
            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                if (!vertexEdgePair.getValue().isIncluded()) {
//                    System.out.println(
//                            "visited vertex " + vertexListPair.getKey().getName() + " has " +
//                                    vertexEdgePair.getKey().getName() + " " +
//                                    vertexEdgePair.getKey().isVisited() + " " +
//                                    vertexEdgePair.getValue().getWeight() + " " +
//                                    vertexEdgePair.getValue().isIncluded());
                    pairList.add(new Pair<>(vertexEdgePair.getKey(), vertexEdgePair.getValue()));
                }
            }
            if (!pairList.isEmpty())
                temps.add(new Pair<>(vertexListPair.getKey(), pairList));
        }
        this.filterEdgesWhichHasBothVisitedVertices(temps);
    }

    private Pair<Vertex, Pair<Vertex, Edge>> findMinimumKey(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> map) {
        List<Pair<Vertex, Pair<Vertex, Edge>>> tempsOfMinimums = new LinkedList<>();
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> pairList : map) {
            Pair<Vertex, Edge> key = Collections.min(pairList.getValue(),
                    Comparator.comparingInt(value -> value.getValue().getWeight()));
            List<String> listOfEqualsWeights = new LinkedList<>();
            // find if map holding same weight keys if true get the minimum based on alphabetical order
            for (Pair<Vertex, Edge> vertexEdgeEntry : pairList.getValue()) {
                if (key.getValue().getWeight() == vertexEdgeEntry.getValue().getWeight()) {
                    listOfEqualsWeights.add(vertexEdgeEntry.getKey().getName());
                }
            }
            if (listOfEqualsWeights.size() > 1) {
                Collections.sort(listOfEqualsWeights);
                String keyName = listOfEqualsWeights.get(0);
                for (Pair<Vertex, Edge> vertexEdgeEntry : pairList.getValue()) {
                    if (keyName.equalsIgnoreCase(vertexEdgeEntry.getKey().getName())) {
                        key = vertexEdgeEntry;
                    }
                }
            }
            tempsOfMinimums.add(new Pair<>(pairList.getKey(), key));
        }
//        for (Pair<Vertex, Pair<Vertex, Edge>> vertexListPair : tempsOfMinimums) {
//            System.out.println(
//                    "Temps of minimum Visited vertex: " + vertexListPair.getKey().getName() +
//                            " Found minimum vertex already connected: " + vertexListPair.getValue().getKey().getName() +
//                            " minimum edge " + vertexListPair.getValue().getValue().getWeight()
//            );
//        }
        return Collections.min(tempsOfMinimums,
                Comparator.comparingInt(value -> value.getValue().getValue().getWeight()));
    }

    private void findMatchingParentNodeAndAddToAnswerList(Pair<Vertex, Pair<Vertex, Edge>> minPair) {
        Vertex visitedVertex = minPair.getKey();
        Vertex minVertex = minPair.getValue().getKey();
        Edge minEdge = minPair.getValue().getValue();
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : adjacencyList.returnList().entrySet()) {
            for (Map.Entry<Vertex, Edge> vertexEdgeEntry : hashMapEntry.getValue().entrySet()) {
                if (vertexEdgeEntry.getKey().getName().equalsIgnoreCase(minVertex.getName())
                        && hashMapEntry.getKey().getName().equalsIgnoreCase(visitedVertex.getName())
                        && (vertexEdgeEntry.getValue().getWeight() == minEdge.getWeight())) {
                    minVertex.setVisited(true);
                    minEdge.setIncluded(true);
                    List<Pair<Vertex, Edge>> pairList = new LinkedList<>();
                    pairList.add(new Pair<>(minVertex, minEdge));
//                    System.out.println(
//                            "City of Top " + hashMapEntry.getKey().getName() +
//                                    " Bottom city " + vertexEdgeEntry.getKey().getName() +
//                                    " Bottom Edge " + vertexEdgeEntry.getValue().getWeight()
//                    );
                    answerList.add(new Pair<>(hashMapEntry.getKey(), pairList));
                    visitedVertexList.add(minVertex);
                }
                if (hashMapEntry.getKey().getName().equalsIgnoreCase(minVertex.getName())
                        && vertexEdgeEntry.getKey().getName().equalsIgnoreCase(visitedVertex.getName())
                        && (vertexEdgeEntry.getValue().getWeight() == minEdge.getWeight())) {
                    hashMapEntry.getKey().setVisited(true);
                    minEdge.setIncluded(true);
                    List<Pair<Vertex, Edge>> pairList = new LinkedList<>();
                    pairList.add(new Pair<>(minVertex, minEdge));
//                    System.out.println(
//                            "Top city " + vertexEdgeEntry.getKey().getName() +
//                                    " Bottom city " + minVertex.getName() +
//                                    " Bottom Edge " + vertexEdgeEntry.getValue().getWeight()
//                    );
                    answerList.add(new Pair<>(vertexEdgeEntry.getKey(), pairList));
                    visitedVertexList.add(hashMapEntry.getKey());
                }
            }
        }
    }

    // check until all vertices get visited
    private boolean isDisconnected() {
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : adjacencyList.returnList().entrySet()) {
            if (!hashMapEntry.getKey().isVisited()) {
                return true;
            }
        }
        return false;
    }

    public List<Pair<Vertex, List<Pair<Vertex, Edge>>>> getAnswerList() {
        return answerList;
    }
}
