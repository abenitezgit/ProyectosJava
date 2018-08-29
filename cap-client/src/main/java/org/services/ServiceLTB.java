package org.services;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.model.LoadTable;
import org.model.LoadTableParam;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.api.FtpAPI2;
import com.api.SFtpAPI;
import com.rutinas.Rutinas;

public class ServiceLTB {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Logger logger;
	MyLogger mylog;
	LoadTable ltb;
	
	List<String> lstExportFiles = new ArrayList<>();
	
	public ServiceLTB(GlobalParams m, LoadTable ltb, MyLogger mylog) {
		this.ltb = ltb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
		this.gParams = m;
	}
	
	public List<String> getLstExportFiles() {
		return this.lstExportFiles;
	}
	
	public boolean execute() throws Exception {
		
		try {
			boolean exitStatus = false;
			
			mylog.info("Validando conexión a BD Destino...");
			MetaData dConn = new MetaData(ltb.getDbType(), mylog);
			
			dConn.open(ltb.getServerIP(), ltb.getDbName(), ltb.getDbPort(), ltb.getLoginName(), ltb.getLoginPass(), 10);
			
			if (!dConn.isConnected()) {
				mylog.error("No es posible validar conexión a BD Destino: "+ltb.getDbName());
				throw new Exception("No es posible validar conexión a BD Destino: "+ltb.getDbName());
			}
			
			mylog.info("Conexión a BD Destino "+ltb.getDbName()+" es exitosa");
			
			mylog.info("Validando Archivo a Cargar...");
			
			String fileName = mylib.parseFnParam(ltb.getLtbFileName());
			String fileType = ltb.getLtbFileType();
			
			String pathFileName = getLocalFilePath()+"/"+fileName;
			
			if (ltb.getFtpEnable()==1) {
				if (ltb.getFtpSecure()==1) {
					if (getSFtpFiles()) {
						mylog.info("Archivo de carga bajado exitosamente");
					}
				} else {
					if (getSFtpFiles()) {
						mylog.info("Archivo de carga bajado exitosamente");
					}
				}
			}
			
			File myFile = new File(pathFileName);
			if (!(myFile.isFile() && myFile.canRead())) {
				mylog.error("No es posible acceder al Archivo de carga "+pathFileName);
				throw new Exception("No es posible acceder al Archivo de carga "+pathFileName);
			}
			
			//Datos a usar
			String tbName = new MetaQuery().parseSqlTableName(ltb.getDbType(), ltb.getDbName(), ltb.getTbName(), ltb.getOwnerName());
			
			//Valida si hay que borrar datos de destino
			if (ltb.getLtbAppend()==0) {
				String delSql;
				if (ltb.getDeleteWhereActive()==1) {
					delSql = "delete from "+tbName+" where "+ltb.getDeleteWhereBody();
				} else {
					delSql = "delete from "+tbName;
				}
				
				//Borra Datos de la Tabla Destino
				dConn.executeUpdate(delSql);
				
				mylog.info("Datos borrados exitosamente de Tabla Destino: "+ltb.getTbName());
			}
			
			
			//Preparando la sentencia de inserción
			dConn.getConnection().setAutoCommit(false);
			String cols="";
			String vals="";
			genColsVals(ltb, cols, vals);
			String strPrep = "insert into "+ tbName + "(" + cols + ") VALUES (" + vals	+ ")";
			PreparedStatement psInsertar = dConn.getConnection().prepareStatement(strPrep);
			mylog.info("Sentencia de inserción de registros a BD Destino completada");

			//Leyendo el archivo de carga
		    List<String> lines = Collections.emptyList();
		    lines = Files.readAllLines(
		    			Paths.get(pathFileName), StandardCharsets.UTF_8);
		    
		    if (!lines.isEmpty()) {
		    	Iterator<String> itr = lines.iterator();

	    		if (ltb.getLtbAppend()==1) {
	    			itr.hasNext();
	    		}
		    	
		    	int rowsRead = 0;
		    	
			    while (itr.hasNext()) {
		    		rowsRead++;
		    		
		    		itr.next();

		    		//Si hay definido maxRows, entonces termina la carga
		    		if (ltb.getLtbMaxRows()>0) {
		    			if (rowsRead>ltb.getLtbMaxRows()) {
		    				break;
		    			}
		    		}
			    }
		    	
		    	
		    } else {
		    	mylog.error("Archivo de carga se encuentra vacío");
		    	throw new Exception("Archivo de carga se encuentra vacío");
		    }
		    
		    
			
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("execute(): "+e.getMessage());
		}
	}
	
