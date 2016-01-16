/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csbc.attendence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ankit
 */
public class DataExtractor{

   private final String login, passwd, host;

   public DataExtractor(String login, String passwd, String host){
       this.login = login;
       this.passwd = passwd;
       this.host = host;
   }

   public String getTableData(String tableName) throws SQLException, ClassNotFoundException {
       Connection con = null;
       Statement st = null;
       ResultSet rs = null;
       try {
           Class.forName("com.mysql.jdbc.Driver");
           con = DriverManager.getConnection("jdbc:mysql:///" + host, login, passwd);

           st = con.createStatement();
           rs = st.executeQuery("select * from " + tableName);

           ResultSetMetaData rsmd = rs.getMetaData();
           int colCount = rsmd.getColumnCount();

           StringBuilder b = new StringBuilder("<table>\n");

           int num = 1;
           while (rs.next()) {
               b.append("<row>");
               b.append("<num>").append(num++).append("</num>");
               for (int i = 1; i <= colCount; i++) {
                   String columnName = rsmd.getColumnName(i);
                   b.append('<').append(columnName).append('>');
                   b.append(rs.getObject(i));
                   b.append("</").append(columnName).append('>');
               }
               b.append("</row>\n");
           }
           b.append("</table>");
           return b.toString();
       } catch (SQLException e) {
           throw e;
       } catch (ClassNotFoundException e) {
           throw e;
       } finally {
           if (rs != null)
               try {
                   rs.close();
               } catch (SQLException e) {
               }
           if (st != null)
               try {
                   st.close();
               } catch (SQLException e) {
               }
           if (con != null)
               try {
                   con.close();
               } catch (SQLException e) {
               }
       }
   }
}
