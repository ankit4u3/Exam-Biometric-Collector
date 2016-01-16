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
public class PropertyConnect {

    public Connection getConnection() throws SQLException, FileNotFoundException, IOException {

        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword("HelloWorld");

        Properties prop = new Properties();
        ////reading properties
        FileInputStream in = new FileInputStream("properties.xml");
        prop.loadFromXML(in);
        //  System.out.println(prop.getProperty("connection"));
        in.close();

        String ip = "192.168.1.16";//bte.decrypt(prop.getProperty("ip"));
        String username = "root";//bte.decrypt(prop.getProperty("username"));
        String password = "v721PL7y";//bte.decrypt(prop.getProperty("password"));
        String db = bte.decrypt(prop.getProperty("db"));

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

    public void continuePing() {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                String s = null;

                try {

                    // run the Unix "ps -ef" command
                    // using the Runtime exec method:
                    Process p = Runtime.getRuntime().exec("ping " + "192.168.1.16" + " ");

                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                    // read the output from the command
                    System.out.println("Here is the standard output of the command:\n");
                    while ((s = stdInput.readLine()) != null) {

                        System.out.println(s);
                    }

                    // read any errors from the attempted command
                    System.out.println("Here is the standard error of the command (if any):\n");
                    while ((s = stdError.readLine()) != null) {
                        System.out.println(s);

                    }

                    //   System.exit(0);
                } catch (IOException e) {

                    System.out.println("exception happened - here's what I know: ");
                    e.printStackTrace();
                    System.exit(-1);
                }

                return null;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();
    }

    public String getIP() throws SQLException, FileNotFoundException, IOException {
        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword("HelloWorld");

        Properties prop = new Properties();
////reading properties
        FileInputStream in = new FileInputStream("properties.xml");
        prop.loadFromXML(in);
        //  System.out.println(prop.getProperty("connection"));
        in.close();

        String ip = bte.decrypt(prop.getProperty("ip"));
        boolean someCondition = true;

        String zIp = (ip != null ? ip : "null");

        return zIp;

    }
}
