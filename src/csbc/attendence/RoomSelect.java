/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csbc.attendence;

import Prop.SetupConnectionXML;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;
import fingerprint.authenticator.CaptureThread;
import fingerprint.authenticator.Main;
import fingerprint.authenticator.MessageBox;
import fingerprint.authenticator.Verification;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import tracker.digitalpersona.UareUSampleJava;

/**
 *
 * @author Ankit Implementing Batch Insert
 *
 *
 * String sql = "insert into employee (name, city, phone) values (?, ?, ?)";
 * Connection connection = new getConnection(); PreparedStatement ps =
 * connection.prepareStatement(sql);
 *
 * final int batchSize = 1000; int count = 0;
 *
 * for (Employee employee: employees) {
 *
 * ps.setString(1, employee.getName()); ps.setString(2, employee.getCity());
 * ps.setString(3, employee.getPhone()); ps.addBatch();
 *
 * if(++count % batchSize == 0) { ps.executeBatch(); } } ps.executeBatch(); //
 * insert remaining records ps.close(); connection.close();
 *
 *
 *
 */
public class RoomSelect extends javax.swing.JInternalFrame implements KeyListener, Observer, ActionListener {

    //
    /*
     Open Room ID : 
     */
    public int room_id_open;
    public String rollLength = null;
    ResultSet rs = null;
    Connection conn = null;
    PreparedStatement pstmt = null;

    ResultSet NavigationResultSet;
    Connection connect;
    private Prop.PropertyCSBC pCSBC;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    Webcam webcam;
    JPanel p;
    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Random RANDOM = new SecureRandom();
    private static final long serialVersionUID = 1;

    public static Logger log = Logger.getLogger(
            Main.class.getName());
    private static final String ACT_SELECTION = "selection";
    private static final String ACT_CAPTURE = "capture";
    private static final String ACT_STREAMING = "streaming";
    private static final String ACT_VERIFICATION = "verification";
    private static final String ACT_IDENTIFICATION = "identification";
    private static final String ACT_ENROLLMENT = "enrollment";
    private static final String ACT_EXIT = "exit";

    private JDialog m_dlgParent;
    private JTextArea m_textReader;

    private ReaderCollection m_collection;
    private Reader m_reader;
    private Fmd enrollmentFMD;

    private static final String ACT_BACK = "back";
    private static final String ACT_LOAD = "load";
    private static final String ACT_LOAD_FROM_DB = "load_from_db";

    private CaptureThread m_capture;

    private Fmd[] m_fmds;

    public FingerDB db = new FingerDB("localhost", "uareu", "root", "password");
    public List<FingerDB.Record> m_listOfRecords = new ArrayList<FingerDB.Record>();
    public List<Fmd> m_fmdList = new ArrayList<Fmd>();
    public Fmd[] m_fmdArray = null;  //Will hold final array of FMDs to identify against

    public Fmd m_enrollmentFmd;

    HashMap<String, Object> mapper = new HashMap();
    private final String m_strPrompt1 = "Verification started\n    put any finger on the reader\n\n";
    private final String m_strPrompt2 = "    put the same or any other finger on the reader\n\n";

