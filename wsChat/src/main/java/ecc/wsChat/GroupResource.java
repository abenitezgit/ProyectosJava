package ecc.wsChat;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import ecc.model.Chat;
import ecc.model.PutResponse;
import ecc.services.IMainService;
import ecc.services.MainServiceImpl;
import ecc.utiles.GlobalParams;

@Path("group")
public class GroupResource {
	Logger logger = Logger.getLogger("wsChat");
	GlobalParams gParams = new GlobalParams();
	Rutinas mylib = new Rutinas();
	PutResponse pResponse = new PutResponse();
	boolean flagFiltros = true;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMethod() {
		
		ResponseBuilder response = Response.ok("Operacion no permitida por GET");
		return response.build();
	}
	
	@POST
	@Path("chatGet")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatGet(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			IMainService ms = new MainServiceImpl(gParams);
			//ChatService srvChat = new ChatService();
			
			logger.info("Inicio GET Chat Grupos via POST");
			logger.info("DataInput: "+dataInput);
			
			//Respuesta Default
			pResponse.setStatus(99);
			pResponse.setMessage("Error general");
			
			logger.info("Inicializando componentes...");
			ms.initComponents();
			
			logger.info("Parsea Data Request");
			ms.parseDataRequest(dataInput);
			
			logger.info("Valida Data Request");
			
			//Recupera las interacciones de Chat Internas
			// 1: internas
			// 2: conversacion
			// 3: salones (room)
			// 4: grupos 
			
			List<Chat> lstChat = new ArrayList<>();
			
			lstChat = ms.getInteractsChatID(4);
			
			
			String strResponse = mylib.serializeObjectToJSon(lstChat, false);
			
			logger.info("Enviando respuesta...");
			response = Response.ok().entity(strResponse);
			
			return response.build();
		} catch (Exception e) {
			logger.error("Error proceso general.."+e.getMessage());
			response = Response.status(500).entity("Error proceso general: "+e.getMessage());
			return response.build();
		}
	}

	@POST
	@Path("chatPut")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatPut(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			IMainService ms = new MainServiceImpl(gParams);
			//ChatService srvChat = new ChatService();
			
			logger.info("Inicio PUT Chat Grupos via POST");
			logger.info("DataInput: "+dataInput);
			
			//Respuesta Default
			pResponse.setStatus(99);
			pResponse.setMessage("Error general");
			
			logger.info("Inicializando componentes...");
			ms.initComponents();
			
			logger.info("Parsea Data Request");
			ms.parseDataRequest(dataInput);
			
			logger.info("Valida Data Request");
			
			//Recupera las interacciones de Chat Internas
			// 1: internas
			// 2: conversacion
			// 3: salones (room)
			// 4: grupos 
			
			logger.info("Insertando en hbase...");
			ms.putChat(4);
			
    			pResponse.setStatus(0);
    			pResponse.setMessage("Put Exitoso");
    			logger.info("Inserción exitosa!");
    			
	        String strResponse = mylib.serializeObjectToJSon(pResponse, false);
	        logger.info("Enviando respuesta...");
			response = Response.ok().entity(strResponse);
			
			return response.build();
		} catch (Exception e) {
			logger.error("Error proceso general.."+e.getMessage());
			response = Response.status(500).entity("Error proceso general: "+e.getMessage());
			return response.build();
		}
	}
}
