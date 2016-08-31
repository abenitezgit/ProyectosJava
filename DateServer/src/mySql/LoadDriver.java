package mySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class LoadDriver {
    public static void main(String[] args) throws SQLException {
        mysqlDB conn = new mysqlDB();
        
        conn.setHostIp("localhost");
        conn.setDbPort("3306");
        conn.setDbUser("root");
        conn.setDbPass("");
        conn.setDbName("srvConf");
        
        conn.conectar();
        
        if (conn.getConnStatus()) {
            System.out.println("connected!!");
            
            String vSQL="select * from tb_ftp";
            ResultSet rs = conn.consultar(vSQL);
            
            if (rs!=null) {
                System.out.println("rs no nulo");
                while (rs.next()) {
                    System.out.println(rs.getString("ftpID"));
                    System.out.println(rs.getString("ftpDESD"));
                }
                
                
            } else {
                System.out.println("rs nulo");
            }
            
            
        } else {
            System.out.println("Error!!");
        }
    }
}