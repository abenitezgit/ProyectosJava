/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasetest01;
import java.net.Socket;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;                    //hadoop-core-1.1.2.jar
import org.apache.hadoop.fs.FileSystem;                         //hadoop-core-1.1.2.jar
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;                               //hadoop-core-1.1.2.jar
import org.apache.hadoop.hbase.HBaseConfiguration;              //hbase-common-1.1.2.2.3.4.0-3485.jar
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;    //hbase-client-1.1.2.2.3.4.0-3485.jar
import org.apache.hadoop.hbase.client.HBaseAdmin;               //hbase-client-1.1.2.2.3.4.0-3485.jar
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

//Librerias Anexas por Errores de Compilacion
//commons-logging-1.2.jar
//protobuf-java-2.5.0.jar para el error com/google/protobuf/ServiceException
//zookeeper-3.4.6.2.3.4.0-3485.jar para el error org/apache/zookeeper/KeeperException
//commons-configuration-1.6.jar para el error org/apache/commons/configuration/Configuration
//commons-lang-2.6.jar para el error org/apache/commons/lang/StringUtils
//hbase-protocol-1.1.2.2.3.4.0-3485.jar para el error org/apache/hadoop/hbase/protobuf/generated/MasterProtos$MasterService$BlockingInterface
//slf4j-api-1.7.10.jar para el error java.lang.NoClassDefFoundError: org/slf4j/LoggerFactory
//slf4j-log4j12-1.7.10.jar Failed to load class "org.slf4j.impl.StaticLoggerBinder".
//htrace-core-3.1.0-incubating.jar para el error java.lang.ClassNotFoundException: org.apache.htrace.Trace
//Todos los commons-*.jar
//guava-12.0.1.jar  para el error java.lang.NoClassDefFoundError: com/google/common/collect/ListMultimap
//netty-3.2.4.Final.jar para el error java.lang.NoClassDefFoundError: io/netty/channel/EventLoopGroup
//netty-all-4.0.23.Final.jar
//log4j-1.2.17.jar java.lang.NoClassDefFoundError: org/apache/log4j/Level
//hadoop-common-0.23.3.jar  --- Downloaded  Error: SocketInputWrapper.class 
//hadoop-core-0.20.2-cdh3u5.jar   -- Downloaded
//guava-r09-jarjar.jar   -- Downloaded
//hadoop-auth-0.23.1.jar   







/**
 *
 * @author andresbenitez
 */
public class HbaseTest01 {

    /**
     * @param args the command line arguments
     * @throws org.apache.hadoop.hbase.ZooKeeperConnectionException
     */
    public static void main(String[] args) throws ZooKeeperConnectionException, IOException {
        // TODO code application logic here
        
            
    Configuration hconfig = HBaseConfiguration.create();
    
    
    //hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/hbase-site.xml"));
    //hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/core-site.xml"));
    //hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/HBase-libs/hdfs-site.xml"));
    
    hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hbase-site.xml"));
    hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/core-site.xml"));
    hconfig.addResource(new Path("/Users/andresbenitez/Documents/Apps/Azure-conf/hdfs-site.xml"));

    
    //hconfig.set("hbase.zookeeper.quorum", "104.42.120.174");// Poner aca las IP's de los nodos del cluster.
    hconfig.set("hbase.client.retries.number", "1");
    hconfig.set("zookeeper.session.timeout", "40000");
    hconfig.set("zookeeper.recovery.retry", "1");
    //hconfig.set("fs.default.name","hdfs://hortonnodo1.e-contact.cl:54310");
    hconfig.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
    hconfig.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());   

    //hconfig.set("zookeeper.znode.parent", "/hbase-unsecure");
    //hconfig.set("fs.default.namedfs","//hortonnodo1.e-contact.cl:9100/");
    //hconfig.set("hbase.zookeeper.quorum", "hortonnodo2.e-contact.cl");
    
    
    @SuppressWarnings("deprecation")
    HBaseAdmin hbase_admin = new HBaseAdmin( hconfig );
    

    
    
    HTableDescriptor htable = new HTableDescriptor("User"); 
    htable.addFamily( new HColumnDescriptor("Id"));
    htable.addFamily( new HColumnDescriptor("Name"));
    System.out.println( "Connecting..." );
    
    System.out.println( "Creating Table..." );
    //hbase_admin.createTable( htable );
    System.out.println("Done!");
    
    HTablePool htp;    
    htp = new HTablePool(hconfig, 10);
    
    ResultScanner resultadoScan;
    Result resultadoGet = null; 
    
    HTableInterface testTable = htp.getTable("infradb");
    
    //Inicio Scan ( Se traeran todas las filas que estan entre la fila inicio y la fila fin)
    Scan scan = new Scan();

    String inicio="ECOSQL"; //No es necesario saber la rowkey exacta
    String fin=   "ECOSQL99";   // No es necesario saber la rowkey exacta , la fila fin debe se lxicograficamente mayor a la fila inicio.   
    
    scan.setStartRow(Bytes.toBytes(inicio));// 
    scan.setStopRow(Bytes.toBytes(fin));//

    scan.setCaching(10000);
    resultadoScan = testTable.getScanner(scan);
    ////////Fin Scan 
    
    
    if ((resultadoScan != null) ){    
        System.out.println("El resultado del SCAN es:");     
        Result r = resultadoScan.next();  
        while (r != null) {
            for (KeyValue kv : r.raw()) {
                    System.out.println("Key: " + Bytes.toStringBinary ( kv.getKey(), 2, kv.getRowLength() ) + " Valor: " + Bytes.toStringBinary( kv.getValue() ) );
                }                
            r=resultadoScan.next();
        }    
                


    
    
    //slf4j-1.7.5.tar  Esta libreia tuvo que se agregada.
    
    //Crear Directorio
    
    
    
    FileSystem hdfs = org.apache.hadoop.fs.FileSystem.get(hconfig);
    
    System.out.println(hdfs.getWorkingDirectory().toString());
    
    String dirName = "TestDirectory";
    Path src = new Path(hdfs.getWorkingDirectory() + "/" + dirName);
    Path sr1 = new Path("hdfs://sandbox.hortonworks.com:8020/user/guest/Test");
    
    //hdfs.mkdirs(sr1);
    
    
    
    FileSystem lhdfs = LocalFileSystem.get(hconfig);
    
    System.out.println(lhdfs.getWorkingDirectory().toString());
    System.out.println(hdfs.getWorkingDirectory().toString());

    //Path sourcePath = new Path("/Users/andresbenitez/Documents/Apps/test.txt");

    //Path destPath = new Path("/Users/andresbenitez/Documents/Apps/test4.txt");

    //hdfs.copyFromLocalFile(sourcePath, destPath);
    
    hdfs.copyToLocalFile(false, new Path("hdfs://sandbox.hortonworks.com:8020/user/guest/installupload.log"), new Path("/Users/andresbenitez/Documents/instaldown3.log"), true);
    
    //hdfs.copyToLocalFile(false, new Path("/Users/andresbenitez/Documents/instaldown.log"), new Path("hdfs://sandbox.hortonworks.com:8020/user/guest/installupload.log"), false);
        
    hdfs.copyFromLocalFile(false, new Path("/Users/andresbenitez/Documents/instaldown.log"), new Path("hdfs://sandbox.hortonworks.com:8020/user/guest/installupload2.log"));
        
    }
    
    }
}