/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication9;

import java.util.List;


/**
 *
 * @author andresbenitez
 */
public class JavaApplication9 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Vecindad vecino = new Vecindad();

        IVecindad port =  vecino.getBasicHttpBindingIVecindad();
        
        String resp;
        
        resp = port.agregarVecino("Andres");
        System.out.println(resp);
        
        resp = port.agregarVecino("hola");
        System.out.println(resp);
        
        resp = port.agregarVecino("mundo");
        System.out.println(resp);  
        
        resp = port.agregarVecino("nombre");
        System.out.println(resp);        
        
        List<String> lista = port.listaVecinos().getString();
        
        for(int i=0; i<lista.size();i++) {
            System.out.println(lista.get(i));
        }
    }
}
