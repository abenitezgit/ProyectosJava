/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/*
    Se deben tener referenciadas estas 3 librerías:
    commons-dbcp2-2.1.1.jar
    commons-pool2-2.4.2.jar
    commons.logging-1.2.jar
*/

/**
 *
 * @author andresbenitez
 */
public class poolDB {
    BasicDataSource bdcp = new BasicDataSource();
    public DataSource ds;
    
    public int getNumActive() {
        return bdcp.getNumActive();
    }
    
    public int getNumIdle() {
        return bdcp.getNumIdle();
    }
    
    public poolDB() throws SQLException {
        
        
        bdcp.setDriverClassName("oracle.jdbc.OracleDriver");
        bdcp.setUsername("process");
        bdcp.setPassword("proc01");
        bdcp.setUrl("jdbc:oracle:thin:@oradb01:1521/oratest");
        bdcp.setMaxTotal(10);
        bdcp.setMinIdle(5);
        
        
        
        ds = bdcp;
        
    }
    
    public static void main(String args[]) throws SQLException, IOException {
        poolDB mypool = new poolDB();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        List<Connection> conn = new ArrayList<>();
        
        for (int i=1; i<10; i++) {
            conn.add(mypool.ds.getConnection());
        }
        
        
        System.out.println(mypool.getNumIdle());
        
        
        
        
        
        boolean exit=false;
        while (!exit) {
            String input= reader.readLine();
            if (input.equals("q")) {
                exit=true;//System.exit(0);
            }
        }
        
        for (int i=0; i<conn.size(); i++) {
            conn.get(i).close();
        }

        exit=false;
        while (!exit) {
            String input= reader.readLine();
            if (input.equals("q")) {
                System.exit(0);
            }
        }

    }
    
    
}
