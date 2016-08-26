/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author andresbenitez
 */
public class findTest {
    
    
    public static void main(String[] args) throws ZooKeeperConnectionException, IOException {
    
    
        Configuration hcfg = HBaseConfiguration.create();
        
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/apps/Cluster-hwkcluster00/conf/hbase-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/apps/Cluster-hwkcluster00/conf/core-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/apps/Cluster-hwkcluster00/conf/hdfs-site.xml"));
        /*
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/ApacheHws/hbase-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/ApacheHws/core-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/ApacheHws/hdfs-site.xml"));
        */

//        try {
//            HBaseAdmin hadmin = new HBaseAdmin( hcfg );
//        
//        } catch (ZooKeeperConnectionException e) {
//            System.out.println("Zookeeper Error: "+ e.getMessage());
//        } catch (IOException e) {
//            System.out.println("IOException Error: "+ e.getMessage());
//        }
        
        
        //find by scan function
        
        //se realiza el enlace a la tabla correspondiente
        
            HTablePool htp;    
            htp = new HTablePool(hcfg, 10);

            ResultScanner resultadoScan;
            Result resultadoGet = null; 
    
            HTableInterface testTable = htp.getTable("hgrab");
        
        
        // se genera el seteo del scan y filtros a realizar
        
            Scan scan = new Scan();
        
        //Agrego columnas para filtros
        
            //scan.addColumn(Bytes.toBytes("user"), Bytes.toBytes("dbadmin"));
        
        //Se agrega la condicion del filtro

            
            
        
            //Filter filter1 = new RowFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes(vKeyValue)));
            //Filter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL,new BinaryComparator(Bytes.toBytes(keyval_type+":"+keyval_Art+":"+keyval_tfile+":"+keyval_fcrea+":"+keyval_file)));
            //Filter filter2 = new FamilyFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("grab")));
                    

        //se agrega el filtro al scan
        
            //scan.setFilter(filter1);
            //scan.setFilter(filter2);
          
        
        
        
        //String inicio   = keyval_type+":"+keyval_Art+":"+keyval_tfile+":"+keyval_fcrea+":"+keyval_file;     //No es necesario saber la rowkey exacta
        //String fin      = keyval_type+":"+keyval_Art+":"+keyval_tfile+":"+keyval_fcrea+":"+keyval_file+"1";     // No es necesario saber la rowkey exacta , la fila fin debe se lxicograficamente mayor a la fila inicio.   
    
        //String inicio   = vKeyValue;
        //String fin      = vKeyValue+"A";
        
        String inicio   = "C00001201603011400";
        String fin      = "C00003201603011410";
        
        scan.setStartRow(Bytes.toBytes(inicio));// 
        scan.setStopRow(Bytes.toBytes(fin));//
        
        //Recupera los datos en orden inverso
            
            //scan.setReversed(true);
        
        //Limita resultados a columnas especificas
            
            //scan.addFamily(Bytes.toBytes("grab"));   //Global a la familia
            //scan.addColumn(Bytes.toBytes("grab"), Bytes.toBytes("url"));  //especifico a una familia, columan
        
        //Filtro de columnas con valores
        
            List<Filter> filters = new ArrayList<Filter>();

            SingleColumnValueFilter colValFilterLABEL = new SingleColumnValueFilter(Bytes.toBytes("label"), Bytes.toBytes("color")
                , CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("ROJO")));
            
            SingleColumnValueFilter colValFilter2 = new SingleColumnValueFilter(Bytes.toBytes("grab"), Bytes.toBytes("url")
                , CompareFilter.CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes("http://srv-gui-g.e-contact.cl/e-recorder/audio/20160310/09/01_20160310_095259")));

            SingleColumnValueFilter colValFilterDNIS = new SingleColumnValueFilter(Bytes.toBytes("cli"), Bytes.toBytes("color")
                , CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("rojo")));
            
        
            colValFilterLABEL.setFilterIfMissing(true); //Si la columna no existe para una fila entonces no la muestra
            colValFilterLABEL.filterRow();
            colValFilterLABEL.getLatestVersionOnly();

            colValFilterDNIS.setFilterIfMissing(true); //Si la columna no existe para una fila entonces no la muestra
            colValFilterDNIS.filterRow();
            colValFilterDNIS.getLatestVersionOnly();

            
            filters.add(colValFilterDNIS);
            //filters.add(colValFilter2);
            //filters.add(filter2);
            
            //FilterList fl = new FilterList( FilterList.Operator.MUST_PASS_ALL, filters);
            FilterList fl = new FilterList( FilterList.Operator.MUST_PASS_ALL, filters);
            
            scan.setFilter(fl);
            
            // Si agrego dos filtros para completar un AND sobre ellos debo explicitar su busqueda
            
            //scan.addColumn(Bytes.toBytes("grab"), Bytes.toBytes("url"));
            //scan.addColumn(Bytes.toBytes("grab"), Bytes.toBytes("dnis"));
            //scan.addColumn(Bytes.toBytes("grab"), Bytes.toBytes("URL"));
            //scan.addColumn(Bytes.toBytes("grab"), Bytes.toBytes("status"));
            
            //scan.addFamily(Bytes.toBytes("cli"));
            //scan.addFamily(Bytes.toBytes("time"));
            
                 
        scan.setCaching(10000);
        
        resultadoScan = testTable.getScanner(scan);
        
        int total=0;
        //resultado del scan
        if ((resultadoScan != null) ){    
            System.out.println("El resultado del SCAN es:");
            
            Result r = resultadoScan.next();  
            while (r != null) {
                for (KeyValue kv : r.raw()) {
                    System.out.println("Key: " + Bytes.toStringBinary ( kv.getKey(), 2, kv.getRowLength() ) +  "  FamCol: "+ Bytes.toStringBinary(kv.getFamily()) + ":"+ Bytes.toString(kv.getQualifier())   + "  Valor: " + Bytes.toStringBinary( kv.getValue() ) );
                }                
            r=resultadoScan.next();
            total++;
            
            }
            System.out.println("Total Registros: "+total);
        }
    }
}
