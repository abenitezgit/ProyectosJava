package ecc.wsProxyGrab;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import ecc.services.MainService;
import ecc.utiles.GlobalParams;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("getWav2")
public class ExtractResource2 {
	

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getNull() {
		Logger logger = Logger.getLogger("wsProxyGrab");
		logger.info("Inicio Consulta por GET");
		logger.info("Debe ingresar valor de un ID, devolviendo respuesta...");
		return Response.status(429).entity("Debe ingresar valor de un ID").build();
	}
    
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWav(@PathParam("id") String id) {
		Logger logger = Logger.getLogger("wsProxyGrab");
		GlobalParams gParams = new GlobalParams();
		MainService ms = new MainService(gParams);
		
		try {
			logger.info("Iniciando Proxy wsGrab...");
			
			//String strResponse="";
			
			//Recuperando parametros de inicio
			logger.info("Recuperando Parametros de inicio...");
			if (ms.initComponents()) {
				
				String server = gParams.getInfo().getRestServer();
				String port = gParams.getInfo().getRestPort();
				String urlBase = gParams.getInfo().getRestUrlExtract();
				//String contentType = gParams.getInfo().getRestContentType();
				
				logger.info("Ejecutando API Request: "+id);
				String BASE_URI = "http://"+server+":"+port+urlBase+"/"+id;

				logger.info("Iniciando Redirect hacia URL: "+BASE_URI);
				
		        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		        HttpGet getRequest = new HttpGet(BASE_URI);
		        		
				CloseableHttpResponse response = null;
				
				logger.info("Ejecutando Request...");
				
				response = httpClient.execute(getRequest);
				int codeStatus = response.getStatusLine().getStatusCode();

				String CD=null;
				
				logger.info("Response status code: "+codeStatus);
				if (codeStatus==200) {
					
					final InputStream is = response.getEntity().getContent();
					StringTokenizer tokens = new StringTokenizer(id,"+");
					
					String strID = "";
					while (tokens.hasMoreElements()) {
						strID = strID + tokens.nextToken();
					}
					
					File downloadFile = new File(gParams.getInfo().getRestMediaWork()+"/"+strID+".mp3");
					FileOutputStream fosDownloadFile = new FileOutputStream(downloadFile);   	//Puntero para escribir el archivo
					OutputStream osDownloadFile = new BufferedOutputStream(fosDownloadFile);

			        //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
			        byte[] bytesArray = new byte[4096];
			        int bytesRead = -1;
			        while ((bytesRead = is.read(bytesArray)) != -1) {
			        		osDownloadFile.write(bytesArray, 0, bytesRead);
			        }

			        osDownloadFile.close();
			        
					Header[] headers = response.getAllHeaders();
					for (Header h : headers) {
						if (h.getName().equals("Content-Disposition")) {
							CD = h.getValue();
						}
					}
			
					response.close();
					httpClient.close();
					
					File file = new File(gParams.getInfo().getRestMediaWork()+"/"+strID+".mp3");
					FileInputStream fis = new FileInputStream(file);
					
					logger.info("Encontrado Content: "+CD);
					
					StreamingOutput output = new StreamingOutput() {
					    @Override
					    public void write(OutputStream out) throws IOException, WebApplicationException {  
					        int length;
					        byte[] buffer = new byte[4096];
					        while((length = is.read(buffer)) != -1) {
					            out.write(buffer, 0, length);
					        }
					        out.flush();
					        is.close();
					    }   
					};
					
					
					
					
					//ResponseBuilder rb = Response.ok((Object) is);
			        ResponseBuilder rb = Response.ok((Object) output);
			        rb.header("Content-Disposition", CD);
			        
			        logger.info("Retornando Archivo adjunto");
			        
			        return rb.build();
				} else {
					logger.error("Error ejecutando request code: "+codeStatus);
					return Response.status(codeStatus).entity("Error ejecutando request").build();
				}
				
			} else {
				logger.error("No pudo iniciar parametros de inicio");
				return Response.status(500).entity("No pudo iniciar parametros de inicio").build();
			}
	        
		} catch (Exception e) {
			System.out.println("wsRedirect - Error ejecutando request: "+e.getMessage());
	        return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}
	
	
}
