/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;


import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author andresbenitez
 */
public class hbPutData {
    
    static boolean swSetConf;
    static List<String> vArrayConf = new ArrayList<>();
    static String vErrorMessage;
    static Configuration hbConfig = HBaseConfiguration.create();
    static boolean swFilterStartRow;
    static boolean swFilterStopRow;
    static HTable vhbTable;

    public static void setSwFilterStartRow(boolean swFilterStartRow) {
        hbPutData.swFilterStartRow = swFilterStartRow;
    }

    public static void setSwFilterStopRow(boolean swFilterStopRow) {
        hbPutData.swFilterStopRow = swFilterStopRow;
    }
    
    public static boolean isSwSetConf() {
        return swSetConf;
    }

    public static void setvArrayConf(List<String> vArrayConf) {
        hbPutData.vArrayConf = vArrayConf;
    }


    
    static boolean hbConnect() {
    
        try {
            HBaseAdmin hadmin = new HBaseAdmin( hbConfig );
            return true;
        
        } catch (ZooKeeperConnectionException e) {
            System.out.println("Zookeeper Error: "+ e.getMessage());
            vErrorMessage = "Zookeeper Error: "+ e.getMessage();
            return false;
        } catch (IOException e) {
            System.out.println("IOException Error: "+ e.getMessage());
            vErrorMessage = "IOException Error: "+ e.getMessage();
            return false;
        }
    } 
    
    
    static boolean hbSetConf() {
        
        vErrorMessage = "";
        
        if (vArrayConf.size()>0) {
        
            for (int i=0 ; i < vArrayConf.size() ; i++) {
                hbConfig.addResource(new Path(vArrayConf.get(i)));
            }
        
            return true;
        } else {
            vErrorMessage = "No se han definido los archivos de configuración";
            return false;
        }
    }
    
    
    static void hbPutData(HTable vhbTable, String vRowID, List<List<String>>  vListFamCols) throws IOException {
    
        List<String> vListFams = new ArrayList();
        List<String> vListCols = new ArrayList();
        List<String> vListValues = new ArrayList();
        
        vListFams = vListFamCols.get(0);
        vListCols = vListFamCols.get(1);
        vListValues = vListFamCols.get(2);
        
        
        
        Put p = new Put(Bytes.toBytes(vRowID));
  
        for (int i=0; i < vListCols.size()-1; i++) {
            
            p.add(Bytes.toBytes(vListFams.get(i)), Bytes.toBytes(vListCols.get(i)),Bytes.toBytes(vListValues.get(i)));
        }
        
        vhbTable.put(p);
        
        
    }
    
    public static void main(String args[]) throws IOException {
    
        List<String> listConfig = new ArrayList();
        
       
        
        //Agrega los archivos de Configuracion para Sanbox
        
        //listConfig.add(0, "/Users/andresbenitez/Documents/Apps/Azure-conf/hbase-site.xml");
        //listConfig.add(1,"/Users/andresbenitez/Documents/Apps/Azure-conf/core-site.xml");
        //listConfig.add(2,"/Users/andresbenitez/Documents/Apps/Azure-conf/hdfs-site.xml");
        
        //Agrega los archivos de Configuracion para Local Horton
        
        listConfig.add("/Users/andresbenitez/Documents/apps/HBase-libs/hbase-site.xml");
        listConfig.add("/Users/andresbenitez/Documents/apps/HBase-libs/core-site.xml");
        listConfig.add("/Users/andresbenitez/Documents/apps/HBase-libs/hdfs-site.xml");

        
        
        setvArrayConf(listConfig);
        
        if (hbSetConf()) {
            System.out.println("Conf OK");
            
            if (hbConnect()) {
                System.out.println("Connect OK");
                
                HTable hTable = new HTable(hbConfig,"hgrab");
                
                String vRowID;

                List<List<String>> vArrayFamCols = new ArrayList<List<String>>();

                List<String> vListFams = new ArrayList(1000);
                List<String> vListCols = new ArrayList();
                List<String> vListValue = new ArrayList();

                
                //Genera Data

                vListFams.add("cli");   vListCols.add("name");
                vListFams.add("user");  vListCols.add("name");
                vListFams.add("time");  vListCols.add("fgrab");
                vListFams.add("time");  vListCols.add("ano");
                vListFams.add("time");  vListCols.add("mes");
                vListFams.add("time");  vListCols.add("dia");
                vListFams.add("time");  vListCols.add("hora");
                vListFams.add("time");  vListCols.add("min");
                vListFams.add("time");  vListCols.add("seg");
                vListFams.add("time");  vListCols.add("sem");
                vListFams.add("grab");  vListCols.add("name");
                vListFams.add("grab");  vListCols.add("size");
                vListFams.add("grab");  vListCols.add("type");
                vListFams.add("grab");  vListCols.add("ani");
                vListFams.add("grab");  vListCols.add("dnis");
                vListFams.add("grab");  vListCols.add("scid");
                vListFams.add("grab");  vListCols.add("url");
                vListFams.add("grab");  vListCols.add("rsrv");
                

                vListValue.add("EPCS");
                vListValue.add("abenitez");
                vListValue.add("2016-01-01 10:10:10");
                vListValue.add("2016");
                vListValue.add("01");
                vListValue.add("01");
                vListValue.add("10");
                vListValue.add("10");
                vListValue.add("10");
                vListValue.add("04");
                vListValue.add("GRAB.WAV");
                vListValue.add("104");
                vListValue.add("WAV");
                vListValue.add("11111");
                vListValue.add("222222");
                vListValue.add("222.22");
                vListValue.add("http://grab.wav");
                vListValue.add("recoserv");                

                //Ciclo para Llenar la Dara Row x Row


                vRowID = "C00001201601011010GRAB2.WAV";

                vArrayFamCols.add(vListFams);
                vArrayFamCols.add(vListCols);
                vArrayFamCols.add(vListValue);

                hbPutData(hTable, vRowID, vArrayFamCols);
                    
            }
            
        } else {
            System.out.println("Conf Error");
        }
        
    
    }
    
}
