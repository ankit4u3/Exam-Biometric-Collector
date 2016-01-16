package supervisor.digitalpersona;

import tracker.digitalpersona.*;
import Prop.PropertyConnect;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fid.Fiv;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class ImagePanel
        extends JPanel {

    private static final long serialVersionUID = 5;
    private BufferedImage m_image;
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public static String CONNECTION = "";

    public void showImage(Fid image) {
        Fiv view = image.getViews()[0];
        m_image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        m_image.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());
        repaint();
    }

    public void showImage(Fid image, String filename) {
        Fiv view = image.getViews()[0];
        m_image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        m_image.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());
        repaint();
        saveBack(filename);
    }

    public void paint(Graphics g) {
        g.drawImage(m_image, 0, 0, null);

    }

    public void saveBack(final String filename) {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {

                try {
                    //     saveFingerprint(filename);
// super.done(); //To change body of generated methods, choose Tools | Templates.
                } catch (Exception ex) {
                    Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            protected Object doInBackground() throws Exception {

                try {
                    // retrieve image
                    BufferedImage bi = m_image;
                    File outputfile = new File(filename + ".png");
                    ImageIO.write(bi, "png", outputfile);

                    Prop.PropertyConnect pc = new PropertyConnect();
                    connect = pc.getConnection();

                    saveFingerprint(filename);
                    System.out.println("Saved ok");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                return null;
            }
        }.execute();

    }

    public void saveFingerprint(final String fname) throws Exception {

        try {
            java.awt.image.BufferedImage imageBuffer = m_image;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imageBuffer, "jpg", out);

// Setup stream for blob 
            byte[] buffer = out.toByteArray();
            ByteArrayInputStream inStream = new ByteArrayInputStream(buffer);
            preparedStatement = connect
                    .prepareStatement("insert into  fingerprinttemp values (null, ?,?)");
preparedStatement.setString(1, fname);
            preparedStatement.setBinaryStream( 2, inStream, inStream.available() ); 
            preparedStatement.executeUpdate();
            System.out.println("Saved ok in DB");
        } catch (Exception e) {
            System.out.println(e.getMessage());

            //   throw e;
        } finally {
                connect.close();
        }

    }
}
