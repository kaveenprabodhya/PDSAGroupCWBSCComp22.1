package bsccomp.group.manager;

import bsccomp.group.common.GraphAdjacencyListImpl;
import bsccomp.group.identifyminimumconnectors.IdentifyMinimumConnectors;

public class GameManager {
    // need to manage object states (array-list, hash-set)
    // need a function to find-maximum-profit
    // need a function to identify-shortest-path
    // need a function to identify-minimum-connectors
    public GameManager() {
    }

    public void identifyMinimumConnectors(){
        IdentifyMinimumConnectors minimumConnectors = new IdentifyMinimumConnectors(new GraphAdjacencyListImpl());
        minimumConnectors.findMinimumConnectors();
    }
}
