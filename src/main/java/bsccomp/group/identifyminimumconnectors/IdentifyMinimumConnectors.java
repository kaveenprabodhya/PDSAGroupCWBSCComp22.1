package bsccomp.group.identifyminimumconnectors;

import bsccomp.group.common.GraphAdjacencyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class IdentifyMinimumConnectors {
    private final String[] cities;
    private final ArrayList<String> tempCityList;
    private final GraphAdjacencyList<String> adjacencyList;

    public IdentifyMinimumConnectors(GraphAdjacencyList<String> adjacencyList) {
        this.adjacencyList = adjacencyList;
        this.tempCityList = new ArrayList<>();
        this.cities = new String[]{"Colombo", "Negombo", "Galle", "Kandy", "Sri Jayawardhanapura",
                "Jaffna", "Nuwara Eliya", "Ratnapura", "Dambulla", "Matale"};
    }

    private String selectRandomCity(){
        int index = (int) (Math.random()*10);
        return cities[index];
    }

    private String getRandomCity(){
        String randCity = this.selectRandomCity();
        if(this.tempCityList.isEmpty()){
            this.tempCityList.add(randCity);
        } else {
            try {
                for (String tempCity : this.tempCityList) {
                    if (tempCity.equalsIgnoreCase(randCity)) {
                        randCity = this.getNewRandomCityExceptAlreadySelected();
                    }
                }
                this.tempCityList.add(randCity);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
//        System.out.println(randCity);
        return randCity;
    }

    private String getNewRandomCityExceptAlreadySelected(){
        boolean[] temps = new boolean[10];
        Queue<String> tempQueue = new LinkedList<>();
        Arrays.fill(temps, false);
        for (String tempCity : tempCityList) {
            for (int j = 0; j < cities.length; j++) {
                if (cities[j].equalsIgnoreCase(tempCity)) {
                    temps[j] = true;
                }
            }
        }
        for (int i = 0; i < temps.length; i++) {
            if(!temps[i]){
                tempQueue.add(cities[i]);
            }
        }
        return tempQueue.poll();
    }

    private String getRandomCityExceptCurrentSelected(String randCity) {
        String randomCity = this.selectRandomCity();
        if(randomCity.equalsIgnoreCase(randCity)){
            while (!randomCity.equalsIgnoreCase(randCity)){
                randomCity = this.selectRandomCity();
            }
        }
        return randomCity;
    }

    private Integer getRandomDistance(){
        return (int) (Math.random()*100 + 10);
    }

    public void createMinimumConnectorsGraph(){
        for (int i = 0; i < 10; i++) {
            String randCity = this.getRandomCity();
            this.adjacencyList.addEdge(randCity);
            this.adjacencyList.addNodeAndWeightConnected(randCity, this.getRandomCityExceptCurrentSelected(randCity),
                    this.getRandomDistance());
        }
        this.adjacencyList.printAdjacencyList();
    }

    public void findMinimumConnectors(){
        this.createMinimumConnectorsGraph();
    }
}
