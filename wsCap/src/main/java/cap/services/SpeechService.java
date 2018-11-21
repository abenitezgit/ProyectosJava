package cap.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.api.MysqlAPI;
import com.rutinas.Rutinas;

import cap.utiles.GlobalParams;

public class SpeechService {
	Logger logger = Logger.getLogger("SpeechService");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	
	/*
	 * Recursos de la clase
	 */
	String audioName;
	long audioSize; //Bytes
	
	public SpeechService(GlobalParams m) {
		this.gParams = m;
	}
	
	public boolean inscribeAudio(String id, int swNew) throws Exception {
		try {
			boolean exitStatus = false;
			
			MysqlAPI dbConn = new MysqlAPI(gParams.getConfig().getDbHostName(),
											gParams.getConfig().getDbName(),
											gParams.getConfig().getDbPort(),
											gParams.getConfig().getDbUser(),
											gParams.getConfig().getDbPass(),
											gParams.getConfig().getDbTimeOut());
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = "call sp_add_speechRequest('"+id+"',"+swNew+")";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs!=null) {
						if (rs.next()) {
							String strResp = rs.getString("resp");
							JSONObject jResp = new JSONObject(strResp);
							if (jResp.getString("status").equals("OK")) {
								exitStatus = true;
							}
						}
					}
				}
			} 
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("inscribeAudio(): "+e.getMessage());
		}
	}

	public boolean getAudio(String id) throws Exception {
		try {
			
			String server = "10.240.8.15";
			String port = "8080";
			String urlBase = "/wsGrab/webapi/getWav";
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
				
				InputStream is = response.getEntity().getContent();
				StringTokenizer tokens = new StringTokenizer(id,"+");
				
				String strID = "";
				while (tokens.hasMoreElements()) {
					strID = strID + tokens.nextToken();
				}
				
				File downloadFile = new File("/usr/local/capProject/work/"+strID+".mp3");
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
					System.out.println("header: "+h.getName()+ " : "+h.getValue());
					if (h.getName().equals("Content-Disposition")) {
						CD = h.getValue();
					}
				}
		
				response.close();
				httpClient.close();
				
				audioName = strID+".mp3";
				File audio = new File("/usr/local/capProject/work/"+audioName);
				audioSize = audio.length();  //Bytes
				
		        logger.info("Retornando Archivo adjunto");
		        
			} else {
				logger.error("Error ejecutando request code: "+codeStatus);
			}

			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
