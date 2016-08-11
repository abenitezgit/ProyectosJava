/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testhbase;
//import java.io.IOException;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;

/**
 *
 * @author andresbenitez
 */
public class TestHBase {

    /**
     * @param args the command line arguments
     * @throws org.apache.hadoop.hbase.ZooKeeperConnectionException
     */
    public static void main(String[] args) throws ZooKeeperConnectionException, IOException {
        // TODO code application logic here
    //HBaseConfiguration hconfig = new HBaseConfiguration(new Configuration());
    
    Configuration hconfig = HBaseConfiguration.create();
    
    //Add any necessary configuration files (hbase-site.xml, core-site.xml)
    
    //System.out.println(new Path("/usr/lib/hbase"));
   
    
    hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/hbase-site.xml"));
    hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/core-site.xml"));


/*    
    //hconfig.set("hbase.zookeeper.quorum", "hortonnodo1.e-contact.cl,hortonnodo2.e-contact.cl");  // Here we are running zookeeper locally
    hconfig.set("hbase.zookeeper.quorum", "172.16.83.133");  // Here we are running zookeeper locally
    hconfig.set("hbase.rootdir", "hdfs://172.16.83.133:8020/apps/hbase/data");  // Here we are running zookeeper locally
    //hconfig.set("hbase.zookeeper.znode.parent","/hbase-unsecure");
    hconfig.set("zookeeper.znode.parent","/hbase-unsecure");
    hconfig.set("dfs.domain.socket.path", "/var/lib/hadoop-hdfs/dn_socket");
    hconfig.set("hbase.cluster.distributed", "true");  // Here we are running zookeeper locally
    hconfig.set("hbase.bulkload.staging.dir","/apps/hbase/staging");
    hconfig.set("hbase.local.dir","${hbase.tmp.dir}/local");
    hconfig.set("hbase.security.authentication","simple");
    hconfig.set("hbase.security.authorization","false");
    hconfig.set("hbase.superuser","hbase");
    hconfig.set("hbase.zookeeper.property.clientPort","2181");
    //hconfig.set("hbase.zookeeper.property.dataDir","/usr/hdp/current/zookeeper-server");
    hconfig.set("hbase.zookeeper.property.dataDir","/hadoop/zookeeper");
  */  
        @SuppressWarnings("deprecation")
        HBaseAdmin hbase_admin = new HBaseAdmin( hconfig );


        //slf4j-1.7.5.tar  Esta libreia tuvo que se agregada.



                    FileSystem hdfs = FileSystem.get(hconfig);

                    System.out.println(hdfs.getWorkingDirectory().toString());

                    Path sourcePath = new Path("/Users/andresbenitez/Documents/Apps/test.txt");

                    Path destPath = new Path("/Users/andresbenitez/Documents/Apps/test2.txt");

                    hdfs.copyFromLocalFile(sourcePath, destPath);

                
                
    
    //hbase_admin.getClusterStatus();
    
    
    //hconfig.set("fs.defaultFS", "hdfs://1.2.3.4:8020/user/hbase");
    
    HTableDescriptor htable = new HTableDescriptor("User"); 
    htable.addFamily( new HColumnDescriptor("Id"));
    htable.addFamily( new HColumnDescriptor("Name"));
    System.out.println( "Connecting..." );
    
    System.out.println( "Creating Table..." );
    hbase_admin.createTable( htable );
    System.out.println("Done!");
    
    }
    
}
