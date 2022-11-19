package bsccomp.group;

import bsccomp.group.manager.GameManager;

import java.sql.SQLException;

/**
 * 06-Nov-22
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException, ClassNotFoundException {
        GameManager.makeJDBCConnection();
        GameManager gameManager = new GameManager();
        gameManager.startFindMinimumConnectors();
        System.out.println(gameManager.checkAnswersOfMinimumConnectors(gameManager.getAnswersForMinimumConnectors()));
        GameManager.addMinimumConnectorsAnswersDataToDB(gameManager.getAnswersForMinimumConnectors(), "Kaveen");
//        GameManager.connection.close();
    }
}
