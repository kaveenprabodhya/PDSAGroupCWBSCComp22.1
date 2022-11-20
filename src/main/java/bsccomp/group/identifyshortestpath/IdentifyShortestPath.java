package bsccomp.group.identifyshortestpath;

import bsccomp.group.common.Base;
import bsccomp.group.common.GraphAdjacencyList;
import bsccomp.group.common.models.Edge;
import bsccomp.group.common.models.Pair;
import bsccomp.group.common.models.Vertex;

import java.util.*;

public class IdentifyShortestPath extends Base {
    private final GraphAdjacencyList<Vertex, Edge> adjacencyList;
    //1. vertex 2. previous vertex 3. distance from the source to vertex
    private final List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> table;
    private final Queue<Vertex> nextMinVertexToTaken;
    private Vertex sourceVertex;

    public IdentifyShortestPath(GraphAdjacencyList<Vertex, Edge> adjacencyList) {
        this.adjacencyList = adjacencyList;
        nextMinVertexToTaken = new LinkedList<>();
        table = new LinkedList<>();
    }

    public void findShortestPath() {
        createGraph(adjacencyList);
        displayGraph(adjacencyList, true);
        // select any vertex
        Map.Entry<Vertex, HashMap<Vertex, Edge>> mapEntry = adjacencyList.returnList().entrySet().iterator().next();
        Vertex sourceVertex = mapEntry.getKey();
        sourceVertex.setVisited(true);
        this.setSourceVertex(sourceVertex);
        initializeTable();

        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
            if (tablePair.getKey().equals(sourceVertex)) {
                tablePair.getValue().getValue().setValue(true);
            }
        }

        List<Pair<Vertex, List<Pair<Vertex, Edge>>>> list = new LinkedList<>();

        this.getConnectedVerticesForGiven(sourceVertex, list, adjacencyList);
        this.getOtherLinkedVerticesForGiven(sourceVertex, list, adjacencyList);

        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
                    if (tablePair.getKey().equals(vertexEdgePair.getKey())) {
                        tablePair.setValue(
                                new Pair<>(vertexListPair.getKey(),
                                        new Pair<>(vertexEdgePair.getValue().getWeight(), false)));
                    }
                }
            }
        }

//        System.out.println("\nprint table");
//        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
//            System.out.println(
//                    "vertex: " + tablePair.getKey().getName() +
//                            " previous: " + (Objects.isNull(tablePair.getValue().getKey()) ? "null" : tablePair.getValue().getKey().getName()) +
//                            " distance from source " + (Objects.isNull(tablePair.getValue().getValue()) ? "null" : tablePair.getValue().getValue().getKey()) +
//                            " taken " + tablePair.getValue().getValue().getValue()
//            );
//        }

//        System.out.println("\nprint vertex list from source with distance");
//        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
//            System.out.println("vertexListPair.getKey(): " + vertexListPair.getKey().getName());
//            for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
//                System.out.println(
//                        "source vertex (prev vertex) " + sourceVertex.getName() +
//                                " and start visited vertex " + vertexListPair.getKey().getName() +
//                                " to vertex " + vertexEdgePair.getKey().getName() +
//                                " with distance from source " + vertexEdgePair.getValue().getWeight()
//                );
//            }
//        }


        Pair<Vertex, Edge> min = this.findMinimumKeyShouldTaken(list);

//        System.out.println("Found next minimum: " + min.getKey().getName());

        // loop through the table and true the link
        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
            if (tablePair.getKey().equals(min.getKey())) {
                tablePair.getValue().getValue().setValue(true);
            }
        }

