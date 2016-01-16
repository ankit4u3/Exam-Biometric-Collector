/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Ankit
 */
public class GetStorageLocations {

    public String getImageFolderName() {

        try {
            Properties prop = new Properties();
            ////reading properties
            FileInputStream in = new FileInputStream("server-configuration.xml");
            prop.loadFromXML(in);
            //  System.out.println(prop.getProperty("connection"));
            in.close();

            String imagepath = prop.getProperty("imagepath");

            return imagepath;
        } catch (Exception e) {

        }
        return "SNAPS";
    }

    public String getTemplateFolderName() {

      
        try {
            Properties prop = new Properties();
            ////reading properties
            FileInputStream in = new FileInputStream("server-configuration.xml");
            prop.loadFromXML(in);
            //  System.out.println(prop.getProperty("connection"));
            in.close();

            String fmtpath = prop.getProperty("fmtpath");

            return fmtpath;
        } catch (Exception e) {

        }
        return "TEMPLATE";

    }
    
     public String getGreetings() {

      
        try {
            Properties prop = new Properties();
            ////reading properties
            FileInputStream in = new FileInputStream("greetings-configuration.xml");
            prop.loadFromXML(in);
            //  System.out.println(prop.getProperty("connection"));
            in.close();

            String greetings = prop.getProperty("greetings");

            return greetings;
        } catch (Exception e) {

        }
        return "TEMPLATE";

    }
    
    public String getFingerFolderName() {

      
       
        return "FINGER";

    }
}
