package utiles.common.rutinas;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.net.PrintCommandListener;
import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientWav {
	Rutinas mylib = new Rutinas();
	OutputStream outStream;
	InputStream isResponse;
	int exitStatus=0;
	String fileDecodedName;
	String ftpServer;
	String ftpUser;
	String ftpPass;
	String ftpWorkFolder;
	
	public void setConfig(String zone, String filePropertiesPath, String HBProperty) throws Exception {
		try {
			String Zone = "Zon"+zone;
			Properties fileProperties = new Properties();
			
			fileProperties.load(new FileInputStream(filePropertiesPath));
			ftpServer = fileProperties.getProperty(HBProperty+".ftp"+Zone+".server");
			ftpUser = fileProperties.getProperty(HBProperty+".ftp"+Zone+".user");
			ftpPass = fileProperties.getProperty(HBProperty+".ftp"+Zone+".pass");
			ftpWorkFolder = fileProperties.getProperty(HBProperty+".ftp"+Zone+".workFolder");
			
		} catch (Exception e) {
			throw new Exception("FTPClientWav - setConfig - "+e.getMessage());
		}
	}
	
	public boolean validaParams() {
		return true;
	}
	
	public int exitStatus() {
		return exitStatus;
	}
	
	public InputStream getInputStream() throws Exception {
		return isResponse;
	}
	
	public OutputStream getOutputStream() throws Exception {
		return outStream;
	}
	
	public String getDecodedName() {
		return fileDecodedName;
	}
	
    @SuppressWarnings("resource")
	public void getWAVtoMP3(String remotePathFile, String outFileName) {
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;

        try {
        	ftp.setConnectTimeout(2000);
        	ftp.connect(ftpServer);
        	
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new Exception("Ocurrió un problema al intentar conectar con el servidor FTP");
            }

            ftp.login(ftpUser, ftpPass);
            ftp.setFileType(BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            File tempRemoteFile = File.createTempFile("stream", ".wav",new File(ftpWorkFolder));
            File tempDecodedFile;
            if (mylib.isNullOrEmpty(outFileName)) {
            	tempDecodedFile = File.createTempFile("stream-decoded", ".mp3",new File(ftpWorkFolder));
            } else { 
            	tempDecodedFile = new File(ftpWorkFolder+"/"+outFileName);
            }
            
            mylib.console("Bajando WAV: "+remotePathFile);
            mylib.console("Work Directory: "+ftpWorkFolder);
            mylib.console("Wav Stream Name: "+ tempRemoteFile.getName());
            mylib.console("Mp3 Stream Name: "+ tempDecodedFile.getName());
            
            FileOutputStream outRemoteFile = new FileOutputStream(tempRemoteFile);   			//Puntero para escribir el archivo
            
            OutputStream outputRemoteStream = new BufferedOutputStream(outRemoteFile);  		//Buffer de Escritura hacia el puntero del archivo
            InputStream inputRemoteStream = ftp.retrieveFileStream(remotePathFile);     		//Buffer de Lectura desde un puntero de archivo
            
            //Leyendo el buffer de lectura y escribiendo el buffer de escritura - copia en memoria de buffers
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputRemoteStream.read(bytesArray)) != -1) {
            	outputRemoteStream.write(bytesArray, 0, bytesRead);
            }
 
            boolean success = ftp.completePendingCommand();
            if (success) {
                mylib.console("Download successfully.");
            }
            
            //Rutina para conversion del audio
            //Se entrega un buffer de lectura de un archivo : inputRemoteStream
            //Se espera un buffer de escritura de retorno para devolver
            
            mylib.console("Convirtiendo Wav a Mp3...");
            ConvertMP3 convert = new ConvertMP3();
            convert.setConfig(ftpWorkFolder, tempRemoteFile.getName(), tempDecodedFile.getName());
            mylib.console("Se han seteado los parámetros de conversión...");
            if (convert.isValidaParams()) {
            	mylib.console("Se validaron los parámetros de conversión...");
            	int status = convert.convert();
            	mylib.console("Status de conversión...: "+status);
            	if (status==0) {
            		FileInputStream inpStreamDecodedFile = new FileInputStream(new File(tempDecodedFile.getPath())); 
            		InputStream isDecodeFile = new BufferedInputStream(inpStreamDecodedFile);
            		isResponse = isDecodeFile;
            		fileDecodedName = tempDecodedFile.getPath();
            		exitStatus = 0;
            		mylib.console("Audio Convertido exitosamente");
            	} else {
            		exitStatus = 1;
            		mylib.console(1,"Error convirtiendo Wav a Mp3");
            	}
            }
            
            //Borrando archivos temporales
//            tempRemoteFile.deleteOnExit();
//            tempDecodedFile.deleteOnExit();

        } catch (Exception ex) {
        	exitStatus=1;
            ex.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        }
    }
}
