package bsccomp.group;

import bsccomp.group.manager.GameManager;

/**
 * 06-Nov-22
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // create new game-manager
        GameManager gameManager = new GameManager();
        // call function find-maximum-profit in game-manager
        // call function identify-shortest-path in game-manager


        // call function identify-minimum-connectors in game-manager
        gameManager.identifyMinimumConnectors();
    }
}
