package csbc.attendence;

import Prop.SetupConnectionXML;
import fingerprint.authenticator.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class FingerDB {


    private static final String IP = "127.0.0.1";
    private java.sql.Connection connection = null;

    private String preppedStmtInsert = null;
    private String preppedStmtUpdate = null;
    public Verification vM;

    public class Record {

        String userID;
        byte[] fmdBinary;
        byte[] photo0;
        String rollno;
        String cname;
        String fname;

        Record(String ID, byte[] fmd, byte[] photo, String cn, String fn) {
            userID = ID;
            fmdBinary = fmd;
            photo0 = photo;
            cname = cn;
            fname = fn;

        }
    }


    public void initialize() {
        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Thread.sleep(1000);
                    connection = new SetupConnectionXML().getConnection();
             

                    return "OK";
                } catch (ClassNotFoundException ex) {
                    System.out.println("Connection Failed ..." +ex.getMessage());
                } catch (SQLException ex) {
                 System.out.println("Connection Failed ..." +ex.getMessage());
                } catch (InterruptedException ex) {
                   System.out.println("Connection Failed ..." +ex.getMessage());

                } catch (IOException ex) {
                    Logger.getLogger(FingerDB.class.getName()).log(Level.SEVERE, null, ex);
                }
                return "ERROR";
            }
        }.execute();

    }

    public FingerDB(String _host, String db, String user, String password) {

        initialize();
    }

    public void finalize() {
        try {
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Open() throws SQLException {
        initialize();
    }

    public void Close() throws SQLException {
        connection.close();
    }

   
    public List<Record> GetAllFPData() throws SQLException {
        int i = 0;
        List<Record> listUsers = new ArrayList<Record>();
        String sqlStmt = "SELECT * from fingerprint where user_type=9";
        Statement st = connection.createStatement();
        System.out.println("Executing Query...");
        System.out.println(sqlStmt);
        ResultSet rs = st.executeQuery(sqlStmt);
        while (rs.next()) {
            if (rs.getBytes("fmd0") != null) {
                listUsers.add(new Record(rs.getString("rollno"), rs.getBytes("fmd0"), rs.getBytes("photo0"), rs.getString("cname"), rs.getString("fname")));

                System.out.println("Spooling Records..." + i++);
            }
        }
        return listUsers;
    }

   

   

    public String GetConnectionString() {
        return " User: ";
    }

  
}