	public void gen(LoadTable ltb, String line) throws Exception {
		try {
			if (ltb.getLtbLoadFixed()==1) {
				
			}
			
			for(Map.Entry<Integer, LoadTableParam> param : ltb.getMapLtbParam().entrySet()) {
				if (param.getValue().getTbLoadFromFile()==1) {
					if (ltb.getLtbLoadFixed()==1) {
						String fieldValue = line.substring(param.getValue().getFilePosIni(),param.getValue().getFilePosFin());
					} else {
						String fieldValue = 
					}
				}
				
				
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean getFtpFiles() throws Exception {
		final int FTP_STATUS_OK = 220;
		
		try {
			boolean exitStatus = false;
			
			FtpAPI2 ftp = new FtpAPI2();
			
			ftp.setHostIP(ltb.getFtpServerIP());
			ftp.setUserName(ltb.getFtpUserName());
			ftp.setUserPass(ltb.getFtpUserPass());
			ftp.setConnectTimeout(10000);
			
			mylog.info("Conectando a Sitio FTP: "+ltb.getFtpServerIP()+ " ("+ltb.getFtpUserName()+")");
			ftp.connect();
			
			mylog.info("Estado de conexión: "+ ftp.getReplyCode()+" "+ftp.getReplyString());
			
			if (ftp.getReplyCode()==FTP_STATUS_OK) {
				mylog.info("Conectado exitosamente a sitio ftp");
				
				mylog.info("Bajando archivo de carga del sitio ftp...");
				
				String fileName = mylib.parseFnParam(ltb.getLtbFileName());
				String localPathFileName = getLocalFilePath()+"/"+fileName;
				String remotePathFileName = getRemoteFilePath()+"/"+fileName;
				
				mylog.info("Bajando archivo de carga en : "+localPathFileName);
				ftp.download(remotePathFileName, localPathFileName);
				
				if (ftp.getReplyCode()==FTP_STATUS_OK) {
					mylog.info("Download Success: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					exitStatus = true;
				} else {
					mylog.error("Download ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					throw new Exception("Download ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				}
				ftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				throw new Exception("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("getFtpFiles(): "+e.getMessage());
		}
	}
	
	public boolean getSFtpFiles() throws Exception {
		try {
			boolean exitStatus = false;
			
			SFtpAPI sftp = new SFtpAPI();
			
			sftp.setServerIP(ltb.getFtpServerIP());
			sftp.setUserName(ltb.getFtpUserName());
			sftp.setUserPass(ltb.getFtpUserPass());
			
			mylog.info("Conectando a Sitio SFTP: "+ltb.getFtpServerIP()+ " ("+ltb.getFtpUserName()+")");
			sftp.connect();
			
			if (sftp.isConnect()) {
				mylog.info("Conectado exitosamente a sitio sftp");
				
				mylog.info("Bajando archivo del sitio sftp...");
				
				String fileName = mylib.parseFnParam(ltb.getLtbFileName());
				String localPathFileName = getLocalFilePath()+"/"+fileName;
				String remotePathFileName = getRemoteFilePath()+"/"+fileName;
				
				mylog.info("Bajando Archivo en: "+localPathFileName);
				if (sftp.download(remotePathFileName, localPathFileName)) {
					mylog.info("Download Success");
					exitStatus = true;
				} else {
					mylog.error("Download ERROR");
					throw new Exception("Download ERROR");
				}
				sftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP");
				throw new Exception("Error conectandose al sitio FTP");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("getSFtpFiles(): "+e.getMessage());
		}
	}
	
	public String getLocalFilePath() {
		String filePath=gParams.getAppConfig().getWorkPath();
		
		if (!mylib.isNullOrEmpty(ltb.getLtbFilePath())) {
			filePath = ltb.getLtbFilePath();
		}
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0,filePath.length()-1);
		}
		return filePath;
	}

	public String getRemoteFilePath() {
		String filePath=gParams.getAppConfig().getWorkPath();
		
		if (!mylib.isNullOrEmpty(ltb.getFtpRemotePath())) {
			filePath = ltb.getFtpRemotePath();
		}
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0,filePath.length()-1);
		}
		return filePath;
	}
	
    private void genColsVals(LoadTable ltb, String cols, String vals) throws Exception {
		try {
			List<String> lstCols = new ArrayList<>();
			List<String> lstVals = new ArrayList<>();
			for (Map.Entry<String, LoadTableParam> params : ltb.getMapLtbParam().entrySet()) {
				//Genera Columnas y Variables Bound
				lstCols.add(params.getValue().getLtbFieldName());
				lstVals.add("?");
			}
			cols = String.join(",", lstCols);
			vals = String.join(",", lstVals);
		} catch (Exception e) {
			throw new Exception("Error genColsVals: "+e.getMessage());
		}
}

}