package bsccomp.group.common;

import bsccomp.group.common.models.Vertex;

import java.util.*;

public class Base {
    public static final int maximumCities = 10;
    public static final int maximumDistance = 100;
    public static final int minimumDistance = 10;
    public static final int minimumVertexCanSelect = 1;
    public static final int maximumVertexCanSelect = 5;
    private final String[] cities;
    // used to allocate random select city only once
    private final ArrayList<Vertex> tempCityList;
    // used to allocate random select city only once
    private final ArrayList<Vertex> secondTempCityList;

    public Base() {
        // initializing arrays in constructor
        this.tempCityList = new ArrayList<>();
        this.cities = new String[]{"Colombo", "Negombo", "Galle", "Kandy", "Sri Jayawardhanapura",
                "Jaffna", "Nuwara Eliya", "Ratnapura", "Dambulla", "Matale"};
        secondTempCityList = new ArrayList<>();
    }

    // create graph
    protected void createGraph(GraphAdjacencyList<Vertex> adjacencyList) {
        for (int i = 0; i < 10; i++) {
            String randCity = this.selectRandomCityOnlyOnce();
            adjacencyList.addEdge(new Vertex(randCity));
            int vertexCount = (int) (Math.random() * (maximumVertexCanSelect - minimumVertexCanSelect + 1) + minimumVertexCanSelect);
            for (int j = 0; j < vertexCount; j++) {
                adjacencyList.addNodeAndWeightConnected(new Vertex(randCity), new Vertex(this.selectRandomCityOnlyOnce(randCity)),
                        this.getRandomDistance());
            }
            this.clearSecondTempCityList();
        }
        this.clearTempCityList();
        this.removeDependencies(adjacencyList.get());
    }

    // method for select random city from cities array
    protected String selectRandomCity() {
        int index = (int) (Math.random() * maximumCities);
        return cities[index];
    }

    // method that only once return a random city
    private String selectRandomCityOnlyOnce() {
        String randCity = this.selectRandomCity();
        if (this.tempCityList.isEmpty()) {
            this.tempCityList.add(new Vertex(randCity));
        } else {
            try {
                for (Vertex tempCity : this.tempCityList) {
                    if (tempCity.getName().equalsIgnoreCase(randCity)) {
                        randCity = this.getRandomCityExceptAlreadySelected(tempCityList);
                    }
                }
                this.tempCityList.add(new Vertex(randCity));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        System.out.println(randCity);
        return randCity;
    }

    // method for return random city except for the given city.
    private String selectRandomCityOnlyOnce(String randomExceptCity) {
        String randomCity = this.selectRandomCity();
        if (randomCity.equalsIgnoreCase(randomExceptCity)) {
            randomCity = this.returnCityExceptGiven(randomCity, randomExceptCity);
            if (this.secondTempCityList.isEmpty()) {
                this.secondTempCityList.add(new Vertex(randomCity));
            } else {
                try {
                    for (Vertex tempCity : this.secondTempCityList) {
                        if (tempCity.getName().equalsIgnoreCase(randomCity)) {
                            randomCity = this.getRandomCityExceptAlreadySelected(secondTempCityList);
                        }
                    }
                    this.secondTempCityList.add(new Vertex(randomCity));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return randomCity;
    }

    // method return random city which not selected yet out of 10.
    protected String getRandomCityExceptAlreadySelected(ArrayList<Vertex> tempCityList) {
        boolean[] temps = new boolean[maximumCities];
        Queue<String> tempQueue = new LinkedList<>();
        Arrays.fill(temps, false);
        for (Vertex tempCity : tempCityList) {
            for (int j = 0; j < cities.length; j++) {
                if (cities[j].equalsIgnoreCase(tempCity.getName())) {
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
    private String returnCityExceptGiven(String randomCity, String randomExceptCity) {
        while (randomCity.equalsIgnoreCase(randomExceptCity)) {
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
    protected void removeDependencies(HashMap<Vertex, HashMap<Vertex, Integer>> adjList) {
        for (Map.Entry<Vertex, HashMap<Vertex, Integer>> vertexMapEntry : adjList.entrySet()) {
            for (Map.Entry<Vertex, Integer> desVertexWeightEntry : vertexMapEntry.getValue().entrySet()) {
                Map<Vertex, Vertex> pairOfKeys = this.checkCombinations(vertexMapEntry.getKey().getName(),
                        desVertexWeightEntry.getKey().getName(), adjList);
                if (!pairOfKeys.isEmpty())
                    this.removeFoundDependencies(pairOfKeys, adjList);
            }
        }
        this.checkIfRemovedOnlyCombinationOfVertex(adjList);
    }

    private void removeFoundDependencies(Map<Vertex, Vertex> pairOfKeys,
                                         HashMap<Vertex, HashMap<Vertex, Integer>> adjList) {
        for (Map.Entry<Vertex, Vertex> pairOfKeysEntrySet : pairOfKeys.entrySet()) {
            adjList.get(pairOfKeysEntrySet.getKey()).remove(pairOfKeysEntrySet.getValue());
        }
    }

    // check removed dependency is the only combination if it is re-assign node and check for dependency again
    private void checkIfRemovedOnlyCombinationOfVertex(HashMap<Vertex, HashMap<Vertex, Integer>> adjList) {
        for (Map.Entry<Vertex, HashMap<Vertex, Integer>> vertexMapEntry : adjList.entrySet()) {
            if (vertexMapEntry.getValue().entrySet().isEmpty()) {
                String randCity = this.selectRandomCityOnlyOnce();
                vertexMapEntry.getValue().put(new Vertex(randCity), this.getRandomDistance());
                this.clearTempCityList();
                this.removeDependencies(adjList);
            }
        }
    }

    // check for same city dependencies combinations with different distance assign
    private Map<Vertex, Vertex> checkCombinations(String keyNode,
                                                  String destinationKeyNode,
                                                  HashMap<Vertex, HashMap<Vertex, Integer>> adjList) {
        for (Map.Entry<Vertex, HashMap<Vertex, Integer>> vertexMapEntry : adjList.entrySet()) {
            if (!vertexMapEntry.getKey().getName().equalsIgnoreCase(keyNode)
                    && vertexMapEntry.getKey().getName().equalsIgnoreCase(destinationKeyNode)) {
                for (Map.Entry<Vertex, Integer> desVertexEntry : vertexMapEntry.getValue().entrySet()) {
                    if (desVertexEntry.getKey().getName().equalsIgnoreCase(keyNode)) {
                        return Map.of(vertexMapEntry.getKey(), desVertexEntry.getKey());
                    }
                }
            }
        }
        return Map.of();
    }

    // method generate random distance between 10-100
    protected Integer getRandomDistance() {
        return (int) (Math.random() * maximumDistance + minimumDistance);
    }
}
