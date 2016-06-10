/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;

import com.google.common.hash.HashFunction;
import static com.google.common.hash.Hashing.md5;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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
public class putDataEtl {
    
    
    public static void main(String args[]) throws IOException {
        Properties fileHBConf = new Properties();
        String [] ListHBConf ;
        String    TableHBConf;
        
        
        Configuration hcfg = HBaseConfiguration.create();
        
        fileHBConf.load(new FileInputStream("/Users/andresbenitez/Documents/apps/HBase-Cluster-Conf/HBConf.properties"));
        
        TableHBConf = fileHBConf.getProperty("HBTable1");
        //TableHBConf = fileHBConf.getProperty("HBConf1");
        
        ListHBConf = fileHBConf.getProperty("HBConf1").split(",");
        
        
        for (int i=0; i< ListHBConf.length; i++  ) {
            hcfg.addResource(new Path(ListHBConf[i]));
        }
        
        try {
            HBaseAdmin hadmin = new HBaseAdmin( hcfg );
        
        } catch (ZooKeeperConnectionException e) {
            System.out.println("Zookeeper Error: "+ e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException Error: "+ e.getMessage());
        }
        
        // Inserta Datos para srvViewer
        
        String RowKey = "etl00001";
        
        // Insertar Registros en srvConf
        
        HTablePool pool = new HTablePool(hcfg,100000);
        HTableInterface hTable = pool.getTable(TableHBConf);
        
        // Se inicia el Put definiendo el RowKey
        
        Put p = new Put(Bytes.toBytes(RowKey));
        
        
        // Luego se inserta la Data  (CF, CA, VALUE) (Esto puede estar en un ciclo
        
        

        
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDesc"),Bytes.toBytes("ETL EXTRACCION ITELECOM"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlCliID"),Bytes.toBytes("cli00001"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlCliName"),Bytes.toBytes("ITELECOM"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBSourceName"),Bytes.toBytes("I3_IC_400"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBSourcePort"),Bytes.toBytes("1433"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBSourceUser"),Bytes.toBytes("sa"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBSourcePass"),Bytes.toBytes("1234"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBSourceTableName"),Bytes.toBytes("InteractionSummary"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBSourceType"),Bytes.toBytes("SQL"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBDestName"),Bytes.toBytes("REPORTES_ITELECOM"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBDestPort"),Bytes.toBytes("1433"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBDestUser"),Bytes.toBytes("sa"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBDestPass"),Bytes.toBytes("1234"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBDestTableName"),Bytes.toBytes("etl00001InteractionSummary"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlDBDestType"),Bytes.toBytes("SQL"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlIntervalExtract"),Bytes.toBytes("30"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlFieldKeyExtract"),Bytes.toBytes("StartDateTimeUTC"));
        p.add(Bytes.toBytes("conf"), Bytes.toBytes("etlActive"),Bytes.toBytes("1"));
        
        hTable.put(p);
        //hTable.incrementColumnValue(Bytes.toBytes(RowKey), Bytes.toBytes("srv"), Bytes.toBytes("count"), 1L);
        
        
        hTable.close(); 
        
        
        
            /*
            String hbTable = "hgrab";
            String hbGrabHead = "hgrab_head";
            
            String keyPart1 = "C00001";   //Codigo Cliente
            String keyPart2 = "20150805083753";   //FEchaGrab
            String keyPart3 = "GRAB00002";   //Nombre Grabacion
            
            //C0000120160306GRAB0001.WAV
            
            String vRowKey  = keyPart1 + keyPart2 + keyPart3;
            String vRowKeyHead = "";
            
            //HTable hTable = new HTable(hcfg,hbTable);
            //HTable hGrabHead = new HTable(hcfg,hbGrabHead);
            
            //Put p = new Put(Bytes.toBytes(vRowKey));
            //Put phead = new Put(Bytes.toBytes(vRowKeyHead));
            
            p.add(Bytes.toBytes("cli"), Bytes.toBytes("name"),Bytes.toBytes("EPCS"));
            p.add(Bytes.toBytes("user"), Bytes.toBytes("name"),Bytes.toBytes("abenitez"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("fgrab"),Bytes.toBytes("2015-08-05 08:37:53"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("ano"),Bytes.toBytes("2015"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("mes"),Bytes.toBytes("08"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("dia"),Bytes.toBytes("05"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("hora"),Bytes.toBytes("08"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("min"),Bytes.toBytes("37"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("seg"),Bytes.toBytes("53"));
            p.add(Bytes.toBytes("time"), Bytes.toBytes("semana"),Bytes.toBytes("4"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("name"),Bytes.toBytes("20150805_083753_440964357432_Environment_1438774673.72.WAV"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("size"),Bytes.toBytes("150"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("type"),Bytes.toBytes("WAV"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("ani"),Bytes.toBytes("Environment"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("dnis"),Bytes.toBytes("440964357432"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("scid"),Bytes.toBytes("1438774673.72"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("url"),Bytes.toBytes("http://srv-gui-g.e-contact.cl/grabaciones/2015080508/20150805_083753_440964357432_Environment_1438774673.72.WAV"));
            p.add(Bytes.toBytes("grab"), Bytes.toBytes("rsrv"),Bytes.toBytes("OREKA-00.E-CONTACT.CL"));
            
            hTable.put(p);
            //hGrabHead.put(phead);
            
            hTable.close(); 
            */
    
    }
    
}
