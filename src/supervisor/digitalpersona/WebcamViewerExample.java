package supervisor.digitalpersona;

import tracker.digitalpersona.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Proof of concept of how to handle webcam video stream from Java
 *
 * @author Bartosz Firyn (SarXos)
 */
public class WebcamViewerExample extends JFrame implements Runnable, WebcamListener, WindowListener, ItemListener {

    private static final long serialVersionUID = 1L;
    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;
   
  
    WebcamViewerExample(String str) {
     
        setTitle(str);

    }

    public enum Format {

        BMP,
        JPEG,
        ISO,
        NIST,
        GIF
    }

    public WebcamViewerExample() {
         
       
    }
    private static final WebcamViewerExample SINGLETON = new WebcamViewerExample();

    public static WebcamViewerExample getInstance() {
        return SINGLETON;
    }

    @Override
    public void run() {

        setTitle("SOS Recruitment Facial Recognition");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setSize(new Dimension(200,200));
        setMaximumSize(new Dimension(200,200));
        setLayout(new BorderLayout());

        addWindowListener(this);

        picker = new WebcamPicker();
        picker.addItemListener(this);
        //picker.getSelectedWebcam().getDefault();

        webcam = picker.getSelectedWebcam();

        if (webcam == null) {
            System.out.println("No webcams found...");
            System.exit(1);
        }
             webcam.setViewSize(new Dimension(640,480));
       
        WebcamResolution.VGA.getSize();
       // webcam.setViewSize(WebcamResolution.VGA.getSize());
        System.out.println(" H  "+WebcamResolution.VGA.getSize().height);
        System.out.println("  W  "+WebcamResolution.VGA.getSize().width);
         
        //  webcam.setViewSize(new Dimension(50, 50));
        //      webcam.setViewSize(new Dimension(100,100));
        webcam.addWebcamListener(WebcamViewerExample.this);

        panel = new WebcamPanel(webcam, false);
        panel.setFPSDisplayed(true);
        panel.setFPSLimit(30);
        panel.setBackground(Color.BLACK);
        panel.setSize(new Dimension(200,200));

        add(picker, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        pack();
        setVisible(true);

        Thread t = new Thread() {

            @Override
            public void run() {
                panel.start();

            }
        };
        //t.setName("example-starter");
        //t.setDaemon(true);

        t.start();

    }

    // public static void main(String[] args) {
    // SwingUtilities.invokeLater(new WebcamViewerExample());
    // }
    @Override
    public void webcamOpen(WebcamEvent we) {
        System.out.println("webcam open");

    }

    @Override
    public void webcamClosed(WebcamEvent we) {
        System.out.println("webcam closed");
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        System.out.println("webcam disposed");
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        // do nothing
        
    }

    @Override
    @SuppressWarnings("oracle.jdeveloper.java.at-override-violation")
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        webcam.close();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        WebcamUtils.capture(webcam, "test1", ImageUtils.FORMAT_BMP);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);

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

    public void takesnapShot(String id_byreference, String e) {

        WebcamUtils.capture(webcam, "image", ImageUtils.FORMAT_JPG);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("webcam viewer resumed");
        panel.resume();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("webcam viewer paused");
        panel.pause();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        WebcamUtils.capture(webcam, "test1", ImageUtils.FORMAT_BMP);
        byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
        System.out.println("Bytes length: " + bytes.length);

    }

    public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
        BufferedImage bi = null;
        try {
            ImageIcon ii = new ImageIcon(filename); //path to image
            bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bi;
    }
public BufferedImage rotateImage(BufferedImage filename) {
        BufferedImage bi = null;
        try {
            ImageIcon ii = new ImageIcon(filename); //path to image
            bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(ii.getImage(), 0, 0, 640, 480, null);
            g2d.rotate(90f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bi;
    }
    
    class Producer implements Runnable {

        private final BlockingQueue queue;

        Producer(BlockingQueue q) {
            queue = q;
        }

        public void run() {
            try {
                while (true) {
                    queue.put("0");
                }
            } catch (InterruptedException ex) {
            }
        }

    }

}