//        System.out.println("\nprint table");
//        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
//            System.out.println(
//                    "vertex: " + tablePair.getKey().getName() +
//                            " previous: " + (Objects.isNull(tablePair.getValue().getKey()) ? "null" : tablePair.getValue().getKey().getName()) +
//                            " distance from source " + (Objects.isNull(tablePair.getValue().getValue()) ? "null" : tablePair.getValue().getValue().getKey()) +
//                            " taken " + tablePair.getValue().getValue().getValue()
//            );
//        }

        min.getKey().setVisited(true);
        min.getValue().setIncluded(true);
        nextMinVertexToTaken.add(min.getKey());

        for (int i = 0; i < 10; i++) {
            this.findShortestPathFromDistance();
        }
    }

    private Pair<Vertex, Edge> findMinimumKeyShouldTaken(List<Pair<Vertex, List<Pair<Vertex, Edge>>>> list) {
        // find the shortest edge and add to taken list
        List<Pair<Vertex, Edge>> minPairList = new LinkedList<>();
        for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
            Pair<Vertex, Edge> min = Collections.min(vertexListPair.getValue(),
                    Comparator.comparingInt(value -> value.getValue().getWeight()));
            List<String> listOfEqualWeights = new LinkedList<>();
            // find if map holding same weight keys if true get the minimum based on alphabetical order
            for (Pair<Vertex, Edge> vertexEdgeEntry : vertexListPair.getValue()) {
                if (min.getValue().getWeight() == vertexEdgeEntry.getValue().getWeight()) {
                    listOfEqualWeights.add(vertexEdgeEntry.getKey().getName());
                }
            }
            if (listOfEqualWeights.size() > 1) {
                Collections.sort(listOfEqualWeights);
                String keyName = listOfEqualWeights.get(0);
                for (Pair<Vertex, Edge> vertexEdgeEntry : vertexListPair.getValue()) {
                    if (keyName.equalsIgnoreCase(vertexEdgeEntry.getKey().getName())) {
                        min = vertexEdgeEntry;
                    }
                }
            }
            minPairList.add(min);
        }
        return Collections.min(minPairList,
                Comparator.comparingInt(value -> value.getValue().getWeight()));
    }

    private void initializeTable() {
        table.add(new Pair<>(sourceVertex, new Pair<>(null, new Pair<>(0, false))));
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : adjacencyList.returnList().entrySet()) {
            if (!hashMapEntry.getKey().equals(sourceVertex)) {
                table.add(new Pair<>(hashMapEntry.getKey(), new Pair<>(null, new Pair<>(Integer.MAX_VALUE, false))));
            }
        }
    }

    private void findShortestPathFromDistance() {
        if (!nextMinVertexToTaken.isEmpty()) {
            Vertex vertex = nextMinVertexToTaken.poll();
            // list of connected nodes and edges for given vertex
            List<Pair<Vertex, List<Pair<Vertex, Edge>>>> list = new LinkedList<>();
            // get connected vertex and edges for given vertex
            this.getConnectedVerticesForGiven(vertex, list, adjacencyList);
            // get other links for given vertex
            this.getOtherLinkedVerticesForGiven(vertex, list, adjacencyList);

//            System.out.println("\nbefore remove distance, nextMinTaken to prev");
//            for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
//                for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
//                    System.out.println(
//                            "key Given: " + vertexListPair.getKey().getName() +
//                                    " linked node: " + vertexEdgePair.getKey().getName() +
//                                    " distance from key Given " + vertexEdgePair.getValue().getWeight()
//                    );
//                }
//            }

            // filter to remove edges set included to true
            List<Pair<Vertex, List<Pair<Vertex, Edge>>>> temps = new LinkedList<>();
            for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
                List<Pair<Vertex, Edge>> pairList = new LinkedList<>();
                for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                    if (vertexEdgePair.getValue().isIncluded()) {
                        pairList.add(new Pair<>(vertexEdgePair.getKey(), vertexEdgePair.getValue()));
                    }
                }
                temps.add(new Pair<>(vertexListPair.getKey(), pairList));
            }

            for (Pair<Vertex, List<Pair<Vertex, Edge>>> listPair : list) {
                for (Pair<Vertex, List<Pair<Vertex, Edge>>> temp : temps) {
                    for (Pair<Vertex, Edge> vertexEdgePair : temp.getValue()) {
                        listPair
                                .getValue()
                                .removeIf(vertexEdgePair1 ->
                                        vertexEdgePair1
                                                .getKey()
                                                .equals(vertexEdgePair.getKey()));
                    }
                }
            }

