/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supervisor.digitalpersona;

import tracker.digitalpersona.*;
import Prop.PropertyConnect;
import Prop.SetupConnectionXML;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLC {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public static String CONNECTION = "";

    public MySQLC() {
        try {
                   connect   = new SetupConnectionXML().getConnection();
             

                  
                } catch (SQLException ex) {
                 System.out.println("Connection Failed ..." +ex.getMessage());
                } catch (IOException ex) {
            Logger.getLogger(MySQLC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public MySQLC(String username, String password) {
        try {
            Prop.PropertyConnect prop = new PropertyConnect();
            connect = prop.getConnection();
        } catch (Exception e) {
        }
    }

    public void SetupConnection() throws Exception {
        try {
            Prop.PropertyConnect prop = new PropertyConnect();
            connect = prop.getConnection();
        } catch (Exception e) {
        }
    }

    public Boolean checkLogin(String uname, String pwd) {
        try {

//            pst.setString(1, uname); //this replaces the 1st  "?" in the query for username
//            pst.setString(2, pwd);    //this replaces the 2st  "?" in the query for password
//            //executes the prepared statement
//            rs=pst.executeQuery();
//            if(rs.next())
//            {
            //TRUE iff the query founds any corresponding data
            return true;
//            }
//            else
//            {
//                return false;
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error while validating" + e);
            return false;
        }
    }

    public void writeDataBase_temp(String fname, byte[] image) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver

            preparedStatement = connect
                    .prepareStatement("insert into  temp values (default, ?, ?,?,default)");

            preparedStatement.setString(1, fname);
            preparedStatement.setBytes(2, image);
            preparedStatement.setString(3, "Terminal");
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public void writeDataBase_fingerprint(String barcode, byte[] fmd0) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver

            preparedStatement = connect
                    .prepareStatement("insert into  fingerprint(rollno,fmd0,user_type) values (?,?,?)");

            preparedStatement.setString(1, barcode);
            preparedStatement.setBytes(2, fmd0);
            preparedStatement.setInt(3, 9);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public Image getPhotoofCandidate(String name) throws ClassNotFoundException, SQLException {
        //This will load the MySQL driver, each DB has its own driver

        Image img = null;
        String query = "select image from temp where barcode='" + name + "' order by arrival_in DESC";

        Statement stmt = null;
        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            if (rslt.next()) {
                byte[] imgArr = rslt.getBytes("image");
                img = Toolkit.getDefaultToolkit().createImage(imgArr);

            }

            rslt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
                stmt.close();
            } catch (Exception e) {
            }
        }

        return img;
    }

    public Image getPhotoofCandidatefromServer(String name) throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver

        Image img = null;
        String query = "select photo from photo where photoname='" + name + "' order by arrival_in DESC";

        Statement stmt = null;
        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            if (rslt.next()) {
                byte[] imgArr = rslt.getBytes("photo");
                img = Toolkit.getDefaultToolkit().createImage(imgArr);

            }

            rslt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
                stmt.close();
            } catch (Exception e) {
            }
        }

        return img;
    }

    public void removeDataBase_temp(int id) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver

            preparedStatement = connect
                    .prepareStatement("delete from  temp where id=?");

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            System.out.println("Deleted Row " + id);
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //   Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("myuser");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + website);
            System.out.println("Summary: " + summary);
            System.out.println("Date: " + date);
            System.out.println("Comment: " + comment);
        }
    }

    /////////////////////////////////////////
    ////////////////////////////////////////
    ///////////Jacket Issues
    /*

     jacket Issue


     */
    public void writeDataBase_petsheetinfo(int barcode) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver

            preparedStatement = connect
                    .prepareStatement("insert into  petsheet_info values (default,?,?,default)");

            preparedStatement.setInt(1, barcode);
            preparedStatement.setInt(2, 1);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public void writeDataBase_jacketlink_info(int barcode, int jacket) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver

            preparedStatement = connect
                    .prepareStatement("insert into  jacketlink_info values (default, ?, ?,?,default)");

            preparedStatement.setInt(1, barcode);
            preparedStatement.setInt(2, jacket);
            preparedStatement.setInt(3, 1);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                // connect.close();
            }
        } catch (Exception e) {
        }
        /*

         PET SHEET PRINTED STATUS ENABLER
         */

    }
}
