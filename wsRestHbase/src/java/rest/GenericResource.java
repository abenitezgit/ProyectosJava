/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author andresbenitez
 */

@Path("rest")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }
    
    /**
     * Define los objetos de clase de datos.
     */
    
    UserDao userDao = new UserDao();
    grabDao GrabDao = new grabDao();

   @GET
   @Path("/users")
   @Produces(MediaType.APPLICATION_JSON)
   public List<User> getUsers(){
      return userDao.getAllUsers();
   }	
   
   @GET
   @Path("/getGrabMuestra")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Grabacion> getGrabMuestra(){
      return GrabDao.getMuestra();
   }	   
   

//   @POST
//   @Path("/getName")
//   @Consumes(MediaType.TEXT_PLAIN)
//   public Grabacion getName(int id){
//      return userDao.getName(id);
//   }	
   
   @POST
   @Path("/getName")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response getName(String name){
      return Response.status(200).entity(userDao.getName()).build();
   }	   
   
    @GET
   @Path("/name")
   @Produces(MediaType.APPLICATION_JSON)
   public User getName(){
      return userDao.getName();
   }	
   
    /**
     * Retrieves representation of an instance of rest.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
        return "getresponse";
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
