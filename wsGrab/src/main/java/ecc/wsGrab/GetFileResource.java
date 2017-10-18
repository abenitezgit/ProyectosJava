package ecc.wsGrab;

import java.io.File;
import java.io.FileInputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import ecc.services.GetFileService;
import utiles.common.rutinas.ConvertToMP3;
import utiles.common.rutinas.Rutinas;

@Path("getDecodedFile")
public class GetFileResource {
	Rutinas mylib = new Rutinas();
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getNull() {
		return Response.status(429).entity("Operacion no permitida!!!").build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWav(String dataInput) {
		
		try {
			GetFileService srv = new GetFileService();
			
			srv.paseaDataInput(dataInput);
			
			//Inicia Proceso de Recuperacion del archivo de audio
			mylib.console("Iniciando Servicio getDecodedFile");
			mylib.console("Zona a Acceder: "+srv.getZone());
			mylib.console("Nombre audio a Extraer: "+srv.getAudioPathFile());
			mylib.console("Nombre audio a generar: "+srv.getDownFileName());
			
			//Recuperadno valores de archivo de condiguración
			mylib.console("Recuperando parametros de archivo de configuracion");
			srv.getDataConfig(srv.getZone());
			
			//Via FTP recupera el archivo de audio
			mylib.console("Recupera Audio desde Sitio FTP");
			srv.getAudioFTP();
			
			//Genera consercion del audio a MP3
			String fileSource = srv.getDownFileName();  //connid.wav
			String fileDecode = srv.getConnid()+".mp3";
			ConvertToMP3 mp3 = new ConvertToMP3(srv.getWorkFolder(), fileSource, fileDecode);
			mp3.convert();
			
			//Envia audio al cliente
			mylib.console("Devuelve audio al cliente");
			String localPathFile = srv.getWorkFolder()+"/"+srv.getDownFileName();
			
			
//			File file = new File(localPathFile);
//			ResponseBuilder response = Response.ok((Object) file);
//			response.header("Content-Disposition", "attachment; filename="+srv.getDownFileName());

			File file = new File(localPathFile);
			FileInputStream fis = new FileInputStream(file);
			ResponseBuilder response = Response.ok((Object) fis);
			response.header("Content-Disposition", "attachment; filename="+srv.getDownFileName());
			
			mylib.console("Finalizando en forma Exitosa Servicio getDecodedFile");
			
			return response.build();
			
		} catch (Exception e) {
			mylib.console(1,"Error general Servicio getDecodedFile: "+e.getMessage());
			return Response.status(500).entity("Error general ("+e.getMessage()+")").build();
		}
	}

}
