package org.services;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.model.Mov;
import org.model.MovMatch;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ServiceMOV {
	MetaQuery mq = new MetaQuery();
	Logger logger;
	MyLogger mylog;
	Rutinas mylib = new Rutinas();
	Mov mov;
	
	private int statusCode=0;
	private String statusMesg="";
	PreparedStatement psInsertar = null;
	PreparedStatement psUpdate = null;
	
	public ServiceMOV(Mov mov, MyLogger mylog) {
		this.mov = mov;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
	}
	
	public boolean execute() throws Exception {
		try {
			boolean exitStatus = false;
			
    		MetaData sConn = new MetaData(mov.getsDbType(), mylog);
    		MetaData dConn = new MetaData(mov.getdDbType(), mylog);

    		//Analizando configuración de MOV
    		
    		mylog.info("Base de Datos Origen ("+mov.getsDbType()+"): "+mov.getsDbName());
    		mylog.info("Tabla Origen: "+mov.getsTbName());
    		mylog.info("Origen hostName: "+mov.getsIP());
    		mylog.info("Origen dbName: "+mov.getsDbName());
    		mylog.info("Origen dbPort: "+mov.getsDbPort());
    		mylog.info("Origen dbUser: "+mov.getsLoginName());

    		mylog.info("Base de Datos Destino ("+mov.getdDbType()+"): "+mov.getdDbName());
    		mylog.info("Tabla Destino: "+mov.getdTbName());
    		mylog.info("Destino hostName: "+mov.getdIP());
    		mylog.info("Destino dbName: "+mov.getdDbName());
    		mylog.info("Destino dbPort: "+mov.getdDbPort());
    		mylog.info("Destino dbUser: "+mov.getdLoginName());
			
    		//Crea los componentes de acceso a los datos
    		
    		mylog.info("Estableciendo conexión a BD Origen...");
    		sConn.open(mov.getsIP().trim(), mov.getsDbName().trim(), mov.getsDbPort().trim(), mov.getsLoginName().trim(), mov.getsLoginPass().trim(), 10);
    		
    		if (sConn.isConnected()) {
    			mylog.info("Conexión Exitosa a BD Origen!");
    		} else {
    			mylog.error("No es posible conectarse a BD Origen");
    			throw new Exception("No es posible conectarse a BD Origen");
    		} 
    		
    		mylog.info("Estableciendo conexión a BD Destino...");
    		dConn.open(mov.getdIP().trim(), mov.getdDbName().trim(), mov.getdDbPort().trim(), mov.getdLoginName().trim(), mov.getdLoginPass().trim(), 10);
    		
    		if (dConn.isConnected()) {
    			mylog.info("Conexión Exitosa a BD Destino!");
    		} else {
    			mylog.error("No es posible conectarse a BD Destino");
    			throw new Exception("No es posible conectarse a BD Destino");
    		} 
    		
    		if (mov.getAppendable()==0) {
    			/**
    			 * Inicia borrado de tabla destino
    			 */
    			mylog.info("Iniciando Borrado de Tabla destino: "+mq.parseDestTable(mov));
    			String vSqlDelete = mq.genSqlDeleteTableDest(mov);
    			
    			mylog.info("Query de Borrado tabla destino: "+vSqlDelete);
    			mylog.info("Ejecutando Query de Borrado...");
    			
    			int statDelete = dConn.executeUpdate(vSqlDelete);
    			
    			mylog.info("Codigo retorno query borrado: "+statDelete);
    		} else {
    			mylog.info("Modo Append habilitado, se agrarán registros nuevos a la tabla destino: "+mov.getdTbName());
    		}
    		
    		//Generando Query de Extraccióm
    		String vQuery = getQueryExtraccion(mov);
    		
    		mylog.info("Query Extraccion: "+vQuery);
    		
    		//Generando Ejecucion de consulta a BD Origen
    		int rowsRange = mov.getMaxRowsRange();
    		int rowsRead = 0;
    		int rowsLoad = 0;
    		int rowsError = 0;
    		long maxPctError = mov.getMaxPctError();
    		int maxRowsError = mov.getMaxRowsError();
    		int rowsItera=0;
    		
    		mylog.info("Limite de Filas a Extraer: " + mov.getMaxRows());
    		mylog.info("Maximas Filas permitidas con error: " + maxRowsError);
    		mylog.info("Maximo porcentaje de filas permitidas con error: "+ maxPctError);
    		mylog.info("Numero de filas en Batch Inserción: "+rowsRange);
    		if (mov.getRollbackError()==1) {
    			mylog.info("Rollback Habilitado");
    		} else {
    			mylog.info("Rollback Deshabilitado");
    			mylog.info("AutoCommit cada "+ rowsRange+ " filas cargadas");
    		}
    		
    		mylog.info("Ejecutando Query de Extracción...");
    		if (sConn.executeQuery(vQuery)) {
    			mylog.info("Extraccion BDSource Exitosa");
    			//Recupera el ResultSet de Ejecucion
    			ResultSet rs = sConn.getQuery();
    			ResultSetMetaData rsmd = rs.getMetaData();
				StringBuilder cols = new StringBuilder();
				StringBuilder vals = new StringBuilder();
				String updKeys = "";
				
				genColsVals(rsmd, cols, vals);
				if (mov.getsFieldUpdateActive()==1) {
					updKeys = genUpdKeys(mov);
					sConn.getConnection().setAutoCommit(false);
					psUpdate = sConn.getConnection().prepareStatement("update "+ mq.parseSourceTable(mov) + " set " + mov.getsFieldUpdateName() + " = "+ mov.getsFieldValueUpdate() + " where "+updKeys);
					mylog.info("Sentencia de actualización de registros extraidos completada");
				}
				
				dConn.getConnection().setAutoCommit(false);
				String strPrep = "insert into "+ mq.parseDestTable(mov) + "(" + cols + ") VALUES (" + vals	+ ")";
				psInsertar = dConn.getConnection().prepareStatement(strPrep);
				mylog.info("Sentencia de inserción de registros a BD Destino completada");
    			
				mylog.info("Iniciando Inserciónn de registros en tabla destino");
    			while (rs.next()) {
    				rowsRead++;
    				rowsItera++;
    				
    				//Realiza la inserción del rango definido de resgistros por batch
    				if (rowsItera==rowsRange) {
    					try {
    						psInsertar.executeBatch();
    						if (mov.getsFieldUpdateActive()==1) {
    							psUpdate.executeBatch();
    						}
    						if (mov.getRollbackError()==0) {
    							if (mov.getsFieldUpdateActive()==1) {
    								sConn.getConnection().commit();
    							}
    							dConn.getConnection().commit();
    						}
    						rowsLoad = rowsLoad + rowsItera;
    						mylog.info("Filas recorridas		: "+rowsRead);
    						mylog.info("Nuevas filas insertadas: "+rowsItera);
    						mylog.info("Total filas insertadas	: "+rowsLoad);
	    					rowsItera=0;
    					} catch (Exception e) {
    						mylog.error("No se han podido insertar "+rowsItera+" nuevos registros en base destino: "+e.getMessage());
    						mylog.info("Filas recorridas		: "+rowsRead);
    						mylog.info("Filas Abortadas      	: "+rowsItera);
	    					rowsError = rowsError + rowsItera;
	    					rowsItera=0;
    						continue;
    					}
    					
    				} else {
    					addInsertBatch(rsmd, rs, mov);
    					if (mov.getsFieldUpdateActive()==1) {
    						addUpdateBatch(psUpdate, rs, mov);
    					}
    				}
    			}

    			//Analiza si existen datos que aun no se han insertado
				if (rowsItera>0) {
					
					try {
						psInsertar.executeBatch();
						if (mov.getsFieldUpdateActive()==1) {
							psUpdate.executeBatch();
						}
						if (mov.getRollbackError()==0) {
							if (mov.getsFieldUpdateActive()==1) {
								sConn.getConnection().commit();
							}
							dConn.getConnection().commit();
						}
						rowsLoad = rowsLoad + rowsItera;
						mylog.info("Filas recorridas		: "+rowsRead);
						mylog.info("Nuevas filas insertadas: "+rowsItera);
					} catch (Exception e) {
						mylog.error("No se han podido insertar "+rowsItera+" nuevos registros en base destino: "+e.getMessage());
						mylog.info("Filas recorridas		: "+rowsRead);
						mylog.info("Filas Abortadas		: "+rowsItera);
    					rowsError = rowsError + rowsItera;
    					rowsItera=0;
					}
				} 

    			//Analiza PCT Error
				if (maxPctError>0) {
					long pctError = (rowsError/rowsRead)*100;
					if (pctError>=maxPctError) {
						statusCode = 99;
						statusMesg = "Ha excesido el "+maxPctError+"% de error permitido, encontrando un "+pctError+"% de error de carga";
					}
				} else {
					//Analiza Maximo de Filas de errores cargadas
					if (rowsError>=maxRowsError) {
						statusCode = 99;
						statusMesg = "Ha excesido el maximo("+maxRowsError+") de registros con error permitido, encontrando "+rowsError+" registros de error de carga";
					}
				}
    			
				if (statusCode==0) {
					if (mov.getsFieldUpdateActive()==1) {
						sConn.getConnection().commit();
					}
					dConn.getConnection().commit();
				} else {
				if (mov.getRollbackError()==1) {
					if (mov.getsFieldUpdateActive()==1) {
						sConn.getConnection().rollback();
					}
					dConn.getConnection().rollback();
					rowsLoad=0;
				}
				}
    		} else {
    			statusCode = 99;
    			statusMesg = "No pudo ejecutar Query de Extraccion";
    		}

    		mylog.info("finalizando proceso de carga...");

    		if (statusCode==0) {
    			exitStatus = true;
	    		
    			mylog.info("Total Filas Recorridas	: "+rowsRead);
    			mylog.info("Total Filas Cargadas	: "+rowsLoad);
    			mylog.info("Total Filas Error		: "+rowsError);
    			mylog.info("Finalizando SUCCESS Ejecución MOV: "+mov.getMovID());
    			
    		} else {

    			mylog.info("Total Filas Recorridas	: "+rowsRead);
    			mylog.info("Total Filas Cargadas	: "+rowsLoad);
    			mylog.info("Total Filas Error		: "+rowsError);
    			mylog.error("Codigo Error: "+statusCode+ " Mesg: "+statusMesg);
    			mylog.error("Finalizando con ERROR Ejecución MOV: "+mov.getMovID());
    		}
    		return exitStatus;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
	}
			
	    private String getQueryExtraccion(Mov mov) throws Exception {
    		try {
    			String sourceTableName = mq.parseSourceTable(mov);
    			
    			String strvSql="";
    			
    			StringBuilder strFields = new StringBuilder();
    			
    			List<MovMatch> lstMovMatch = new ArrayList<>();
    			
    			lstMovMatch = mov.getLstMovMatch();
    			
    			if (lstMovMatch.size()>0) {
    				
    				//Inicia el select
    				//Se agrega al final por si hay maxrow definidos
    				//strFields.append("select ");
    				
    				//Extrae los campos
    				for (int i=0; i<lstMovMatch.size(); i++) {
    					strFields.append(parseaSourceField(lstMovMatch.get(i)));
    					if (i<lstMovMatch.size()-1) {
    						strFields.append(",");
    					}
    				}
    				
    				//Genera el from
    				strFields.append(" from "+ sourceTableName);
    				
    				//Genera los where
    				if (mov.getWhereActive()==1) {
    					strFields.append(" where ");
    					strFields.append(mov.getWhereBody());
    				}
    				
    				//Valida si habilita campo de extraccion por valor
    				if (mov.getsFieldUpdateActive()==1) {
        				if (mov.getWhereActive()==1) {
        					strFields.append(" and ");
        				} else {
        					strFields.append(" where ");
        				}
        				strFields.append(mov.getsFieldUpdateName()+ " = ");
        				switch(mov.getsFieldValueType()) {
	        				case "varchar":
	        					strFields.append("'"+mov.getsFieldValueRead()+"'");
	        					break;
	        				case "int":
	        					strFields.append(mov.getsFieldValueRead());
	        					break;
	        				default:
	        					//varchar
	        					strFields.append("'"+mov.getsFieldValueRead()+"'");
	        					break;
        				}
    				}
    				
    				//Habilita Fechas de Extraccion
    				if (mov.getFecExtActive()==1) {
    					if (mov.getWhereActive()==1) {
    						strFields.append(" and ");
    					} else {
    						if (mov.getsFieldUpdateActive()==1) {
    							strFields.append(" and ");
    						} else {
    							strFields.append(" where ");
    						}
    					}
    					
    					//Fecha Ini
    					strFields.append(" (");
    					
    					if (mov.getFecExtIniIn()==1) {
    						strFields.append(mov.getFecExtField()+" >= "+parseaFecha(mov.getFecExtIni(), mov.getFecExtEpoch(), mov.getsDbType()));
    					} else {
    						strFields.append(mov.getFecExtField()+" > "+parseaFecha(mov.getFecExtIni(), mov.getFecExtEpoch(), mov.getsDbType()));
    					}
    					
    					strFields.append(" and ");
    					
    					//Fecha Fin
    					if (mov.getFecExtFinIn()==1) {
    						strFields.append(mov.getFecExtField()+" <= "+parseaFecha(mov.getFecExtFin(), mov.getFecExtEpoch(), mov.getsDbType()));
    					} else {
    						strFields.append(mov.getFecExtField()+" < "+parseaFecha(mov.getFecExtFin(), mov.getFecExtEpoch(), mov.getsDbType()));
    					}
    					
    					strFields.append(") ");
    				}
    				
    				//Analiza si hay maxrow
    				strvSql = appendSelect(mov, strFields.toString());
    				
    			} else {
    				mylog.error("Error getQueryExtraccion: No hay Campos para generar consulta");
    				throw new Exception("Error getQueryExtraccion: No hay Campos para generar consulta");
    			}
    			return strvSql;
    		} catch (Exception e) {
    			throw new Exception("Error getQueryExtraccion: "+e.getMessage());
    		}
    }


	    private void genColsVals(ResultSetMetaData rsmd, StringBuilder cols, StringBuilder vals) throws Exception {
    		try {
    			for (int i=1; i<=rsmd.getColumnCount(); i++) {
    				//Genera Columnas y Variables Bound
    				cols.append(rsmd.getColumnLabel(i));
    				vals.append("?");
    				
    				if (i<rsmd.getColumnCount()) {
    					cols.append(",");
    					vals.append(",");
    				}
    			}
    		} catch (Exception e) {
    			throw new Exception("Error genColsVals: "+e.getMessage());
    		}
    }
	    
	    private String genUpdKeys(Mov mov) throws Exception {
			try {
				String updKeys="";
				String[] cols = mov.getsFieldUpdateKey().split(",");
				//No es necesario validar la existencia de los campos del resultset
				//ya que este podria no contener los campos que se usan como key para actualizar el registro
				
				for (int i=0; i<cols.length; i++) {
					String[] colType = cols[i].split(":");
					updKeys = updKeys + colType[0] + " = ?";
					if ((i<cols.length-1)) {
						updKeys = updKeys + " and ";
					}
				}
				mylog.info("updkey: "+updKeys);
				return updKeys;
			} catch (Exception e) {
				statusCode=99;
				statusMesg="Error updKeys: "+e.getMessage();
				throw new Exception(statusMesg);
			}
	    }

	    private void addInsertBatch(ResultSetMetaData rsmd, ResultSet rs, Mov mov) throws Exception {
    		try {
    			for (int i=1; i<=rsmd.getColumnCount(); i++) {
    				int colType = rsmd.getColumnType(i);
    				String typeName = JDBCType.valueOf(colType).getName(); 
    				String strField = "";
    				int intField = 0;
    				
    				switch (typeName) {
	    				case "VARCHAR":
	    					strField = rs.getString(rsmd.getColumnLabel(i));
	    					psInsertar.setString(i, strField);
	    					break;
	    				case "NVARCHAR":
	    					strField = rs.getNString(rsmd.getColumnLabel(i));
	    					psInsertar.setNString(i, strField);
	    					break;
	    				case "NCHAR":
	    					strField = rs.getNString(rsmd.getColumnLabel(i));
	    					psInsertar.setNString(i, strField);
	    					break;
	    				case "CHAR":
	    					strField = rs.getString(rsmd.getColumnLabel(i));
	    					psInsertar.setString(i, strField);
	    					break;
	    				case "INTEGER":
	    					intField = rs.getInt(rsmd.getColumnLabel(i));
	    					psInsertar.setInt(i, intField);
	    					break;
	    				case "DATE":
	    					psInsertar.setDate(i, rs.getDate(rsmd.getColumnLabel(i)));
	    					break;
	    				case "TIMESTAMP":
	    					psInsertar.setTimestamp(i, rs.getTimestamp(rsmd.getColumnLabel(i)));
	    					break;
	    				case "NUMERIC":
	    					psInsertar.setInt(i, rs.getInt(rsmd.getColumnLabel(i)));
	    					break;
	    				default:
	    					throw new Exception("Error executeUpdate: Tipo de datos de columna no definido: "+ JDBCType.valueOf(colType).getName());
    				}
    			}
    			
    			psInsertar.addBatch();
    			
    		} catch (Exception e) {
    			throw new Exception("Error genColsVals: "+e.getMessage());
    		}
    }

	    private void addUpdateBatch(PreparedStatement psUpdate, ResultSet rs, Mov mov) throws Exception {
			try {
				String[] cols = mov.getsFieldUpdateKey().split(",");

				for (int i=0; i<cols.length; i++) {
					String[] colType = cols[i].split(":");
					
					switch (colType[1]) {
	    				case "VARCHAR":
	    					psUpdate.setString(i+1, rs.getString(colType[0]));
	    					break;
	    				case "NVARCHAR":
	    					psUpdate.setNString(i+1, rs.getNString(colType[0]));
	    					break;
	    				case "NCHAR":
	    					psUpdate.setNString(i+1, rs.getNString(colType[0]));
	    					break;
	    				case "CHAR":
	    					psUpdate.setString(i+1, rs.getString(colType[0]));
	    					break;
	    				case "INTEGER":
	    					psUpdate.setInt(i+1, rs.getInt(colType[0]));
	    					break;
	    				case "DATE":
	    					psUpdate.setDate(i+1, rs.getDate(colType[0]));
	    					break;
	    				default:
	    					throw new Exception("Error addUpdateBatch: Tipo de datos de columna no definido: "+ colType[1]);
					}
				}
				
				psUpdate.addBatch();
				
			} catch (Exception e) {
				statusCode=99;
				statusMesg="Error addUpdateBatch: "+e.getMessage();
				throw new Exception(statusMesg);
			}
	    }

	    private String parseaSourceField(MovMatch mm) throws Exception {
    		try {
    			String field="";
    			 
    			switch(mm.getFieldType()) {
	    			case "FIELD":
    					field = mm.getSourceField() + " as "+ mm.getDestField();
	    				break;
	    			case "VARCHAR":
	    				field = "'"+mm.getSourceField()+"'" + " as "+ mm.getDestField();
	    				break;
	    			case "INTEGER":
	    				field = mm.getSourceField()+ " as "+ mm.getDestField();
	    				break;
	    			case "NULL":
	    				field = "null" + " as "+ mm.getDestField();
	    				break;
	    			default:
	    				throw new Exception("parseaSourceField(): fielType no definido");
    			}
    			return field;
    		} catch (Exception e) {
    			throw new Exception("parseaSourceField(): "+e.getMessage());
    		}
    }

	    private String parseaFecha(String fecParse, int epochTime, String dbType) throws Exception {
    		try {
    			Calendar calendar = Calendar.getInstance();
    			String strYear = "";
    			String strMonth = "";
    			String strDay = "";
    			String strHour = "";
    			String strMin = "";
    			String strSeg = "";
    			
    			String[] strCad = fecParse.split("&"); 
    			for (int i=0; i< strCad.length; i++) {
    				calendar = Calendar.getInstance();
    				String[] f = strCad[i].split(":");
    				switch(f[0]) {
    				case "Y":
    					if (f[1].substring(0, 1).equals("t")) {
    						strYear = String.format("%04d",Integer.valueOf(f[1].substring(1)));
    					} else {
    						int yearsBack = Integer.valueOf(f[1]);
    						calendar.add(Calendar.YEAR, yearsBack*(-1));
    						strYear = String.format("%04d",calendar.get(Calendar.YEAR));
    					}
    					break;
    				case "M":
    					if (f[1].substring(0, 1).equals("t")) {
    						strMonth = String.format("%02d",Integer.valueOf(f[1].substring(1)));
    					} else {
    						int monthsBack = Integer.valueOf(f[1]);
    						calendar.add(Calendar.MONTH, monthsBack*(-1));
    						int month = calendar.get(Calendar.MONTH)+1;
    						strMonth = String.format("%02d",month);
    					}
    					break;
    				case "D":
    					if (f[1].substring(0, 1).equals("t")) {
    						strDay = String.format("%02d",Integer.valueOf(f[1].substring(1)));
    					} else {
    						int daysBack = Integer.valueOf(f[1]);
    						calendar.add(Calendar.DAY_OF_MONTH, daysBack*(-1));
    						strDay = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));
    					}
    					break;
    				case "H":
    					if (f[1].substring(0, 1).equals("t")) {
    						strHour = String.format("%02d",Integer.valueOf(f[1].substring(1)));
    					} else {
    						int hoursBack = Integer.valueOf(f[1]);
    						calendar.add(Calendar.HOUR_OF_DAY, hoursBack*(-1));
    						strHour = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY));
    					}
    					break;
    				case "MI":
    					if (f[1].substring(0, 1).equals("t")) {
    						strMin = String.format("%02d",Integer.valueOf(f[1].substring(1)));
    					} else {
    						int minsBack = Integer.valueOf(f[1]);
    						calendar.add(Calendar.MINUTE, minsBack*(-1));
    						strMin = String.format("%02d",calendar.get(Calendar.MINUTE));
    					}
    					break;
    				case "S":
    					if (f[1].substring(0, 1).equals("t")) {
    						strSeg = String.format("%02d",Integer.valueOf(f[1].substring(1)));
    					} else {
    						int segsBack = Integer.valueOf(f[1]);
    						calendar.add(Calendar.SECOND, segsBack*(-1));
    						strSeg = String.format("%02d",calendar.get(Calendar.SECOND));
    					}
    					break;
    				default:
    					break;
    				}
    			}
    			
