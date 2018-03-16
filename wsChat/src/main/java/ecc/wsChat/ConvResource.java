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
import ecc.model.DataResponse;
import ecc.model.PutResponse;
import ecc.services.IMainService;
import ecc.services.MainServiceImpl;
import ecc.utiles.GlobalParams;

@Path("conv")
public class ConvResource {
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
	@Path("chatMessage")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatSearchText(String dataInput) {
		DataResponse dResp = new DataResponse();
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			IMainService ms = new MainServiceImpl(gParams);
			logger.info("Inicio GET Texto en Message Conversacion via POST");
			logger.info("DataInput: "+dataInput);
			
			/*
			 * Valida el json de entrada
			 * opcion:1 Consulta por Messsage
			 */
			logger.info("Parsea Data Request");
			ms.parseDataRequest(dataInput);

			ms.validaDataRequest(1);
			
			/*
			 * Si Texto buscado existe recupera parametros del servicio
			 */
			if (ms.lastStatus()==0) {
				logger.info("Datos de entra validados");
				
				/*
				 * Recupera los parametros del servicio
				 */
				ms.initComponents();
				
				if (ms.lastStatus()==0) {
					logger.info("Parametros del servicio recuperados");
					
					/*
					 * Recuperanndo Interaccciones de Chat que contengan texto buscado en message
					 */
					List<Chat> lstChats = new ArrayList<>();
					lstChats = ms.getChatText(2);
					
					if (ms.lastStatus()==0) {
						if (lstChats.size()>0) {
							logger.info("Chats encontrados: "+lstChats.size());
							dResp.setCodeStatus(ms.lastStatus());
							dResp.setStatus("OK");
							dResp.setMessage(mylib.serializeObjectToJSon(lstChats, false));
						} else {
							dResp.setCodeStatus(ms.lastStatus());
							dResp.setStatus("OK");
							dResp.setMessage("No hay Chats con texto buscado");
							logger.info("No hay Chats con texto buscado");
						}
						
					} else {
						dResp.setCodeStatus(ms.lastStatus());
						dResp.setStatus("ERROR");
						dResp.setMessage("No pudo recuperar Chats");
						logger.error("No pudo recuperar Chats");
					}
				} else {
					dResp.setCodeStatus(ms.lastStatus());
					dResp.setStatus("ERROR");
					dResp.setMessage("No pudo recuperar parametros del servicio");
					logger.error("No pudo recuperar parametros del servicio");
				}
			} else {
				dResp.setCodeStatus(ms.lastStatus());
				dResp.setStatus("ERROR");
				dResp.setMessage("Error en datos de entrada");
				logger.error("Error en datos de entrada");
			}
			
			
			String strResponse = mylib.serializeObjectToJSon(dResp, false);
			
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
	@Path("chatLastInt")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatLastInt(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			IMainService ms = new MainServiceImpl(gParams);
			//ChatService srvChat = new ChatService();
			
			logger.info("Inicio GET Chat Conversacion via POST");
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
			
			Chat chat = new Chat();
			
			chat = ms.getLastInteractionChatID(2);
			
			String strResponse = mylib.serializeObjectToJSon(chat, false);
			
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
	@Path("chatGet")
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatGet(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			IMainService ms = new MainServiceImpl(gParams);
			//ChatService srvChat = new ChatService();
			
			logger.info("Inicio GET Chat Conversacion via POST");
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
			
			lstChat = ms.getInteractsChatID(2);
			
			
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
			
			logger.info("Inicio PUT Chat Conversacion via POST");
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
			ms.putChat(2);
			
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
