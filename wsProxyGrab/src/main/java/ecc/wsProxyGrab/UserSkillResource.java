package ecc.wsProxyGrab;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("getSkill")
public class UserSkillResource {
	

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSkillQuery(@QueryParam("usuario") String user) {
		Logger logger = Logger.getLogger("wsProxyGrab");
		
		try {
			logger.info("Iniciando Proxy wsUserSkill...");
			
				
				String server = "10.240.8.15";
				String port = "8080";
				String urlBase = "/wsUserSkill/webapi/getSkill";
				
				logger.info("Ejecutando API Request");
				String BASE_URI = "http://"+server+":"+port+urlBase+"/"+user.toUpperCase();

				logger.info("Iniciando Redirect hacia URL: "+BASE_URI);
				
		        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		        HttpGet getRequest = new HttpGet(BASE_URI);
		        		
				CloseableHttpResponse response = null;
				
				logger.info("Ejecutando Request...");
				
				response = httpClient.execute(getRequest);
				int codeStatus = response.getStatusLine().getStatusCode();

				logger.info("Response status code: "+codeStatus);
				if (codeStatus==200) {
					
					//ResponseBuilder rb = Response.ok((Object) is);
					HttpEntity httpEntity = response.getEntity();
					
			        ResponseBuilder rb = Response.ok((Object) EntityUtils.toString(httpEntity));
			        
			        logger.info("Enviando respuesta...");
			        
			        return rb.build();
				} else {
					logger.error("Error ejecutando request code: "+codeStatus);
					return Response.status(codeStatus).entity("Error ejecutando request").build();
				}
				
		} catch (Exception e) {
			logger.error("wsRedirect - Error ejecutando request: "+e.getMessage());
	        return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}
    
	@GET
	@Path("/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWav(@PathParam("user") String user) {
		Logger logger = Logger.getLogger("wsProxyGrab");
		
		try {
			logger.info("Iniciando Proxy wsUserSkill...");
			
				
				String server = "10.240.8.15";
				String port = "8080";
				String urlBase = "/wsUserSkill/webapi/getSkill";
				
				logger.info("Ejecutando API Request");
				String BASE_URI = "http://"+server+":"+port+urlBase+"/"+user;

				logger.info("Iniciando Redirect hacia URL: "+BASE_URI);
				
		        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		        HttpGet getRequest = new HttpGet(BASE_URI);
		        		
				CloseableHttpResponse response = null;
				
				logger.info("Ejecutando Request...");
				
				response = httpClient.execute(getRequest);
				int codeStatus = response.getStatusLine().getStatusCode();

				logger.info("Response status code: "+codeStatus);
				if (codeStatus==200) {
					
					//ResponseBuilder rb = Response.ok((Object) is);
					HttpEntity httpEntity = response.getEntity();
					
			        ResponseBuilder rb = Response.ok((Object) EntityUtils.toString(httpEntity));
			        
			        logger.info("Enviando respuesta...");
			        
			        return rb.build();
				} else {
					logger.error("Error ejecutando request code: "+codeStatus);
					return Response.status(codeStatus).entity("Error ejecutando request").build();
				}
				
		} catch (Exception e) {
			logger.error("wsRedirect - Error ejecutando request: "+e.getMessage());
	        return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}
}
