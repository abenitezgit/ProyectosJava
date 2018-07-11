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
import cap.services.DBService;
import cap.services.IMainService;
import cap.utiles.GlobalParams;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("db")
public class DBResource {
	Logger logger = Logger.getLogger("DBResource");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams = new GlobalParams();
	DBService dbs = new DBService(gParams);

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
    		try {
    			String response = "";
    			
    			logger.info("Inicio DB Request...");
    			logger.info("DataInput: "+dataInput);
    			
    			logger.info("Serializando DataInput...");
    			DataRequest dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    			
    			logger.info("Recuperando parametros de configuración...");
    			IMainService ms = new MainServiceImpl(gParams);
    			ms.initComponents();
    			
    			response = dbs.getDBRequest(dr.getMethod(), dr.getParam());
    			
    			logger.info("Enviando respuesta...");
    			return Response.ok().entity(response).build();
    		} catch (Exception e) {
    			logger.error(e.getMessage());
    			return Response.status(500).entity(e.getMessage()).build();
    		}
    	
    }
}
