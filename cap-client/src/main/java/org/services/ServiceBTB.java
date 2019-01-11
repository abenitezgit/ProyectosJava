package org.services;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.model.BackTable;
import org.utilities.GlobalParams;
import org.utilities.MyLogger;
import org.utilities.MyUtils;

import com.rutinas.CSVUtils;
import com.rutinas.Rutinas;

public class ServiceBTB {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Logger logger;
	MyLogger mylog;
	BackTable btb;
	MyUtils utils;
	
	List<String> lstExportFiles = new ArrayList<>();
	String vCols;
	List<String> readCols = new ArrayList();
	
	public ServiceBTB(GlobalParams m, BackTable btb, MyLogger mylog) {
		this.gParams = m;
		this.btb = btb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
		this.utils = new MyUtils(gParams);
	}
	
	private Date fecTask;
	
	public void setFecTask(Date fecTask) {
		this.fecTask = fecTask;
	}
	
	public List<String> getLstExportFiles() {
		return this.lstExportFiles;
	}
	
	public boolean execute() throws Exception {
		
		try {
			boolean exitStatus = false;
			
			Map<Integer, List<String>> mapRows = new TreeMap<>();
			
			mylog.info("Iniciando Backup de datos del cliente...");
			mapRows = getRows();
			
			mylog.info("Total de Registros Leidos desde Base de Datos cliente: "+mapRows.size());

			if (exportCSV(mapRows)) {
				mylog.info("Archivo exportado exitosamente");
				exitStatus = true;
			} else {
				mylog.error("Archivo no pudo ser exportado");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("execute(): "+e.getMessage());
		}
	}
	
	public boolean exportCSV(Map<Integer, List<String>> mapRows) {
		try {
			boolean exitStatus = false;
			
			mylog.info("Inicio Exportación de Datos...");
			
			mylog.info("Registros a Exportar: "+mapRows.size());

			if (mapRows.size()>0) {
				//Separator
				char separator = "|".charAt(0);
				mylog.info("Default Separador: "+separator);
												
				CSVUtils csv = new CSVUtils();
				
				String localPath = utils.getLocalPath(btb.getBtbFilePath());
				String fileName = genFileName();
				String controlName = genControlName(fileName);
				String localPathFile = localPath + "/" + fileName;
				String localPathControl = localPath + "/" + controlName;
				
				FileWriter fw = new FileWriter(localPathFile, false);
				lstExportFiles.add(genFileName());
				
				mylog.info("Exportando a archivo: "+localPathFile);
					
				mylog.info("Recorriendo registros y exportando...");
				for (Map.Entry<Integer, List<String>> entry : mapRows.entrySet()) {
					
					csv.writeLine(fw, entry.getValue(), separator);
					
				}
				
				mylog.info("Registros exportados Exitosamente!");
				
				//Close Writer
				fw.close();
				
				//Generando el archivo de control de carga si es Oracle
				if (btb.getDbType().substring(0, 3).equals("ORA")) {
					
					mylog.info("Generando archivo de control...");
					
					FileWriter fwC = new FileWriter(localPathControl, false);
					
					csv.writeLine(fwC, new ArrayList<String>(Arrays.asList("load data")), separator);
					csv.writeLine(fwC, new ArrayList<String>(Arrays.asList("infile "+localPath+"/"+genFileName())), separator);
					csv.writeLine(fwC, new ArrayList<String>(Arrays.asList("into table "+btb.getBtbTableName())), separator);
					csv.writeLine(fwC, new ArrayList<String>(Arrays.asList("fields terminated by '|' ")), separator);
					csv.writeLine(fwC, new ArrayList<String>(Arrays.asList("(  "+String.join(",", readCols)+")")), separator);
					
					fwC.close();
					
					mylog.info("Archivo de control generado exitosamenre!");
				}
				
				exitStatus = true;
			} else {
				mylog.info("No hay datos a exportar, No se generara archivo vació");
				exitStatus = true;
			}
			
			
			mylog.info("Termino Exitoso de Backup de Datos");
			return exitStatus;
		} catch (Exception e) {
			mylog.error("exportCSV: "+e.getMessage());
			return false;
		} 
	}
	
	public String getFilePath() {
		String filePath=gParams.getAppConfig().getWorkPath();
		
		if (!Objects.isNull(btb.getBtbFilePath()) && !btb.getBtbFilePath().isEmpty()) {
			filePath = btb.getBtbFilePath();
		}
		
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0,filePath.length()-1);
		}
		return filePath;
	}
	
	public String genFileName() throws Exception {
		try {
			
			String pathFileName = mylib.parseFnParam(btb.getBtbFileName(), fecTask)+".back";
			
			return pathFileName;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public String genControlName(String fileName) throws Exception {
		try {
			String simpleName = fileName.substring(0, fileName.lastIndexOf("."));
			
			String pathFileName = simpleName+".ctl";
			
			return pathFileName;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public String getVsqlQuery(String vCols) throws Exception {
		try {
			MetaQuery mq = new MetaQuery(btb.getDbType());
			
			StringBuilder vSql = new StringBuilder();

			//Conforma el Select
			vSql.append("select ");
			vSql.append(vCols);
			vSql.append(" from ");
			vSql.append(mq.parseSqlTableName(btb.getDbType(), btb.getDbName(), btb.getBtbTableName(), btb.getOwnerName()));
			
			if (btb.getBtbWhere_active()==1) {
				vSql.append(" where ");
				vSql.append(btb.getBtbWhere_body());
			}
			
			return vSql.toString();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<Integer, List<String>> getRows() throws Exception {
		try {
			Map<Integer, List<String>> mapRows = new TreeMap<>();
			
			mylog.info("Conectando a Base de datos del cliente...");
			MetaData md = new MetaData(btb.getDbType(), mylog);
			
			md.open(btb.getServerIP(), btb.getDbName(), btb.getDbPort(), btb.getLoginName(), btb.getLoginPass(), 10);
			
			if (md.isConnected()) {
				
				//vCols = new MetaQuery().getBackColumns(md, btb.getDbType(), btb.getBtbTableName());
				vCols = " * ";
				
				String vSql = getVsqlQuery(vCols);
				mylog.info("Ejecutando query: "+vSql);
				
				if (md.executeQuery(vSql)) {
					ResultSet rs = md.getQuery();
					ResultSetMetaData rsm = rs.getMetaData();
					
					int row = 0;
					int numCols = rsm.getColumnCount();
					boolean isFirstRow = true;
					
					mylog.info("Recuperando registros...");
					while(rs.next()) {
						
						row++;
						
						List<String> cols = new ArrayList<>();
						
						for (int i=1; i<rsm.getColumnCount(); i++) {
							
							String colName = rsm.getColumnLabel(i);
							if (isFirstRow) {
								readCols.add(colName);
							}
							String columnType = rsm.getColumnTypeName(i);
							switch(rsm.getColumnType(i)) {
								case java.sql.Types.VARCHAR:
									if (!mylib.isNullOrEmpty(rsm.getColumnLabel(i))) {
										cols.add(rs.getString(rsm.getColumnLabel(i)));
									} else {
										cols.add("");
									}
									break;
								case java.sql.Types.INTEGER:
									if (!mylib.isNullOrEmpty(rsm.getColumnLabel(i))) {
										try {
											cols.add(String.valueOf(rs.getInt(rsm.getColumnLabel(i))));
										} catch (Exception e) {
											try {
												cols.add(String.valueOf(rs.getLong(rsm.getColumnLabel(i))));
											} catch (Exception e2) {
												try {
													cols.add(String.valueOf(rs.getFloat(rsm.getColumnLabel(i))));
												} catch (Exception e3) {
													cols.add("");
													mylog.error("Error row: "+ row+ " col: " + rsm.getColumnLabel(i) + " - " + e3.getMessage() );
												}
											}
										}
									} else {
										cols.add("");
									}
									break;
								case java.sql.Types.NUMERIC:
									if (!mylib.isNullOrEmpty(rsm.getColumnLabel(i))) {
										try {
											cols.add(String.valueOf(rs.getInt(rsm.getColumnLabel(i))));
										} catch (Exception e) {
											try {
												cols.add(String.valueOf(rs.getLong(rsm.getColumnLabel(i))));
											} catch (Exception e2) {
												try {
													cols.add(String.valueOf(rs.getFloat(rsm.getColumnLabel(i))));
												} catch (Exception e3) {
													cols.add("");
													mylog.error("Error row: "+ row+ " col: " + rsm.getColumnLabel(i) + " - " + e3.getMessage() );
												}
											}
										}
									} else {
										cols.add("");
									}
									break;
								case java.sql.Types.FLOAT:
									if (!mylib.isNullOrEmpty(rsm.getColumnLabel(i))) {
										try {
											cols.add(String.valueOf(rs.getFloat(rsm.getColumnLabel(i))));
										} catch (Exception e) {
											try {
												cols.add(String.valueOf(rs.getInt(rsm.getColumnLabel(i))));
											} catch (Exception e2) {
												try {
													cols.add(String.valueOf(rs.getLong(rsm.getColumnLabel(i))));
												} catch (Exception e3) {
													cols.add("");
													mylog.error("Error row: "+ row+ " col: " + rsm.getColumnLabel(i) + " - " + e3.getMessage() );
												}
											}
										}
									} else {
										cols.add("");
									}
									break;
								default:
									if (!mylib.isNullOrEmpty(rsm.getColumnLabel(i))) {
										cols.add(rs.getString(rsm.getColumnLabel(i)));
									} else {
										cols.add("");
									}
									break;
							}
						}
						//mylog.info("row: "+ row + " " + mylib.serializeObjectToJSon(cols, false));
						
						mapRows.put(row, cols);
						isFirstRow = false;
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