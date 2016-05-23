package nl.han.asd.project.client.commonclient.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-5-2016
 */
public class DatabaseConnectionTest {

    private DatabaseConnection connection;

    @Before
    public void setupTestSuite() throws SQLException, NoSuchAlgorithmException {
        connection = new DatabaseConnection("test", "test123");
        connection.resetDatabase();
    }

    @Test
    public void testResetDatabaseAcceptsSuccessfulAndAcceptsNewInserts() throws SQLException {
        ResultSet contactsBefore = connection.select("SELECT * FROM Contact");
        connection.executeQuery("INSERT INTO Contact (username) VALUES ('Test')");
        ResultSet contactsAfter = connection.select("SELECT * FROM Contact");
        Assert.assertFalse(contactsBefore.next());
        Assert.assertTrue(contactsAfter.next());
    }

    @Test
    public void testResetDatabaseSuccessful() throws SQLException {
        ResultSet messages = connection.select("SELECT * FROM Message");
        ResultSet contacts = connection.select("SELECT * FROM Contact");
        Assert.assertFalse(contacts.next());
        Assert.assertFalse(messages.next());
    }

    @Test
    public void testExecuteQuerySuccessful() throws SQLException {
        Assert.assertTrue(connection.executeQuery("INSERT INTO Contact(username) VALUES ('Test')"));
    }

    @Test(expected = SQLIntegrityConstraintViolationException.class)
    public void testExecuteQueryPrimaryKeyViolation() throws SQLException {
        connection.executeQuery("INSERT INTO Contact(username) VALUES ('Test')");
        connection.executeQuery("INSERT INTO Contact(username) VALUES ('Test')");
    }

    @Test
    public void testSelectSuccessful() throws SQLException {
        String randomUsername = UUID.randomUUID().toString().substring(0, 12);
        connection.executeQuery("INSERT INTO Contact(username) VALUES ('" + randomUsername + "')");
        ResultSet result = connection.select("SELECT * FROM Contact");
        result.next();
        Assert.assertEquals(result.getObject(2), randomUsername);
    }

    @Test
    public void testIsDatabaseConnectionOpen() throws SQLException {
        Assert.assertTrue(connection.isOpen());
    }

    @Test
    public void testCanDatabaseConnectionBeClosed() throws SQLException {
        connection.stop();
        Assert.assertFalse(connection.isOpen());
    }

}