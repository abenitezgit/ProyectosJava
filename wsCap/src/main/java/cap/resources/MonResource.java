package cap.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import cap.implement.MainServiceImpl;
import cap.model.DataRequest;
import cap.model.DataResponse;
import cap.services.IMainService;
import cap.services.MonService;
import cap.utiles.GlobalParams;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("monitor")
public class MonResource {
	Logger logger = Logger.getLogger("MonResource");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams = new GlobalParams();
	MonService mons = new MonService(gParams);

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response groupManage(String dataInput) {
    		DataResponse dResponse = new DataResponse();
    		try {
    			logger.info("Inicio Mon Request...");
    			logger.info("DataInput: "+dataInput);
    			
    			logger.info("Serializando DataInput...");
    			DataRequest dRequest = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    			
    			logger.info("Recuperando parametros de configuración...");
    			IMainService ms = new MainServiceImpl(gParams);
    			ms.initComponents();
    			
    			dResponse = mons.getMonRequest(dRequest);
    			
    			String strResponse = mylib.serializeObjectToJSon(dResponse, false);
    			
    			logger.info("Enviando respuesta...");
    			return Response.ok().entity(strResponse).build();
    		} catch (Exception e) {
    			logger.error(e.getMessage());
    			dResponse.setCode(500);
    			dResponse.setMessage(e.getMessage());
    			dResponse.setStatus("ERROR");
    			String strError=e.getMessage();
    			try {
					strError = mylib.serializeObjectToJSon(dResponse, false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			
    			return Response.status(500).entity(strError).build();
    		}
    	
    }
}
