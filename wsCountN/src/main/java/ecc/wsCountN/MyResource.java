
package ecc.wsCountN;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/facet")
public class MyResource {
    
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     * 
     * 
     * 
     */
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStreamGET() {
		try {
			System.out.println("Consulta totales via GET");
			System.out.println("Iniciando Re-Direct hacia WS Interno...");
			
			APIRest api = new APIRest();
			
			api.setContentType("application/json");
			api.setMethod("GET");
			api.setPort("8080");
			api.setServer("10.240.8.15");
			//api.setServer("localhost");
			api.setUrlBase("/wsTest/webapi/facet");

			api.executeGET();
			
			if (api.getResponseCode()==200) {
				System.out.println("Respuesta Exitosa: "+api.getDataResponse());
				return Response.ok(api.getDataResponse()).build();
			} else {
				System.out.println("Error respuesta api..: "+api.getResponseMessage());
				return Response.status(460).build();
			}
			
		} catch (Exception e) {
			return Response.status(460).build();
		}

	}
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStream(String inputData) {
    		try {
    			System.out.println("InputData: "+inputData);
    			System.out.println("Iniciando Re-Direct hacia WS Interno...");
    			
    			APIRest api = new APIRest();
    			
    			api.setContentType("application/json");
    			api.setMethod("POST");
    			api.setPort("8080");
    			api.setServer("10.240.8.15");
    			api.setUrlBase("/wsTest/webapi/facet");
    			
    			api.execute(inputData);
    			
    			if (api.getResponseCode()==200) {
    				System.out.println("Respuesta Exitosa: "+api.getDataResponse());
    				return Response.ok(api.getDataResponse()).build();
    			} else {
    				System.out.println("Error respuesta api..: "+api.getResponseMessage());
    				return Response.status(460).build();
    			}
    			
    		} catch (Exception e) {
    			return Response.status(460).build();
    		}
    }
}
