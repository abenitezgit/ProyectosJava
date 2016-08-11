/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testhbase2;


import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 *
 * @author andresbenitez
 */
public class TestHBase2 { 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
        // TODO code application logic here
        
        
        Configuration hconfig = HBaseConfiguration.create();
        
        hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/hbase-site.xml"));
        hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/core-site.xml"));
        
        HBaseAdmin hbase_admin = new HBaseAdmin( hconfig );
        
        HTableDescriptor htable = new HTableDescriptor("User"); 
        htable.addFamily( new HColumnDescriptor("Id"));
        htable.addFamily( new HColumnDescriptor("Name"));
        System.out.println( "Connecting..." );

        System.out.println( "Creating Table..." );
        hbase_admin.createTable(htable);
        System.out.println("Done!");
        
        
        
        
        
        
    }
    
}
