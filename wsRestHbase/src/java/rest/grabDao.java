/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import utiles.hbaseDB;

public class grabDao {
    
   public List<Grabacion> getMuestra() {
       Grabacion grab = null;
       List<Grabacion> lstGrab = new ArrayList<>();
       try {
           hbaseDB conn = new hbaseDB("/Users/andresbenitez/Documents/app/ABTViewer3/srvConf.properties","HBConf2");
           List<Filter> filters = new ArrayList<>();
           
            SingleColumnValueFilter colValFilter = new SingleColumnValueFilter(Bytes.toBytes("grab"), Bytes.toBytes("ani")
                , CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("974038607")));
                       
        
            colValFilter.setFilterIfMissing(true);
            colValFilter.filterRow();
            colValFilter.getLatestVersionOnly();
            
            filters.add(colValFilter);
            
        //Se crea la matriz de columnas a devolver
        List<String> qualifiers = new ArrayList<>();

        //Agrega las Columnas y Qualifiers
        qualifiers.add("cli:name");
        qualifiers.add("grab:ani");
        qualifiers.add("grab:dnis");
        qualifiers.add("grab:name");
        qualifiers.add("grab:url");
        qualifiers.add("time:ano");
        qualifiers.add("time:dia");
        qualifiers.add("time:fgrab");
        qualifiers.add("time:hora");
        qualifiers.add("time:mes");
        qualifiers.add("time:min");
        qualifiers.add("time:seg");
        qualifiers.add("time:semana");
        
         DefaultTableModel dtmData = conn.getRsQuery("hgrab", qualifiers, filters);
         
         if (dtmData!=null) {
            int numItems = dtmData.getRowCount();
            int it = 0;
            while (it<numItems) {
                grab = new Grabacion(dtmData.getValueAt(0, 0).toString(), dtmData.getValueAt(0, 1).toString(), dtmData.getValueAt(0, 2).toString(), dtmData.getValueAt(0, 3).toString(), dtmData.getValueAt(0, 4).toString(), dtmData.getValueAt(0, 5).toString(), dtmData.getValueAt(0, 6).toString());
                lstGrab.add(grab);
                it++;
            }
         } 
           
         return lstGrab;
       } catch (Exception e) {
           return null;
       }
   }
    
    
   public List<User> getAllUsers(){
      List<User> userList = null;
      try {
         File file = new File("Users.dat");
         if (!file.exists()) {
            User user = new User(1, "Mahesh", "Teacher");
            userList = new ArrayList<User>();
            userList.add(user);
            saveUserList(userList);		
         }
         else{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userList = (List<User>) ois.readObject();
            ois.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }		
      return userList;
   }

   private void saveUserList(List<User> userList){
      try {
         File file = new File("Users.dat");
         FileOutputStream fos;

         fos = new FileOutputStream(file);

         ObjectOutputStream oos = new ObjectOutputStream(fos);
         oos.writeObject(userList);
         oos.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }   
}