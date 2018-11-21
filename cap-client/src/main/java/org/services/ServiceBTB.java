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

import com.rutinas.CSVUtils;
import com.rutinas.Rutinas;

public class ServiceBTB {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Logger logger;
	MyLogger mylog;
	BackTable btb;
	
	List<String> lstExportFiles = new ArrayList<>();
	String vCols;
	
	public ServiceBTB(GlobalParams m, BackTable btb, MyLogger mylog) {
		this.gParams = m;
		this.btb = btb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
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
				
				String expFilePath = getFilePath();
				
				FileWriter fw = new FileWriter(expFilePath+"/"+genFileName(), false);
				lstExportFiles.add(genFileName());
				
				mylog.info("Exportando a archivo: "+expFilePath+"/"+genFileName());
					
				mylog.info("Recorriendo registros y exportando...");
				for (Map.Entry<Integer, List<String>> entry : mapRows.entrySet()) {
					
					csv.writeLine(fw, entry.getValue(), separator);
					
				}
				
				mylog.info("Registros exportados!");
				
				//Close Writer
				fw.close();
				
				//Generando el archivo de control de carga si es Oracle
				if (btb.getDbType().substring(0, 3).equals("ORA")) {
					FileWriter fwC = new FileWriter(expFilePath+"/"+genControlName(), false);
					
					csv.writeLine(fw, new ArrayList<String>(Arrays.asList("load data")), separator);
					csv.writeLine(fw, new ArrayList<String>(Arrays.asList("infile "+expFilePath+"/"+genFileName())), separator);
					csv.writeLine(fw, new ArrayList<String>(Arrays.asList("into table "+btb.getBtbTableName())), separator);
					csv.writeLine(fw, new ArrayList<String>(Arrays.asList("fields terminated by ',' optionally enclosed by '\"'\n")), separator);
					csv.writeLine(fw, new ArrayList<String>(Arrays.asList("(  "+vCols+")")), separator);
					
					fwC.close();
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
			
			String pathFileName = mylib.parseFnParam(btb.getBtbFileName(), fecTask);
			
			return pathFileName;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public String genControlName() throws Exception {
		try {
			String fileName = mylib.parseFnParam(btb.getBtbFileName(), fecTask);
			
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
				
				vCols = new MetaQuery().getBackColumns(md, btb.getDbType(), btb.getBtbTableName());
				
				String vSql = getVsqlQuery(vCols);
				mylog.info("Ejecutando query: "+vSql);
				
				if (md.executeQuery(vSql)) {
					ResultSet rs = md.getQuery();
					ResultSetMetaData rsm = rs.getMetaData();
					
					int row = 0;
					
					mylog.info("Recuperando registros...");
					while(rs.next()) {
						
						row++;
						
						List<String> cols = new ArrayList<>();
						
						for (int i=0; i<rsm.getColumnCount(); i++) {
							switch(rsm.getColumnType(i)) {
								case java.sql.Types.VARCHAR:
									cols.add(rs.getString(rsm.getColumnLabel(i)));
									break;
								case java.sql.Types.INTEGER:
									cols.add(String.valueOf(rs.getInt(rsm.getColumnLabel(i))));
									break;
								default:
									cols.add(rs.getString(rsm.getColumnLabel(i)));
									break;
							}
						}
						
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