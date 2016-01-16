/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csbc.attendence;

/**
 *
 * @author Ankit
 */
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DB2CSV {

    /**
     * @param args
     */
    public static void main(String[] args) {

    
      
        //usual database connection part
        Connection con = null;
  
 
        String driver = "com.mysql.jdbc.Driver";
        String user = "username";
        String pass = "password";
        String ip = "127.0.0.1";//bte.decrypt(prop.getProperty("ip"));
        String username = "root";//bte.decrypt(prop.getProperty("username"));
        String password = "v721PL7y";//bte.decrypt(prop.getProperty("password"));
        String db = "movedb";
        FileWriter fw;
         
        try {

        
                // This will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver");
                // Setup the connection with the DB
                con = DriverManager
                        .getConnection("jdbc:mysql://" + ip + "/" + db + "?"
                                + "user=" + username + "&password=" + password);

                System.out.println("Connected to database." + username);
                //     JOptionPane.showMessageDialog(null, "System Connected");
     
             

                Statement st = con.createStatement();

                //this query gets all the tables in your database(put your db name in the query)
                ResultSet res = st.executeQuery("SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'movedb' ");

                //Preparing List of table Names
                List<String> tableNameList = new ArrayList<String>();
                while (res.next()) {
                    tableNameList.add(res.getString(1));
                     System.out.println("Connected to database." +res.getString(1));
                }

                //path to the folder where you will save your csv files
                String filename = "E://";

                //star iterating on each table to fetch its data and save in a .csv file
                for (String tableName : tableNameList) {
                    int k = 0;

                    int j = 1;

                    System.out.println(tableName);

                    List<String> columnsNameList = new ArrayList<String>();

                    //select all data from table
                    res = st.executeQuery("select * from " + tableName);

                    //colunm count is necessay as the tables are dynamic and we need to figure out the numbers of columns
                    int colunmCount = getColumnCount(res);

                    try {
                        fw = new FileWriter(filename + "" + tableName + ".csv");

                        //this loop is used to add column names at the top of file , if you do not need it just comment this loop
                        for (int i = 1; i <= colunmCount; i++) {
                            fw.append(res.getMetaData().getColumnName(i));
                            fw.append(",");

                        }

                        fw.append(System.getProperty("line.separator"));

                        while (res.next()) {
                            for (int i = 1; i <= colunmCount; i++) {

                            //you can update it here by using the column type but i am fine with the data so just converting 
                                //everything to string first and then saving
                                if (res.getObject(i) != null) {
                                    String data = res.getObject(i).toString();
                                    fw.append(data);
                                    fw.append(",");
                                } else {
                                    String data = "null";
                                    fw.append(data);
                                    fw.append(",");
                                }

                            }
                            //new line entered after each row
                            fw.append(System.getProperty("line.separator"));
                        }

                        fw.flush();
                        fw.close();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                con.close();
            } catch (ClassNotFoundException e) {
                System.err.println("Could not load JDBC driver");
                e.printStackTrace();
            } catch (SQLException ex) {
                System.err.println("SQLException information");
            }
        }
        //to get numbers of rows in a result set 
public static int getRowCount(ResultSet res) throws SQLException {
        res.last();
        int numberOfRows = res.getRow();
        res.beforeFirst();
        return numberOfRows;
    }

    //to get no of columns in result set
    public static int getColumnCount(ResultSet res) throws SQLException {
        return res.getMetaData().getColumnCount();
    }

}
