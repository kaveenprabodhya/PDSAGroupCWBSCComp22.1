package bsccomp.group.identifyminimumconnectors;

import bsccomp.group.common.Base;
import bsccomp.group.common.GraphAdjacencyList;

public class IdentifyMinimumConnectors extends Base {
    private final GraphAdjacencyList<String> adjacencyList;

    public IdentifyMinimumConnectors(GraphAdjacencyList<String> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    private void createMinimumConnectorsGraph(){
        for (int i = 0; i < 10; i++) {
            String randCity = this.getRandomCity();
            this.adjacencyList.addEdge(randCity);
            int count = (int) (Math.random()*(5-1+1)+1);
            for (int j = 0; j < count; j++) {
                this.adjacencyList.addNodeAndWeightConnected(randCity, this.getRandomCity(randCity),
                    this.getRandomDistance());
            }
            this.clearSecondTempCityList();
        }
        this.clearTempCityList();
        this.adjacencyList.printAdjacencyList();
    }

    public void findMinimumConnectors(){
        this.createMinimumConnectorsGraph();
    }
}
