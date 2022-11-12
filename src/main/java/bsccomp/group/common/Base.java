package bsccomp.group.common;

import bsccomp.group.common.models.Edge;
import bsccomp.group.common.models.Vertex;

import java.util.*;

public class Base {
    public static final int maximumCities = 10;
    public static final int maximumDistance = 100;
    public static final int minimumDistance = 10;
    public static final int minimumVertexCanSelect = 1;
    public static final int maximumVertexCanSelect = 5;
    private final Vertex[] cities;
    // used to allocate random select city only once
    private final ArrayList<Vertex> tempCityList;
    // used to allocate random select city only once
    private final ArrayList<Vertex> secondTempCityList;

    public Base() {
        // initializing arrays in constructor
        this.tempCityList = new ArrayList<>();
        this.cities = new Vertex[]{
                new Vertex("Colombo"),
                new Vertex("Negombo"),
                new Vertex( "Galle"),
                new Vertex("Kandy"),
                new Vertex("Sri Jayawardhanapura"),
                new Vertex("Jaffna"),
                new Vertex("Nuwara Eliya"),
                new Vertex("Ratnapura"),
                new Vertex("Dambulla"),
                new Vertex("Matale")
        };
        secondTempCityList = new ArrayList<>();
    }

    // create graph
    protected void createGraph(GraphAdjacencyList<Vertex, Edge> adjacencyList) {
        for (int i = 0; i < 10; i++) {
            Vertex randCity = this.selectRandomCityOnlyOnce();
            adjacencyList.addEdge(randCity);
            int vertexCount = (int) (Math.random() * (maximumVertexCanSelect - minimumVertexCanSelect + 1) + minimumVertexCanSelect);
            for (int j = 0; j < vertexCount; j++) {
                adjacencyList.addNodeAndWeightConnected(randCity, this.selectRandomCityOnlyOnce(randCity),
                        this.getRandomDistance());
            }
            this.clearSecondTempCityList();
        }
        this.clearTempCityList();
        this.removeDependencies(adjacencyList.returnList());
    }

    // method for select random city from cities array
    protected Vertex selectRandomCity() {
        int index = (int) (Math.random() * maximumCities);
        return cities[index];
    }

