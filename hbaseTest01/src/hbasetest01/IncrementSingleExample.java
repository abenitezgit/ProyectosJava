/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;
// cc IncrementSingleExample Example using the single counter increment methods
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import hbasetest01.HBaseHelper;
import org.apache.hadoop.fs.Path;
/**
 *
 * @author andresbenitez
 */
public class IncrementSingleExample {
    public static void main(String[] args) throws IOException {
    Configuration conf = HBaseConfiguration.create();
    
    conf.addResource(new Path("/Users/andresbenitez/Downloads/hbase-1.2.0/conf/hbase-site.xml"));
    conf.addResource(new Path("/Users/andresbenitez/Downloads/hbase-1.2.0/conf/core-site.xml"));
    conf.addResource(new Path("/Users/andresbenitez/Downloads/hbase-1.2.0/conf/hdfs-site.xml"));
    /*
    conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hbase-site.xml"));
    conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/core-site.xml"));
    conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hdfs-site.xml"));
    /*
    conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hbase-site.xml"));
    conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/core-site.xml"));
    conf.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hdfs-site.xml"));
    
    
    
    */
    HBaseHelper helper = HBaseHelper.getHelper(conf);
    helper.dropTable("testtable");
    helper.createTable("testtable", "daily");
    Connection connection = ConnectionFactory.createConnection(conf);
    Table table = connection.getTable(TableName.valueOf("testtable"));
    // vv IncrementSingleExample
    long cnt1 = table.incrementColumnValue(Bytes.toBytes("20110101"), // co IncrementSingleExample-1-Incr1 Increase counter by one.
      Bytes.toBytes("daily"), Bytes.toBytes("hits"), 1);
    long cnt2 = table.incrementColumnValue(Bytes.toBytes("20110101"), // co IncrementSingleExample-2-Incr2 Increase counter by one a second time.
      Bytes.toBytes("daily"), Bytes.toBytes("hits"), 1);

    long current = table.incrementColumnValue(Bytes.toBytes("20110101"), // co IncrementSingleExample-3-GetCurrent Get current value of the counter without increasing it.
      Bytes.toBytes("daily"), Bytes.toBytes("hits"), 0);

    long cnt3 = table.incrementColumnValue(Bytes.toBytes("20110101"), // co IncrementSingleExample-4-Decr1 Decrease counter by one.
      Bytes.toBytes("daily"), Bytes.toBytes("hits"), -1);
    // ^^ IncrementSingleExample
    System.out.println("cnt1: " + cnt1 + ", cnt2: " + cnt2 +
      ", current: " + current + ", cnt3: " + cnt3);
  }
}

