/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andresbenitez
 */
public class findList2 {
 
    public static void main(String args[]) {
        List<String> lista = new ArrayList<>();
        
        lista.add("'asasas','asasasas','srv00001'");
        lista.add("'asasas','asasasas','srv00002'");
        lista.add("'asasas','asasasas','srv00003'");
        
        
        List<Object> toRemove = new ArrayList<Object>();
        

        
        for (int i=0; i<toRemove.size(); i++) {
            System.out.println(toRemove.get(i));
        }
    }
}
