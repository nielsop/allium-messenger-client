package nl.han.asd.project.client.commonclient.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines an interface for common database functions.
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 24-05-2016
 */
public interface IDatabase {

    /**
     * Resets the database by dropping the Message and Contact table if they exist, and then re-making them.
     * Clears out all database entries.
     *
     * @return <tt>true</tt> if both tables were reset successfully, <tt>false</tt> otherwise.
     * @throws SQLException if a database access error occurs.
     */
    boolean resetDatabase() throws SQLException;

    /**
     * Initializes the Contact and Message table.
     * @throws SQLException if a database access error occurs.
     */
    void initializeDatabase() throws SQLException;

    /**
     * Executes a INSERT, UPDATE or DELETE query.
     * @param sqlQuery The query string.
     * @return <tt>true</tt> if the query was executed successfully, <tt>false</tt> otherwise
     * @throws SQLException if a database access error occurs.
     */
    boolean query(String sqlQuery) throws SQLException;

    /**
     * Executes a SELECT query.
     * @param sqlQuery The query string.
     * @return a ResultSet object containing the results of the query.
     * @throws SQLException if a database access error occurs.
     */
    ResultSet select(String sqlQuery) throws SQLException;

    /**
     * Closes the database connection.
     * @throws SQLException if a database access error occurs.
     */
    void stop() throws SQLException;

    /**
     * Checks whether the database connection is open or not.
     * @return <tt>true</tt> if a database connection is or can be opened, <tt>false</tt> otherwise.
     * @throws SQLException if a database access error occurs.
     */
    boolean isOpen() throws SQLException;
}
