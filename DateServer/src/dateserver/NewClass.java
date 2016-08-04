/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateserver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author andresbenitez
 */
public class NewClass {
    
    public static void main(String args[]) {
        
        
        Usuario user;
        List<Usuario> lstUsers = new ArrayList<>();
        
        user = new Usuario();
        
        user.setNombre("andres");
        user.setEdad(10);
        user.setRut("11833519-8");
        
        lstUsers.add(user);

        user = new Usuario();
        
        user.setNombre("pepe");
        user.setEdad(20);
        user.setRut("222222");
        
        lstUsers.add(user);
        
        int numItems = lstUsers.size();
        
        for (int i=0; i<numItems; i++) {
            System.out.println(lstUsers.get(i).getNombre());
            System.out.println(lstUsers.get(i).getEdad());
            System.out.println(lstUsers.get(i).getRut());
        }
        
        
        System.out.println(lstUsers.contains(user));
        
        Usuario myUserFind = new Usuario();
        
        myUserFind.setNombre("pepe");
        
        List<Usuario> myName = lstUsers.stream().filter(pq -> pq.getEdad() >= 1).collect(Collectors.toList());
        
        numItems = myName.size();
        
        for (int i=0; i<numItems; i++) {
            System.out.println("Nomnre: "+myName.get(i).getNombre());
            System.out.println("Rut: "+myName.get(i).getRut());
            System.out.println("Edad: "+myName.get(i).getEdad());
        }
        
        
    }
    
    static class Usuario {
        String nombre;
        int edad;
        String rut;

        public int getEdad() {
            return edad;
        }

        public void setEdad(int edad) {
            this.edad = edad;
        }
        
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getRut() {
            return rut;
        }

        public void setRut(String rut) {
            this.rut = rut;
        }
        
    }
    
}
