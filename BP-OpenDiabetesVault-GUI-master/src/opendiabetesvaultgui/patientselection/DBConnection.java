/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.patientselection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import static opendiabetesvaultgui.launcher.FatherController.PREFS_FOR_ALL;

/**
 * Tool for connecting to the Database using the hsqldb jbdc Driver.
 *
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class DBConnection {

    private final static String DATABASE_URL = databaseType();
    private final static String USER = "root";
    private final static String PASSWORD = "root";
    private static Connection conn;

    /**
     * This method is responsible for establishing a new Connection to
     * the database.
     * It is also responsible for creating a new
     * folder structure at the given database path
     *
     * @return the currently active Connection
     * @throws java.sql.SQLException if sql statements invalid.
     * @throws java.io.IOException if failed or interrupted I/O operations.
     * @throws java.lang.ClassNotFoundException if class was not found.
     *
     */
    public static Connection connect() throws SQLException, IOException, ClassNotFoundException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
            File yourFile = new File(PREFS_FOR_ALL.get("pathDatabase", "")
                    + File.separator + "vault.db");
            yourFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream oFile = new FileOutputStream(yourFile, false);
        } //Driver not found
        catch (InstantiationException ie) {
            //Wrong Type
            System.err.println("Error: " + ie.getMessage());
        } catch (IllegalAccessException ae) {
            //Wrong Access Credentials
            System.err.println("Error: " + ae.getMessage());
        }
        conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        return conn;
    }

    /**
     * Depending on which database type was chosen, this method returns the
     * current database path as a String. To build the path to the embedded
     * database, it calls #pathString().
     *
     * @return String containing the path to the current database
     */
    public static String databaseType() {
        if (PREFS_FOR_ALL.getBoolean("databaseInMemory", false)) {
            return "jdbc:hsqldb:mem:vaultDB";
        } else {
            return "jdbc:hsqldb:file:" + pathString();
        }
    }

    /**
     * Returns the current Connection, if it is still active and valid It
     * otherwise establishes a new Connection by calling {@link #connect()}
     *
     * @return Connection conn
     * @throws SQLException if sql statements invalid.
     * @throws IOException if failed or interrupted I/O operations.
     * @throws java.lang.ClassNotFoundException if class was not found.
     */
    public static Connection getConnection()
            throws SQLException, IOException, ClassNotFoundException {
        if (conn != null && !conn.isClosed()) {
            return conn;
        }
        connect();
        return conn;

    }

    /**
     * Safely closes the currently active database connection
     *
     * @throws SQLException if sql statements invalid.
     */
    public static void closeConnection() throws SQLException {
        Statement st = conn.createStatement();
        System.out.println("CONNECTION CLOSED");
        st.execute("SHUTDOWN");
        conn.close();
    }

    /**
     * This method builds a full path to the embedded database by appending all
     * components necessary
     *
     * @return String containing a fully built database path
     */
    private static String pathString() {
        StringBuilder test = new StringBuilder();
        test.append(PREFS_FOR_ALL.get("pathDatabase", ""));
        test.append(File.separator);
        test.append("vault.db");
        return test.toString();
    }

}
