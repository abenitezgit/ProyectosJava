package ecc.wsGrab;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.rutinas.Rutinas;

import ecc.services.ExtractDataService;
import ecc.services.GrabService;
import ecc.utiles.GlobalArea;

import org.apache.log4j.Logger;

@Path("getWav")
public class ExtractDataID {
	Logger logger = Logger.getLogger("wsGrab");
	GlobalArea gDatos = new GlobalArea();
	ExtractDataService eds = new ExtractDataService(gDatos);
	GrabService srvGrab;
	Rutinas mylib = new Rutinas();
	
	public ExtractDataID() {
		srvGrab = new GrabService(gDatos);
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getNull() {
		logger.info("Inicio Consulta por GET");
		logger.info("Debe ingresar valor de un ID, devolviendo respuesta...");
		return Response.status(429).entity("Debe ingresar valor de un ID").build();
	}
	
	
	@GET
	@Path("/{ID}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWav(@PathParam("ID") String ID) {
		try {
			logger.info("Iniciando extraccion grabacion para el ID: "+ID);
			
			logger.info("Inicializando componentes");
			srvGrab.initComponent();
			
			logger.info("Recuperando datos del Connid...");
			
			//Conecta a SOLR para extraer información requerida
			eds.getDataID(ID);
			
			//Recupera parametros requeridos
			int zone = eds.getZone();
			String urlAudio = eds.getUrlAudio();
			String audioPathFile = eds.getFtpDir()+eds.getFname();
			int rstorage = eds.getRstorage();
			
			//Visualiza datos recuperados
			logger.info("Zona Ubicacion Audios: "+zone);
			logger.info("urlGetDecodedAudio: "+urlAudio);
			logger.info("AudioPathFile: "+audioPathFile);
			logger.info("rstorage: "+rstorage);
			
			//Ejecuta Nueva API getDecodedFile
			logger.info("Ejecutando API Rest getDecodedFile URL: "+urlAudio);
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost postRequest = new HttpPost(urlAudio);

			JSONObject json = new JSONObject();
			json.put("zone", zone);
			json.put("audioPathFile", audioPathFile);
			json.put("ID", ID);
			json.put("rstorage", rstorage);
			
			//StringEntity setContentType
			
			StringEntity input = new StringEntity(json.toString());
			input.setContentType("application/json");
			postRequest.setEntity(input);
			
			CloseableHttpResponse response;
			try {
				response = httpClient.execute(postRequest);
			} catch (Exception e) {
				logger.error("Error ejecutando API: "+urlAudio);
				throw new Exception("Error ejecutando API: "+urlAudio+" :"+e.getMessage());
			}
			
			logger.info("Analizando Respuesta de la API...");
			
			File downloadFile = new File(eds.getUploadFolder(zone)+ID+".mp3");
			FileOutputStream fosDownloadFile = new FileOutputStream(downloadFile);   	//Puntero para escribir el archivo
			OutputStream osDownloadFile = new BufferedOutputStream(fosDownloadFile);
			
			int codeStatus = response.getStatusLine().getStatusCode();
			
			InputStream  is;
			
			if (codeStatus==200) {
				logger.info("Respuesta exitosa desde API");
				logger.info("Recuperando Stream de audio...");

				is = response.getEntity().getContent();
				
		        //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
		        byte[] bytesArray = new byte[4096];
		        int bytesRead = -1;
		        while ((bytesRead = is.read(bytesArray)) != -1) {
		        		osDownloadFile.write(bytesArray, 0, bytesRead);
		        }
		        
		        logger.info("Stream de audio recupeado");
		        logger.info("Cerrando conexiones de API");
		        
		        osDownloadFile.close();
		        response.close();
		        httpClient.close();
			} else {
				logger.error("Error en respuesta de API, statusCode: "+codeStatus);
		        osDownloadFile.close();
		        response.close();
		        httpClient.close();
				throw new Exception("Error en respiesta de API, statusCode: "+codeStatus);
			}

			logger.info("Audio a reproducir: "+eds.getUploadFolder(zone)+ID+".mp3");
			logger.info("Enviando audio via stream...");
			File file = new File(eds.getUploadFolder(zone)+ID+".mp3");
			FileInputStream fis = new FileInputStream(file);
			//ResponseBuilder rb = Response.ok((Object) uploadFile);
			ResponseBuilder rb = Response.ok((Object) fis);
			//rb.header("Content-Disposition", "attachment; filename="+connid+".mp3");
			logger.info("Audio informado en Header del request: "+eds.getFname().substring(0, eds.getFname().length()-4)+".mp3");
			rb.header("Content-Disposition", "attachment; filename="+eds.getFname().substring(0, eds.getFname().length()-4)+".mp3");

				
			return rb.build();
		} catch (Exception e) {
			logger.error("Error general Servicio Rest: "+e.getMessage());
			logger.error("Abortando Peticion");
			return Response.status(500).entity("Error ("+e.getMessage()+")").build();
		}
	}

}
