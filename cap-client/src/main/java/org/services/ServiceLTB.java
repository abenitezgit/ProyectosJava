package org.services;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.model.LoadTable;
import org.model.LoadTableParam;
import org.model.LtbResult;
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
	private Object txResult;
	
	public ServiceLTB(GlobalParams m, LoadTable ltb, MyLogger mylog) {
		this.ltb = ltb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
		this.gParams = m;
	}
	
	private Date fecTask;
	
	public void setFecTask(Date fecTask) {
		this.fecTask = fecTask;
	}
	
	public Object getTxResult() {
		return txResult;
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
			
			String fileName = mylib.parseFnParam(ltb.getLtbFileName(), fecTask);
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
			String tbName = new MetaQuery().parseSqlTableName(ltb.getDbType(), ltb.getDbName(), ltb.getLtbTableName(), ltb.getOwnerName());
			
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
				
				mylog.info("Datos borrados exitosamente de Tabla Destino: "+ltb.getLtbTableName());
			}
			
			
			//Preparando la sentencia de inserción
			dConn.getConnection().setAutoCommit(true);
			StringBuilder cols = new StringBuilder();
			StringBuilder vals = new StringBuilder();
			genColsVals(ltb, cols, vals);
			String strPrep = "insert into "+ tbName + "(" + cols + ") VALUES (" + vals	+ ")";
			PreparedStatement psInsertar = dConn.getConnection().prepareStatement(strPrep);
			mylog.info("Sentencia de inserción de registros a BD Destino completada");

			//Leyendo el archivo de carga
		    List<String> lines = Collections.emptyList();
		    lines = Files.readAllLines(
		    			Paths.get(pathFileName), StandardCharsets.UTF_8);
		    
		    if (!lines.isEmpty()) {
		    	List<Map<String,Object>> lstRows = new ArrayList<>();
		    	
		    	Iterator<String> itr = lines.iterator();

	    		if (ltb.getLtbHeader()==1) {
	    			itr.next();
	    		}
		    	
		    	int rowsRead = 0;
		    	
		    	//Inicia la conformación de la Lista de Filas que serán cargadas
			    while (itr.hasNext()) {
		    		rowsRead++;
		    		
		    		String rowLine = itr.next();

		    		//Si hay definido maxRows, entonces termina la carga
		    		if (ltb.getLtbMaxRows()>0) {
		    			if (rowsRead>ltb.getLtbMaxRows()) {
		    				break;
		    			}
		    		}
		    		
		    		if (ltb.getLtbLoadFixed()==1) {
		    			parseFixedRow(rowLine, lstRows);
		    		} else {
		    			parseDynamicRow(rowLine, lstRows);
		    		}
		    		
			    }
			    
	    		//Inicia Carga de Datos en BD Destino
			    int rowsInserted=0;
	    		for(int i=0; i<lstRows.size(); i++) {
	    			try {
	    				addInsertBatch(lstRows.get(i), psInsertar);
	    				rowsInserted++;
	    			} catch (Exception e) {
						mylog.error("Error insertarndo fila: "+mylib.serializeObjectToJSon(lstRows.get(i), false));
					}
	    		}
	    		
	    		mylog.info("Rows Leídas: "+rowsRead);
	    		mylog.info("Rows Inserted: "+rowsInserted);
	    		mylog.info("Rows Error: "+(rowsRead-rowsInserted));
	    		
	    		LtbResult lrs = new LtbResult();
	    		lrs.setRowsError((rowsRead-rowsInserted));
	    		lrs.setRowsInserted(rowsInserted);
	    		lrs.setRowsRead(rowsRead);
	    		
	    		txResult = lrs;
	    		
	    		exitStatus = true;
		    	
		    } else {
		    	mylog.error("Archivo de carga se encuentra vacío");
		    	throw new Exception("Archivo de carga se encuentra vacío");
		    }
		    
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("execute(): "+e.getMessage());
		}
	}
	
    private void addInsertBatch(Map<String,Object> cols, PreparedStatement psInsertar) throws Exception {
		try {
			
			int nOrder = 1;
			
			for(Map.Entry<String, Object> col : cols.entrySet()) {

				String typeName = col.getValue().getClass().getSimpleName().toUpperCase();
				
				switch (typeName) {
					case "STRING":
						String varcharField = (String) col.getValue();
						psInsertar.setString(nOrder, varcharField);
						break;
					case "NVARCHAR":
						String nVarcharField = (String) col.getValue();
						psInsertar.setNString(nOrder, nVarcharField);
						break;
					case "NCHAR":
						String nCharField = (String) col.getValue();
						psInsertar.setNString(nOrder, nCharField);
						break;
					case "CHAR":
						String charField = (String) col.getValue();
						psInsertar.setString(nOrder, charField);
						break;
					case "INTEGER":
						int intField = (int) col.getValue();
						psInsertar.setInt(nOrder, intField);
						break;
					case "NUMERIC":
						int numericField = (int) col.getValue();
						psInsertar.setInt(nOrder, numericField);
						break;
					default:
						throw new Exception("Error executeUpdate: Tipo de datos de columna no definido: "+ typeName);
				}
				
				nOrder++;
			}
				
			//psInsertar.addBatch();
			psInsertar.execute();
			
		} catch (Exception e) {
			throw new Exception("Error addInsertBatch: "+e.getMessage());
		}
}

	
	private void parseDynamicRow(String rowLine, List<Map<String,Object>> mapRows) throws Exception {
		try {
			Map<String,Object> row = new HashMap<>();
			for (Map.Entry<Integer, LoadTableParam> param : ltb.getMapLtbParam().entrySet()) {
				if (param.getValue().getEnable()==1) {
					String fieldDataType = param.getValue().getTbFieldDataType();
					String fieldName = param.getValue().getTbFieldName();
					int loadOrder = param.getValue().getFileLoadOrder()-1;
					
				    StringTokenizer st = new StringTokenizer(rowLine,ltb.getLtbFileSep());
				    int numTokens = st.countTokens();
				   
				    Object[] fields = new Object[numTokens];
				    
				    for (int i=0; i<numTokens; i++) {
				    	fields[i] = st.nextToken();
				    }

					Object fieldValue;
					if (param.getValue().getTbLoadFromFile()==1) {
						fieldValue = parseField(fields[loadOrder],fieldDataType);
					} else {
						fieldValue = parseField(param.getValue().getTbFieldValue(),fieldDataType);
					}
					row.put(fieldName, fieldValue);
				}
			}
			mapRows.add(row);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private void parseFixedRow(String rowLine, List<Map<String,Object>> mapRows) throws Exception {
		try {
			Map<String,Object> row = new HashMap<>();
			for (Map.Entry<Integer, LoadTableParam> param : ltb.getMapLtbParam().entrySet()) {
				if (param.getValue().getEnable()==1) {
					String fieldDataType = param.getValue().getTbFieldDataType();
					String fieldName = param.getValue().getTbFieldName();
					Object fieldValue;
					if (param.getValue().getTbLoadFromFile()==1) {
						fieldValue = parseField(rowLine.substring(param.getValue().getFilePosIni(), param.getValue().getFilePosFin()),fieldDataType);
					} else {
						fieldValue = parseField(param.getValue().getTbFieldValue(),fieldDataType);
					}
					row.put(fieldName, fieldValue);
				}
			}
			mapRows.add(row);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private Object parseField(Object fieldValue, String type) throws Exception {
		try {
			Object response;
			switch (type) {
				case "VARCHAR":
					String varcharValue = String.valueOf(fieldValue);
					response = varcharValue;
					break;
				case "INTEGER":
					int intValue = Integer.valueOf((String) fieldValue);
					response = intValue;
					break;
				case "DATE":
					Date dateValue = (Date) fieldValue;
					response = dateValue;
					break;
				default:
					response = (String) fieldValue;
					break;
			}
			
			return response;
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
				
				String fileName = mylib.parseFnParam(ltb.getLtbFileName(), fecTask);
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
				
				String fileName = mylib.parseFnParam(ltb.getLtbFileName(), fecTask);
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
	
    private void genColsVals(LoadTable ltb, StringBuilder cols, StringBuilder vals) throws Exception {
		try {
			List<String> lstCols = new ArrayList<>();
			List<String> lstVals = new ArrayList<>();
			for (Map.Entry<Integer, LoadTableParam> params : ltb.getMapLtbParam().entrySet()) {
				//Genera Columnas y Variables Bound
				lstCols.add(params.getValue().getTbFieldName());
				lstVals.add("?");
			}
			cols.append(String.join(",", lstCols));
			vals.append(String.join(",", lstVals));
		} catch (Exception e) {
			throw new Exception("Error genColsVals: "+e.getMessage());
		}
}

}