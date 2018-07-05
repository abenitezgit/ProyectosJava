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

import com.rutinas.CSVUtils;
import com.rutinas.Rutinas;

public class ServiceETB {
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	Logger logger;
	MyLogger mylog;
	ExpTable etb;
	
	public ServiceETB(GlobalParams m, ExpTable etb, MyLogger mylog) {
		this.gParams = m;
		this.etb = etb;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
	}
	
	public boolean execute() throws Exception {
		
		try {
			boolean exitStatus = false;
			
			Map<Integer, List<String>> mapRows = new TreeMap<>();
			
			mylog.info("Extrayendo Registros desde Tabla de Base de Datos cliente...");
			mapRows = getRows();
			
			mylog.info("Total de Registros Leidos desde Base de Datos cliente: "+mapRows.size());
			
			CSVUtils csv = new CSVUtils();
			
			String csvFile = gParams.getAppConfig().getWorkPath()+"/paso.csv";
	        FileWriter writer = new FileWriter(csvFile);
	        		
	        char sep = ',';
	        		
	        for (Map.Entry<Integer, List<String>> entry : mapRows.entrySet()) {
//	        	for (String col : entry.getValue()) {
//	        		mylog.info("row: "+entry.getKey()+" colValue: "+col);
//	        	}
	        	csv.writeLine(writer, entry.getValue(), sep);
	        }
	        
	        writer.close();
			
			exitStatus = true;
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("execute(): "+e.getMessage());
		}
	}
	
	public String genFileName() throws Exception {
		try {
			
			String fileName = mylib.parseFnParam(etb.getEtbFileName());
			String fileExt = etb.getEtbFileExt();
			
			return fileName+"."+fileExt;
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
				if (md.executeQuery(vSql)) {
					ResultSet rs = md.getQuery();
					
					int row = 0;
					
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