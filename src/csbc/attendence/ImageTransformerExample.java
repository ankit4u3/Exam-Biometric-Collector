/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csbc.attendence;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.github.sarxos.webcam.util.jh.JHBlurFilter;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author developeracer
 */
public class ImageTransformerExample implements WebcamImageTransformer, WebcamListener, WindowListener {

    private static final JHGrayFilter GRAY = new JHGrayFilter();
    private static final JHBlurFilter BLUR =new JHBlurFilter();
    Webcam webcam;
    RoomSelect nj;

    public ImageTransformerExample() {

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.setImageTransformer(this);
        webcam.open();
        JFrame window = new JFrame("Test Transformer");
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setFillArea(true);
        window.add(panel);
        window.pack();
        window.setVisible(true);
        window.setSize(100, 100);
    window.setAlwaysOnTop(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
    }

    public ImageTransformerExample(RoomSelect k) {
        this.nj = k;

        webcam = Webcam.getDefault();
      //  webcam.setViewSize(WebcamResolution.VGA.getSize());
         webcam.setViewSize(new Dimension(176,144));
      //  webcam.setImageTransformer(this);
        webcam.open();
        JFrame window = new JFrame("Test Transformer");
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setFillArea(true);
        window.add(panel);
        window.pack();
        window.setVisible(true);
        window.setSize(100, 100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setAlwaysOnTop(true);
    }

    @Override
    public BufferedImage transform(BufferedImage image) {
        return BLUR.filter(image, null);
    }

    public static void main(String[] args) {
        new ImageTransformerExample();
    }

    @Override
    public void webcamOpen(WebcamEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void webcamClosed(WebcamEvent we) {

//windowClosing();
//  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void takesnapShot() {
        WebcamUtils.capture(webcam, "image", ImageUtils.FORMAT_JPG);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);
    }

    public byte[] takesnapShot(String id_byreference) {
        WebcamUtils.capture(webcam, id_byreference, ImageUtils.FORMAT_BMP);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");

        return bytes;

        //System.out.println("Bytes length: " + bytes.length);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent e) {

        WebcamUtils.capture(webcam, "test1", ImageUtils.FORMAT_BMP);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);

//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {

        WebcamUtils.capture(webcam, "test1", ImageUtils.FORMAT_BMP);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);

//  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
