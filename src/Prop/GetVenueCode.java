/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ankit
 */
public class GetVenueCode {

    public String getCenterCode() {
        Properties prop = new Properties();
        ////reading properties
        FileInputStream in;
        try {
            in = new FileInputStream("location-configuration.xml");
            prop.loadFromXML(in);
            in.close();

            String venuecode = prop.getProperty("venuecode");
            return venuecode;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetVenueCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetVenueCode.class.getName()).log(Level.SEVERE, null, ex);
        }
return "0420";
    }
    
    
     public String getSiteType() {
        Properties prop = new Properties();
        ////reading properties
        FileInputStream in;
        try {
            in = new FileInputStream("location-configuration.xml");
            prop.loadFromXML(in);
            in.close();

            String sitetype = prop.getProperty("sitetype");
            return sitetype;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetVenueCode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetVenueCode.class.getName()).log(Level.SEVERE, null, ex);
        }
return "0420";
    }
}
