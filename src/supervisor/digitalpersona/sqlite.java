package supervisor.digitalpersona;

import tracker.digitalpersona.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

class sqlite {

    List<Record> listUsers;
    private final Connection conn;
    int iTimeout = 3000;
    String sMakeTable = "CREATE TABLE IF NOT EXISTS candidate  (id INTEGER PRIMARY KEY   AUTOINCREMENT, response text,name text,fname text,mname text,dob text,shift text,examdate text,blacklist text,present text,shiftchanged text,terminal text,photo blob,fmd0 blob,fmd1 blob,fmd2 blob,fmd3 blob,fmd4 blob,fmd5 blob)";
    String sMakeAttendence = "CREATE TABLE IF NOT EXISTS attendence  (id INTEGER PRIMARY KEY   AUTOINCREMENT, response text,intime text,outtime text)";
    String sMakeInsert = "INSERT INTO candidate VALUES(1,'Hello from the database')";
    String sMakeSelect = "SELECT response from dummy";
    private String preppedStmtInsert = null;
    private String preppedStmtUpdate = null;

    public class Record {

        String userID;
        byte[] fmdBinary;
        byte[] photo;

        Record(String ID, byte[] fmd, byte[] imag) {
            userID = ID;
            fmdBinary = fmd;
            photo = imag;

        }
    }

//     public class Record {
//
//        String userID;
//        byte[] fmdBinary;
//
//
//        Record(String ID, byte[] fmd) {
//            userID = ID;
//            fmdBinary = fmd;
//
//        }
//    }
    public sqlite() throws ClassNotFoundException, SQLException {
        String sDriverName = "org.sqlite.JDBC";

        Calendar cal = Calendar.getInstance();
        Class.forName(sDriverName);
        String sTempDb = "candidate.db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb;
        // which will produce a legitimate Url for SqlLite JDBC :
        // jdbc:sqlite:hello.db

        conn = DriverManager.getConnection(sDbUrl);
        Statement stmt = conn.createStatement();

        stmt.setQueryTimeout(iTimeout);
        stmt.executeUpdate(sMakeTable);
        stmt.executeUpdate(sMakeAttendence);
        // stmt.executeUpdate(sMakeInsert);
    }

    public void addCandidateTemplateToDB(String id, byte[] b) throws SQLException {

        String sql = "UPDATE FINAL SET fmd0=? Where roll_number=?";

        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setBytes(1, b);
            preparedStatement.setString(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Template Updated Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Detected Try Again");
        }

    }

    public void insert2jokes(String body, String md5val, Byte[] b) throws SQLException {

        Statement insertSt = conn.createStatement();
        insertSt.executeUpdate("INSERT INTO candidate (id,response,photo) VALUES ( '" + md5val + "','" + body + "','"
                + b + "')");
        insertSt.close();

        System.out.println("Insertion Complete for Id: " + body);
    }

    public void insertAttendence(String response, String intime, String outtime) throws SQLException {

        Statement insertSt = conn.createStatement();
        insertSt.executeUpdate("INSERT INTO attendence (response,intime,outtime) VALUES ( '" + response + "','" + intime + "','"
                + outtime + "')");
        insertSt.close();

    }

    public void Insert(String id, byte[] b) throws SQLException {

        String sql = "UPDATE FINAL SET fmd0=? Where roll_number=?";

        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setBytes(1, b);
            preparedStatement.setString(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Template Updated Successfully for ");
        } catch (SQLException ex) {

        }

    }

