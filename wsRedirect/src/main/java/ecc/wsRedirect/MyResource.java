
package ecc.wsRedirect;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.catalina.logger.SystemOutLogger;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;


/** Example resource class hosted at the URI path "/myresource"
 */
@Path("getWav")
public class MyResource {
    
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @Produces("text/plain")
    public String getIt() {
        return "Not enabled!";
    }
    
	@GET
	@Path("/stream/{connid}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWavStream(@PathParam("connid") String connid) {
		
		try {
			System.out.println("wsRedirect - Iniciando GetWav de connid: "+connid);
	        String BASE_URI = "http://wsecc:8080/wsGrab/webapi/getWav/"+connid;
	
	        System.out.println("wsRedirect - Iniciando Redirect hacia URL: "+BASE_URI);
	       
	        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	        HttpGet getRequest = new HttpGet(BASE_URI);
	        		
			CloseableHttpResponse response = null;
			
			System.out.println("wsRedirect - Ejecutando Request...");
			
			response = httpClient.execute(getRequest);
			
			int codeStatus = response.getStatusLine().getStatusCode();
			
			System.out.println("wsRedirect - Retorno respuesta: "+codeStatus);
			
			String CD=null;
			
			if (codeStatus==200) {
				final InputStream is = response.getEntity().getContent();
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        int len;
		        byte[] buffer = new byte[4096];
		        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
		            baos.write(buffer, 0, len);
		        }
				
//				StreamingOutput output = new StreamingOutput() {
//				    @Override
//				    public void write(OutputStream out) throws IOException, WebApplicationException {  
//				        int length;
//				        byte[] buffer = new byte[1024];
//				        while((length = is.read(buffer)) != -1) {
//				            out.write(buffer, 0, length);
//				        }
//				        out.flush();
//				        is.close();
//				    }   
//				};
				
		        
				Header[] headers = response.getAllHeaders();
				for (Header h : headers) {
					if (h.getName().equals("Content-Disposition")) {
						CD = h.getValue();
					}
				}
		
				response.close();
				httpClient.close();
				
				
				
				System.out.println("wsRedirect - Encontrado Content: "+CD);

		        System.out.println("wsRedirect - Retornando Archivo adjunto");
		        
		        return Response.ok(baos).build();

//				return Response.ok(baos).header(
//				        "Content-Disposition", "attachment, filename=\"...\"").build();
				
		        
			} else {
				return Response.status(codeStatus).entity("Error ejecutando request").build();
			}
	        
		} catch (Exception e) {
			System.out.println("wsRedirect - Error ejecutando request: "+e.getMessage());
	        return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}

    
	@GET
	@Path("/{connid}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWav(@PathParam("connid") String connid) {
		
		try {
			System.out.println("wsRedirect - Iniciando GetWav de connid: "+connid);
	        String BASE_URI = "http://wsecc:8080/wsGrab/webapi/getWav/"+connid;
	
	        System.out.println("wsRedirect - Iniciando Redirect hacia URL: "+BASE_URI);
	       
	        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	        HttpGet getRequest = new HttpGet(BASE_URI);
	        		
			CloseableHttpResponse response = null;
			
			System.out.println("wsRedirect - Ejecutando Request...");
			
			response = httpClient.execute(getRequest);
			
			int codeStatus = response.getStatusLine().getStatusCode();
			
			System.out.println("wsRedirect - Retorno respuesta: "+codeStatus);
			
			String CD=null;
			
			if (codeStatus==200) {
				InputStream is = response.getEntity().getContent();
				
				StringTokenizer tokens = new StringTokenizer(connid,"+");
				
				String strID = "";
				while (tokens.hasMoreElements()) {
					strID = strID + tokens.nextToken();
				}
				
				File downloadFile = new File("/root/tomcat8/webapps/media/"+strID+".mp3");
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
				
				
				File file = new File("/root/tomcat8/webapps/media/"+strID+".mp3");
				FileInputStream fis = new FileInputStream(file);

				
				System.out.println("wsRedirect - Encontrado Content: "+CD);
				
				//ResponseBuilder rb = Response.ok((Object) is);
		        ResponseBuilder rb = Response.ok((Object) fis);
		        rb.header("Content-Disposition", CD);
		        
		        System.out.println("wsRedirect - Retornando Archivo adjunto");
		        
		        return rb.build();
			} else {
				return Response.status(codeStatus).entity("Error ejecutando request").build();
			}
	        
		} catch (Exception e) {
			System.out.println("wsRedirect - Error ejecutando request: "+e.getMessage());
	        return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}
}
