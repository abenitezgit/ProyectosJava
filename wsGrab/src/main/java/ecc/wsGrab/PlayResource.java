package ecc.wsGrab;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import ecc.services.ExtractService;
import utiles.common.rutinas.Rutinas;

@Path("playAudio")
public class PlayResource {
	Rutinas mylib = new Rutinas();
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getNull() {
		return Response.status(429).entity("Debe ingresar valor de un connid").build();
	}
	
	
	@GET
	@Path("/{connid}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWav(@PathParam("connid") String connid) throws Exception {
		ExtractService srv = new ExtractService();
		try {
			mylib.console("Iniciando extraccion grabacion para el connid: "+connid);
			mylib.console("Recuperando datos del Connid...");
			
			//Conecta a SOLR para extraer información requerida
			srv.getDataConnID(connid);

			//Recupera parametros requeridos
			int zone = srv.getZone();
			String urlAudio = srv.getUrlAudio();
			String audioPathFile = srv.getFtpDir()+srv.getFname();
			
			
			//Valida si archivo mp3 ya fue generado
			mylib.console("Buscando Existencia previa de audio: "+srv.getUploadFolder(zone)+connid+".mp3");
			if (!mylib.fileExist(srv.getUploadFolder(zone)+connid+".mp3")) {
				
				//Visualiza datos recuperados
				mylib.console("Zona Ubicacion Audios: "+zone);
				mylib.console("urlGetDecodedAudio: "+urlAudio);
				mylib.console("AudioPathFile: "+audioPathFile);
				
				//Ejecuta Nueva API getDecodedFile
				mylib.console("Ejecutando API Rest getDecodedFile");
				
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost postRequest = new HttpPost(urlAudio);
	
				JSONObject json = new JSONObject();
				json.put("zone", String.valueOf(zone));
				json.put("audioPathFile", audioPathFile);
				json.put("connid", connid);
				
				StringEntity input = new StringEntity(json.toString());
				input.setContentType("application/json");
				postRequest.setEntity(input);
				
				CloseableHttpResponse response;
				try {
					response = httpClient.execute(postRequest);
				} catch (Exception e) {
					mylib.console(1,"Error ejecutando API: "+urlAudio);
					throw new Exception("Error ejecutando API: "+urlAudio+" :"+e.getMessage());
				}
				
				mylib.console("Analizando Respuesta de la API...");
				
				File downloadFile = new File(srv.getUploadFolder(zone)+connid+".mp3");
				FileOutputStream fosDownloadFile = new FileOutputStream(downloadFile);   	//Puntero para escribir el archivo
				OutputStream osDownloadFile = new BufferedOutputStream(fosDownloadFile);
				
				int codeStatus = response.getStatusLine().getStatusCode();
				
				
				
				if (codeStatus==200) {
					mylib.console("Respuesta exitosa desde API");
					mylib.console("Recuperando Stream de audio...");
	
					InputStream  is = response.getEntity().getContent();
					
			        //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
			        byte[] bytesArray = new byte[4096];
			        int bytesRead = -1;
			        while ((bytesRead = is.read(bytesArray)) != -1) {
			        		osDownloadFile.write(bytesArray, 0, bytesRead);
			        }
			        
			        mylib.console("Stream de audio recupeado");
			        mylib.console("Cerrando conexiones de API");
			        
			        is.close();
			        osDownloadFile.close();
			        response.close();
			        httpClient.close();
				} else {
					mylib.console(1,"Error en respuesta de API, statusCode: "+codeStatus);
			        osDownloadFile.close();
			        response.close();
			        httpClient.close();
					throw new Exception("Error en respiesta de API, statusCode: "+codeStatus);
				}
			} 

	        final InputStream fis = new FileInputStream(new File(srv.getUploadFolder(zone)+connid+".mp3"));
			mylib.console("Enviando audio via stream...");
			
    			StreamingOutput output = new StreamingOutput() {
                @Override
                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = fis.read(buffer)) != -1){
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.flush();

                }
            };

			
			//ResponseBuilder rb = Response.ok((Object) uploadFile);
			ResponseBuilder rb = Response.ok(output);
			//rb.header("Content-Disposition", "attachment; filename="+connid+".mp3");
			
			rb.header("Content-Disposition", "attachment; filename="+srv.getFname().substring(0, srv.getFname().length()-4)+".mp3");

//			String ZoneMyURL = srv.getZoneURL(connid);
//			String[] temp = ZoneMyURL.split(":");
//			
//			if (!mylib.isNullOrEmpty(temp[1])) {
//				fileDecodedName = srv.getWav(temp[0], temp[1], connid);
//				if (!mylib.isNullOrEmpty(fileDecodedName)) {
//					mylib.console("Decoded File Generated: "+fileDecodedName);
//				} else {
//					mylib.console(1, "No se pudo generar decoded file");
//					errCode = 428;
//					errMesg = "No se pudo generar decoded file";
//				}
//			} else {
//				errCode = 427;
//				errMesg = "No se encontro Wav asociado al connid "+connid;
//			}
//			
//			//Finalizando la Extraccion
//			if (errCode==0) {
//				mylib.console("Finalizando extracción grabacion.");
				
//				InputStream is = new FileInputStream(fileDecodedName);
//		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		        int len;
//		        byte[] buffer = new byte[4096];
//		        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
//		            baos.write(buffer, 0, len);
//		        }
//		        System.out.println("Server size: " + baos.size());
//		        return Response.ok(baos).build();
				
				
//				File file = new File(fileDecodedName);
//				String[] tokens = fileDecodedName.split("/");
//				String onlyFileName = tokens[tokens.length-1];
//				ResponseBuilder response = Response.ok((Object) file);
//				response.header("Content-Disposition", "attachment; filename="+onlyFileName);
//				//return Response.ok().entity("getDecodedWav para connid: "+connid+ " file: "+fileDecodedName).build();
//				return response.build();
//			} else {
//				mylib.console("errCode: "+errCode);
//				mylib.console("errMesg: "+errMesg);
//				mylib.console("No se encontro Wav asociado al connid "+connid);
//				return Response.status(errCode).entity(errMesg).build();
//			}
				
			return rb.build();
		} catch (Exception e) {
			mylib.console(1,"Error general Servicio Rest: "+e.getMessage());
			mylib.console(1,"Abortando Peticion");
			return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}

}