    public void InsertImageClicked(String userID, byte[] print1) throws SQLException {
        preppedStmtInsert = "INSERT INTO " + "candidate" + "(" + "response" + "," + "photonew" + ") VALUES(?,?)";
        java.sql.PreparedStatement pst = conn.prepareStatement(preppedStmtInsert);
        pst.setString(1, userID);
        pst.setBytes(2, print1);
        pst.execute();
    }

//    public List<Record> GetAllFPData() throws SQLException {
//        List<Record> listUsers = new ArrayList<Record>();
//        String sqlStmt = "Select * from " + "FINAL";
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery(sqlStmt);
//        while (rs.next()) {
//            if (rs.getBytes("fmd0") != null) {
//                listUsers.add(new Record(rs.getString("roll_number"), rs.getBytes("fmd0"),rs.getBytes("photo")));
//            }
//        }
//        return listUsers;
//    }
    public List<Record> GetAllFPData() throws SQLException {
        List<Record> listUsers = new ArrayList<Record>();
        String sqlStmt = "Select * from " + "FINAL ";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sqlStmt);
        while (rs.next()) {
            if (rs.getBytes("fmd0") != null) {
                listUsers.add(new Record(rs.getString("roll_number"), rs.getBytes("fmd0"), rs.getBytes("photo")));

            }
        }
        return listUsers;
    }

    public void insert2candidate(String id, String name, String barcode, String fmd) throws SQLException {

        Statement insertSt = conn.createStatement();
        insertSt.executeUpdate("INSERT INTO candidate (id,response,fdm1,fdm2) VALUES ( '" + id + "','" + name + "','"
                + barcode + "','" + fmd + "','" + fmd + "')");
        insertSt.close();

        System.out.println("Insertion Complete for Id: " + id);
    }

    public void updatecandidate(String id, byte[] b) throws SQLException {

        String update = "UPDATE CANDIDATE SET PHOTO=" + b.toString() + " WHERE RESPONSE=" + '"' + id + '"';
        Statement insertSt = conn.createStatement();
        insertSt.executeUpdate(update);
        System.out.println("Jacket Allocated to Candidate Successful : " + id);
    }

    public void updatecandidatejacket(String id, String jacket) throws SQLException {
        String update = "UPDATE CANDIDATE SET PHOTO=1 WHERE RESPONSE=" + '"' + id + '"';
        Statement insertSt = conn.createStatement();
        insertSt.executeUpdate(update);
        System.out.println("Jacket Allocated to Candidate Successful : " + id);

    }
    // create fmd update statement based on candidate barcode number
    //update candidate set fmd=this where barcode =this

    public void terminate() throws SQLException {

        conn.close();

    }

    @SuppressWarnings("oracle.jdeveloper.java.insufficient-catch-block")
    public void getduplicate() throws SQLException {

        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.setQueryTimeout(iTimeout);
                //	stmt.executeUpdate(sMakeTable);
                //	stmt.executeUpdate(sMakeInsert);
                ResultSet rs
                        = stmt.executeQuery(" SELECT id , COUNT(*) FROM candidate GROUP BY response HAVING COUNT(*) > 1");
                try {
                    while (rs.next()) {
                        String sResult = rs.getString(1); //getString("response");

                        System.out.println(sResult);

                    }
                } finally {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                }
            } finally {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                conn.close();
                terminate();

            } catch (Exception ignore) {
            }
        }
    }

//    public Candidate getCandidateInfofromBarcode(String barcode) throws SQLException {
//
//        String query = "SELECT * FROM candidate where response='" + barcode + "'";
//        System.out.println(query);
//        Candidate c = new Candidate();
//
//        Statement stmt = conn.createStatement();
//        try {
//            //   stmt.setQueryTimeout(iTimeout);
//            //      stmt.executeUpdate(sMakeTable);
//            //      stmt.executeUpdate(sMakeInsert);
//            ResultSet rs = stmt.executeQuery(query);
//            try {
//                while (rs.next()) {
//                    String sResult = rs.getString(2); //getString("response");
//                    System.out.println(sResult);
//                    c.setName(rs.getString(2));
//                    c.setRollno(rs.getString(1));
//                }
//            } catch (Exception e) {
//            }
//        } catch (Exception e) {
//        }
//        return c;
//
//    }
}