    /**
     * Creates new form RoomSelect
     */
    public void EPOCValid(final String epoctime) {
        try {

            //    InputStreamReader isr = new InputStreamReader(System.in);
            //     System.out.print("pelase enter Epoch tme stamp : ");
            //    BufferedReader br = new BufferedReader(isr);
            String s = epoctime;
            long l = Long.parseLong(s);
            l = l * 1000;

            Date date = new Date(l);

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            String formatted = format.format(date);
            System.out.println(formatted);
            format.setTimeZone(TimeZone.getTimeZone("IST"));

            formatted = format.format(date);
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(formatted));
            formatted = format.format(c.getTime());
            System.out.println(formatted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureScreen(String fileName) throws Exception {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        ImageIO.write(image, "png", new File(fileName));

    }

    public void writeConfig(final String lastfile) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("lastroom", lastfile);

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void writeConfigPreAlloted() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("prealloted.properties");

            // set the properties value
            prop.setProperty("lastroom", "yes");

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void readConfig() {

        File f = new File("config.properties");
        if (f.exists() == true) {
            Properties prop = new Properties();
            InputStream input = null;

            try {

                input = new FileInputStream("config.properties");

                // load a properties file
                prop.load(input);

                // get the property value and print it out
                System.out.println(prop.getProperty("lastroom"));
                room_id.setText("CURRENT ROOM OPEN : " + prop.getProperty("lastroom"));
                openroom_btn.setEnabled(false);
                room_id_open = Integer.valueOf(prop.getProperty("lastroom"));
                rinvigilator.setText(String.valueOf(prop.getProperty("lastroom")));
                rinvigilator.setEnabled(false);

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
    }

    public int readPreAllotedRooms() {

        File f = new File("config.properties");
        if (f.exists() == true) {
            return 0;
        } else {
            System.out.println("Config file Missing");
            return 1;
        }

    }

    public int Connect() {

        try {

            connect = new SetupConnectionXML().getConnection();

            return 0;
        } catch (Exception e) {
            System.out.println("Failed to connect " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());
            return -1;
        }

    }

    public void EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
    Logger logger = Logger.getLogger("MyLog");
    FileHandler fh;

    @Override
    public void doDefaultCloseAction() {
         webcam.close();
        super.doDefaultCloseAction(); //To change body of generated methods, choose Tools | Templates.
    }

    public RoomSelect() {
        try {
            initComponents();
            //  pgStatus.setText(new Administration.Messages().readGreetings().toString());
            new Thread(new Runnable() {

                @Override
                public void run() {

                    int connectStatus = Connect();
                    if (connectStatus == 0) {
                        try {
                            getVenueCode();
                            getYearCode();
                        } catch (SQLException ex) {
                            Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    webcam = Webcam.getDefault();
                    webcam.setViewSize(WebcamResolution.VGA.getSize());
                    //   webcam.setImageTransformer(this);
                    webcam.open();

                    WebcamPanel panel = new WebcamPanel(webcam);
                   // panel.setFPSDisplayed(true);
                    panel.setFillArea(true);
                    panel.setSize(200, 200);
                    packer.add(panel);

                    packer.setVisible(true);
                    

                }

            }).start();
            setTitle("Short Cut Keys ; F1 Take Photo # F2 Biometric # F11 Absent # ");
            p = new JPanel() {

                public void paintComponent(Graphics g) {

                    g.setColor(new Color(0, 0, 0, 140));

                    g.fillRect(0, 0, getWidth(), getHeight());

                }

            };
            readConfig();
            int ifPrealloted = readPreAllotedRooms();
            if (ifPrealloted == 0) {

                openroom_btn.setEnabled(false);
                //  closeroom_btn.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "This Center has Rooms PreAlloted The Room Creation Feature is Disabled on this SITE",
                        "Important Message ",
                        JOptionPane.ERROR_MESSAGE);

            }
            pgStatus.setText(new Prop.GetStorageLocations().getGreetings());
            try {

                // This block configure the logger with handler and formatter
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                fh = new FileHandler("Biometric_Info_ROOM_SELECT_Log_" + timeStamp + "-txt.log");
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);

                // the following statement is used to log any messages
                logger.info(" Universal Biometric Digital Collector @ SOS 2015 Using Digital Persona Dev EMAPPS Ankit  ");
                logger.info(" Contact +91-7080344400 If You are Reading this Error Log for Explaination  ");

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rollLength = new Prop.SetupConnectionXML().getLegth();
            logger.info("Program Accepts Roll Number of Length " + rollLength);
            logger.info("Hi How r u? This is a Error Monitoring LOG");
        } catch (SQLException ex) {
            Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getVenueCode() throws SQLException {

        jComboBox1.removeItemListener(null);
        int itemCount = jComboBox1.getItemCount();

        /*
         -1 was added since there is a change listener added to the combobox so
         to avoid null pointer exception one element was left  
         */
        for (int i = 0; i < itemCount - 1; i++) {
            jComboBox1.removeItemAt(0);
        }
        jComboBox1.addItem(new Prop.GetVenueCode().getCenterCode().toString());
        jComboBox1.addItemListener(null);

    }

    private void getYearCode() throws SQLException {

        roomselector.removeItemListener(null);
        Statement statement = null;

        String selectTableSQL = "SELECT id from room";

        try {

            statement = connect.createStatement();

            System.out.println(selectTableSQL);

            // execute select SQL stetement
            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {

                String userid = String.valueOf(rs.getInt("id"));

                System.out.println("userid : " + userid);
                if (userid != null) {
                    userid = userid.trim();
                }

                roomselector.addItem(userid);

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                //       connect.close();
            }

        }

    }

    private void getVenuedetails() throws SQLException {

        Statement statement = null;

        String selectTableSQL = "SELECT venuedetails from csbc_dup ";

        try {

            statement = connect.createStatement();

            System.out.println(selectTableSQL);

            // execute select SQL stetement
            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {

                String userid = rs.getString("venuecode");

                System.out.println("userid : " + userid);
                if (userid != null) {
                    userid = userid.trim();
                }
                jComboBox1.addItem(userid);

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                //       connect.close();
            }

        }

    }

    private void SearchCandidate(final String rollnumber) throws SQLException {

        Statement statement = null;

        String selectTableSQL = "SELECT distinct venuecode from csbc_dup ";

        try {

            statement = connect.createStatement();

            System.out.println(selectTableSQL);

            // execute select SQL stetement
            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {

                String userid = rs.getString("venuecode");

                System.out.println("userid : " + userid);
                if (userid != null) {
                    userid = userid.trim();
                }
                jComboBox1.addItem(userid);

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                //       connect.close();
            }

        }

    }

    private ResultSet NavigateCandidate(final String venuecode) throws SQLException {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect;
            String query = "select * from csbc_dup where venuecode = ? order by rollno asc ";

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setString(1, venuecode); // set input parameter
            rs = pstmt.executeQuery();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private ResultSet NavigateCandidatebyRoom(final String roomCode) throws SQLException {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect;
            String query = "SELECT EXTRACT(YEAR FROM csbc_dup.dob1) from csbc_dup";

            pstmt = conn.prepareStatement(query); // create a statement
            //   pstmt.setString(1, roomCode); // set input parameter
            rs = pstmt.executeQuery();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public void getCandidateList(final String venuecode) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect;
            String query = "select count(*) from csbc_dup where venuecode = ?";

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setString(1, venuecode); // set input parameter
            rs = pstmt.executeQuery();
            // extract data from the ResultSet
            if (rs.next()) {
                String getc = rs.getString("count(*)");
                roomStrength.setText("Center Strength : " + getc);
            }
        } catch (Exception e) {
      
        } finally {
            try {
                rs.close();
                pstmt.close();
                //       conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
  
            }
        }

    }

    public void getPassCount() {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect;
            String query = "select count(*) from attendence where status = 1";

            pstmt = conn.prepareStatement(query); // create a statement

            rs = pstmt.executeQuery();
            // extract data from the ResultSet
            if (rs.next()) {
                String getc = rs.getString("count(*)");
                jLabel15.setText("Present Count : " + getc);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                //       conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void getFailedCount() {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect;
            String query = "select count(*) from attendence where status = 0";

            pstmt = conn.prepareStatement(query); // create a statement

            rs = pstmt.executeQuery();
            // extract data from the ResultSet
            if (rs.next()) {
                String getc = rs.getString("count(*)");
                jLabel17.setText("Absent Count : " + getc);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                //       conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void getCurrentRoomCount() {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connect;
            String query = "select count(*) from attendence where room_id = ?";

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setInt(1, room_id_open); // set input parameter
            rs = pstmt.executeQuery();
            // extract data from the ResultSet
            if (rs.next()) {
                String getc = rs.getString("count(*)");
                roomStrength.setText("Room Strength : " + getc);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(RoomSelect.this,
                    e.getMessage(),
                    "Internal Error ",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                //       conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void getCandidateStatus(final String rollnumber) {

        try {

            String query = "select status from attendence where barcode = ?";

            pstmt = connect.prepareStatement(query); // create a statement
            pstmt.setString(1, rollnumber); // set input parameter
            rs = pstmt.executeQuery();
            // extract data from the ResultSet
            if (rs.next()) {
                int getc = rs.getInt("status");
                System.out.println(getc);
                switch (getc) {
                    case 1:
                        candidate_status.setText("PRESENT");
                        candidate_status.setForeground(Color.GREEN);
                        btn_takephoto.setEnabled(false);
                        enrollBiometricbtn.setEnabled(false);
                        break;
                    case 2:
                        candidate_status.setForeground(Color.yellow);
                        break;
                    case 0:
                        candidate_status.setText("ABSENT");
                        candidate_status.setForeground(Color.RED);
                        btn_takephoto.setEnabled(false);
                        enrollBiometricbtn.setEnabled(false);
                        break;
                    default:
                        candidate_status.setForeground(Color.WHITE);

                        break;

                }
            } else {
                candidate_status.setText("NOT CHECKED");
                candidate_status.setForeground(Color.darkGray);
                btn_takephoto.setEnabled(true);
                enrollBiometricbtn.setEnabled(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(RoomSelect.this,
                    e.getMessage(),
                    "Internal Error ",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.out.println(e.getCause() + e.getMessage());
        } finally {

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        roomselector = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        room_id = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        pgStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        rollnumber_txt = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        rinvigilator = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        openroom_btn = new javax.swing.JButton();
        closeroom_btn = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        center_status = new javax.swing.JLabel();
        btn_yes = new javax.swing.JButton();
        btn_no = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_text = new javax.swing.JTextArea();
        jButton9 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel28 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        imageLoad = new javax.swing.JLabel();
        cname_txt = new javax.swing.JTextField();
        fname_txt = new javax.swing.JTextField();
        candidate_status_txt = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        dob_txt = new javax.swing.JTextField();
        doAbsentbtn = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        roomStrength = new javax.swing.JLabel();
        btn_takephoto = new javax.swing.JButton();
        candidate_status = new javax.swing.JLabel();
        enrollBiometricbtn = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        pngfmtexists = new javax.swing.JLabel();
        showPhototick = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        packer = new javax.swing.JPanel();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        custom_set_rollnumber = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        m_loadFromDB = new javax.swing.JButton();
        m_load = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setClosable(true);
        setTitle("Digital Biometric Collector @ 2015");

        jToolBar1.setBorder(javax.swing.BorderFactory.createTitledBorder("Operational Tools"));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator7);

        jButton1.setText("<<");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator6);

        jButton2.setText(">>");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator5);

        jButton3.setText("||");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);
        jToolBar1.add(jSeparator4);

        jComboBox1.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECT VENUE", " " }));
        jComboBox1.setMaximumSize(new java.awt.Dimension(156, 20));
        jComboBox1.setMinimumSize(new java.awt.Dimension(156, 20));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBox1);
        jToolBar1.add(jSeparator8);

        roomselector.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        roomselector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NO ROOM", " " }));
        roomselector.setMaximumSize(new java.awt.Dimension(156, 20));
        roomselector.setMinimumSize(new java.awt.Dimension(156, 20));
        roomselector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                roomselectorItemStateChanged(evt);
            }
        });
        roomselector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomselectorActionPerformed(evt);
            }
        });
        jToolBar1.add(roomselector);

        jButton4.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        jButton4.setText("LOCK ");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);
        jToolBar1.add(jSeparator1);

        room_id.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        room_id.setText("ROOM OPEN ID : XXX");
        jToolBar1.add(room_id);
        jToolBar1.add(jSeparator2);
        jToolBar1.add(jSeparator3);

        pgStatus.setFont(new java.awt.Font("Arial Unicode MS", 0, 10)); // NOI18N
        pgStatus.setText("PROGRAM STATUS : NO ROOM SELECTED ");
        jToolBar1.add(pgStatus);

        rollnumber_txt.setText("Enter The Flying Rollnumber here");

        jButton11.setText("Navigate");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel1.setText("Caution : For situation when you have to navigate to a specific Roll Number");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(rollnumber_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rollnumber_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(326, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Navigation ", jPanel9);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jButton7.setText("Image Click");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel3.setText("Room Number : ");

        rinvigilator.setText("Enter Room Number");

        jButton16.setText("Biometric Enroll");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton8.setText("Save");

        openroom_btn.setText("Create Room");
        openroom_btn.setFocusable(false);
        openroom_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openroom_btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openroom_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openroom_btnActionPerformed(evt);
            }
        });

        closeroom_btn.setText("CLOSE ROOM");
        closeroom_btn.setFocusable(false);
        closeroom_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeroom_btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        closeroom_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeroom_btnActionPerformed(evt);
            }
        });

