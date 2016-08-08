/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbsamples;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andresbenitez
 */
public class db001  {
    static oracleDB conn = new oracleDB();
    
    public static void main(String args[]) throws SQLException {
        conn.setHostIp("oradb01");
        conn.setDbPort("1521");
        conn.setDbName("oratest");
        conn.setDbUser("process");
        conn.setDbPass("proc01");
        
        conn.conectar();


        Timer c1 = new Timer();
        c1.schedule(new maintimer(), 5000, 5000);
    
    }
    
    static class maintimer extends TimerTask {
        
        public maintimer() {}

    @Override
    public void run() {
        
        System.out.println("Inicio....");
        
        
        String vSQL;
        
        for (int i=0; i<5; i++) {
        
            vSQL = "select * from tb_agenda";
            
            try (ResultSet rs = conn.consultar(vSQL)) {
                while (rs.next()) {
                    System.out.println("AgeID: " + rs.getString("AGEID"));
                    break;
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(db001.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

        vSQL = "select * from tb_agenda";
        
        try (ResultSet rs2 = conn.consultar(vSQL)) {
            while (rs2.next()) {
                System.out.println("AgeID: " + rs2.getString("AGEID"));
            }
            rs2.close();
        }   catch (SQLException ex) {
                Logger.getLogger(db001.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
    }
    
}
