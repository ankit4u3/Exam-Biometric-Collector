/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prop;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author developeracer
 */
public class DataSourceRegistration {

    private static DataSourceRegistration datasource;
    private Connection cpds;
    public static String MASTERSERVER = "192.168.1.16";

    DataSourceRegistration() throws IOException, SQLException, PropertyVetoException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        cpds = DriverManager
                .getConnection("jdbc:mysql://" + MASTERSERVER + "/sportstracker_dev?"
                        + "user=RaceAdmin&password=v721PL7y");

    }

    public static DataSourceRegistration getInstance() throws IOException, SQLException, PropertyVetoException, ClassNotFoundException {
        if (datasource == null) {
            datasource = new DataSourceRegistration();
            System.err.println("New Connection Was Build");
            return datasource;
        } else {
            System.err.println("Old Connection Used");
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds;
    }

}
