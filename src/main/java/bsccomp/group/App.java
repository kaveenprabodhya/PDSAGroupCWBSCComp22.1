package bsccomp.group;

import bsccomp.group.common.models.User;
import bsccomp.group.manager.GameManager;

import java.sql.SQLException;

/**
 * 06-Nov-22
 */
public class App {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        GameManager.makeJDBCConnection();
        GameManager gameManager = new GameManager();

        gameManager.startFindMinimumConnectors();
        if (gameManager.checkAnswersOfMinimumConnectors(gameManager.getAnswersForMinimumConnectors())) {
            GameManager.addMinimumConnectorsAnswersDataToDB(gameManager.getAnswersForMinimumConnectors(), new User("Kaveen"));
        } else {
            System.out.println("wrong answer");
        }

        gameManager.startFindMaximumProfit();
        boolean b = gameManager.checkAnswersOfMaximumProfit(gameManager.getMaximumProfitFor10kgs());
        System.out.println(gameManager.getMaximumProfitFor10kgs());
        if (b) {
            GameManager.addMaximumProfitAnswersDataToDB(String.valueOf(gameManager.getMaximumProfitFor10kgs()), new User("Kaveen"));
        } else {
            System.out.println("wrong answer");
        }

        gameManager.startFindShortestPath();
        gameManager.printShortestPathTable();
        boolean b1 = gameManager.checkAnswersOfShortestPath(gameManager.getAnswersForShortestPath());
        if (b1) {
            GameManager.addShortestPathAnswersDataToDB(gameManager.getAnswersForShortestPath(), new User("Kaveen"));
        } else {
            System.out.println("wrong answer");
        }

        GameManager.connection.close();
    }
}
