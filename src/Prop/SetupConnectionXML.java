/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author Ankit
 */
public class SetupConnectionXML {

    public Connection getConnection() throws SQLException, FileNotFoundException, IOException {

        Properties prop = new Properties();
        ////reading properties
        FileInputStream in = new FileInputStream("server-configuration.xml");
        prop.loadFromXML(in);
        //  System.out.println(prop.getProperty("connection"));
        in.close();

//         properties.setProperty("serverippath", SERVER_IP_TXT.getText().toString());
//         properties.setProperty("serveralias", SERVER_ALIAS_TXT.getText().toString());
//         properties.setProperty("serveruid", SERVER_UID_TXT.getText().toString());
//         properties.setProperty("serverpwd", SERVER_PWD_TXT.getText().toString());
//         properties.setProperty("serverdb", SERVER_DB_TXT.getSelectedItem().toString());
        String ip = prop.getProperty("serverippath");
        String username = prop.getProperty("serveruid");
        String password = prop.getProperty("serverpwd");
        String db = prop.getProperty("serverdb");

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

    public String getFolderName() throws SQLException, FileNotFoundException, IOException {

        Properties prop = new Properties();
        ////reading properties
        FileInputStream in = new FileInputStream("server-configuration.xml");
        prop.loadFromXML(in);
        //  System.out.println(prop.getProperty("connection"));
        in.close();

        String imagepath = prop.getProperty("imagepath");

        return imagepath;

    }
    public String getLegth() throws SQLException, FileNotFoundException, IOException {

        Properties prop = new Properties();
        ////reading properties
        FileInputStream in = new FileInputStream("server-configuration.xml");
        prop.loadFromXML(in);
        //  System.out.println(prop.getProperty("connection"));
        in.close();

        String imagepath = prop.getProperty("length");

        return imagepath;

    }
    
}
