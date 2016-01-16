/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prop;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author Developer
 */
public class PropertyCSBC {

    public Connection getConnection() throws SQLException, FileNotFoundException, IOException {

     
        String ip = "127.0.0.1";//bte.decrypt(prop.getProperty("ip"));
        String username = "root";//bte.decrypt(prop.getProperty("username"));
        String password = "v721PL7y";//bte.decrypt(prop.getProperty("password"));
        String db = "movedb";

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            Connection connect = DriverManager
                    .getConnection("jdbc:mysql://" + ip + "/" + db + "?"
                            + "user=" + username + "&password=" + password);

            System.out.println("Connected to database." + username);
       //     JOptionPane.showMessageDialog(null, "System Connected");

            return connect;
        } catch (Exception e) {
            System.out.println("Failed to connect " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return null;

    }

    
   
}
