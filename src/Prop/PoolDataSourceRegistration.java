/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prop;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author developeracer
 */
public class PoolDataSourceRegistration {

    private static PoolDataSourceRegistration datasource;
    private ComboPooledDataSource cpds;

    PoolDataSourceRegistration() throws IOException, SQLException, PropertyVetoException {
        try {
            BasicTextEncryptor bte = new BasicTextEncryptor();
            bte.setPassword("HelloWorld");
            Properties prop = new Properties();
            ////reading properties
            FileInputStream in = new FileInputStream("properties.xml");
            prop.loadFromXML(in);
            //  System.out.println(prop.getProperty("connection"));
            in.close();
            String ip = bte.decrypt(prop.getProperty("ip"));
            String username = bte.decrypt(prop.getProperty("username"));
            String password = bte.decrypt(prop.getProperty("password"));
            String db = bte.decrypt(prop.getProperty("db"));
            System.out.println(ip+"\n"+username+"\n"+password+"\n"+db);
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
            cpds.setJdbcUrl("jdbc:mysql://" + "192.168.1.16" + "/registration?");
            cpds.setUser("root");
            cpds.setPassword("v721PL7y");
            // the settings below are optional -- c3p0 can work with defaults
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(5);
            cpds.setMaxStatements(180);
            cpds.setAcquireRetryAttempts(10);
            cpds.setAcquireRetryDelay(10);

        } catch (Exception e) {
            try {
                datasource = new PoolDataSourceRegistration();
            } catch (PropertyVetoException ex) {

            }
        }

    }

    public static PoolDataSourceRegistration getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new PoolDataSourceRegistration();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}
