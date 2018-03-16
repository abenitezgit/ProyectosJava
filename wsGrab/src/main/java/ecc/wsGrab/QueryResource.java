package ecc.wsGrab;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rutinas.Rutinas;

import ecc.model.Grabacion;
import ecc.services.QueryService;
import ecc.utiles.GlobalArea;

@Path("query")
public class QueryResource {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	
	@GET
	@Path("/connid/{connid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRow(@PathParam("connid") String connid) {
		try {
			QueryService qSrv = new QueryService(gDatos);
			Map<String, Grabacion> mapGrab = new HashMap<>();
			mapGrab = qSrv.getRow(connid);
			
			if (mapGrab.size()!=0) {
				String strGrab = mylib.serializeObjectToJSon(mapGrab, false);
				return Response.ok().entity(strGrab).build();
			} else {
				return Response.status(427).entity("Connid no encontrado").build();
			}
		} catch (Exception e) {
			return Response.status(500).entity("Error query ("+e.getMessage()+")").build();
		}
		
	}

}
