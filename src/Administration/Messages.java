/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Ankit
 */
public class Messages {

    public String readGreetings() {

        File f = new File("greetings-configuration");
        if (f.exists() == true) {
            Properties prop = new Properties();
            InputStream input = null;

            try {

                input = new FileInputStream("greetings-configuration");

                // load a properties file
                prop.load(input);

                // get the property value and print it out
                System.out.println(prop.getProperty("greetings"));
                return prop.getProperty("greetings");
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Config file Missing");
          
        }
          return "BIOMETRIC COLLECTOR";
    }
}
