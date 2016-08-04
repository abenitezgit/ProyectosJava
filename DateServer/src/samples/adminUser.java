/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.util.List;
import samples.strucData.Usuario;


/**
 *
 * @author andresbenitez
 */
public class adminUser implements NewInterface {
    strucData Usuario;
    
    Usuario user;
    List<Usuario> listUser;

    public adminUser() {
    }

    @Override
    public List<Usuario> getUser() {
        return listUser;
    }

    @Override
    public Usuario addUser(String nombre) {
        user.setNombre(nombre);
        listUser.add(user);
        return user;
    }
    
    public static void main(String args[]) {
        NewInterface myUser = new adminUser();
        
        myUser.addUser("Andres");
        
        int numUsers = myUser.getUser().size();
        
        for (int i=0; i<numUsers; i++) {
            System.out.println(myUser.getUser().get(i).getNombre());
        }
    
    }

}
