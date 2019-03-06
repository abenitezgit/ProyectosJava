package org.services;

import java.io.File;
import java.nio.charset.Charset;
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
import org.utilities.MyUtils;

import com.api.FtpAPI2;
import com.api.SFtpAPI;
import com.rutinas.Rutinas;

public class ServiceLTB {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Logger logger;
	MyLogger mylog;
	LoadTable ltb;
	MyUtils utils;
	
	List<String> lstExportFiles = new ArrayList<>();
	private Object txResult;
	
	public ServiceLTB(GlobalParams m, LoadTable ltb, MyLogger mylog) {
		this.ltb = ltb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
		this.gParams = m;
		this.utils = new MyUtils(gParams);
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
			
			String pathFileName = utils.getLocalPath(ltb.getLtbFilePath())+"/"+fileName;
			
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
				mylog.info("Procediendo a limpiar tabla de carga...");
				String delSql;
				if (ltb.getDeleteWhereActive()==1) {
					delSql = "delete from "+tbName+" where "+ltb.getDeleteWhereBody();
				} else {
					delSql = "delete from "+tbName;
				}
				
				//Borra Datos de la Tabla Destino
				mylog.info("Ejecutando script de limpieza: " + delSql);
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
			mylog.info("Leyendo archivo de carga en formato ISO-8859-1...");
		    List<String> lines = Collections.emptyList();
		    
		    Charset charset = Charset.forName("ISO-8859-1");
		    
		    lines = Files.readAllLines(
		    			Paths.get(pathFileName), charset);
		    
		    if (!lines.isEmpty()) {
		    	mylog.info("Archivo de carga leído exitosamenente!!");
		    	List<Map<String,Object>> lstRows = new ArrayList<>();
		    	
		    	Iterator<String> itr = lines.iterator();

	    		if (ltb.getLtbHeader()==1) {
	    			itr.next();
	    		}
		    	
		    	int rowsRead = 0;
		    	int itRead = 0;
		    	int showIt = 2000;
		    	
		    	//Inicia la conformación de la Lista de Filas que serán cargadas
		    	mylog.info("Total de Registros a cargar: "+lines.size());
		    	mylog.info("Iniciando parseo de registros....");
			    while (itr.hasNext()) {
		    		rowsRead++;
		    		itRead++;
		    		
		    		if (itRead==showIt) {
		    			mylog.info("# rows parsing: "+rowsRead+" of " + lines.size());
		    			itRead = 0;
		    		}
		    		
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
			    mylog.info("Iniciando inserción de registros en BD....");
			    int rowsInserted=0;
			    int itRow = 0;
			    int maxRowsBatch = 1000;
			    boolean rowsPending = true;
			    
	    		for(int i=0; i<lstRows.size(); i++) {
	    			try {
	    				itRow ++;
	    				addInsertBatch(cols, lstRows.get(i), psInsertar);
	    				rowsPending = true;
	    				rowsInserted++;
	    				
	    				if (itRow==maxRowsBatch) {
	    					psInsertar.executeBatch();
	    					mylog.info("# rows write: "+rowsInserted+" of " + lines.size());
	    					itRow = 0;
	    					rowsPending = false;
	    				}
	    				
	    			} catch (Exception e) {
						mylog.error("Error insertarndo filas: " + e.getMessage());
					}
	    		}
	    		
	    		if (rowsPending) {
					psInsertar.executeBatch();
					mylog.info("# rows write: "+rowsInserted+" of " + lines.size());
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
	
    private void addInsertBatch(StringBuilder cols,  Map<String,Object> row, PreparedStatement psInsertar) throws Exception {
		try {
			
			String[] columns = cols.toString().split(",");
			
			int nOrder = 1;
			
			/*
			 * 
			 */
			for (int i=0; i<columns.length; i++) {
				if (row.containsKey(columns[i])) {
					String columnName = columns[i];
					Object value = row.get(columns[i]);
					
					String typeName = value.getClass().getSimpleName().toUpperCase();
					
					switch (typeName) {
						case "STRING":
							String varcharField = (String) value;
							psInsertar.setString(nOrder, varcharField.trim());
							break;
						case "NVARCHAR":
							String nVarcharField = (String) value;
							psInsertar.setNString(nOrder, nVarcharField.trim());
							break;
						case "NCHAR":
							String nCharField = (String) value;
							psInsertar.setNString(nOrder, nCharField.trim());
							break;
						case "CHAR":
							String charField = (String) value;
							psInsertar.setString(nOrder, charField.trim());
							break;
						case "INTEGER":
							int intField = (int) value;
							psInsertar.setInt(nOrder, intField);
							break;
						case "NUMERIC":
							int numericField = (int) value;
							psInsertar.setInt(nOrder, numericField);
							break;
						default:
							throw new Exception("addInsertBatch(): Tipo de datos de columna no definido: "+ typeName);
					}
					
					nOrder++;
				}
			}
			
			//psInsertar.execute();
			
			psInsertar.addBatch();
			
			//psInsertar.addBatch();
			//psInsertar.execute();
			
		} catch (Exception e) {
			throw new Exception("addInsertBatch(): "+e.getMessage());
		}
}

	
	private void parseDynamicRow(String rowLine, List<Map<String,Object>> mapRows) throws Exception {
		try {
			Map<String,Object> row = new HashMap<>();
			
		    StringTokenizer st = new StringTokenizer(rowLine,ltb.getLtbFileSep());
		    int numTokens = st.countTokens();
		   
		    Object[] fields = new Object[numTokens];
		    
		    for (int i=0; i<numTokens; i++) {
		    	fields[i] = st.nextToken();
		    }
			
			for (Map.Entry<Integer, LoadTableParam> param : ltb.getMapLtbParam().entrySet()) {
				if (param.getValue().getEnable()==1) {
					String fieldDataType = param.getValue().getTbFieldDataType();
					String fieldName = param.getValue().getTbFieldName();
					int loadOrder = param.getValue().getFileLoadOrder()-1;

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
					int posIni = param.getValue().getFilePosIni()-1;
					int posFin = param.getValue().getFilePosFin();
					
					String substr = "";
					
					try {
						substr = rowLine.substring(posIni, posFin);
					} catch (Exception e) {
						mylog.error("Error parsing substrin("+posIni+","+posFin+") de rowLine: "+rowLine);
						mylog.error(e.getMessage());
					}
					
					Object fieldValue;
					if (param.getValue().getTbLoadFromFile()==1) {
						fieldValue = parseField(substr,fieldDataType);
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
				String localPathFileName = utils.getLocalPath(ltb.getLtbFilePath())+"/"+fileName;
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
				String localPathFileName = utils.getLocalPath(ltb.getLtbFilePath())+"/"+fileName;
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
				if (params.getValue().getEnable()==1) {
					lstCols.add(params.getValue().getTbFieldName());
					lstVals.add("?");
				}
			}
			
			cols.append(String.join(",", lstCols));
			vals.append(String.join(",", lstVals));
			
		} catch (Exception e) {
			throw new Exception("Error genColsVals: "+e.getMessage());
		}
}

}