//    			String strFecha = strYear+strMonth+strDay+strHour+strMin+strSeg;
//    			SimpleDateFormat formatterTemp = new SimpleDateFormat(formatGen.toString());
//    			Date dateFecha = formatterTemp.parse(strFecha);
//    			calendar = Calendar.getInstance();
//    			calendar.setTime(dateFecha);
    			
    			String returnFecha = "";
    			
    			if (epochTime==1) {
    				returnFecha = String.valueOf(calendar.getTimeInMillis()/1000);
    			} else {
    				switch(dbType) {
    				case "mySQL":
    					returnFecha = "STR_TO_DATE('"+strDay+"/"+strMonth+"/"+strYear+" "+strHour+":"+strMin+":"+strSeg+"','%d/%m/%Y %T')";
    					break;
    				case "SQL":
    					returnFecha = "convert(datetime,'"+strYear+"-"+strMonth+"-"+strDay+" "+strHour+":"+strMin+":"+strSeg+"',120)";
    					break;
    				case "ORA":
    					returnFecha = "to_date('"+strDay+"/"+strMonth+"/"+strYear+" "+strHour+":"+strMin+":"+strSeg+"','dd/mm/rrrr hh24:mi:ss')";
    					break;
    				case "ORA11":
    					returnFecha = "to_date('"+strDay+"/"+strMonth+"/"+strYear+" "+strHour+":"+strMin+":"+strSeg+"','dd/mm/rrrr hh24:mi:ss')";
    					break;
    				}
    			}
    			
    			return returnFecha;
    		} catch (Exception e) {
    			throw new Exception("Error parseaFecIniFin: "+e.getMessage());
    		}
    }

	    private String appendSelect(Mov mov, String strFields) throws Exception {
    		try {
    			String dbType = mov.getsDbType().substring(0, 3);
    			
    			String strSelect = "";
				switch(dbType) {
				case "mySQL":
					if (mov.getMaxRows()!=-1) {
						strSelect = "select "+strFields+" limit "+mov.getMaxRows();
					} else {
						strSelect = "select "+strFields;
					}
					break;
				case "SQL":
					if (mov.getMaxRows()!=-1) {
						strSelect = "select top "+mov.getMaxRows()+" "+strFields;
					} else {
						strSelect = "select "+strFields;
					}
					break;
				case "ORA":
					if (mov.getMaxRows()!=-1) {
						if (mov.getWhereActive()==1) {
							strSelect = "select "+strFields+" and rownum<="+mov.getMaxRows();
						} else {
							strSelect = "select "+strFields+" where rownum<="+mov.getMaxRows();
						}
					} else {
						strSelect = "select "+strFields;
					}
					break;
				case "ORA11":
					if (mov.getMaxRows()!=-1) {
						if (mov.getWhereActive()==1) {
							strSelect = "select "+strFields+" and rownum<="+mov.getMaxRows();
						} else {
							strSelect = "select "+strFields+" where rownum<="+mov.getMaxRows();
						}
					} else {
						strSelect = "select "+strFields;
					}
					break;
				default:
					strSelect = "select "+strFields;
					break;
				}
    			return strSelect;
    		} catch (Exception e) {
    			throw new Exception("Error getSelectQuery: "+e.getMessage());
    		}
    }

}
