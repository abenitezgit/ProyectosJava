package ecc.wsProxyGrab;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.api.RestAPI;

import ecc.services.MainService;
import ecc.utiles.GlobalParams;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("grabdata")
public class GrabResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Transaccion No autorizada por GET";
    }
    
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response grabaciones(String dataInput) {
		Logger logger = Logger.getLogger("wsProxyGrab");
		GlobalParams gParams = new GlobalParams();
		RestAPI restApi = new RestAPI();
		MainService ms = new MainService(gParams);
		
		try {
			logger.info("Iniciando Proxy wsGrab...");
			
			String strResponse="";
			
			//Recuperando parametros de inicio
			logger.info("Recuperando Parametros de inicio...");
			if (ms.initComponents()) {
				
				String server = gParams.getInfo().getRestServer();
				String port = gParams.getInfo().getRestPort();
				String urlBase = gParams.getInfo().getRestUrlBase();
				String contentType = gParams.getInfo().getRestContentType();
				
				restApi.setMethod("POST");
				restApi.setServer(server);
				restApi.setPort(port);
				restApi.setUrlBase(urlBase);
				restApi.setContentType(contentType);
				
				logger.info("Ejecutando API Request: "+dataInput);
				restApi.execute(dataInput);
				
				logger.info("Response status code: "+restApi.getResponseCode());
				if (restApi.getResponseCode()==200) {
					logger.info("API Rest Success!!");
					
					logger.info("Respuesta API: "+restApi.getDataResponse());
					strResponse = restApi.getDataResponse();
					
				} else {
					logger.error("Error API Rest: "+restApi.getResponseCode()+" "+restApi.getResponseMessage());
					return Response.status(restApi.getResponseCode()).entity(restApi.getResponseMessage()).build();
				}
				
			} else {
				logger.error("No pudo iniciar parametros de inicio");
				return Response.status(500).entity("No pudo iniciar parametros de inicio").build();
			}
			
            logger.info("Enviando respuesta...");
			return Response.ok().entity(strResponse).build();
		} catch (Exception e) {
			logger.error("Error proceso general.."+e.getMessage());
			return Response.status(500).entity("Error proceso general: "+e.getMessage()).build();
		}
	}
	
	
}
