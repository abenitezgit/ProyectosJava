package ecc.wsCount;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import utiles.common.rutinas.APIRest;
import utiles.common.rutinas.Rutinas;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("facet")
public class MyResource {
	Rutinas mylib = new Rutinas();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStream(String inputData) {
    		try {
    			mylib.console("InputData: "+inputData);
    			mylib.console("Iniciando Re-Direct hacia WS Interno...");
    			
    			APIRest api = new APIRest();
    			
    			api.setContentType("application/json");
    			api.setMethod("POST");
    			api.setPort("8080");
    			api.setServer("10.240.8.15");
    			api.setUrlBase("/wsTest/webapi/facet");
    			
    			api.execute(inputData);
    			
    			if (api.getResponseCode()==200) {
    				mylib.console("Respuesta Exitosa: "+api.getDataResponse());
    				return Response.ok(api.getDataResponse()).build();
    			} else {
    				mylib.console(1,"Error respuesta api..: "+api.getResponseMessage());
    				return Response.status(460, "Error API.."+api.getResponseMessage()).build();
    			}
    			
    		} catch (Exception e) {
    			return Response.status(460, "Error general").build();
    		}
    }
}
