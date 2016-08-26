/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abtGlobals;
import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.hbase.HBaseConfiguration; 
import org.apache.hadoop.hbase.client.HTable; 
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author andresbenitez
 */
public class hbaseTest1 {
    
    public static void main(String args[]) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        

//        conf.addResource(new Path("/Users1/andresbenitez/Documents/apps/Cluster-hwkcluster00/conf/hbase-site.xml"));
//        conf.addResource(new Path("/Users1/andresbenitez/Documents/apps/Cluster-hwkcluster00/conf/core-site.xml"));
//        conf.addResource(new Path("/Users1/andresbenitez/Documents/apps/Cluster-hwkcluster00/conf/hdfs-site.xml"));
//        

        conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-Conf/hbase-site.xml"));
        conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-Conf/conf/core-site.xml"));
        conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-Conf/hdfs-site.xml"));


        
        conf.set("hbase.rpc.timeout", "3000");
        conf.set("zookeeper.recovery.retry", "1");
        
        HTable table = new HTable(conf, "testtable");
        table.setOperationTimeout(3000);
        
        Put put = new Put(Bytes.toBytes("row1"));
        
        put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));
        put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"), Bytes.toBytes("val2"));
        
        table.put(put);
    
    }
}
