package ecc.wsGrab;

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

import ecc.model.DataResponse;
import ecc.model.DataResponseMin;
import ecc.model.GrabMin;
import ecc.services.GrabService;
import ecc.utiles.GlobalArea;


@Path("grabobject")
public class GrabResourceSinSkill {
	Logger logger = Logger.getLogger("wsGrab");
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataResponse dResponse = new DataResponse();
	DataResponseMin dResponseMin = new DataResponseMin();
	GrabService srvGrab = new GrabService(gDatos);
	List<GrabMin> lstGrab = new ArrayList<>();
	boolean flagFiltros = true;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMethod() {
		
		ResponseBuilder response = Response.ok("Operacion no permitida por GET");
		return response.build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response grabaciones(String dataInput) {
		ResponseBuilder response;
		try {
			//Instancia Clase Servicio
			
			
			logger.info("Iniciando extraccion de grabaciones via POST");
			logger.info("DataInput: "+dataInput);
			
			//Respuesta Default
			dResponse.setStatus(99);
			dResponse.setMessage("Error general");
			
			logger.info("Inicializando componentes...");
			srvGrab.initComponents(dataInput);

            /**
             * Resuelve el tipo de filtro de consulta
             */
			logger.info("Analizando tipo de filtro de consulta");
            int tipoConsulta = srvGrab.getTipoConsultaSinSkill();
            switch (tipoConsulta) {
                case 1:
                	logger.info("Filtro de Consulta: ConnID sin Skill y sin Fechas");
                    break;
                case 2:
                	logger.info("Filtro de Consulta: Agenfe sin Skill y sin Fechas");
                    break;
                case 3:
                	logger.info("Filtro de Consulta: Ani sin Skill y sin Fechas");
                    break;
                case 4:
                	logger.info("Filtro de Consulta: Dnis sin Skill y sin Fechas");
                    break;
                case 5:
                	logger.info("Filtro de Consulta: UniqueID sin Skill y sin Fechas");
                    break;
                case 6:
                	logger.info("Filtro de Consulta: InteractionID sin Skill y sin Fechas");
                    break;
                case 98:
                	logger.error("Error Filtro de Consulta: Debe ingresar al menos un ConnID o un Agente");
                    dResponse.setStatus(98);
                    dResponse.setMessage("Error Filtro de Consulta: Debe ingresar al menos un ConnID o un Agente");
                    flagFiltros=false;
                    break;
                default:
                	logger.error("Error Filtro de Consulta: Error de parametros de entrada");
                    dResponse.setStatus(94);
                    dResponse.setMessage("Error Filtro de Consulta: Error de parametros de entrada");
                    flagFiltros=false;
                    break;
            }
            
            if (flagFiltros) {
            	logger.info("Realizando busqueda de grabaciones...");
	            	lstGrab = srvGrab.getGrabDataSinSkill(tipoConsulta);
	            	logger.info("Se encontraron "+lstGrab.size()+" grabaciones");
	            	if (lstGrab.size()==0) {
	            		dResponse.setStatus(0);
	            		dResponse.setMessage("No se encontraron grabaciones");
	            		logger.warn("No se encontraron grabaciones");
	            	} else {
		            	dResponse.setStatus(0);
						dResponse.setMessage("SUCCESS");
						dResponse.setNumFound(lstGrab.size());
						dResponse.setLimit(srvGrab.getLimitRows());
						//dResponse.setData(lstGrab);
	            	}
            } else {
	        		dResponse.setStatus(0);
	        		dResponse.setMessage("Error identificando tipo de consulta");            	
            }
			
            dResponseMin.setData(lstGrab);
            String strResponse = mylib.serializeObjectToJSon(dResponseMin, false);
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
