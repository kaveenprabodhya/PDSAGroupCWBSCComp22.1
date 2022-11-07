package bsccomp.group.identifyminimumconnectors;

import bsccomp.group.common.GraphAdjacencyList;

public class IdentifyMinimumConnectors {
    private final int[] distance = new int[10];
    private final String[] city = {"Colombo", "Negombo", "Galle", "Kandy", "Sri Jayawardhanapura",
            "Jaffna", "Nuwara Eliya", "Ratnapura", "Dambulla", "Matale"};
    private GraphAdjacencyList<String> adjacencyList;
    public IdentifyMinimumConnectors(GraphAdjacencyList<String> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public String getRandomCity(){
        int index = (int) (Math.random()*10);
        return city[index];
    }

    public Integer getRandomDistance(){
        return (int) (Math.random()*100 + 10);
    }

    public void createGraph(){
        String randCity = this.getRandomCity();
        this.adjacencyList.addEdge(randCity);
        this.adjacencyList.addNodeAndWeightConnected(randCity, this.getRandomCity(), this.getRandomDistance());
        this.adjacencyList.printAdjacencyList();
    }

    public void findMinimumConnectors(){
        this.createGraph();
    }
}
