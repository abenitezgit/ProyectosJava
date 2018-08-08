package ecc.wsGrab;

import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

//import com.api.ConvertToMP3;
import com.rutinas.Rutinas;

import ecc.services.GetFileService;
import ecc.services.GrabService;
import ecc.utiles.GlobalArea;

@Path("getDecodedFile")
public class GetFileResource {
	Logger logger = Logger.getLogger("wsGrab");
	GlobalArea gDatos = new GlobalArea();
	GetFileService srv;
	GrabService srvGrab;
	Rutinas mylib = new Rutinas();
	
	public GetFileResource( ) {
		srvGrab = new GrabService(gDatos);
		srv = new GetFileService(gDatos);
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getNull() {
		return Response.status(429).entity("Operacion no permitida!!!").build();
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWav(String dataInput) {
		
		try {
			logger.info("Iniciando Request geDocededFile");
			logger.info("Data Input getDecodedFile: "+dataInput);
			
			srv.paseaDataInput(dataInput);
			
			logger.info("Inicianlizando componentes");
			srvGrab.initComponent();
			
			//Inicia Proceso de Recuperacion del archivo de audio
			logger.info("Iniciando Servicio getDecodedFile");
			logger.info("Zona a Acceder: "+srv.getZone());
			logger.info("Nombre audio a Extraer: "+srv.getAudioPathFile());
			logger.info("Nombre audio a generar: "+srv.getDownFileName());
			
			//Recuperadno valores de archivo de condiguración
			logger.info("Recuperando parametros de archivo de configuracion");
			srv.getDataConfig(srv.getZone(), srv.getRstorage());
			
			//Via FTP recupera el archivo de audio
			logger.info("Recupera Audio desde Sitio FTP");
			srv.getAudioFTP();
			
			//Genera convercion del audio a MP3
			String fileSource = srv.getDownFileName();  //connid.wav
			
			StringTokenizer tokens = new StringTokenizer(srv.getID(),"+");
			
			String strID="";
			
			while (tokens.hasMoreTokens()) {
				strID = strID+tokens.nextToken();
			}
			
			String fileDecode = strID+".mp3";
			
			logger.info("Inicia concersion de Audio...");
			ConvertToMP3 mp3 = new ConvertToMP3(srv.getWorkFolder(), fileSource, fileDecode);
			mp3.convert();
			
			//Envia audio al cliente
			logger.info("Devuelve audio al cliente");
			//String localPathFile = srv.getWorkFolder()+"/"+srv.getDownFileName();
			String localPathFile = srv.getWorkFolder()+"/"+fileDecode;
			
			
//			File file = new File(localPathFile);
//			ResponseBuilder response = Response.ok((Object) file);
//			response.header("Content-Disposition", "attachment; filename="+srv.getDownFileName());

			
			File file = new File(localPathFile);
			FileInputStream fis = new FileInputStream(file);
			ResponseBuilder response = Response.ok((Object) fis);
			response.header("Content-Disposition", "attachment; filename="+fileDecode);

			logger.info("getWorkFolder: "+srv.getWorkFolder());
			logger.info("fileSource: "+fileSource);
			logger.info("fileCode: "+fileDecode);
			logger.info("getDownFileName: "+srv.getDownFileName());
			logger.info("localPathFile: "+localPathFile);
			logger.info("getDownFileName: "+srv.getDownFileName());
			
			logger.info("Finalizando en forma Exitosa Servicio getDecodedFile");
			
			return response.build();
			
		} catch (Exception e) {
			logger.error("Error general Servicio getDecodedFile: "+e.getMessage());
			return Response.status(500).entity("Error general ("+e.getMessage()+")").build();
		}
	}

}