        jButton5.setText("LAST ROOM");

        jButton19.setText("REOPEN ROOM");

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("What Type of Center is This")));

        center_status.setText("Center has Rooms Pre Alloted ?");

        btn_yes.setText("Yes");
        btn_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_yesActionPerformed(evt);
            }
        });

        btn_no.setText("No.");
        btn_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_noActionPerformed(evt);
            }
        });

        jLabel9.setText("Information :");

        jLabel10.setText("If center has no preallocated room use the above mentioned controls to allocate");

        jLabel11.setText("If Allocation has been done for the given room the Rooms will be loaded on");

        jLabel12.setText("Venue Selection from the Combo Box ");

        jLabel13.setText("The Box Looks Like this ++++===>");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(center_status)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(btn_yes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_no))
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(center_status)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_yes)
                    .addComponent(btn_no))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13))
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(openroom_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(closeroom_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton19)))
                        .addContainerGap(144, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rinvigilator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(closeroom_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(openroom_btn)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton5)
                                .addComponent(jButton19)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(rinvigilator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Invigilator Info", jPanel3);

        m_text.setColumns(20);
        m_text.setRows(5);
        jScrollPane2.setViewportView(m_text);

        jButton9.setText("Archive Data");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addGap(0, 86, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Console", jPanel12);

        jLabel16.setText("1) Remove Wrong Entry from The Database ");

        jLabel22.setText("2) Remove Image Clicked");

        jLabel23.setText("3) Change Candidate Status ");

        jButton20.setText("Process");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createTitledBorder("Process Manager "));
        jScrollPane4.setViewportView(jTextArea1);

        jLabel28.setText("Barcode Number :");

        jTextField9.setText("Enter the Barcode Number ");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22)
                                .addGap(25, 25, 25)
                                .addComponent(jLabel23))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(228, 228, 228)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20)))
                        .addGap(0, 99, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(92, 92, 92)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Accidental Case Report Filing", jPanel15);

        imageLoad.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
        );

        cname_txt.setEditable(false);
        cname_txt.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        cname_txt.setText("Candidate Name");

        fname_txt.setEditable(false);
        fname_txt.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        fname_txt.setText("Father Name");

        candidate_status_txt.setEditable(false);
        candidate_status_txt.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        candidate_status_txt.setText("State");

        jTextField5.setEditable(false);
        jTextField5.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jTextField5.setText("City / Zipcode");

        dob_txt.setEditable(false);
        dob_txt.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        dob_txt.setText("Date of Birth / SEX / Other Info will be displayed Here");

        doAbsentbtn.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        doAbsentbtn.setText("Absent");
        doAbsentbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doAbsentbtnActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Room Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(255, 0, 51)));

        jLabel17.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setText("Absent Count    : ###");

        jLabel15.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 204, 0));
        jLabel15.setText("Present Count   : ###");

        roomStrength.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        roomStrength.setText("Room Strength : ###");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel15)
                    .addComponent(roomStrength))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roomStrength)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btn_takephoto.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        btn_takephoto.setText("Photo");
        btn_takephoto.setFocusable(false);
        btn_takephoto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_takephoto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_takephoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_takephotoActionPerformed(evt);
            }
        });

        candidate_status.setFont(new java.awt.Font("Arial Unicode MS", 1, 36)); // NOI18N
        candidate_status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        candidate_status.setText("   ###########");
        candidate_status.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, new java.awt.Color(153, 153, 153), null, null));

        enrollBiometricbtn.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        enrollBiometricbtn.setText("Enroll Biometrics");
        enrollBiometricbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enrollBiometricbtnActionPerformed(evt);
            }
        });

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel20.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel20.setText("Template");

        jLabel21.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel21.setText("Photo");

        pngfmtexists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/cross.jpg"))); // NOI18N

        showPhototick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/cross.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(candidate_status, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                    .addComponent(cname_txt)
                    .addComponent(fname_txt)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(candidate_status_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5))
                    .addComponent(dob_txt)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel21)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(showPhototick))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pngfmtexists)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btn_takephoto, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(enrollBiometricbtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(doAbsentbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(cname_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fname_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(candidate_status_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dob_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(candidate_status, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(showPhototick)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pngfmtexists)
                            .addComponent(jLabel20))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_takephoto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(enrollBiometricbtn, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(doAbsentbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator9))
                .addGap(0, 35, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Candidate Info", jPanel2);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane1.setViewportView(jTextArea2);

        jLabel4.setText("On Absent Biometric Not Required  ");

        jLabel5.setText("On Frame Close the Camera is Turned Off Can be Reinitialized when the form is activated");

        jLabel6.setText("XML file is introduced with a Roll Number Length Option to set The MAX Accepted Number");

        jLabel7.setText("XML File is used for All Necessary Configuration ");

        jLabel8.setText("jLabel8");

        jLabel14.setText("@SOS Developers Compiled on 6/8/2015");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(255, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jLabel14))
        );

        jTabbedPane1.addTab("Change Log", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        packer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout packerLayout = new javax.swing.GroupLayout(packer);
        packer.setLayout(packerLayout);
        packerLayout.setHorizontalGroup(
            packerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(packerLayout.createSequentialGroup()
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(212, 212, 212))
        );
        packerLayout.setVerticalGroup(
            packerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(packerLayout.createSequentialGroup()
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel18.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel18.setText("Start Sequence : ");

        jLabel19.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jLabel19.setText("Enter the Starting Roll Number :####");

        custom_set_rollnumber.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        custom_set_rollnumber.setText("Enter Starting Rollnumber");

        jButton14.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton14.setText("GO.TO");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton14))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(custom_set_rollnumber)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(1, 1, 1)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(custom_set_rollnumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addContainerGap())
        );

        m_loadFromDB.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        m_loadFromDB.setText("LOAD DB");
        m_loadFromDB.setFocusable(false);
        m_loadFromDB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        m_loadFromDB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        m_loadFromDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_loadFromDBActionPerformed(evt);
            }
        });

        m_load.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        m_load.setText("FMD MANUAL");
        m_load.setFocusable(false);
        m_load.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        m_load.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton13.setFont(new java.awt.Font("Arial Unicode MS", 0, 11)); // NOI18N
        jButton13.setText("HELP");
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jSeparator11.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Power Tools");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        jMenuItem1.setText("Present");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        jMenuItem2.setText("Absent");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem3.setText("Take Photo");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItem4.setText("Take Biometrics");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Navigation ");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        jMenuItem5.setText("Forward");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jMenuItem6.setText("Backward");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(packer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_loadFromDB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m_load))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(packer, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_load, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(m_loadFromDB, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator11))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        moveBack();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
//JComboBox cmb = jComboBox1;
//ResultSet rs = null; 
//while(rs.next()) {
//    String result = rs.getString(1); // Retrieves the value of the designated column in the current row of this ResultSet object as a String
//    if (result != null) {
//        result = result.trim();
//    }
//    cmb.addItem(result);
//} 
//rs.close();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed
    public void displayImag(ImageIcon imag) {

        imageLoad.setIcon(imag);

    }
    private void btn_takephotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_takephotoActionPerformed

        new Thread(new Runnable() {

            @Override
            public void run() {
                File mkDir = new File(new Prop.GetStorageLocations().getImageFolderName());
                if (mkDir.exists() == false) {
                    mkDir.mkdir();

                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //   final String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
                // Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
                Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");
                //   Pattern digitPattern = Pattern.compile("\\d{6}");
                System.out.println("does " + candidate_status_txt.getText().toString() + " is number : "
                        + digitPattern.matcher(candidate_status_txt.getText().toString()).matches());
                if (digitPattern.matcher(candidate_status_txt.getText().toString()).matches() == true) {
                    final String imageFileName = candidate_status_txt.getText().toString();

                    WebcamUtils.capture(webcam, new Prop.GetStorageLocations().getImageFolderName() + "//" + imageFileName, ImageUtils.FORMAT_BMP);
                    byte[] b = WebcamUtils.getImageBytes(webcam, "jpg");

                    try {
                        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(b));
                        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(b));
                        Graphics g = bi.createGraphics();
                        g.drawImage(imag, 0, 0, 20, 20, null);
                        AffineTransform tx = new AffineTransform();
                        tx.rotate(90);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(imag, "jpg", baos);
                        baos.flush();
                        byte[] imageInByte = baos.toByteArray();
                        SaveClickedImage(candidate_status_txt.getText().toString(), imageInByte);
                        baos.close();

                        Image newimg = imag.getScaledInstance(180, 203, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon newIcon = new ImageIcon(newimg);
                        displayImag(newIcon);

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                File mkDir = new File(new Prop.GetStorageLocations().getImageFolderName());
                                if (mkDir.exists() == false) {
                                    mkDir.mkdir();

                                }
                                File file = new File(new Prop.GetStorageLocations().getImageFolderName() + "//" + imageFileName + ".bmp");

                                // Tests whether the file denoted by this abstract pathname exists.
                                boolean exists = file.exists();

                                System.out.println("File " + file.getAbsolutePath() + " exists: " + exists);
                                showPhototick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/tick.jpg"))); // NOI18N
                                markwhenBothPresent();
                            }
                        }).start();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(RoomSelect.this,
                                ex.getMessage(),
                                "Internal Error ",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(RoomSelect.this,
                                ex.getMessage(),
                                "Internal Error ",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("Sorry  !!!!    Length Incorrect ");
                }
            }
        }).start();

        // TODO add your handling code here:
    }//GEN-LAST:event_btn_takephotoActionPerformed

    public void markwhenBothPresent() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //   final String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
                Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");
// Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
                //   Pattern digitPattern = Pattern.compile("\\d{6}");
                System.out.println("does " + candidate_status_txt.getText().toString() + " is number : "
                        + digitPattern.matcher(candidate_status_txt.getText().toString()).matches());
                if (digitPattern.matcher(candidate_status_txt.getText().toString()).matches() == true) {
                    final String imageFileName = candidate_status_txt.getText().toString();

                    File file = new File(new Prop.GetStorageLocations().getImageFolderName() + "/" + imageFileName + ".bmp");
                    File file2 = new File(new Prop.GetStorageLocations().getTemplateFolderName() + "/" + imageFileName + ".fmt");
                    // Tests whether the file denoted by this abstract pathname exists.
                    boolean exists = file.exists();
                    boolean exists2 = file2.exists();
                    System.out.println("File " + file.getAbsolutePath() + " exists: " + exists);
                    System.out.println("File " + file2.getAbsolutePath() + " exists: " + exists2);

                    if (exists == true && exists2 == true) {

                        //Perform Automatic Present & Insertion function Call
                        candidate_status.setText("** CHECKED IN **");
                        candidate_status.setForeground(Color.GREEN);
                        markStatus(candidate_status_txt.getText().toString(), 1);

                    }

                } else {

                }

            }
        }).start();
    }

    public void say(final String s1, final String s2) {
        System.out.println(s1 + " " + s2);
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("THREAD RUNNING " + s1 + "%%%%%%%%%%%%%%%");

                File file = new File(s1 + ".fmt");

                // Tests whether the file denoted by this abstract pathname exists.
                boolean exists = file.exists();

                System.out.println("File " + file.getAbsolutePath() + " exists: " + exists);
                //  showPhototick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/tick.jpg"))); // NOI18N
            }

        }).start();
    }

    public void checkFmtExisits() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");
// Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
                //   Pattern digitPattern = Pattern.compile("\\d{6}");
                System.out.println("does " + candidate_status_txt.getText().toString() + " is number : "
                        + digitPattern.matcher(candidate_status_txt.getText().toString()).matches());
                if (digitPattern.matcher(candidate_status_txt.getText().toString()).matches() == true) {

                    File file = new File(new Prop.GetStorageLocations().getTemplateFolderName() + "/" + candidate_status_txt.getText().toString().trim() + ".fmt");

                    // Tests whether the file denoted by this abstract pathname exists.
                    boolean exists = file.exists();

                    System.out.println("File " + file.getAbsolutePath() + " exists: " + exists);

                    if (exists == true) {
                        pngfmtexists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/tick.jpg"))); // NOI18N
                    } else {
                        pngfmtexists.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/cross.jpg"))); // NOI18N

                    }
                }
            }
        }).start();
    }

    public void checkBMPExisits() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");
