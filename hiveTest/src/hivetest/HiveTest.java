/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hivetest;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andresbenitez
 */
public class HiveTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO code application logic here
        
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        
        //java.sql.Connection cnct = DriverManager.getConnection("jdbc:hive2://hortonserver:10000", "hive", "hive");
        java.sql.Connection cnct = DriverManager.getConnection("jdbc:hive2://hwk10.e-contact.cl:10000/default","hive","hive");
        
        System.out.println("Conectado");
        
        Statement stmt = cnct.createStatement();
        
        String tableName = "hgrab";
        
            // describe table
        String sql = "describe " + tableName;
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
          System.out.println(res.getString(1) + "\t" + res.getString(2));
        }
        
        /*
        sql = "select * from hive_hgrab where rowkey >= 'C0000220150915' and rowkey<='C0000220150930'  ";
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
          System.out.println(res.getString(1) + "\t" + res.getString(2));
        }        
        */
        
        sql = "select * from hgrab where key >= 'C0000120150101' and key<='C0000220160110'  ";
        //sql = "select * from hgrab where dia=10 and mes=01";
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        System.out.println(stmt.getFetchSize());
        int i=0;
        while (res.next()) {
            i++;
          System.out.print(res.getString(1) );
          System.out.print(" "+res.getString(2) );
          System.out.print(" "+res.getString(3) );
          System.out.print(" "+res.getString(4) );
          System.out.println(" "+res.getString(5) );
        }
        System.out.println(i);
        

    }
    
}
