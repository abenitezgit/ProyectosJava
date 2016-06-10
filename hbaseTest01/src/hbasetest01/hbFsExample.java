/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 *
 * @author andresbenitez
 */
public class hbFsExample {
    
    public static void main(String args[]) throws IOException {
    
        Configuration hcfg = HBaseConfiguration.create();
    
    /*
    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hbase-site.xml"));
    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/core-site.xml"));
    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hdfs-site.xml"));
    */
    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hbase-site.xml"));
    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/core-site.xml"));
    hcfg.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-Cluster-conf/hdfs-site.xml"));
    

    FileSystem hdfs = org.apache.hadoop.fs.FileSystem.get(hcfg);
    
    System.out.println(hdfs.getWorkingDirectory().toString());
    
    String dirName = "TestDirectory";
    Path src = new Path(hdfs.getWorkingDirectory() + "/" + dirName);
    Path sr1 = new Path("hdfs://sandbox.hortonworks.com:8020/user/guest/Test");
    
    FileSystem lhdfs = LocalFileSystem.get(hcfg);
    
    System.out.println(lhdfs.getWorkingDirectory().toString());
    System.out.println(hdfs.getWorkingDirectory().toString());

    //Path sourcePath = new Path("/Users/andresbenitez/Documents/Apps/test.txt");

    //Path destPath = new Path("/Users/andresbenitez/Documents/Apps/test4.txt");

    //hdfs.copyFromLocalFile(sourcePath, destPath);
    
    
    try {
        hdfs.copyFromLocalFile(false, new Path("/Users/andresbenitez/Documents/paso/01.mp3"), new Path("hdfs://hortonserver.e-contact.cl:8020/user/guest/02.mp3"));

        //hdfs.copyToLocalFile(false, new Path("hdfs://hortonnodo2.e-contact.cl:8020/user/guest/installupload.log"), new Path("/Users/andresbenitez/Documents/instaldown3.log"), true);
    
    } catch (IOException e) {
        System.out.println("Error IO: "+ e.getMessage());
    }
    //hdfs.copyToLocalFile(false, new Path("/Users/andresbenitez/Documents/instaldown.log"), new Path("hdfs://sandbox.hortonworks.com:8020/user/guest/installupload.log"), false);
        
    

    
    
    }
    
}