//  Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
                //   Pattern digitPattern = Pattern.compile("\\d{6}");
                System.out.println("does " + candidate_status_txt.getText().toString() + " is number : "
                        + digitPattern.matcher(candidate_status_txt.getText().toString()).matches());
                if (digitPattern.matcher(candidate_status_txt.getText().toString()).matches() == true) {

                    File file = new File(new Prop.GetStorageLocations().getImageFolderName() + "/" + candidate_status_txt.getText().toString().trim() + ".bmp");

                    // Tests whether the file denoted by this abstract pathname exists.
                    boolean exists = file.exists();

                    System.out.println("File " + file.getAbsolutePath() + " exists: " + exists);

                    if (exists == true) {
                        try {
                            BufferedImage img = null;
                            img = ImageIO.read(new File(file.getAbsolutePath()));
                            Image newimg = img.getScaledInstance(180, 203, java.awt.Image.SCALE_SMOOTH);
                            ImageIcon newIcon = new ImageIcon(newimg);
                            displayImag(newIcon);

                        } catch (IOException e) {
                        }
                        showPhototick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/tick.jpg"))); // NOI18N
                    } else {
                        showPhototick.setIcon(new javax.swing.ImageIcon(getClass().getResource("/csbc/attendence/cross.jpg"))); // NOI18N

                    }
                }
            }
        }).start();
    }

    private void SaveClickedImage(String rollno, byte[] image) {
        Connection connect = null;

        PreparedStatement preparedStatement = null;

        try {
            // This will load the MySQL driver, each DB has its own driver
            connect = new SetupConnectionXML().getConnection();

            Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            st = connect.createStatement();
            ResultSet rs = st.executeQuery("select count(*) from  captured where rollnumber=" + rollno);

            // get the number of rows from the result set
            rs.next();
            int rowCount = rs.getInt(1);
            if (rowCount == 0) {
                setTitle(rollno + " STATUS OK ");

            } else {
                JOptionPane.showMessageDialog(null, "Previous Images Will Be Deleted ");
                setTitle(rollno + " Removing Previous Image Stored");
                st.executeUpdate("delete  from  captured where rollnumber=" + rollno);
                JOptionPane.showMessageDialog(null, "Command Issued Prevoous Images REmoved OK");
            }
            preparedStatement = connect
                    .prepareStatement("insert into  captured(rollnumber,photo0) values ( ?, ?)");

            preparedStatement.setString(1, rollno);
            preparedStatement.setBytes(2, image);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {

        }
    }

    private void closeroom_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeroom_btnActionPerformed

        openroom_btn.setEnabled(true);
        rinvigilator.setEnabled(true);
        rinvigilator.setText("            ");

        File exists = new File("config.properties");
        if (exists.exists() == true) {
            exists.delete();
            JOptionPane.showMessageDialog(this,
                    "Room Has Been Closed And Locked",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_closeroom_btnActionPerformed

    public Thread onCheckBoot() throws InterruptedException {
        File file = new File("test.mdb");
        if (file.exists() == true) {
            onSecondBootReceived().start();
        } else {
            onFirstBootReceived().start();
        }

        return null;

    }

    public Thread onFirstBootReceived() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    setGlassPane(p);
                    p.setVisible(true);
                    File file = new File("test.mdb");
                    Database db = new DatabaseBuilder(file)
                            .setFileFormat(Database.FileFormat.V2000)
                            .create();
                    Table table = new TableBuilder("Test")
                            .addColumn(new ColumnBuilder("ID", DataType.LONG)
                                    .setAutoNumber(true))
                            .addColumn(new ColumnBuilder("Name", DataType.TEXT))
                            .addColumn(new ColumnBuilder("Salary", DataType.MONEY))
                            .addColumn(new ColumnBuilder("StartDate", DataType.SHORT_DATE_TIME))
                            .toTable(db);
                    p.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        ;
        return null;
    }

    public Thread onSecondBootReceived() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    setGlassPane(p);
                    p.setVisible(true);
                    File file = new File("test.mdb");
                    Database db = new DatabaseBuilder(file)
                            .setFileFormat(Database.FileFormat.V2000)
                            .open();
                    Table table = db.getTable("Test");
                    Cursor cursor = CursorBuilder.createCursor(table);
                    boolean found = cursor.findFirstRow(Collections.singletonMap("ID", 1));
                    if (found) {
                        System.out.println(String.format("Row found: Name = '%s'.",
                                cursor.getCurrentRowValue(table.getColumn("Name"))));
                    } else {
                        System.out.println("No matching row was found.");
                    }
                    p.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        ;
        return null;
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        new Thread(new Runnable() {

            @Override
            public void run() {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                final String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

                WebcamUtils.capture(webcam, imageFileName, ImageUtils.FORMAT_BMP);
                byte[] b = WebcamUtils.getImageBytes(webcam, "jpg");

                try {
                    BufferedImage imag = ImageIO.read(new ByteArrayInputStream(b));
                    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(b));
                    Graphics g = bi.createGraphics();
                    g.drawImage(imag, 0, 0, 20, 20, null);
                    AffineTransform tx = new AffineTransform();
                    tx.rotate(90);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(imag, "jpg", baos);
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();

                    baos.close();
                    Image newimg = imag.getScaledInstance(89, 100, java.awt.Image.SCALE_SMOOTH);
                    ImageIcon newIcon = new ImageIcon(newimg);
                    jLabel2.setIcon(newIcon);

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            File file = new File(imageFileName + ".bmp");

                            // Tests whether the file denoted by this abstract pathname exists.
                            boolean exists = file.exists();

                            System.out.println("File " + file.getAbsolutePath() + " exists: " + exists);
                        }
                    }).start();
                } catch (IOException ex) {

                } catch (Exception ex) {

                }

            }
        }).start();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        new Thread(new Runnable() {

            @Override
            public void run() {
                File file = new File(candidate_status_txt.getText().toString() + ".bmp");

                // Tests whether the file denoted by this abstract pathname exists.
                boolean exists = file.exists();
                File file2 = new File(candidate_status_txt.getText().toString() + ".fmt");

                // Tests whether the file denoted by this abstract pathname exists.
                boolean exists2 = file2.exists();

                if (exists == true && exists2 == true) {
                    candidate_status.setText("** CHECKED IN **");
                    candidate_status.setForeground(Color.GREEN);
                    markStatus(candidate_status_txt.getText().toString(), 1);
                    moveNext();
                } else {
                    candidate_status.setText("**  Incomplete Data  **");
                }

            }
        }).start();

        //   JOptionPane.showMessageDialog(this, "PPP Eggs are not supposed to be green.");        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        candidate_status.setText("** ABSENT **");
        candidate_status.setForeground(Color.RED);
        candidate_status.setText("AUTH REQUIRED");

        doAbsentbtn.doClick();
        //     JOptionPane.showMessageDialog(this, " AAA are not supposed to be green.");        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        btn_takephoto.doClick();

// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void enrollBiometricbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enrollBiometricbtnActionPerformed

        Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");
        //  Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
        //   Pattern digitPattern = Pattern.compile("\\d{6}");
        System.out.println("does " + candidate_status_txt.getText().toString() + " is number : "
                + digitPattern.matcher(candidate_status_txt.getText().toString()).matches());
        if (digitPattern.matcher(candidate_status_txt.getText().toString()).matches() == true) {
            new UareUSampleJava(candidate_status_txt.getText().toString(), this).show();
        } else {
            JOptionPane.showMessageDialog(this, "ROLLNUMBER ERROR");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_enrollBiometricbtnActionPerformed

    private void doAbsentbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doAbsentbtnActionPerformed

        markStatus(candidate_status_txt.getText().toString(), 0);
//        try {
//            this.m_collection = UareUGlobal.GetReaderCollection();
//            m_collection.GetReaders();
//        } catch (UareUException e1) {
//            // TODO Auto-generated catch block
//            JOptionPane.showMessageDialog(null, "Error getting collection");
//            return;
//        }
//
//        if (m_collection.size() == 0) {
//            MessageBox.Warning("Reader is not selected");
//            return;
//        }
//
//        m_reader = m_collection.get(0);
//
//        if (null == m_reader) {
//            MessageBox.Warning("Reader is not selected");
//        } else {
//            m_enrollmentFmd = this.enrollmentFMD;
//            m_reader = m_reader;
//            m_fmds = new Fmd[2];
//            //     Verification.Run(m_reader, this.enrollmentFMD);
//            try {
//
//                m_reader.Open(Reader.Priority.COOPERATIVE);
//
//            } catch (UareUException e) {
//                MessageBox.DpError("Reader.Open()", e);
//            }
//            candidate_status.setText("AUTH REQUIRED");
//            startLoading();
//
//            //start capture thread
//            StartCaptureThread();
//        }
    }//GEN-LAST:event_doAbsentbtnActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        /*
         Here's one solution using lookarounds: (?<=\D|^)\d+(?=[1-9])\d*

         (?<=\D|^)   # lookbehind for non-digit or beginning of line
         \d+         # match any number of digits 0-9
         (?=[1-9])   # but lookahead to make sure there is 1-9
         \d*         # then match all subsequent digits, once the lookahead is satisfied
         */
