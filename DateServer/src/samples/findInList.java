/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



/**
 *
 * @author andresbenitez
 */
public class findInList {
    
    public static void main(String args[]) {
    
        List<String> lista = new ArrayList<>();
        
        lista.add("'asasas','asasasas','srv00001'");
        lista.add("'asasas','asasasas','srv00002'");
        lista.add("'asasas','asasasas','srv00003'");
        
        for (int i=0; i<lista.size(); i++) {
            System.out.println(lista.get(i));
        }
        
        String srvName = "srv00002";
        
        List<String> findlist = lista.stream().filter(item -> item.contains(srvName)).collect(Collectors.toList());
        
        lista.removeAll(findlist);
        
        System.out.println(findlist.size());
        
        for (int i=0; i<lista.size(); i++) {
            System.out.println(lista.get(i));
        }
        
        

    }
}
