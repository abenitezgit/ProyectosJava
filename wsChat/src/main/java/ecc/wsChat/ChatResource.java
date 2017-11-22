package ecc.wsChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import ecc.model.Chat;
import ecc.model.PutResponse;
import ecc.services.ChatService;
import utiles.common.rutinas.Rutinas;

@Path("chatdata")
public class ChatResource {
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
	@Path("select")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatSelect(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			ChatService srvChat = new ChatService();
			
			mylib.console("Iniciando Select chatData via POST");
			mylib.console("DataInput: "+dataInput);
			
			//Respuesta Default
			pResponse.setStatus(99);
			pResponse.setMessage("Error general");
			
			mylib.console("Inicializando componentes...");
			srvChat.initSelectComponents(dataInput);
			
			List<Chat> lstChat = new ArrayList<>();
			
			lstChat = srvChat.getChatData(1);
			
			String strResponse = mylib.serializeObjectToJSon(lstChat, false);
			
			mylib.console("Enviando respuesta...");
			response = Response.ok().entity(strResponse);
			
			return response.build();
		} catch (Exception e) {
			mylib.console(1,"Error proceso general.."+e.getMessage());
			response = Response.status(500).entity("Error proceso general: "+e.getMessage());
			return response.build();
		}
	}
	
	@POST
	@Path("put")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatMain(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			ChatService srvChat = new ChatService();
			
			mylib.console("Iniciando put chatData via POST");
			mylib.console("DataInput: "+dataInput);
			
			//Respuesta Default
			pResponse.setStatus(99);
			pResponse.setMessage("Error general");
			
			mylib.console("Inicializando componentes...");
			srvChat.initComponents(dataInput);

			mylib.console("Insertando en hbase...");
    			int result = srvChat.executeUpdate();
    			
    			if (result==0) {
        			pResponse.setStatus(0);
        			pResponse.setMessage("Put Exitoso");
    				mylib.console("Inserción exitosa!");
    			} else {
        			pResponse.setStatus(98);
        			pResponse.setMessage("Error insertando en hbase");
    				mylib.console(1,"Inserción exitosa!");
    			}
        			
            String strResponse = mylib.serializeObjectToJSon(pResponse, false);
            mylib.console("Enviando respuesta...");
			response = Response.ok().entity(strResponse);
			
			return response.build();
		} catch (Exception e) {
			mylib.console(1,"Error proceso general.."+e.getMessage());
			response = Response.status(500).entity("Error proceso general: "+e.getMessage());
			return response.build();
		}
	}


}