//        getCandidateList(jTextField17.getText().toString());
//        Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
//        //   Pattern digitPattern = Pattern.compile("\\d{6}");
//        System.out.println("does " + jTextField17.getText().toString() + " is number : "
//                + digitPattern.matcher(jTextField17.getText().toString()).matches());
//        if (digitPattern.matcher(jTextField17.getText().toString()).matches() == true) {
//            EPOCValid(jTextField17.getText().toString());
//        }
        
      
        //   Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
        Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");

        if (digitPattern.matcher(custom_set_rollnumber.getText().toString()).matches() == true) {
            getCandidateList(rollnumber_txt.getText().toString());
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (NavigationResultSet.next()) {

                            if (NavigationResultSet.getString("rollno").compareTo(custom_set_rollnumber.getText().toString()) == 0) {

                                cname_txt.setText(NavigationResultSet.getString("cname"));
                                fname_txt.setText(NavigationResultSet.getString("fname"));
                                candidate_status.setText(NavigationResultSet.getString("rollno"));
                                candidate_status_txt.setText(NavigationResultSet.getString("rollno"));
                                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String today = formatter.format(NavigationResultSet.getDate("dob1"));
                                System.out.println("Today : " + today);
                                dob_txt.setText(today);
                                getCandidateStatus(NavigationResultSet.getString("rollno"));
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        imageLoad.setIcon(null);
                                        checkBMPExisits();
                                        checkFmtExisits();
                                    }
                                }).start();
                                break;
                            }
                        }

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(RoomSelect.this,
                                e.getMessage(),
                                "Internal Error ",
                                JOptionPane.ERROR_MESSAGE);
                        try {
                            this.finalize();
                        } catch (Throwable ex) {
                            Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }).start();
        } else {
            JOptionPane.showMessageDialog(RoomSelect.this,
                    "Length Exception ",
                    "Inane error",
                    JOptionPane.ERROR_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void roomselectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomselectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roomselectorActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        String getSelected = jComboBox1.getSelectedItem().toString();

        Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d");
        //   Pattern digitPattern = Pattern.compile("\\d{6}");

        if (digitPattern.matcher(getSelected).matches() == true) {
            try {
                getCandidateList(getSelected);
                NavigationResultSet = NavigateCandidate(getSelected);

            } // TODO add your handling code here:
            catch (SQLException ex) {
                Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

//        try {
//            if (NavigationResultSet.next()) {
//
//                cname_txt.setText(NavigationResultSet.getString("cname"));
//                fname_txt.setText(NavigationResultSet.getString("fname"));
//                candidate_status.setText(NavigationResultSet.getString("rollno"));
//                candidate_status_txt.setText(NavigationResultSet.getString("rollno"));
//                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                String today = formatter.format(NavigationResultSet.getDate("dob1"));
//                System.out.println("Today : " + today);
//                dob_txt.setText(today);
//                getCandidateStatus(NavigationResultSet.getString("rollno"));
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        imageLoad.setIcon(null);
//                        checkBMPExisits();
//                        checkFmtExisits();
//                    }
//                }).start();
//
//            }
//
//        } catch (Exception e) {
//
//        }
        moveNext();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    public void moveNext() {
        try {
            if (NavigationResultSet.next()) {

                cname_txt.setText(NavigationResultSet.getString("cname"));
                fname_txt.setText(NavigationResultSet.getString("fname"));
                candidate_status.setText(NavigationResultSet.getString("rollno"));
                candidate_status_txt.setText(NavigationResultSet.getString("rollno"));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(NavigationResultSet.getDate("dob1"));
                System.out.println("Today : " + today);
                dob_txt.setText(today);
                getCandidateStatus(NavigationResultSet.getString("rollno"));
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        imageLoad.setIcon(null);
                        checkBMPExisits();
                        checkFmtExisits();
                    }
                }).start();

            }

        } catch (Exception e) {

        }
    }

    public void moveBack() {
        try {
            if (NavigationResultSet.previous()) {

                cname_txt.setText(NavigationResultSet.getString("cname"));
                fname_txt.setText(NavigationResultSet.getString("fname"));
                candidate_status.setText(NavigationResultSet.getString("rollno"));
                candidate_status_txt.setText(NavigationResultSet.getString("rollno"));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(NavigationResultSet.getDate("dob1"));
                System.out.println("Today : " + today);
                dob_txt.setText(today);
                getCandidateStatus(NavigationResultSet.getString("rollno"));
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        imageLoad.setIcon(null);
                        checkBMPExisits();
                        checkFmtExisits();

                    }
                }).start();

            }

        } catch (Exception e) {

        }
    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        getCandidateList(rollnumber_txt.getText().toString());
        Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
        //   Pattern digitPattern = Pattern.compile("\\d{6}");

        if (digitPattern.matcher(rollnumber_txt.getText().toString()).matches() == true) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (NavigationResultSet.next()) {

                            cname_txt.setText(NavigationResultSet.getString("cname"));
                            fname_txt.setText(NavigationResultSet.getString("fname"));
                            candidate_status.setText(NavigationResultSet.getString("rollno"));
                            if (NavigationResultSet.getString("rollno").compareTo(rollnumber_txt.getText().toString()) == 0) {
                                break;
                            }
                        }

                    } catch (Exception e) {

                    }

//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }).start();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void roomselectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_roomselectorItemStateChanged

        String getSelected = roomselector.getSelectedItem().toString();

        Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d");
        //   Pattern digitPattern = Pattern.compile("\\d{6}");

        if (digitPattern.matcher(getSelected).matches() == true) {
            try {

                NavigationResultSet = NavigateCandidate(getSelected);

            } // TODO add your handling code here:
            catch (SQLException ex) {
                Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

// TODO add your handling code here:
    }//GEN-LAST:event_roomselectorItemStateChanged

    private void openroom_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openroom_btnActionPerformed
        //  jTabbedPane1.setSelectedIndex(1);
        openRoom(111);
        JOptionPane.showMessageDialog(this,
                "Dynamic Room has Been Generated",
                "Important Message ",
                JOptionPane.ERROR_MESSAGE);
// TODO add your handling code here:
    }//GEN-LAST:event_openroom_btnActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        new supervisor.digitalpersona.UareUSampleJava(rinvigilator.getText().toString(), this).show();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        enrollBiometricbtn.doClick();        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        try {
            if (NavigationResultSet.next()) {

                cname_txt.setText(NavigationResultSet.getString("cname"));
                fname_txt.setText(NavigationResultSet.getString("fname"));
                candidate_status.setText(NavigationResultSet.getString("rollno"));
                candidate_status_txt.setText(NavigationResultSet.getString("rollno"));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(NavigationResultSet.getDate("dob1"));
                System.out.println("Today : " + today);
                dob_txt.setText(today);
                getCandidateStatus(NavigationResultSet.getString("rollno"));
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        checkBMPExisits();
                        checkFmtExisits();
                    }
                }).start();

            }

        } catch (Exception e) {

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            if (NavigationResultSet.previous()) {

                cname_txt.setText(NavigationResultSet.getString("cname"));
                fname_txt.setText(NavigationResultSet.getString("fname"));
                candidate_status.setText(NavigationResultSet.getString("rollno"));
                candidate_status_txt.setText(NavigationResultSet.getString("rollno"));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String today = formatter.format(NavigationResultSet.getDate("dob1"));
                System.out.println("Today : " + today);
                dob_txt.setText(today);
                getCandidateStatus(NavigationResultSet.getString("rollno"));
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        checkBMPExisits();
                        checkFmtExisits();

                    }
                }).start();

            }

        } catch (Exception e) {

        }        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        makeZip();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btn_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_yesActionPerformed

// TODO add your handling code here:
    }//GEN-LAST:event_btn_yesActionPerformed

    private void btn_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_noActionPerformed
        writeConfigPreAlloted();
        JOptionPane.showMessageDialog(this,
                "Settings have been Saved",
                "Important Message ",
                JOptionPane.ERROR_MESSAGE);        // TODO add your handling code here:
    }//GEN-LAST:event_btn_noActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        Pattern digitPattern = Pattern.compile("\\d{" + rollLength + "}");
        //Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d");
        //   Pattern digitPattern = Pattern.compile("\\d{6}");

        if (digitPattern.matcher(jTextField9.getText().toString()).matches() == true) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //check image 
                        //check fingerprint
                        //check database
                        //remove valid marks 
                        ///new Prop.GetStorageLocations().getImageFolderName()+"//"
                        File f = new File(jTextField9.getText().toString() + ".png");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        f = new File(new Prop.GetStorageLocations().getImageFolderName() + "//" + jTextField9.getText().toString() + ".bmp");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        f = new File(new Prop.GetStorageLocations().getTemplateFolderName() + "//" + jTextField9.getText().toString() + ".fmt");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        f = new File(new Prop.GetStorageLocations().getFingerFolderName() + "//" + jTextField9.getText().toString() + "-1.png");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        f = new File(new Prop.GetStorageLocations().getFingerFolderName() + "//" + jTextField9.getText().toString() + "-2.png");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        f = new File(new Prop.GetStorageLocations().getFingerFolderName() + "//" + jTextField9.getText().toString() + "-3.png");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        f = new File(new Prop.GetStorageLocations().getFingerFolderName() + "//" + jTextField9.getText().toString() + "-4.png");
                        if (f.exists() == true) {
                            f.delete();
                        }
                        JOptionPane.showMessageDialog(null, "Removed Entry ");
                    } catch (Exception e) {

                    }

