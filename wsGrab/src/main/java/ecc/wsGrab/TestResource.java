package ecc.wsGrab;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
	

@Path("getAudio")
public class TestResource {

	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getAudio() throws Exception {
		
		
		String url="http://localhost:8080/wsGrab/webapi/getDecodedFile";
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(url);

		JSONObject json = new JSONObject();
		json.put("zone", "2");
		json.put("audioPathFile", "/GrabacionesNV/2017/33/Plataforma_0001/SKILL_V01_ENTELP_TLV_ORN_PROV/20170815_152911_Skill_V01_EntelP_Tlv_ORN_Prov_03CF02A868CB8119_1098_9804949119502.WAV");
		json.put("connid", "03cf02a868cb8119");
		
		StringEntity input = new StringEntity(json.toString());
		input.setContentType("application/json");
		postRequest.setEntity(input);
		
		HttpResponse response = httpClient.execute(postRequest);
		
		
		File downloadFile = new File("/usr/local/hadoop/work/aa.mp3");
		FileOutputStream fosDownloadFile = new FileOutputStream(downloadFile);   	//Puntero para escribir el archivo
		OutputStream osDownloadFile = new BufferedOutputStream(fosDownloadFile);
		
		int codeStatus = response.getStatusLine().getStatusCode();
		InputStream  is = response.getEntity().getContent();
		
        //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
        byte[] bytesArray = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = is.read(bytesArray)) != -1) {
        		osDownloadFile.write(bytesArray, 0, bytesRead);
        }
        
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int len;
//        byte[] buffer = new byte[4096];
//        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
//            baos.write(buffer, 0, len);
//        }


		//response.close();
		httpClient.close();

//		StreamingOutput output = new StreamingOutput() {
//		    @Override
//		    public void write(OutputStream out) throws IOException, WebApplicationException {  
//		        int length;
//		        byte[] buffer = new byte[1024];
//		        while((length = is.read(buffer)) != -1) {
//		            out.write(buffer, 0, length);
//		        }
//		        out.flush();
//		        is.close();
//		    }   
//		};
		
		
//		ResponseBuilder rb = Response.ok( baos, MediaType.APPLICATION_OCTET_STREAM);
//		rb.header("Content-Disposition", "attachment; filename="+"pp.mp3");

		ResponseBuilder rb = Response.ok((Object) downloadFile);
		rb.header("Content-Disposition", "attachment; filename="+"pp.mp3");
		// Copy the stream to the response's output stream.
		//IOUtils.copy(is, baos);

//		return Response.ok(is).header(
//		        "Content-Disposition", "attachment, filename=\"pp2.mp3").build();
		//return Response.ok(baos).build();
        return rb.build();
	}
}
