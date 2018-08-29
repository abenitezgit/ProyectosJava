package org.services;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.model.ExpTable;
import org.model.ExpTableParam;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;

import com.api.FtpAPI2;
import com.api.SFtpAPI;
//import com.api.FtpAPI;
import com.rutinas.CSVUtils;
import com.rutinas.Rutinas;

public class ServiceETB {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Logger logger;
	MyLogger mylog;
	ExpTable etb;
	
	List<String> lstExportFiles = new ArrayList<>();
	
	public ServiceETB(GlobalParams m, ExpTable etb, MyLogger mylog) {
		this.gParams = m;
		this.etb = etb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
	}
	
	public List<String> getLstExportFiles() {
		return this.lstExportFiles;
	}
	
	public boolean execute() throws Exception {
		
		try {
			boolean exitStatus = false;
			
			Map<Integer, List<String>> mapRows = new TreeMap<>();
			
			mylog.info("Extrayendo Registros desde Tabla de Base de Datos cliente...");
			mapRows = getRows();
			
			mylog.info("Total de Registros Leidos desde Base de Datos cliente: "+mapRows.size());

			if (exportCSV(mapRows)) {
				mylog.info("Archivo exportado exitosamente");
				
				if (etb.getFtpEnable()==1) {
					mylog.info("Enviando archivo generado por FTP...");
					
					for (String file : lstExportFiles) {
						mylog.info("Archivo a Exportar: "+file);
					}
					
					if (etb.getFtpSecure()==0) {
						if (sendFtpFiles()) {
							mylog.info("Archivos enviados Exitosamente");
							exitStatus = true;
						} else {
							mylog.error("Archivos no pudieron se enviados por FTP");
						}
					} else {
						if (sendSFtpFiles()) {
							mylog.info("Archivos enviados Exitosamente");
							exitStatus = true;
						} else {
							mylog.error("Archivos no pudieron se enviados por FTP");
						}
					}
				} else {
					exitStatus = true;
				}
				
			} else {
				mylog.error("Archivo no pudo ser exportado");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("execute(): "+e.getMessage());
		}
	}
	
	public boolean sendFtpFiles() throws Exception {
		final int FTP_STATUS_OK = 220;
		
		try {
			boolean exitStatus = false;
			
			FtpAPI2 ftp = new FtpAPI2();
			
			ftp.setHostIP(etb.getFtpServerIP());
			ftp.setUserName(etb.getFtpUserName());
			ftp.setUserPass(etb.getFtpUserPass());
			ftp.setConnectTimeout(10000);
			
			mylog.info("Conectando a Sitio FTP: "+etb.getFtpServerIP()+ " ("+etb.getFtpUserName()+")");
			ftp.connect();
			
			mylog.info(ftp.getReplyCode()+" "+ftp.getReplyString());
			
			if (ftp.getReplyCode()==FTP_STATUS_OK) {
				mylog.info("Conectado exitosamente a sitio ftp");
				
				mylog.info("Enviando archivos a sitio ftp...");
				
				for (String file : lstExportFiles) {
					String localPathFileName = gParams.getAppConfig().getWorkPath()+"/"+file;
					String remotePathFileName = etb.getFtpRemotePath()+"/"+file;
					
					mylog.info("Uploading file: "+localPathFileName);
					ftp.upload(remotePathFileName, localPathFileName);
					
					if (ftp.getReplyCode()==FTP_STATUS_OK) {
						mylog.info("Upload Success: "+ftp.getReplyCode()+" "+ftp.getReplyString());
						exitStatus = true;
					} else {
						mylog.error("Upload ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
						throw new Exception("Upload ERROR: "+ftp.getReplyCode()+" "+ftp.getReplyString());
					}
				}
				ftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
				throw new Exception("Error conectandose al sitio FTP: "+ftp.getReplyCode()+" "+ftp.getReplyString());
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("sendFtpFiles(): "+e.getMessage());
		}
	}
	
	public boolean sendSFtpFiles() throws Exception {
		try {
			boolean exitStatus = false;
			
			SFtpAPI sftp = new SFtpAPI();
			
			sftp.setServerIP(etb.getFtpServerIP());
			sftp.setUserName(etb.getFtpUserName());
			sftp.setUserPass(etb.getFtpUserPass());
			
			mylog.info("Conectando a Sitio SFTP: "+etb.getFtpServerIP()+ " ("+etb.getFtpUserName()+")");
			sftp.connect();
			
			if (sftp.isConnect()) {
				mylog.info("Conectado exitosamente a sitio sftp");
				
				mylog.info("Enviando archivos a sitio sftp...");
				
				for (String file : lstExportFiles) {
					String localPathFileName = gParams.getAppConfig().getWorkPath()+"/"+file;
					String remotePathFileName = etb.getFtpRemotePath()+"/"+file;
					
					mylog.info("Uploading file: "+localPathFileName);
					if (sftp.upload(remotePathFileName, localPathFileName)) {
						mylog.info("Upload Success");
						exitStatus = true;
					} else {
						mylog.error("Upload ERROR");
						throw new Exception("Upload ERROR");
					}
				}
				sftp.disconnect();
				
				exitStatus = true;
			} else {
				mylog.error("Error conectandose al sitio FTP");
				throw new Exception("Error conectandose al sitio FTP");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("sendFtpFiles(): "+e.getMessage());
		}
	}
	
	public boolean exportCSV(Map<Integer, List<String>> mapRows) {
		try {
			boolean exitStatus = false;
			
			mylog.info("Inicio Exportación a CSV...");
			
			mylog.info("Registros a Exportar: "+mapRows.size());

			if (mapRows.size()>0) {
				//Separator
				char separator = etb.getEtbFileSep().charAt(0);
				mylog.info("Separador: "+separator);
				
				//Lista para cabecera de nombres de columnas
				List<String> headers = new ArrayList<>();
				
				//Control de multiples files
				int numFile = 1;
				int itNumRowMultiFiles = 0;
				int maxRowsMultiFiles = etb.getEtbMaxRows_multiFiles();
				
				//Valida si son multiples files
				if (etb.getEtbMultiFiles()==1) {
					
					mylog.info("Exportación de Multiples Archivos...");
					if (maxRowsMultiFiles<=0) {
						//Se generara un default de 5000
						maxRowsMultiFiles = 5000;
					}
				} 
				
				CSVUtils csv = new CSVUtils();
				
				String expFilePath = getFilePath();
				
				FileWriter fw = new FileWriter(expFilePath+"/"+genFileName(numFile), etb.getEtbAppend()==1);
				lstExportFiles.add(genFileName(numFile));
				
				mylog.info("Exportando a archivo: "+expFilePath+"/"+genFileName(numFile));
					
				if (etb.getEtbHeader()==1) {
					mylog.info("Generando cabecera de nombres de columnas...");
					headers = genHeaderFile();
					csv.writeLine(fw, headers, separator);
				} else {
					mylog.info("Exportación SIN headers");
				}
				
				mylog.info("Recorriendo registros y exportando...");
				for (Map.Entry<Integer, List<String>> entry : mapRows.entrySet()) {
					itNumRowMultiFiles ++;
					
					if (etb.getEtbMultiFiles()==1) {
						if (itNumRowMultiFiles > maxRowsMultiFiles) {
							fw.close();
							numFile++;
							fw = new FileWriter(expFilePath+"/"+genFileName(numFile), etb.getEtbAppend()==1);
							lstExportFiles.add(genFileName(numFile));
							
							mylog.info("Exportando a archivo: "+genFileName(numFile));
							
							if (etb.getEtbHeader()==1) {
								mylog.info("Generando cabecera de nombres de columnas...");
								headers = genHeaderFile();
								csv.writeLine(fw, headers, separator);
							}
							itNumRowMultiFiles = 0;
						}
					}
					
					csv.writeLine(fw, entry.getValue(), separator);
					
				}
				
				mylog.info("Registros exportados!");
				
				//Close Writer
				fw.close();
				
				exitStatus = true;
			} else {
				if (etb.getEtbGetEmptyFile()==1) {
					mylog.info("Generando archivo vacío: "+genFileName(0));
					genEmptyFile();
				} else {
					mylog.info("No hay datos a exportar, No se generara archivo vació");
				}
				exitStatus = true;
			}
			
			
			mylog.info("Termino Exitoso de exportacion a CSV");
			return exitStatus;
		} catch (Exception e) {
			mylog.error("exportCSV: "+e.getMessage());
			return false;
		} 
	}
	
	public List<String> genHeaderFile() throws Exception {
		try {
			List<String> headers = new ArrayList<>();
			
			Map<String, ExpTableParam> mapParams = new TreeMap<>(etb.getMapEtbParam());
			
			for (Map.Entry<String, ExpTableParam> entry : mapParams.entrySet()) {
				if (!mylib.isNullOrEmpty(entry.getValue().getEtbFieldLabel())) {
					headers.add(entry.getValue().getEtbFieldLabel());
				} else {
					headers.add(entry.getValue().getEtbFieldName());
				}
			}
			return headers;
		} catch (Exception e) {
			throw new Exception("genHeaderFile: "+e.getMessage());
		}
	}
	
	public String getFilePath() {
		String filePath=gParams.getAppConfig().getWorkPath();
		
//		if (!mylib.isNullOrEmpty(etb.getEtbFilePath())) {
//			filePath = etb.getEtbFilePath();
//			filePath.replace("\\", "/");
//		}
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0,filePath.length()-1);
		}
		return filePath;
	}
	
	public void genEmptyFile() throws Exception {
		try {
			FileWriter fw = new FileWriter(genFileName(0), false);
			fw.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String genFileName(int numFile) throws Exception {
		try {
			String pathFileName="";
			
			String fileName = mylib.parseFnParam(etb.getEtbFileName());
			
			if (etb.getEtbMultiFiles()==0) {
				pathFileName = fileName;
			} else {
				pathFileName = fileName+"_"+String.format("%05d", numFile);
			}
			
			return pathFileName;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String genOrderBody() throws Exception {
		try {
			StringBuilder strOrder = new StringBuilder();
			
			if (etb.getEtbOrder_active()==1) {
				strOrder.append(" order by ");
				strOrder.append(etb.getEtbOrder_body());
			}
			
			return strOrder.toString();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String genWhereBody() throws Exception {
		try {
			StringBuilder strWhere = new StringBuilder();
			
			if (etb.getEtbWhere_active()==1) {
				strWhere.append(" where ");
				strWhere.append(etb.getEtbWhere_body());
			}
			
			return strWhere.toString();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String genQueryFields() throws Exception {
		try {
			StringBuilder strFields = new StringBuilder();
			
			boolean isFirst = true;
			
			if (etb.getMapEtbParam().size()>0) {
				for (Map.Entry<String, ExpTableParam> entry : etb.getMapEtbParam().entrySet()) {
					
					if (!isFirst) {
						strFields.append(",");
					} else {
						isFirst = false;
					}
						
					
					switch(entry.getValue().getEtbFieldType()) {
						case "FIELD":
							if (!mylib.isNullOrEmpty(entry.getValue().getEtbFieldLabel())) {
								strFields.append(entry.getValue().getEtbFieldLabel());
							} else {
								strFields.append(entry.getValue().getEtbFieldName());
							}
							break;
						case "VARCHAR":
							strFields.append("'"+entry.getValue().getEtbFieldName()+"'");
							break;
						case "INTEGER":
							strFields.append(entry.getValue().getEtbFieldName());
							break;
					}
				}
			}
			
			return strFields.toString();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String getVsqlQuery() throws Exception {
		try {
			MetaQuery mq = new MetaQuery(etb.getDbType());
			
			String response = "";
			
			response = mq.getStringBuilderQuery(genQueryFields(),
										etb.getOwnerName(),
										etb.getEtbTableName(),
										genWhereBody(),
										genOrderBody(),
										etb.getEtbMaxRows());
			
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<Integer, List<String>> getRows() throws Exception {
		try {
			Map<Integer, List<String>> mapRows = new TreeMap<>();
			Map<String, ExpTableParam> mapEtbParam = new TreeMap<>(etb.getMapEtbParam());
			
			mylog.info("Conectando a Base de datos del cliente...");
			MetaData md = new MetaData(etb.getDbType(), mylog);
			
			md.open(etb.getDbServerIP(), etb.getDbName(), etb.getDbPort(), etb.getLoginName(), etb.getLoginPass(), 10);
			
			if (md.isConnected()) {
				
				String vSql = getVsqlQuery();
				mylog.info("Ejecutando query: "+vSql);
				
				if (md.executeQuery(vSql)) {
					ResultSet rs = md.getQuery();
					
					int row = 0;
					
					mylog.info("Recuperando registros...");
					while(rs.next()) {
						
						row++;
						
						List<String> cols = new ArrayList<>();
						
						for (Map.Entry<String, ExpTableParam> entry : mapEtbParam.entrySet()) {
							
							switch(entry.getValue().getEtbDataType()) {
								case "VARCHAR":
									if (!mylib.isNullOrEmpty(entry.getValue().getEtbFieldLabel())) {
										cols.add(rs.getString(entry.getValue().getEtbFieldLabel()));
									} else {
										cols.add(rs.getString(entry.getValue().getEtbFieldName()));
									}
									break;
								case "INTEGER":
									if (!mylib.isNullOrEmpty(entry.getValue().getEtbFieldLabel())) {
										cols.add(String.valueOf(rs.getInt(entry.getValue().getEtbFieldLabel())));
									} else {
										cols.add(String.valueOf(rs.getInt(entry.getValue().getEtbFieldName())));
									}
									break;
								default:
									if (!mylib.isNullOrEmpty(entry.getValue().getEtbFieldLabel())) {
										cols.add(rs.getString(entry.getValue().getEtbFieldLabel()));
									} else {
										cols.add(rs.getString(entry.getValue().getEtbFieldName()));
									}
									break;
							} //End switch
							
						} //End for
						
						mapRows.put(row, cols);
					} //End while
				}
				
				md.close();
			}
			
			mylog.info("Terminando acceso a la base de datos del cliente");
			
			return mapRows;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}