//            System.out.println("\nafter remove distance, nextMinTaken to prev");
//            for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
//                for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
//                    System.out.println(
//                            "key Given: " + vertexListPair.getKey().getName() +
//                                    " linked node: " + vertexEdgePair.getKey().getName() +
//                                    " distance from key Given " + vertexEdgePair.getValue().getWeight()
//                    );
//                }
//            }

            // after gathering the links vertices for the given, needs to find-out the vertices that connected to the source,
            // and check that vertices distance from source to linked vertex > distance
            // to the current vertex + distance to linked vertex from current
            // 1. minVertexFoundInTable 2. Its Linked Vertices 3. Distance From Src
            List<Pair<Vertex, Pair<Vertex, Integer>>> calDistanceForTheGiven = new LinkedList<>();
            for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> listPair : table) {
                for (Pair<Vertex, List<Pair<Vertex, Edge>>> vertexListPair : list) {
                    for (Pair<Vertex, Edge> vertexEdgePair : vertexListPair.getValue()) {
                        // get addition (distance from src) of vertexListPair.getKey() (given vertex) distance from the
                        // prev node and to the vertexEdgePair.getKey() (to its linked nodes)
                        // check from the table that addition distance is greater than to the distance in table of linked node (vertexEdgePair.getKey())
                        // if it is true update the current distance by putting the linked vertex distance
                        // or else add that distance and prev node to the vertexEdgePair.getKey() (related linked node)
                        if (vertexListPair.getKey().equals(listPair.getKey())) {
                            Integer distanceToTakenMinNode = listPair.getValue().getValue().getKey();
                            Integer weight = vertexEdgePair.getValue().getWeight();
                            Integer distanceFromSrc = distanceToTakenMinNode + weight;
                            calDistanceForTheGiven
                                    .add(new Pair<>(vertexListPair.getKey(),
                                            new Pair<>(vertexEdgePair.getKey(), distanceFromSrc)));
                        }
                    }
                }
            }
//            System.out.println("\nprint, distances from source to the linked nodes to the found nextTakenKey of the table");
//            for (Pair<Vertex, Pair<Vertex, Integer>> pair : calDistanceForTheGiven) {
//                System.out.println(
//                        "Taken key from table: " + pair.getKey().getName() +
//                                " to its linked node " + pair.getValue().getKey().getName() +
//                                " had distance from source " + pair.getValue().getValue()
//                );
//
//            }

//            System.out.println("\nupdating table");
            // 1. minVertexFoundInTable 2. Its Linked Vertices 3. Distance From Src
            for (Pair<Vertex, Pair<Vertex, Integer>> pair : calDistanceForTheGiven) {
                for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
                    if (tablePair.getKey().getName().equalsIgnoreCase(pair.getValue().getKey().getName())) {
//                        System.out.println(
//                                "table vertex " + tablePair.getKey().getName() +
//                                        " which is equals to taken key's linked node " +
//                                        pair.getValue().getKey().getName() +
//                                        " has distance in the table from src to linked vertex " +
//                                        tablePair.getValue().getValue().getKey() +
//                                        " from prev of " +
//                                        (Objects.isNull(tablePair.getValue().getKey()) ? "null" : tablePair.getValue().getKey().getName()) +
//                                        " in table, and found another distance by adding given key distance to linked edge weight " +
//                                        pair.getValue().getValue() +
//                                        " prev vertex of " + pair.getKey().getName() +
//                                        " and linked vertex of " + pair.getValue().getKey().getName()
//                        );
                        if (Objects.isNull(tablePair.getValue().getValue().getKey()) || tablePair.getValue().getValue().getKey() > pair.getValue().getValue()) {
                            tablePair.getValue().getValue().setKey(pair.getValue().getValue());
                            tablePair.getValue().setKey(pair.getKey());
                        }
                    }
                }
            }

