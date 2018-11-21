package cap.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import cap.implement.MainServiceImpl;
import cap.model.DataResponse;
import cap.services.IMainService;
import cap.services.SpeechService;
import cap.utiles.GlobalParams;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("speech")
public class SpeechResource {
	Logger logger = Logger.getLogger("SpeechResource");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams = new GlobalParams();
	SpeechService ss = new SpeechService(gParams);

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
    
	@GET
	@Path("/set/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setSTT(@PathParam("id") String id, @QueryParam("swNew") int swNew) {
		DataResponse dResponse = new DataResponse();
		try {
			logger.info("Inicio Speech Request...");
			logger.info("Id de Grabacion: "+id);
			logger.info("swNew Update: "+swNew);
			
			logger.info("Recuperando parametros de configuración...");
			IMainService ms = new MainServiceImpl(gParams);
			ms.initComponents();

			/**
			 * Inscribir petición de Speech to Text con ID único.
			 * y retornar OK si fue inscrito correctamente
			 * 
			 * Un proceso independiente en Background está realizando el proceso completo de STT
			 */
			
			//boolean result = ss.inscribeAudio(id, 0);  //Retorna el SpeechID de proceso 
			logger.info("Inscribiendo Request STT...");
			//boolean result = true;  //Retorna el SpeechID de proceso
			boolean result = ss.inscribeAudio(id, swNew);

			if (result) {
				logger.info("Inscripción Exitosa de ID: "+id);
				dResponse.setCode(0);
				dResponse.setData(result);
				dResponse.setMessage("ID: "+id+" inscrito correctamente");
				dResponse.setStatus("OK");
			} else {
				logger.error("No se pudo inscribir ID: "+id);
				dResponse.setCode(1);
				dResponse.setData(result);
				dResponse.setMessage("ID: "+id+" no pudo ser inscrito");
				dResponse.setStatus("ERROR");
			}
				
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
	
	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSTT(@PathParam("id") String id) {
		DataResponse dResponse = new DataResponse();
		try {
			logger.info("Inicio Speech Request...");
			logger.info("Id de Grabacion: "+id);
			
			logger.info("Recuperando parametros de configuración...");
			IMainService ms = new MainServiceImpl(gParams);
			ms.initComponents();

			/**
			 * Inscribir petición de Speech to Text con ID único.
			 * y retornar OK si fue inscrito correctamente
			 * 
			 * Un proceso independiente en Background está realizando el proceso completo de STT
			 */
			
			//boolean result = ss.inscribeAudio(id, 0);  //Retorna el SpeechID de proceso 
			logger.info("Inscribiendo Request STT...");
			boolean result = true;  //Retorna el SpeechID de proceso

			if (result) {
				logger.info("Inscripción Exitosa de ID: "+id);
				dResponse.setCode(0);
				dResponse.setData(result);
				dResponse.setMessage("ID: "+id+" inscrito correctamente");
				dResponse.setStatus("OK");
			} else {
				logger.error("No se pudo inscribir ID: "+id);
				dResponse.setCode(1);
				dResponse.setData(result);
				dResponse.setMessage("ID: "+id+" no pudo ser inscrito");
				dResponse.setStatus("ERROR");
			}
				
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
