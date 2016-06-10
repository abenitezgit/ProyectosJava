/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mypkg.SQLConnect;
import mypkg.cargaRSonJT;
import mypkg.sqlDB;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author andresbenitez
 */
public class extractSQL {
    
    
    public static void main(String args[]) {
    
        sqlDB conn1 = new sqlDB();
        
        
        conn1.setHostIp("172.18.66.51");
        conn1.setDbPort("1433");
        conn1.setDbName("Grabaciones");
        conn1.setDbUser("sa");
        conn1.setDbPass("econtact2010");
        
        /*
        conn1.setHostIp("10.33.19.25");
        conn1.setDbPort("1433");
        conn1.setDbName("CFG_GRABACIONES");
        conn1.setDbUser("EcoAdmin");
        conn1.setDbPass("Sqladmin2016");
        
        conn1.setHostIp("192.168.220.12");
        conn1.setDbPort("1433");
        conn1.setDbName("Reporteria");
        conn1.setDbUser("sa");
        conn1.setDbPass("econtact2008");
        */
        try {
        
            conn1.conectar();
        
            if (conn1.getConnStatus()) {
                
                System.out.println("Connected Conn1");
                String vSQL2 =  "select\n" +
                                "	UniqueID,\n" +
                                "	ANI,\n" +
                                "	DNIS,\n" +
                                "	Convert(varchar(8),FechaInicio,112) Fecha,\n" +
                                "	FechaInicio FechaGrab,\n" +
                                "	Year(FechaInicio) Anno,\n" +
                                "	MOnth(FechaInicio) Mes,\n" +
                                "	Day(FechaInicio) Dia,\n" +
                                "	DatePart(Hour, FechaInicio) Hora,\n" +
                                "	DatePart(Mi,FechaInicio) Mi,\n" +
                                "	DatePart(ss, FechaInicio) Seg,\n" +
                                "	DatePart(ww, FechaInicio) sem,\n" +
                                "	isNULL(fName,'NULO') RecordName,\n" +
                                "	ubicacionLocal,\n" +
                                "	url RecordURL,\n" +
                                "	duracion,\n" +
                                "	plat_dbID,\n" +
                                "	estadoLlamada\n" +
                                "from\n" +
                                "	Grabaciones\n" +
                                "where\n" +
                                "	FechaInicio >= '2016-03-01 00:00:00' \n" +
                                "	And FechaInicio < '2016-03-11 00:00:00' ";
                
                String vSQL =   "select \n" +
                                "	ANI,\n" +
                                "	DNIS,\n" +
                                "	SIPCallerID,\n" +
                                "	Convert(varchar(8),RecordStartTime,112) Fecha,\n" +
                                "	RecordStartTime FechaGrab,\n" +
                                "	Year(RecordStartTime) Anno,\n" +
                                "	MOnth(RecordStartTime) Mes,\n" +
                                "	Day(RecordStartTime) Dia,\n" +
                                "	DatePart(Hour, RecordStartTime) Hora,\n" +
                                "	DatePart(Mi,RecordStartTime) Mi,\n" +
                                "	DatePart(ss, RecordStartTime) Seg,\n" +
                                "	RecordName,\n" +
                                "	RecordURL,\n" +
                                "	RecordServerName\n" +
                                "from\n" +
                                "	CALL_RECORD\n" +
                                "where\n" +
                                "	RecordStartTime >= '2015-09-29 00:00:00' And RecordStartTime < '2015-10-30 00:00:00'";
                
                ResultSet rs = conn1.consultar(vSQL2);
               
                
                
                if (rs != null) {
                    //Conecta a HBASE
                    
                    Configuration hcfg = HBaseConfiguration.create();
                    /*
                    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hbase-site.xml"));
                    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/core-site.xml"));
                    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hdfs-site.xml"));
                    */
                    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hbase-site.xml"));
                    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/core-site.xml"));
                    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hdfs-site.xml"));

                    try {
                        HBaseAdmin hadmin = new HBaseAdmin( hcfg );

                    } catch (ZooKeeperConnectionException e) {
                        System.out.println("Zookeeper Error: "+ e.getMessage());
                    } catch (IOException e) {
                        System.out.println("IOException Error: "+ e.getMessage());
                    }
                    
                    String hbTable = "hgrab";
                    String hbGrabHead = "hgrab_head";
                    
                    HTablePool pool = new HTablePool(hcfg,100000);
                    HTableInterface hTable = pool.getTable(hbTable);
                    
                    while (rs.next()) {
                    
                                    
            
                        String keyPart1 = "C00002";   //Codigo Cliente
                        String keyPart2 = String.format("%08d",Integer.parseInt(rs.getString("Fecha"))) + String.format("%02d",Integer.parseInt(rs.getString("Hora"))) + String.format("%02d",Integer.parseInt(rs.getString("Mi"))) + String.format("%02d",Integer.parseInt(rs.getString("Seg")));
                        String keyPart3 = "NULO";
                        
                        if ((rs.getString("RecordName")!=null) && (!rs.getString("RecordName").isEmpty())) {
                            keyPart3 = rs.getString("RecordName");   //Nombre Grabacion
                        }                       

                        //C0000120160306GRAB0001.WAV

                        String vRowKey  = keyPart1 + keyPart2 + keyPart3;
                        String vRowKeyHead = keyPart3;


                        //HTable hgrabHead = new HTable(hcfg,hbGrabHead);

                        Put p = new Put(Bytes.toBytes(vRowKey));
                        //Put phead = new Put(Bytes.toBytes(vRowKeyHead));
                        
                        //phead.add(Bytes.toBytes("head"), Bytes.toBytes("URL"), Bytes.toBytes(rs.getString("RecordURL")));

                        
                        p.add(Bytes.toBytes("cli"), Bytes.toBytes("name"),Bytes.toBytes("CLARO"));
                        p.add(Bytes.toBytes("user"), Bytes.toBytes("name"),Bytes.toBytes("abenitez"));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("fgrab"),Bytes.toBytes(rs.getString("FechaGrab")));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("ano"),Bytes.toBytes(String.format("%02d",Integer.parseInt(rs.getString("Anno")))));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("mes"),Bytes.toBytes(String.format("%02d",Integer.parseInt(rs.getString("Mes")))));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("dia"),Bytes.toBytes(String.format("%02d",Integer.parseInt(rs.getString("Dia")))));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("hora"),Bytes.toBytes(String.format("%02d",Integer.parseInt(rs.getString("Hora")))));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("min"),Bytes.toBytes(String.format("%02d",Integer.parseInt(rs.getString("Mi")))));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("seg"),Bytes.toBytes(String.format("%02d",Integer.parseInt(rs.getString("Seg")))));
                        p.add(Bytes.toBytes("time"), Bytes.toBytes("semana"),Bytes.toBytes(String.format("%02d",Integer.parseInt("4"))));
                        p.add(Bytes.toBytes("grab"), Bytes.toBytes("name"),Bytes.toBytes(rs.getString("RecordName")));
                        p.add(Bytes.toBytes("grab"), Bytes.toBytes("size"),Bytes.toBytes("150"));
                        p.add(Bytes.toBytes("grab"), Bytes.toBytes("type"),Bytes.toBytes("WAV"));
                            p.add(Bytes.toBytes("grab"), Bytes.toBytes("dur"),Bytes.toBytes(rs.getString("duracion")));
                        p.add(Bytes.toBytes("grab"), Bytes.toBytes("ani"),Bytes.toBytes(rs.getString("ANI")));
                        p.add(Bytes.toBytes("grab"), Bytes.toBytes("dnis"),Bytes.toBytes(rs.getString("DNIS")));
                        //p.add(Bytes.toBytes("grab"), Bytes.toBytes("scid"),Bytes.toBytes(rs.getString("SipCallerID")));
                        p.add(Bytes.toBytes("grab"), Bytes.toBytes("url"),Bytes.toBytes(rs.getString("RecordURL")));
                            p.add(Bytes.toBytes("grab"), Bytes.toBytes("local"),Bytes.toBytes(rs.getString("ubicacionLocal")));
                            p.add(Bytes.toBytes("grab"), Bytes.toBytes("pdbid"),Bytes.toBytes(rs.getString("plat_dbid")));
                            p.add(Bytes.toBytes("grab"), Bytes.toBytes("ellam"),Bytes.toBytes(rs.getString("estadoLlamada")));
                        //p.add(Bytes.toBytes("grab"), Bytes.toBytes("rsrv"),Bytes.toBytes(rs.getString("RecordServerName")));

                        //hgrabHead.put(phead);
                        hTable.put(p);
                        
                        //pool.close();
                        

                        
                    }
                    
                
                }
                

                
            }

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        
        try {
            conn1.closeConexion();
            
            System.out.println("Disconnected ALL");
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
}