//            System.out.println("\nprint table");
//            for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
//                System.out.println(
//                        "vertex: " + tablePair.getKey().getName() +
//                                " previous: " + (Objects.isNull(tablePair.getValue().getKey()) ? "null" : tablePair.getValue().getKey().getName()) +
//                                " distance from source " + (Objects.isNull(tablePair.getValue().getValue()) ? "null" : tablePair.getValue().getValue().getKey()) +
//                                " taken " + tablePair.getValue().getValue().getValue()
//                );
//            }

            // after updated the table find the minimum should have taken and update the takenNodes lists
            // filer the nodes that has not visited and taken the next minimum shortest distance from source
            List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> tempsOfNotTakenPairs = new LinkedList<>();
            for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
                if (!tablePair.getValue().getValue().getValue()) {
                    tempsOfNotTakenPairs.add(
                            new Pair<>(tablePair.getKey(),
                                    new Pair<>(tablePair.getValue().getKey(),
                                            new Pair<>(tablePair.getValue().getValue().getKey(),
                                                    tablePair.getValue().getValue().getValue())
                                    )
                            )
                    );
                }
            }
            if (!tempsOfNotTakenPairs.isEmpty()) {
//                System.out.println("tempsOfNotTakenPairs.isEmpty()");
                Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> min = Collections.min(tempsOfNotTakenPairs,
                        Comparator.comparingInt(value -> value.getValue().getValue().getKey()));

                List<String> listOfEqualDistances = new LinkedList<>();
                // find if map holding same weight keys if true get the minimum based on alphabetical order
                for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tempPair : tempsOfNotTakenPairs) {
                    if (Objects.equals(min.getValue().getValue().getKey(), tempPair.getValue().getValue().getKey())) {
                        listOfEqualDistances.add(tempPair.getKey().getName());
                    }
                }
                if (listOfEqualDistances.size() > 1) {
                    Collections.sort(listOfEqualDistances);
                    String keyName = listOfEqualDistances.get(0);
                    for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tempsPair : tempsOfNotTakenPairs) {
                        if (keyName.equalsIgnoreCase(tempsPair.getKey().getName())) {
                            min = tempsPair;
                        }
                    }
                }

//                System.out.println("found next minimum should taken: " + min.getKey().getName());
                min.getKey().setVisited(true);
                // loop through the table and true the link
                for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
                    if (tablePair.getKey().equals(min.getKey())) {
                        tablePair.getValue().getValue().setValue(true);
                    }
                }
                for (Map.Entry<Vertex, HashMap<Vertex, Edge>> hashMapEntry : adjacencyList.returnList().entrySet()) {
                    for (Map.Entry<Vertex, Edge> vertexEdgeEntry : hashMapEntry.getValue().entrySet()) {
                        if (vertexEdgeEntry.getKey().equals(min.getKey())
                                && hashMapEntry.getKey().equals(min.getValue().getKey())) {
                            vertexEdgeEntry.getValue().setIncluded(true);
                        }
                        if (hashMapEntry.getKey().equals(min.getKey())
                                && vertexEdgeEntry.getKey().equals(min.getValue().getKey())) {
                            vertexEdgeEntry.getValue().setIncluded(true);
                        }
                    }
                }
                nextMinVertexToTaken.add(min.getKey());
            }
        }
    }

    public List<Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>>> getTable() {
        return table;
    }

    public GraphAdjacencyList<Vertex, Edge> getAdjacencyList() {
        return adjacencyList;
    }

    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(Vertex sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public void printTable() {
        System.out.println("\nprint table");
        for (Pair<Vertex, Pair<Vertex, Pair<Integer, Boolean>>> tablePair : table) {
            System.out.println(
                    "vertex: " + tablePair.getKey().getName() +
                            " previous: " + (Objects.isNull(tablePair.getValue().getKey()) ? "null" : tablePair.getValue().getKey().getName()) +
                            " distance from source " + (Objects.isNull(tablePair.getValue().getValue()) ? "null" : tablePair.getValue().getValue().getKey()) +
                            " taken " + tablePair.getValue().getValue().getValue()
            );
        }
    }
}
