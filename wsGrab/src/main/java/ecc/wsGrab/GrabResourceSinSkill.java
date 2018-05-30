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
import ecc.services.IMainServices;
import ecc.services.MainServicesImpl;
import ecc.utiles.GlobalArea;


@Path("grabobject")
public class GrabResourceSinSkill {
	Logger logger = Logger.getLogger("wsGrab");
	GlobalArea gParams = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataResponse dResponse = new DataResponse();
	DataResponseMin dResponseMin = new DataResponseMin();
	GrabService srvGrab = new GrabService(gParams);
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
		IMainServices mainS = new MainServicesImpl(gParams);
		ResponseBuilder response;
		try {
			logger.info("Iniciando extraccion de grabaciones via POST");
			logger.info("DataInput: "+dataInput);
			
			/**
			 * Recibe los datos de entrada y los mapea en una clase DataRequest()
			 */
			logger.info("Serializando datos de entrada...");
			try {
				srvGrab.fillDataRequest(dataInput);
			} catch (Exception e) {
				return  Response.status(500).entity("Datos de entrada no son validos: "+e.getMessage()).build();
			}
			
			/**
			 * Inicializando componentes -  cargando parametros
			 */
			try {
				srvGrab.initComponent();
			} catch (Exception e) {
				return  Response.status(500).entity("Error cargando archivo de parametros: "+e.getMessage()).build();
			}
			
            /**
             * Resuelve el tipo de filtro de consulta
             */
			logger.info("Inicia Gestion de Opcion de Consulta");
			try {
				mainS.gestionaOpcionConsulta();
			} catch (Exception e) {
				return  Response.status(500).entity("Opcion de Consulta no es valida: "+e.getMessage()).build();
			}

			/**
			 * Genera los filtros para la query de busqueda en SOLR
			 */
			logger.info("Inicia Gestion Filtros para solR");
			try {
				mainS.genSolrFilterMapping();
			} catch (Exception e) {
				return  Response.status(500).entity("Error generando query hacia SolR: "+e.getMessage()).build();
			}
			
			/**
			 * Ejecutando Query en SOLR
			 */
            	logger.info("Realizando busqueda de grabaciones...");
            	try {
	            	lstGrab = srvGrab.getGrabData();
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
            	} catch (Exception e) {
            		return  Response.status(500).entity("Error recuperando Grabaciones desde Base de datos: "+e.getMessage()).build();
            	}
			
            	/**
            	 * Generando objeto de respuesta
            	 */
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
