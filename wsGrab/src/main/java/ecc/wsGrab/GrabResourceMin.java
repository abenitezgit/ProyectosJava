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

import ecc.model.DataResponse;
import ecc.model.DataResponseMin;
import ecc.model.GrabMin;
import ecc.services.GrabService;
import utiles.common.rutinas.Rutinas;


@Path("grabdata")
public class GrabResourceMin {
	Rutinas mylib = new Rutinas();
	DataResponse dResponse = new DataResponse();
	DataResponseMin dResponseMin = new DataResponseMin();
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
			GrabService srvGrab = new GrabService();
			
			mylib.console("Iniciando extraccion de grabaciones via POST");
			mylib.console("DataInput: "+dataInput);
			
			//Respuesta Default
			dResponse.setStatus(99);
			dResponse.setMessage("Error general");
			
			mylib.console("Inicializando componentes...");
			srvGrab.initComponents(dataInput);

            /**
             * Resuelve el tipo de filtro de consulta
             */
            mylib.console("Analizando tipo de filtro de consulta");
            int tipoConsulta = srvGrab.getTipoConsulta();
            switch (tipoConsulta) {
                case 1:
                    mylib.console("Filtro de Consulta: ConnID sin Fechas");
                    break;
                case 2:
                    mylib.console("Filtro de Consulta: ConnID con rango de fechas");
                    break;
                case 3:
                    mylib.console("Filtro de Consulta: UniqueID");
                    break;
                case 4:
                    mylib.console("Filtro de Consulta: Agente");
                    break;
                case 5:
                    mylib.console("Filtro de Consulta: ANI con rango de fechas");
                    break;
                case 6:
                    mylib.console("Filtro de Consulta: Ani sin fechas");
                    break;
                case 7:
                    mylib.console("Filtro de Consulta: Dnis con rango de fechas");
                    break;
                case 8:
                    mylib.console("Filtro de Consulta: Dnis sin fechas");
                    break;
                case 9:
                    mylib.console("Filtro de Consulta: Busqueda masiva por fechas");
                    break;
                case 10:
                    mylib.console("Filtro de Consulta: Agente por fechas");
                    break;
                case 97:
                    mylib.console(1, "Error Filtro de Consulta: Debe ingresar al menos un rango de fechas si es que no ha seleccionado ningun otro valor");
                    dResponse.setStatus(97);
                    dResponse.setMessage("Error Filtro de Consulta: Debe ingresar al menos un rango de fechas si es que no ha seleccionado ningun otro valor");
                    flagFiltros=false;
                    break;
                case 98:
                    mylib.console(1, "Error Filtro de Consulta: Debe ingresar al menos un SKILL");
                    dResponse.setStatus(98);
                    dResponse.setMessage("Error Filtro de Consulta: Debe ingresar al menos un SKILL");
                    flagFiltros=false;
                    break;
                case 99:
                    mylib.console(1, "Error Filtro de Consulta: Error de Ejecución");
                    dResponse.setStatus(99);
                    dResponse.setMessage("Error Filtro de Consulta: Error de Ejecución");
                    flagFiltros=false;
                    break;
                default:
                    mylib.console(1, "Error Filtro de Consulta: Error de parametros de entrada");
                    dResponse.setStatus(94);
                    dResponse.setMessage("Error Filtro de Consulta: Error de parametros de entrada");
                    flagFiltros=false;
                    break;
            }
            
            if (flagFiltros) {
            	mylib.console("Realizando busqueda de grabaciones...");
            	lstGrab = srvGrab.getGrabData(tipoConsulta);
            	mylib.console("Se encontraron "+lstGrab.size()+" grabaciones");
            	if (lstGrab.size()==0) {
            		dResponse.setStatus(0);
            		dResponse.setMessage("No se encontraron grabaciones");
            		mylib.console(2,"No se encontraron grabaciones");
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
