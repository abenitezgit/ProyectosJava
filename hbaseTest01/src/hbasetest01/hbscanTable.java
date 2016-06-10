/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
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
public class hbscanTable {
    
    
    public static void main(String args[]) throws IOException {
    
        Configuration hcfg = HBaseConfiguration.create();
        /*
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hbase-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/core-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hdfs-site.xml"));
        *//*
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hbase-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/core-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hdfs-site.xml"));
        */
        hcfg.addResource(new Path("/Users/andresbenitez/Downloads/hbase-1.2.0/conf/hbase-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Downloads/hbase-1.2.0/conf/core-site.xml"));
        hcfg.addResource(new Path("/Users/andresbenitez/Downloads/hbase-1.2.0/conf/hdfs-site.xml"));
        
        
        HTable hTable = new HTable(hcfg, "testtable");
        
        HTableDescriptor tbDesc =  new HTableDescriptor("testtable");
        

        Scan scan = new Scan();
        //scan.setStartRow(Bytes.toBytes("C000022015091612380601_20150916_123806_Environment_440998178568_1442417886.40359.wav"));
        //scan.setStopRow(Bytes.toBytes("C000022015091612380601_20150916_123806_Environment_440998178568_1442417886.40359.wav"+1));
        
        //scan.setFilter(new ColumnPrefixFilter("n".getBytes()));
        ResultScanner scanner = hTable.getScanner(scan);
        Iterator<Result> resultsIter = scanner.iterator();
        while (resultsIter.hasNext())
        {

            Result result = resultsIter.next();

            List<KeyValue> values = result.list();
            for (KeyValue value : values)
            {
                //System.out.println(value.getKeyString());
                //System.out.println(value.getKey());
                String cf = new String(value.getFamily());
                String ca = new String(value.getQualifier());
                
                System.out.println("CF: "+cf+"  CA: "+ca);
            }
        }

        }
    
}