//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }).start();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

    private void m_loadFromDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_loadFromDBActionPerformed
        try {
            System.err.print(new Prop.SetupConnectionXML().getLegth());

// TODO add your handling code here:
        } catch (SQLException ex) {
            Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_m_loadFromDBActionPerformed

    public int checkImageExists() {
        return 0;
    }

    public int checkBiometricExists() {
        return 0;
    }

    public int checkImageExists_db() {
        return 0;
    }

    public int checkBiometricExists_db() {
        return 0;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_no;
    private javax.swing.JButton btn_takephoto;
    private javax.swing.JButton btn_yes;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel candidate_status;
    private javax.swing.JTextField candidate_status_txt;
    private javax.swing.JLabel center_status;
    private javax.swing.JButton closeroom_btn;
    private javax.swing.JTextField cname_txt;
    private javax.swing.JTextField custom_set_rollnumber;
    private javax.swing.JButton doAbsentbtn;
    private javax.swing.JTextField dob_txt;
    private javax.swing.JButton enrollBiometricbtn;
    private javax.swing.JTextField fname_txt;
    private javax.swing.JLabel imageLoad;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton m_load;
    private javax.swing.JButton m_loadFromDB;
    private javax.swing.JTextArea m_text;
    private javax.swing.JButton openroom_btn;
    private javax.swing.JPanel packer;
    private javax.swing.JLabel pgStatus;
    private javax.swing.JLabel pngfmtexists;
    private javax.swing.JTextField rinvigilator;
    private javax.swing.JTextField rollnumber_txt;
    private javax.swing.JLabel roomStrength;
    private javax.swing.JLabel room_id;
    private javax.swing.JComboBox roomselector;
    private javax.swing.JLabel showPhototick;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent ke) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int c = ke.getKeyCode();
        if (c == KeyEvent.VK_ENTER) {
            // do something.
            JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Update called with Arguments: " + arg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACT_BACK)) {
            //cancel capture
            StopCaptureThread();
            m_dlgParent.setVisible(false);
        } else if (e.getActionCommand().equals(ACT_LOAD_FROM_DB)) {

            try {
                ConsoleMsg("Opening Database Connection ...");
                ConsoleMsg(("Connecting ..... "));
                db.Open();

                this.m_listOfRecords = db.GetAllFPData();
                for (FingerDB.Record record : this.m_listOfRecords) {
                    Fmd fmd = UareUGlobal.GetImporter().ImportFmd(record.fmdBinary, com.digitalpersona.uareu.Fmd.Format.DP_REG_FEATURES, com.digitalpersona.uareu.Fmd.Format.DP_REG_FEATURES);
                    this.m_fmdList.add(fmd);
                }
                m_fmdArray = new Fmd[this.m_fmdList.size()];
                this.m_fmdList.toArray(m_fmdArray);
                ConsoleMsg("Data Saved Localy ");
                ConsoleMsg("Loading Info of Users ");
                //       loadDbinMem();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                MessageBox.DpError("Failed to load FMDs from database.  Please check connection string in code.", null);
                return;
            } catch (UareUException e1) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, "Error importing fmd data.");
                return;
            }

            this.m_load.setEnabled(false); //Dont allow user to load fmd from file (confusing).
            this.m_enrollmentFmd = null;

        } else if (e.getActionCommand().equals(CaptureThread.ACT_CAPTURE)) {
            //process result
            CaptureThread.CaptureEvent evt = (CaptureThread.CaptureEvent) e;
            if (evt.capture_result.image != null) {
                if (ProcessCaptureResult(evt)) {
                    //restart capture thread
                    WaitForCaptureThread();
                    StartCaptureThread();
                } else {
                    //destroy dialog
                    m_dlgParent.setVisible(false);
                }
            }
        }

    }

    public void ConsoleMsg(String msg) {

        m_text.append("\n" + msg);
        m_text.setCaretPosition(m_text.getDocument().getLength());
    }

    private boolean ProcessCaptureResult(CaptureThread.CaptureEvent evt) {
        boolean bCanceled = false;
        bajao();
        ConsoleMsg("\n Searching for Fingerprint In Database Please Wait \n\n Search In Progress ...");

        if (this.m_enrollmentFmd == null && this.m_listOfRecords.size() == 0) {
            MessageBox.Warning("You cannot verify until you register or load a template.");
            m_loadFromDB.doClick();
            return !bCanceled;
        }

        if (null != evt.capture_result) {
            if (null != evt.capture_result.image && Reader.CaptureQuality.GOOD == evt.capture_result.quality) {
                //extract features
                Engine engine = UareUGlobal.GetEngine();

                try {
                    //   m_imagePanel.showImage(evt.capture_result.image);
                    Fmd fmd = engine.CreateFmd(evt.capture_result.image, Fmd.Format.DP_VER_FEATURES);
                    m_fmds[0] = fmd;

                    //Lets perform 1:1 comparison
                    if (this.m_enrollmentFmd != null) {
                        m_fmds[1] = this.m_enrollmentFmd;

                        try {
                            int falsematch_rate = engine.Compare(m_fmds[0], 0, m_fmds[1], 0);
                            int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000; //target rate is 0.00001
                            if (falsematch_rate < target_falsematch_rate) {

                                m_text.append("MATCHED !!!\n");
                                String str = String.format("    dissimilarity score: 0x%x.\n", falsematch_rate);
                                m_text.append(str);
                                str = String.format("    false match rate: %e.\n\n\n", (double) (falsematch_rate / Engine.PROBABILITY_ONE));
                                m_text.append(str);
                            } else {
                                longbajao();
                                m_text.append("NO MATCH!!!\n\n\n");
                            }
                        } catch (UareUException e) {
                            MessageBox.DpError("Engine.Compare exception()", e);
                        }

                        //discard FMDs
                        m_fmds[0] = null;
                        m_fmds[1] = null;

                    } else //Perform identification
                    {
                        int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000; //target rate is 0.00001
                        Engine.Candidate[] matches = engine.Identify(m_fmds[0], 0, m_fmdArray, target_falsematch_rate, 1);
                        if (matches.length == 1) {
                            ConsoleMsg("\n\n User Found : Details Loading ...");
                            StopCaptureThread();
                            WaitForCaptureThread();

                            //close reader
                            try {
                                if (m_reader != null) {
                                    m_reader.Close();
                                }
                            } catch (UareUException e) {
                                MessageBox.DpError("Reader.Close()", e);
                            }
                            candidate_status.setText("** ABSENT **");
                            markStatus(candidate_status_txt.getText().toString(), 0);
                            JOptionPane.showMessageDialog(this,
                                    "Student Marked Absent by " + this.m_listOfRecords.get(matches[0].fmd_index),
                                    "Figerprint Confirmed", 1);
                            //    setImageII(this.m_listOfRecords.get(matches[0].fmd_index).photo0, this.m_listOfRecords.get(matches[0].fmd_index).cname, this.m_listOfRecords.get(matches[0].fmd_index).fname, "");
                        } else {
                            ConsoleMsg("\n\n User NOT FOUND  ...");
                            longbajao();
                        }
                    }
                } catch (UareUException e) {
                    MessageBox.DpError("Engine.CreateFmd()", e);
                }
            } else {
                //the loop continues
                m_text.append(m_strPrompt2);
            }
        } else if (Reader.CaptureQuality.CANCELED == evt.capture_result.quality) {
            //capture or streaming was canceled, just quit
            bCanceled = true;
        } else if (null != evt.exception) {
            //exception during capture
            MessageBox.DpError("Capture", evt.exception);
            bCanceled = true;
        } else if (null != evt.reader_status) {
            //reader failure
            MessageBox.BadStatus(evt.reader_status);
            bCanceled = true;
        } else {
            //bad quality
            MessageBox.BadQuality(evt.capture_result.quality);
        }

        return !bCanceled;
    }

    public void markStatus(final String rollnumber, final int status) {

        try {
            // This will load the MySQL driver, each DB has its own driver

            PreparedStatement preparedStatement = null;
            preparedStatement = connect
                    .prepareStatement("insert into  attendence(barcode,status,room_id) values (?,?,?)");

            preparedStatement.setString(
                    1, rollnumber);

            preparedStatement.setInt(
                    2, status);
            preparedStatement.setInt(
                    3, room_id_open);
            preparedStatement.executeUpdate();

            try {
                Thread.sleep(2000);
                jButton2.doClick();
                bajao();
            } catch (InterruptedException ex) {
                Logger.getLogger(RoomSelect.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            longbajao();
        } finally {
            getPassCount();
            getFailedCount();
        }

    }

    public void openRoom(final int rollnumber) {

        try {
            // This will load the MySQL driver, each DB has its own driver

            PreparedStatement preparedStatement = null;
            preparedStatement = connect
                    .prepareStatement("insert into  room(authid) values (?)");

            preparedStatement.setInt(
                    1, rollnumber);

            preparedStatement.executeUpdate();
            ResultSet keyResultSet = preparedStatement.getGeneratedKeys();
            int newCustomerId = 0;
            if (keyResultSet.next()) {
                newCustomerId = (int) keyResultSet.getInt(1);
                System.out.println(newCustomerId + " The Generated Key ID is  ?? <+");

                room_id.setText("CURRENT ROOM OPEN : " + newCustomerId);
                openroom_btn.setEnabled(false);
                room_id_open = newCustomerId;
                rinvigilator.setText(String.valueOf(newCustomerId));
                rinvigilator.setEnabled(false);
                writeConfig(String.valueOf(newCustomerId));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void StartCaptureThread() {
        m_capture = new CaptureThread(m_reader, false, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT);
        m_capture.start(this);
    }

    public static void tone(final int hz, final int msecs, final double vol)
            throws LineUnavailableException {

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                byte[] buf = new byte[1];
                AudioFormat af
                        = new AudioFormat(
                                8000f, // sampleRate
                                8, // sampleSizeInBits
                                1, // channels
                                true, // signed
                                false);      // bigEndian
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();
                for (int i = 0; i < msecs * 8; i++) {
                    double angle = i / (8000f / hz) * 2.0 * Math.PI;
                    buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
                    sdl.write(buf, 0, 1);
                }
                sdl.drain();
                sdl.stop();
                sdl.close();
                return null;
//  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
                .execute();

    }

    private void StopCaptureThread() {
        if (null != m_capture) {
            m_capture.cancel();
        }
    }

    private void WaitForCaptureThread() {
        if (null != m_capture) {
            m_capture.join(10000);

        }
    }

    public void bajao() {
        try {
            tone(1000, 100, 0.9);

        } catch (LineUnavailableException ex) {
            java.util.logging.Logger.getLogger(Verification.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void longbajao() {
        try {
            tone(5000, 500, 0.9);

        } catch (LineUnavailableException ex) {
            java.util.logging.Logger.getLogger(Verification.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startLoading() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                bajao();
                candidate_status.setText("PUT FINGER");
                ConsoleMsg("Loading Completed "); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected void process(List<Object> chunks) {
                super.process(chunks); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected Object doInBackground() throws Exception {

                Thread.sleep(2000);
                try {
                    ConsoleMsg("Opening Database Connection ...");
                    ConsoleMsg(("Connecting ..... "));
                    ConsoleMsg(("This Process may take 1 Min. ..... "));
                    ConsoleMsg(("Be Patient  ..... "));
                    db.Open();

                    m_listOfRecords = db.GetAllFPData();
                    for (FingerDB.Record record : m_listOfRecords) {
                        Fmd fmd = UareUGlobal.GetImporter().ImportFmd(record.fmdBinary, com.digitalpersona.uareu.Fmd.Format.DP_REG_FEATURES, com.digitalpersona.uareu.Fmd.Format.DP_REG_FEATURES);
                        m_fmdList.add(fmd);
                    }
                    m_fmdArray = new Fmd[m_fmdList.size()];
                    m_fmdList.toArray(m_fmdArray);

                    //  loadDbinMem();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    ConsoleMsg("ERROR MESSAGE " + e1.getMessage());
                    MessageBox.DpError("Failed to load FMDs from database.  Please check connection string in code.", null);

                } catch (UareUException e1) {

                }

                return null;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();

    }

    public void makeZip() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {

                ConsoleMsg("ZIP File Created ... ");
//super.done(); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected Object doInBackground() throws Exception {

                ZipUtil zipper = new ZipUtil();
                ConsoleMsg("Reading Directory ... ");
                File directoryToZip = new File(GetExecutionPath());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String zipFilePath = "E:\\CSBC_BIOMETRIC_Backup_" + timeStamp + ".zip";
                List<File> listFiles = new ArrayList<File>(1);
                ConsoleMsg("ZIP Indexing  ... ");
                listFiles.add(directoryToZip);
                ConsoleMsg("ZIP Compression in Progress ... ");
                zipper.compressFiles(listFiles, zipFilePath);

                return null;
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();
        // TODO add your handling code here:
    }

    public String GetExecutionPath() {
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        absolutePath = absolutePath.replaceAll("%20", " "); // Surely need to do this here
        return absolutePath;
    }

}

class ZipUtil {

    /**
     * A constants for buffer size used to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Compresses a collection of files to a destination zip file
     *
     * @param listFiles A collection of files and directories
     * @param destZipFile The path of the destination zip file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void compressFiles(List<File> listFiles, String destZipFile) throws FileNotFoundException, IOException {

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));

        for (File file : listFiles) {
            if (file.isDirectory()) {
                addFolderToZip(file, file.getName(), zos);
            } else {
                addFileToZip(file, zos);
            }
        }

        zos.flush();
        zos.close();
    }

    /**
     * Adds a directory to the current zip output stream
     *
     * @param folder the directory to be added
     * @param parentFolder the path of parent directory
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void addFolderToZip(File folder, String parentFolder,
            ZipOutputStream zos) throws FileNotFoundException, IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addFolderToZip(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }

            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));

            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));

            long bytesRead = 0;
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;

            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
                bytesRead += read;
            }

            zos.closeEntry();

        }
    }

    /**
     * Adds a file to the current zip output stream
     *
     * @param file the file to be added
     * @param zos the current zip output stream
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void addFileToZip(File file, ZipOutputStream zos)
            throws FileNotFoundException, IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));

        long bytesRead = 0;
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;

        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
            bytesRead += read;
        }

        zos.closeEntry();
    }
}

class ObservedObject extends Observable {

    private String watchedValue;

    public ObservedObject(String value) {
        watchedValue = value;
    }

    public void setValue(String value) {
        // if value has changed notify observers
        if (!watchedValue.equals(value)) {
            System.out.println("Value changed to new value: " + value);
            watchedValue = value;

            // mark as value changed
            setChanged();
            // trigger notification
            notifyObservers(value);
        }
    }
}
