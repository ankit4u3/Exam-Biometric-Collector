package fingerprint.authenticator;

import Prop.SetupConnectionXML;
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

    private static final String tableName = "fingerprint";
    private static final String userColumn = "barcode";
    private static final String print1Column = "print1";
    private static final String print2Column = "print2";
    private static final String IP = "127.0.0.1";
    private java.sql.Connection connection = null;
    private java.sql.Connection connectionSports = null;
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

    public String getInfo(final String barcode) {

        try {
            String selectSQL = "select * from total where total.rollno = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, barcode);
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            while (rs.next()) {
                String userid = rs.getString("cname");
                String username = rs.getString("fname");
                return userid + " " + username;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "NOT FOUND";
        }
        return "NOT FOUND";
    }

    public HashMap getDictionaryHashes() throws ClassNotFoundException, SQLException {

        String query = "select barcode,snap0 from lapdetails\n"
                + "where lapdetails.`status`=1 \n"
                + "order by edate desc limit 1500 ";

        Statement stmt = null;
        Dictionary jackets = new Hashtable();
        HashMap<String, Object> mapper = new HashMap();
        try {
            stmt = connectionSports.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            while (rslt.next()) {

                mapper.put(String.valueOf(rslt.getString("barcode")),
                        new candidate(
                                rslt.getString("barcode"), //1

                                rslt.getBytes("snap0")//10

                        )
                );
                System.out.println(" Loading   " + String.valueOf(rslt.getString("barcode")));

                Object obj = mapper.get(String.valueOf(rslt.getString("barcode")));
                // ((candidate) obj).getCname();

                System.out.println(((candidate) obj).getAppno());
            }
            return mapper;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                stmt.close();
            } catch (Exception e) {
            }
        }
        return null;

    }

    public void initialize() {
        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() {
                try {
                 connection   = new SetupConnectionXML().getConnection();
                    return "OK";
                } catch (SQLException ex) {
                    System.out.println("Connection Failed ...");
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

    public void getDetails(String str) {

        if (str != null) {

            System.out.println(">>>>>>>>>>>>>>> Searching .....");

            ResultSet rs = null;
            PreparedStatement prepstmt = null;
            try {
                Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);

                st = connection.createStatement();
                if (connection.isClosed() == true) {
//
                }

                prepstmt = connection
                        .prepareStatement("select jacket from completejacketlink_info  where completejacketlink_info.barcode=?");
                prepstmt.setString(1, str);
                //     prepstmt.setInt(2, 111);

                rs = prepstmt.executeQuery();

                if (rs.next()) {
                    System.out.println(">>>>>>>>>>>>>>> Found .....");
                    String rowCount = rs.getString("jacket");
                    System.out.println(rowCount);
                    //     ConsoleMsg("Registration Count for SHift " + str + "   iS   " + rowCount);
                    str = null;
                }
            } catch (Exception e) {

                System.out.println("Error in Thread Process a6" + e.getMessage());
            }

        }
    }

    public String getInfoDetails(String str) {

        if (str != null) {

            System.out.println(">>>>>>>>>>>>>>> Searching .....");

            ResultSet rs = null;
            PreparedStatement prepstmt = null;
            try {
                Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);

                st = connection.createStatement();
                if (connection.isClosed() == true) {
//
                }

                prepstmt = connection
                        .prepareStatement("select * from total where rollno=?");
                prepstmt.setString(1, str);
                //     prepstmt.setInt(2, 111);

                rs = prepstmt.executeQuery();

                if (rs.next()) {
                    System.out.println(">>>>>>>>>>>>>>> Found .....");
                    String rowCount = rs.getString("cname");
                    String rowfCount = rs.getString("fname");
                    System.out.println(rowCount + " : " + rowfCount);

                    //     ConsoleMsg("Registration Count for SHift " + str + "   iS   " + rowCount);
                    str = null;
                    return "Candidate Name : " + rowCount + " : \n Father Name \n " + rowfCount;
                }
            } catch (Exception e) {

                System.out.println("Error in Thread Process a6" + e.getMessage());
            }

        }
        return "NOT FOUND";
    }

    public boolean UserExists(String userID) throws SQLException {
        String sqlStmt = "Select " + userColumn + " from " + tableName + " WHERE " + userColumn + "='" + userID + "'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sqlStmt);
        return rs.next();
    }

    public void Insert(String userID, byte[] print1) throws SQLException {
        java.sql.PreparedStatement pst = connection.prepareStatement(preppedStmtInsert);
        pst.setString(1, userID);
        pst.setBytes(2, print1);
        pst.execute();
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    public void InsertMarks(String userID, int type, int marks, String who) {
        try {
            String insertTableSQL = "INSERT INTO workout"
                    + "(barcode, typeid, marks,operator) VALUES"
                    + "(?,?,?,?)";
            PreparedStatement preparedStatement = connectionSports.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, type);
            preparedStatement.setInt(3, marks);
            preparedStatement.setString(4, who);
            preparedStatement.executeUpdate();
            System.out.println("Saved ...");
        } catch (SQLException ex) {
            Logger.getLogger(FingerDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getWorkOutInfo(final String barcode, final int id) {

        try {

            String selectSQL = "SELECT barcode, marks ,typeid,sum(marks) as 'marksobt'\n"
                    + "  FROM  workout\n"
                    + "  WHERE barcode =" + barcode
                    + "  and typeid in (1,2,3)\n"
                    + "  ";

            PreparedStatement preparedStatement = connectionSports.prepareStatement(selectSQL);
            //    preparedStatement.setString(1, barcode.trim());
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            while (rs.next()) {
                String userid = rs.getString("barcode");
                String username = String.valueOf(rs.getInt("marksobt"));
                return userid + " " + username;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "NOT FOUND";
        }
        return "NOT FOUND";
    }

    public String getWorkOutInfoSum(final String barcode, final int id) {

        try {

            String selectSQL = "SELECT sum(marks) as 'marksobt'\n"
                    + "  FROM  workout\n"
                    + "  WHERE barcode =" + barcode
                    + "  and typeid in (1,2) \n"
                    + "  ";

            PreparedStatement preparedStatement = connectionSports.prepareStatement(selectSQL);
            //    preparedStatement.setString(1, barcode.trim());
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            while (rs.next()) {

                String username = String.valueOf(rs.getInt("marksobt"));
                if (rs.getInt("marksobt") >= 5) {
                    return "PASS : " + username;
                } else {
                    return "FAIL";
                }
                // return username;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "NOT FOUND";
        }
        return "NOT FOUND";
    }

    public String getWorkOutInfoHasRun100M(final String barcode, final int id) {

        try {

            String selectSQL = "SELECT count(*) as 'marksx'\n"
                    + "  FROM  workout\n"
                    + "  WHERE barcode =" + barcode
                    + "  and typeid =2\n"
                    + "  ";

            PreparedStatement preparedStatement = connectionSports.prepareStatement(selectSQL);
            //    preparedStatement.setString(1, barcode.trim());
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            if (rs.next()) {

                if (rs.getInt("marksx") == 0) {
                    System.out.println("NOTRUN");
                    return "NOTRUN";
                } else {
                    System.out.println("HASRUN");
                    return "HASRUN";
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "ERROR";
        }
        return "NOT FOUND";
    }

    public String getWorkOutInfo100M(final String barcode, final int id) {

        try {

            String selectSQL = "SELECT barcode, marks ,typeid,sum(marks) as 'marksobt'\n"
                    + "  FROM  workout\n"
                    + "  WHERE barcode =" + barcode
                    + "  and typeid =1\n"
                    + "  ";

            PreparedStatement preparedStatement = connectionSports.prepareStatement(selectSQL);
            //    preparedStatement.setString(1, barcode.trim());
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            while (rs.next()) {

                String username = String.valueOf(rs.getInt("marks"));
                return username;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "NOT FOUND";
        }
        return "NOT FOUND";
    }

    public String getWorkOutInfoLongJump(final String barcode, final int id) {

        try {

            String selectSQL = "SELECT barcode, marks ,typeid,sum(marks) as 'marksobt'\n"
                    + "  FROM  workout\n"
                    + "  WHERE barcode =" + barcode
                    + "  and typeid =2\n"
                    + "  ";

            PreparedStatement preparedStatement = connectionSports.prepareStatement(selectSQL);
            //    preparedStatement.setString(1, barcode.trim());
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            while (rs.next()) {

                String username = String.valueOf(rs.getInt("marks"));
                return username;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "NOT FOUND";
        }
        return "NOT FOUND";
    }

    public String getWorkOutInfoHighJump(final String barcode, final int id) {

        try {

            String selectSQL = "SELECT barcode, marks ,typeid,sum(marks) as 'marksobt'\n"
                    + "  FROM  workout\n"
                    + "  WHERE barcode =" + barcode
                    + "  and typeid =3\n"
                    + "  ";

            PreparedStatement preparedStatement = connectionSports.prepareStatement(selectSQL);
            //    preparedStatement.setString(1, barcode.trim());
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            while (rs.next()) {

                String username = String.valueOf(rs.getInt("marks"));
                return username;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "NOT FOUND";
        }
        return "NOT FOUND";
    }

    public List<Record> GetAllFPData() throws SQLException {
        int i = 0;
        List<Record> listUsers = new ArrayList<Record>();
        String sqlStmt = "SELECT `fingerprint`.`fingerprint`.`fmd0`, `fingerprint`.`snaps`.`photo0`,\n"
                + "  `fingerprint`.`total`.`cname`, `fingerprint`.`total`.`fname`,\n"
                + "  `fingerprint`.`total`.`Rollno`, `fingerprint`.`total`.`edate`,\n"
                + "  `fingerprint`.`total`.`dob`, `fingerprint`.`total`.`sex`, `fingerprint`.`total`.`cat`\n"
                + "  FROM\n"
                + "       `fingerprint`.`fingerprint` JOIN `fingerprint`.`snaps` ON `fingerprint`.`fingerprint`.`barcode` = `fingerprint`.`snaps`.`rollno` JOIN `fingerprint`.`total` ON `fingerprint`.`snaps`.`rollno` = `fingerprint`.`total`.`Rollno` limit 10";
        Statement st = connection.createStatement();
        System.out.println("Executing Query...");
        System.out.println(sqlStmt);
        ResultSet rs = st.executeQuery(sqlStmt);
        while (rs.next()) {
            if (rs.getBytes("fmd0") != null) {
                listUsers.add(new Record(rs.getString("Rollno"), rs.getBytes("fmd0"), rs.getBytes("photo0"), rs.getString("cname"), rs.getString("fname")));

                System.out.println("Spooling Records..." + i++);
            }
        }
        return listUsers;
    }

    public List<Record> GetAllFPOperator() throws SQLException {
        int i = 0;
        List<Record> listUsers = new ArrayList<Record>();
        String sqlStmt = "SELECT `fingerprint`.`fingerprintoperator`.`fmd0`, `fingerprint`.`snaps`.`photo0`,\n"
                + "  `fingerprint`.`total`.`cname`, `fingerprint`.`total`.`fname`,\n"
                + "  `fingerprint`.`total`.`Rollno`, `fingerprint`.`total`.`edate`,\n"
                + "  `fingerprint`.`total`.`dob`, `fingerprint`.`total`.`sex`, `fingerprint`.`total`.`cat`\n"
                + "  FROM\n"
                + "       `fingerprint`.`fingerprintoperator` JOIN `fingerprint`.`snaps` ON `fingerprint`.`fingerprintoperator`.`barcode` = `fingerprint`.`snaps`.`rollno` JOIN `fingerprint`.`total` ON `fingerprint`.`snaps`.`rollno` = `fingerprint`.`total`.`Rollno` limit 10";

        Statement st = connection.createStatement();
        System.out.println("Executing Query...");
        System.out.println(sqlStmt);
        ResultSet rs = st.executeQuery(sqlStmt);
        while (rs.next()) {
            if (rs.getBytes("fmd0") != null) {
                listUsers.add(new Record(rs.getString("Rollno"), rs.getBytes("fmd0"), rs.getBytes("photo0"), rs.getString("cname"), rs.getString("fname")));

                System.out.println("Spooling Records..." + i++);
            }
        }
        return listUsers;
    }

    public List<Record> GetAllFPData(Verification v) throws SQLException {
        int i = 0;
        vM = v;
        List<Record> listUsers = new ArrayList<Record>();
        String sqlStmt = "SELECT `fingerprint`.`fingerprint`.`fmd0`, `fingerprint`.`snaps`.`photo0`,\n"
                + "  `fingerprint`.`total`.`cname`, `fingerprint`.`total`.`fname`,\n"
                + "  `fingerprint`.`total`.`Rollno`, `fingerprint`.`total`.`edate`,\n"
                + "  `fingerprint`.`total`.`dob`, `fingerprint`.`total`.`sex`, `fingerprint`.`total`.`cat`\n"
                + "  FROM\n"
                + "       `fingerprint`.`fingerprint` JOIN `fingerprint`.`snaps` ON `fingerprint`.`fingerprint`.`barcode` = `fingerprint`.`snaps`.`rollno` JOIN `fingerprint`.`total` ON `fingerprint`.`snaps`.`rollno` = `fingerprint`.`total`.`Rollno`";
        Statement st = connection.createStatement();
        System.out.println("Executing Query...");
        ResultSet rs = st.executeQuery(sqlStmt);
        while (rs.next()) {
            if (rs.getBytes("fmd0") != null) {
                listUsers.add(new Record(rs.getString("Rollno"), rs.getBytes("fmd0"), rs.getBytes("photo0"), rs.getString("cname"), rs.getString("fname")));

                System.out.println("Spooling Records..." + i++);
                vM.ConsoleMsg("Building Data Tree ." + rs.getString("Rollno"));
            }
        }
        vM.ConsoleMsg("Data Loading Completed Records Available " + i);
        return listUsers;
    }

    public String GetConnectionString() {
        return " User: ";
    }

    public String GetExpectedTableSchema() {
        return "Table: " + tableName + " PK(VARCHAR(32)): " + userColumn + "VARBINARY(4000): " + print1Column;
    }
}