    // method that only once return a random city
    private Vertex selectRandomCityOnlyOnce() {
        Vertex randCity = this.selectRandomCity();
        if (this.tempCityList.isEmpty()) {
            this.tempCityList.add(randCity);
        } else {
            try {
                for (Vertex tempCity : this.tempCityList) {
                    if (tempCity.getName().equalsIgnoreCase(randCity.getName())) {
                        randCity = this.getRandomCityExceptAlreadySelected(tempCityList);
                    }
                }
                this.tempCityList.add(randCity);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return randCity;
    }

    // method for return random city except for the given city.
    private Vertex selectRandomCityOnlyOnce(Vertex randomExceptCity) {
        Vertex randomCity = this.selectRandomCity();
        if (randomCity.getName().equalsIgnoreCase(randomExceptCity.getName())) {
            randomCity = this.returnCityExceptGiven(randomCity, randomExceptCity);
            if (this.secondTempCityList.isEmpty()) {
                this.secondTempCityList.add(randomCity);
            } else {
                try {
                    for (Vertex tempCity : this.secondTempCityList) {
                        if (tempCity.getName().equalsIgnoreCase(randomCity.getName())) {
                            randomCity = this.getRandomCityExceptAlreadySelected(secondTempCityList);
                        }
                    }
                    this.secondTempCityList.add(randomCity);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return randomCity;
    }

    // method return random city which not selected yet out of 10.
    protected Vertex getRandomCityExceptAlreadySelected(ArrayList<Vertex> tempCityList) {
        boolean[] temps = new boolean[maximumCities];
        Queue<Vertex> tempQueue = new LinkedList<>();
        Arrays.fill(temps, false);
        for (Vertex tempCity : tempCityList) {
            for (int j = 0; j < cities.length; j++) {
                if (cities[j].getName().equalsIgnoreCase(tempCity.getName())) {
                    temps[j] = true;
                }
            }
        }
        for (int i = 0; i < temps.length; i++) {
            if (!temps[i]) {
                tempQueue.add(cities[i]);
            }
        }
        return tempQueue.poll();
    }

    // method for check given random city and generated random city equality.
    private Vertex returnCityExceptGiven(Vertex randomCity, Vertex randomExceptCity) {
        while (randomCity.getName().equalsIgnoreCase(randomExceptCity.getName())) {
            randomCity = this.selectRandomCity();
        }
        return randomCity;
    }

    // method for clear temp list
    protected void clearTempCityList() {
        this.tempCityList.clear();
    }

    // method for clear temp list
    protected void clearSecondTempCityList() {
        this.secondTempCityList.clear();
    }

    // remove dependencies vertex having
    protected void removeDependencies(HashMap<Vertex, HashMap<Vertex, Edge>> adjList) {
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> vertexMapEntry : adjList.entrySet()) {
            for (Map.Entry<Vertex, Edge> desVertexWeightEntry : vertexMapEntry.getValue().entrySet()) {
                Map<Vertex, Vertex> pairOfKeys = this.checkCombinations(vertexMapEntry.getKey().getName(),
                        desVertexWeightEntry.getKey().getName(), adjList);
                if (!pairOfKeys.isEmpty())
                    this.removeFoundDependencies(pairOfKeys, adjList);
            }
        }
        this.checkIfRemovedOnlyCombinationOfVertex(adjList);
    }

    private void removeFoundDependencies(Map<Vertex, Vertex> pairOfKeys,
                                         HashMap<Vertex, HashMap<Vertex, Edge>> adjList) {
        for (Map.Entry<Vertex, Vertex> pairOfKeysEntrySet : pairOfKeys.entrySet()) {
            adjList.get(pairOfKeysEntrySet.getKey()).remove(pairOfKeysEntrySet.getValue());
        }
    }

    // check removed dependency is the only combination if it is re-assign node and check for dependency again
    private void checkIfRemovedOnlyCombinationOfVertex(HashMap<Vertex, HashMap<Vertex, Edge>> adjList) {
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> vertexMapEntry : adjList.entrySet()) {
            if (vertexMapEntry.getValue().entrySet().isEmpty()) {
                Vertex randCity = this.selectRandomCityOnlyOnce(vertexMapEntry.getKey());
                vertexMapEntry.getValue().put(randCity, this.getRandomDistance());
                this.clearTempCityList();
                this.removeDependencies(adjList);
            }
        }
    }

    // check for same city dependencies combinations with different distance assign
    private Map<Vertex, Vertex> checkCombinations(String keyNode,
                                                  String destinationKeyNode,
                                                  HashMap<Vertex, HashMap<Vertex, Edge>> adjList) {
        for (Map.Entry<Vertex, HashMap<Vertex, Edge>> vertexMapEntry : adjList.entrySet()) {
            if (!vertexMapEntry.getKey().getName().equalsIgnoreCase(keyNode)
                    && vertexMapEntry.getKey().getName().equalsIgnoreCase(destinationKeyNode)) {
                for (Map.Entry<Vertex, Edge> desVertexEntry : vertexMapEntry.getValue().entrySet()) {
                    if (desVertexEntry.getKey().getName().equalsIgnoreCase(keyNode)) {
                        return Map.of(vertexMapEntry.getKey(), desVertexEntry.getKey());
                    }
                }
            }
        }
        return Map.of();
    }

    // method generate random distance between 10-100
    protected Edge getRandomDistance() {
        return new Edge((int) (Math.random() * maximumDistance + minimumDistance));
    }

    // print graph
    public void displayGraph(GraphAdjacencyList<Vertex, Edge> adjList){
        adjList.printAdjacencyList();
    }
}
