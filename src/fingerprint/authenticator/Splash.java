/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.authenticator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDialog;

/**
 *
 * @author martijn
 */
public class Splash {

    public static void splash() {
        try {
            final BufferedImage img = ImageIO.read(Splash.class.getResourceAsStream("/path/to/your/splash/image/splash.png"));
            JDialog dialog = new JDialog() {

                @Override
                public void paint(Graphics g) {
                    g.drawImage(img, 0, 0, null);
                }
            };
            // use the same size as your image
            dialog.setPreferredSize(new Dimension(450, 300)); 
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.setUndecorated(true);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.repaint();
            try {
                // Now, we are going to init the look and feel:

                Class uim = Class.forName("javax.swing.UIManager");
                uim.getDeclaredMethod("setLookAndFeel", String.class).invoke(null, (String) uim.getDeclaredMethod("getSystemLookAndFeelClassName").invoke(null));

                // And now, we are going to invoke our loader method:
                Class clazz = Class.forName("yourpackage.YourClass");
                dialog.dispose();
                // suppose your method is called init and is static
                clazz.getDeclaredMethod("init").invoke(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dialog.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}