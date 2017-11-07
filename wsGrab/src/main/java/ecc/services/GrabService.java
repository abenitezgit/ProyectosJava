package ecc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.common.params.ModifiableSolrParams;

import dataAccess.HBaseDB;
import dataAccess.SolRDB;
import ecc.model.DataRequest;
import ecc.model.GrabMin;
import ecc.model.Grabacion;
import ecc.utiles.GlobalArea;
import utiles.common.rutinas.Rutinas;

public class GrabService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataRequest dr = new DataRequest();

	public void initComponent() throws Exception {
		
		throw new Exception("Error inicializando componentes");
	}
	
	public int getLimitRows() {
		return dr.getLimit();
	}
	
    public void fillDataRequest(String dataInput) throws Exception {
        dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    }
    
    public Map<String, Grabacion> getGrabaciones(int tipoConsulta) throws Exception {
    	SolRDB solrConn = new SolRDB();
    	try {
    	    
    	    Map<String, Grabacion> mapGrab = new HashMap<>();
    	    
    	    solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties());
    	    solrConn.open();
    	    
    	    if (solrConn.isConnected()) {
    	    	mylib.console("Conectado a solR");
    	    	
    	    	ModifiableSolrParams parameters = buildSolrFilters(tipoConsulta);
    	    	mylib.console("Se recuperaron los filtros para solR");
    	    	
    	    	List<String> keys = new ArrayList<>();
    	    	mylib.console("Recuperando Ids de grabaciones");
    	    	keys = solrConn.getIds(parameters);
    	    	solrConn.close();
    	    	
    	    	mylib.console("Se encontraron "+keys.size()+ " ids de grabaciones");
    	    	
    	    	for (String key : keys) {
    	    		mylib.console("key: "+key);
    	    	}
    	    	
    	    	if (keys.size()>0) {
    	            //Consulta Datos a HBase
    	            HBaseDB hbConn = new HBaseDB();
    	            hbConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties(),"grabaciones");
    	            
    	            Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
    	            
    	            Table table = conn.getTable(TableName.valueOf("grabaciones"));
    	            
    	            mylib.console("Conectado a HBase");
    	            
    	            Grabacion grabacion;
    	    	
    	            mylib.console("Recuperando rows desde Hbase");
	    	    	for (String key : keys) {
	    	    		mylib.console("get key: "+key);
	    	            Get g = new Get(Bytes.toBytes(key));
	    	            Result rs = table.get(g);
	    	            
	    	            grabacion = new Grabacion();
	    	            
	    	            for (Cell cell : rs.listCells()) {
	    	              setValue(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), grabacion);
	    	            }
	    	            
	    	            mapGrab.put(key, grabacion);
	    	    	}
	    	    	mylib.console("Se recuperaron "+mapGrab.size()+ " rows desde HBase");
	    	    	conn.close();
    	    	}
    	    }
    		
    	    return mapGrab;
    	} catch (Exception e) {
    		if (solrConn.isConnected()) {
    			solrConn.close();
    		}
    		throw new Exception(e.getMessage());
    	}
    }

    
    public List<GrabMin> getGrabData(int tipoConsulta) throws Exception {
	    	SolRDB solrConn = new SolRDB();
	    	try {
	    	    
	    	    List<GrabMin> lstGrab = new ArrayList<>();
	    	    
	    	    solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties());
	    	    solrConn.open();
	    	    
	    	    if (solrConn.isConnected()) {
	    	    	mylib.console("Conectado a solR");
	    	    	
	    	    	ModifiableSolrParams parameters = buildSolrFilters(tipoConsulta);
	    	    	mylib.console("Se recuperaron los filtros para solR");
	    	    	
	    	    	List<String> keys = new ArrayList<>();
	    	    	mylib.console("Recuperando Ids de grabaciones");
	    	    	keys = solrConn.getIds(parameters);
	    	    	solrConn.close();
	    	    	
	    	    	mylib.console("Se encontraron "+keys.size()+ " ids de grabaciones");
	    	    	
	    	    	for (String key : keys) {
	    	    		mylib.console("key: "+key);
	    	    	}
	    	    	
	    	    	if (keys.size()>0) {
				//Consulta Datos a HBase
				HBaseDB hbConn = new HBaseDB();
				hbConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties(),"grabaciones");
				
				Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
				
				Table table = conn.getTable(TableName.valueOf("grabaciones"));
				
				mylib.console("Conectado a HBase");
				
				GrabMin grabMin;
				
				mylib.console("Recuperando rows desde Hbase");
				for (String key : keys) {
			    		mylib.console("get key: "+key);
		            Get g = new Get(Bytes.toBytes(key));
		            Result rs = table.get(g);
		            
		            grabMin = new GrabMin();
		            String fName="";
		            for (Cell cell : rs.listCells()) {
		              setValueMin(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), grabMin, fName);
		              String myString = new String(CellUtil.cloneQualifier(cell));
		              if (myString.equals("17")) {
		            	  	fName = new String(CellUtil.cloneValue(cell));
		              }
		            }
		            
		            String campo1="<a href='#' onclick='javascript:proxygrabaciones.Escuchargrab(\"" + grabMin.getConnid() + "\")'><img src='../images/sound.png' title='Escuchar'></a>";
		            String campo2="<a href='#' onclick='javascript:proxygrabaciones.Descargagrab(\"" + grabMin.getConnid() + ";"+fName.substring(0, fName.length()-4)+".mp3\")'><img src='../images/disk.png' title='Descargar'></a>";
		            grabMin.setCampo1(campo1);
		            grabMin.setCampo2(campo2);
		            
		            lstGrab.add(grabMin);
		    	    	}
		    	    	mylib.console("Se recuperaron "+lstGrab.size()+ " rows desde HBase");
		    	    	conn.close();
	    	    	}
    	    }
    		
    	    return lstGrab;
    	} catch (Exception e) {
    		if (solrConn.isConnected()) {
    			solrConn.close();
    		}
    		throw new Exception(e.getMessage());
    	}
    }

    public List<GrabMin> getGrabDataSinSkill(int tipoConsulta) throws Exception {
    	SolRDB solrConn = new SolRDB();
    	try {
    	    
    	    List<GrabMin> lstGrab = new ArrayList<>();
    	    
    	    solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties());
    	    solrConn.open();
    	    
    	    if (solrConn.isConnected()) {
    	    	mylib.console("Conectado a solR");
    	    	
    	    	ModifiableSolrParams parameters = buildSolrFiltersSinSkill(tipoConsulta);
    	    	mylib.console("Se recuperaron los filtros para solR");
    	    	
    	    	List<String> keys = new ArrayList<>();
    	    	mylib.console("Recuperando Ids de grabaciones");
    	    	keys = solrConn.getIds(parameters);
    	    	solrConn.close();
    	    	
    	    	mylib.console("Se encontraron "+keys.size()+ " ids de grabaciones");
    	    	
    	    	for (String key : keys) {
    	    		mylib.console("key: "+key);
    	    	}
    	    	
    	    	if (keys.size()>0) {
			//Consulta Datos a HBase
			HBaseDB hbConn = new HBaseDB();
			hbConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties(),"grabaciones");
			
			Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
			
			Table table = conn.getTable(TableName.valueOf("grabaciones"));
			
			mylib.console("Conectado a HBase");
			
			GrabMin grabMin;
			
			mylib.console("Recuperando rows desde Hbase");
			for (String key : keys) {
		    		mylib.console("get key: "+key);
	            Get g = new Get(Bytes.toBytes(key));
	            Result rs = table.get(g);
	            
	            grabMin = new GrabMin();
	            String fName="";
	            for (Cell cell : rs.listCells()) {
	              setValueMin(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), grabMin, fName);
	              String myString = new String(CellUtil.cloneQualifier(cell));
	              if (myString.equals("17")) {
	            	  	fName = new String(CellUtil.cloneValue(cell));
	              }
	            }
	            
	            String campo1="<a href='#' onclick='javascript:proxygrabaciones.Escuchargrab(\"" + grabMin.getConnid() + "\")'><img src='../images/sound.png' title='Escuchar'></a>";
	            String campo2="<a href='#' onclick='javascript:proxygrabaciones.Descargagrab(\"" + grabMin.getConnid() + ";"+fName.substring(0, fName.length()-4)+".mp3\")'><img src='../images/disk.png' title='Descargar'></a>";
	            grabMin.setCampo1(campo1);
	            grabMin.setCampo2(campo2);
	            
	            lstGrab.add(grabMin);
	    	    	}
	    	    	mylib.console("Se recuperaron "+lstGrab.size()+ " rows desde HBase");
	    	    	conn.close();
    	    	}
	    }
		
	    return lstGrab;
	} catch (Exception e) {
		if (solrConn.isConnected()) {
			solrConn.close();
		}
		throw new Exception(e.getMessage());
	}
}

    private void setValueMin(String cq, String valor, GrabMin grab, String fName) {
        switch (cq) {
        	case "01":
        		grab.setAni(valor);
        		break;
        	case "02":
        		grab.setDnis(valor);
        		break;
        	case "04":
	            grab.setConnid(valor);
	            break;
        	case "05":
        		grab.setUniqueid(valor);
        		break;
        	case "08":
	            grab.setFecha(valor);
	            break;
        	case "09":
        		grab.setDurationtotal(valor);
        		break;
        	case "17":
        		fName = valor;
        		break;
        	case "26":
        		grab.setInteraction_id(valor);
        		break;
            case "31":
            	grab.setSkill(valor);
            	break;
            case "38":
            	grab.setAgente(valor);
            	break;
            default:
                break;
        }
    }
    
    private void setValue(String cq, String valor, Grabacion grab) {
        switch (cq) {
        	case "01":
        		grab.setAni(valor);
        		break;
        	case "02":
        		grab.setDnis(valor);
        		break;
        	case "03":
        		grab.setNumerodiscado(valor);
        		break;
        	case "04":
	            grab.setConnid(valor);
	            break;
        	case "05":
        		grab.setUniqueid(valor);
        		break;
        	case "06":
        		grab.setStarttime(valor);
        		break;
        	case "07":
        		grab.setEndtime(valor);
        		break;
        	case "08":
	            grab.setFecha(valor);
	            break;
        	case "09":
        		grab.setDurationtotal(valor);
        		break;
        	case "10":
        		grab.setStatuschannel(valor);
        		break;
        	case "11":
        		grab.setChannel(valor);
        		break;
        	case "12":
        		grab.setProveedor(valor);
        		break;
        	case "13":
        		grab.setAsteriskserver(valor);
        		break;
        	case "14":
        		grab.setServicio(valor);
        		break;
        	case "15":
        		grab.setCallsonline(valor);
        		break;
        	case "16":
        		grab.setPathRecorder(valor);
        		break;
            case "17":
                grab.setNamerecorder(valor);
                break;
        	case "18":
        		grab.setRecordnum(valor);
        		break;
        	case "19":
        		grab.setAttributethisqueue(valor);
        		break;
        	case "20":
        		grab.setAttributeotherdn(valor);
        		break;
        	case "21":
        		grab.setAttributethisdn(valor);
        		break;
        	case "22":
        		grab.setUserdata2genesys(valor);
        		break;
        	case "23":
        		grab.setVirtualqueue(valor);
        		break;
        	case "24":
        		grab.setSipcallerid(valor);
        		break;
        	case "25":
        		grab.setCodigoservicio(valor);
        		break;
        	case "26":
        		grab.setInteraction_id(valor);
        		break;
        	case "27":
        		grab.setResultado_segmento(valor);
        		break;
        	case "28":
        		grab.setTtr(valor);
        		break;
            case "29":
                grab.setUrl(valor);
                break;
            case "30":
            	grab.setTipo_interaction(valor);
            	break;
            case "31":
            	grab.setSkill(valor);
            	break;
            case "32":
            	grab.setTipo_recurso(valor);
            	break;
            case "33":
            	grab.setNombre_recurso(valor);
            	break;
            case "34":
            	grab.setRut(valor);
            	break;
            case "35":
            	grab.setNombre(valor);
            	break;
            case "36":
            	grab.setGestion(valor);
            	break;
            case "37":
            	grab.setRol_recurso(valor);
            	break;
            case "38":
            	grab.setAgente(valor);
            	break;
            case "39":
            	grab.setIdcdr(valor);
            	break;
            case "40":
            	grab.setInfogestion(valor);
            	break;
            case "41":
            	grab.setBkpftp(valor);
            	break;
            case "42":
            	grab.setPathrecorder2(valor);
            	break;
            case "43":
            	grab.setZone(valor);
            	break;
            default:
                break;
        }
    }
    
    public void initComponents(String dataInput) throws Exception {
        try {
            /**
             * fill Global DataRequest
             */
            fillDataRequest(dataInput);
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
	
    public int getTipoConsulta() throws Exception {
        try {
            /**
             * Se analizan las combinaciones de datos para establecer
             * el tipo de consulta a realizar
             * Setea la variable global dr (DataRequest)
             * 
             * Los tipos de resultados son (Todos con al menos un SKILL de consulta):
             * return 1: ConnID sin Fechas
             * return 2: ConnID con rango de fechas
             * return 3: UniqueID
             * return 4: Agente
             * return 5: ANI con rango de fechas
             * return 6: Ani sin fechas
             * return 7: Dnis con rango de fechas
             * return 8: Dnis sin fechas
             * return 9: Busqueda masiva por fechas
             * return 10: Busqueda Agente con Skill y Fechas
             * return 97: Error: Debe ingresar al menos un rango de fechas si es que no ha seleccionado ningun otro valor
             * return 98: Error: Debe ingresar al menos un SKILL
             * return 99: Error de Ejecución
             */
            List<String> lstSkill = new ArrayList<>();
            lstSkill = dr.getLstSkill();

            /**
             * Primara validacion: Debe venir al menos un SKILL
             */
            if (lstSkill.size()>0) {
                    if (dr.getConnid()!=null && !dr.getConnid().equals("")) {
                            /**
                             * Busqueda con ConnID
                             */
                            if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                    if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                            /**
                                             * Busqueda ConnID con FechaDesde y FechaHasta (Ingresada)
                                             */
                                            return 2;
                                    } else {
                                            /**
                                             * Busqueda ConnID con FechaDesde y FechaHasta (completada con getNow)
                                             */
                                            dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                            return 2;
                                    }
                            } else {
                                    /**
                                     * Busqueda ConnID SIN Fechas
                                     */
                                    return 1;
                            }
                    } else {
                            /**
                             * Busqueda SIN connid
                             */
                            if (dr.getUniqueid()!=null && !dr.getUniqueid().equals("")) {
                                    /**
                                     * Busqueda por UniqueID
                                     */
                                    return 3;
                            } else {
                                    if (dr.getAgente()!=null && !dr.getAgente().equals("")) {
                                            /**
                                             * Busqueda por Agente
                                             */
	                                        if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
	                                            /**
	                                             * Busqueda de Agente por Fecha
	                                             */
	                                            if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
	                                                    return 10;
	                                            } else {
	                                                    dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
	                                                    return 10;
	                                            }
		                                    } else {
		                                            /**
		                                             * Busqueda por Agente sin fecha
		                                             */
		                                            return 4;
		                                    }
                                    } else {
                                            if (dr.getAni()!=null && !dr.getAni().equals("")) {
                                                    /**
                                                     * Busqueda por ANI
                                                     * Se evaluara si se suma por fecha o no
                                                     */
                                                    if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                                            /**
                                                             * Busqueda de ANI por Fecha
                                                             */
                                                            if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                                                    return 5;
                                                            } else {
                                                                    dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                                                    return 5;
                                                            }
                                                    } else {
                                                            /**
                                                             * Busqueda por ANI sin fecha
                                                             */
                                                            return 6;
                                                    }
                                            } else {
                                                    if (dr.getDnis()!=null && !dr.getDnis().equals("")) {
                                                            /**
                                                             * Busqueda por DNIS
                                                             * Se evaluara si se suma por fecha o no
                                                             */
                                                            if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                                                    /**
                                                                     * Busqueda de DNIS por Fecha
                                                                     */
                                                                    if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                                                            return 7;
                                                                    } else {
                                                                            dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                                                            return 7;
                                                                    }
                                                            } else {
                                                                    /**
                                                                     * Busqueda por DNIS sin fecha
                                                                     */
                                                                    return 8;
                                                            }
                                                    } else {
                                                            /**
                                                             * Busqueda Masiva por Fechas
                                                             */
                                                            if (dr.getFechaDesde()!=null && !dr.getFechaDesde().equals("")) {
                                                                    /**
                                                                     * Consulta por fecha
                                                                     * Determinar si completa fecha Hasta
                                                                     */
                                                                    if (dr.getFechaHasta()!=null && !dr.getFechaHasta().equals("")) {
                                                                            return 9;
                                                                    } else {
                                                                            dr.setFechaHasta(mylib.getDateNow("YYYYMMDDHHmmss"));
                                                                            return 9;
                                                                    }
                                                            } else {
                                                                    mylib.console(1,"Debe ingresar un rango de fechas ");
                                                                    return 97;
                                                            } 
                                                    }
                                            }
                                    }
                            }
                    }

            } else {
                    mylib.console(1,"Debe ingresar al menos un Skill ");
                    return 98;
            }
        } catch (Exception e) {
            mylib.console(1,"Error en getTipoConsulta ("+e.getMessage()+")");
            throw new Exception(e.getMessage());
        }
    }

    public int getTipoConsultaSinSkill() throws Exception {
        try {
            /**
             * Se analizan las combinaciones de datos para establecer
             * el tipo de consulta a realizar
             * Setea la variable global dr (DataRequest)
             * 
             * Los tipos de resultados son (Todos con al menos un SKILL de consulta):
             * return 1: Connid sin Skill ni fechas
             * return 2: Agente sin Skill ni fechas
             * return 98: Error: Debe ingresar un connid o agente
             */
        	
        		if (dr.getConnid()!=null && !dr.getConnid().equals("")) {
        			return 1;
        		} else {
        			if (dr.getAgente()!=null && !dr.getAgente().equals("")) {
        				return 2;
        			} else {
        				if (dr.getAni()!=null && !dr.getAni().equals("")) {
        					return 3;
        				} else {
        					if (dr.getDnis()!=null && !dr.getDnis().equals("")) {
        						return 4;
        					} else {
        						if (dr.getUniqueid()!=null && !dr.getUniqueid().equals("")) {
        							return 5;
        						} else {
        							return 98;
        						}
        					}
        				}
        			}
        		}
        } catch (Exception e) {
            mylib.console(1,"Error en getTipoConsulta ("+e.getMessage()+")");
            throw new Exception(e.getMessage());
        }
    }

    public ModifiableSolrParams buildSolrFilters(int tipoConsulta) throws Exception {
        ModifiableSolrParams parameters = new ModifiableSolrParams();
        String q="*:*";
        
        switch (tipoConsulta) {
        	case 1:
        		/**
        		 * Connid sin rango de fechas
        		 */
        		q = buildConnidQuery();
        		break;
        	case 2:
        		/**
        		 * Connid con rango de fechas
        		 */
        		q = buildConnidFechasQuery();
        		break;
        	case 3:
        		/**
        		 * UniqueID sin rango de fechas
        		 */
        		q = buildUniqueIDQuery();
        		break;
        	case 4:
        		/**
        		 * Agente sin rango de fechas
        		 */
        		q = buildAgenteIDQuery();
        		break;
        	case 5:
        		/**
        		 * Ani con rango de fechas
        		 */
        		q = buildAniFechasQuery();
        		break;
        	case 6:
        		/**
        		 * Ani sin rango de fechas
        		 */
        		q = buildAniIDQuery();
        		break;
        	case 7:
        		/**
        		 * Dnis con rango de fechas
        		 */
        		q = buildDnisFechasQuery();
        		break;
        	case 8:
        		/**
        		 * Dnis sin rango de fechas
        		 */
        		q = buildDnisIDQuery();
        		break;
        case 9:
	        	/*
	        	 * Busquedas masivas por fechas
	        	 */
            q = buildSkillFechasQuery();
            break;
        case 10:
	        	/*
	        	 * Busquedas Agente con Skill y Fechas
	        	 */
	        q = buildAgenteFechasQuery();
	        break;
        default:
            break;
        }
        
        parameters.set("q", "*:*");
        parameters.set("fq", q);
        parameters.set("start", 0);
        parameters.set("rows", dr.getLimit());
        parameters.set("fl", "id");
        
        mylib.console("Filtro consulta q: "+q);
        
        return parameters;
    }
    
    public ModifiableSolrParams buildSolrFiltersSinSkill(int tipoConsulta) throws Exception {
        ModifiableSolrParams parameters = new ModifiableSolrParams();
        String q="*:*";
        
        switch (tipoConsulta) {
	        	case 1:
	        		/**
	        		 * Connid sin Skill y sin Fechas
	        		 */
	        		q = buildConnidSinSkill();
	        		break;
	        	case 2:
	        		/**
	        		 * Agente sin Skill y sin Fechas
	        		 */
	        		q = buildAgenteSinSkill();
	        		break;
	        	case 3:
	        		/**
	        		 * Ani sin Skill y sin Fechas
	        		 */
	        		q = buildAniSinSkill();
	        		break;
	        	case 4:
	        		/**
	        		 * Dnis sin Skill y sin Fechas
	        		 */
	        		q = buildDnisSinSkill();
	        		break;
	        	case 5:
	        		/**
	        		 * Dnis sin Skill y sin Fechas
	        		 */
	        		q = buildUniqueIDSinSkill();
	        		break;
            default:
            		break;
        }
        
        parameters.set("q", "*:*");
        parameters.set("fq", q);
        parameters.set("start", 0);
        parameters.set("rows", dr.getLimit());
        parameters.set("fl", "id");
        
        mylib.console("Filtro consulta q: "+q);
        
        return parameters;
    }
    
    private String buildDnisIDQuery() {
    	List<String> skills = dr.getLstSkill();
    	String dnis = dr.getDnis();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("dnis:%s", dnis);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    private String buildAniIDQuery() {
    	List<String> skills = dr.getLstSkill();
    	String ani = dr.getAni();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("ani:%s", ani);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    private String buildAgenteIDQuery() {
    	List<String> skills = dr.getLstSkill();
    	String agente = dr.getAgente();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("agente:%s", agente);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    private String buildUniqueIDQuery() {
    	List<String> skills = dr.getLstSkill();
    	String uniqueid = dr.getUniqueid();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("uniqueid:%s", uniqueid);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }

    
    private String buildConnidQuery() {
    	List<String> skills = dr.getLstSkill();
    	String connid = dr.getConnid();
    	
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:%s+*", sk));
        }
        String filter1 = StringUtils.join(newList, " OR ");
        String filter2 = String.format("connid:%s", connid);
        String filters = String.format("(%s) AND (%s)", filter1, filter2);

        return filters;
    }
    
    private String buildUniqueIDSinSkill() {
    		String uniqueid = dr.getUniqueid();
		
	    String filter2 = String.format("uniqueid:%s", uniqueid);
	
	    return filter2;
    }

    private String buildConnidSinSkill() {
	    	String connid = dr.getConnid();
    	
        String filter2 = String.format("connid:%s", connid);

        return filter2;
    }
    
    private String buildAgenteSinSkill() {
    		String agente = dr.getAgente();
		
	    String filter2 = String.format("agente:%s", agente);
	
	    return filter2;
    }

    private String buildAniSinSkill() {
    		String ani = dr.getAni();
	
    		String filter2 = String.format("ani:%s", ani);

    		return filter2;
    }

    private String buildDnisSinSkill() {
    		String dnis = dr.getDnis();

		String filter2 = String.format("dnis:%s", dnis);

		return filter2;
    }

    private String buildAgenteFechasQuery() {
	    	List<String> skills = dr.getLstSkill();
	    	String fechaDesde = dr.getFechaDesde();
	    	String fechaHasta = dr.getFechaHasta();
	    	String agente = dr.getAgente();
	    	
	    	List<String> newList = new ArrayList<>();
	    	for (String sk : skills) {
	    		newList.add(String.format("id:[%s+%s+ TO %s+%s+]", 
	    				sk, fechaDesde, 
	    				sk, fechaHasta));
	    	}
	    	String filter1 = StringUtils.join(newList, " OR ");
	    	String filter2 = String.format("agente:%s", agente);
	    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
	    	return filters;
    }

    private String buildDnisFechasQuery() {
    	List<String> skills = dr.getLstSkill();
    	String fechaDesde = dr.getFechaDesde();
    	String fechaHasta = dr.getFechaHasta();
    	String dnis = dr.getDnis();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[%s+%s+ TO %s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	String filter1 = StringUtils.join(newList, " OR ");
    	String filter2 = String.format("dnis:%s", dnis);
    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
    	return filters;
    }

    private String buildAniFechasQuery() {
    	List<String> skills = dr.getLstSkill();
    	String fechaDesde = dr.getFechaDesde();
    	String fechaHasta = dr.getFechaHasta();
    	String ani = dr.getAni();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[%s+%s+ TO %s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	String filter1 = StringUtils.join(newList, " OR ");
    	String filter2 = String.format("ani:%s", ani);
    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
    	return filters;
    }

    private String buildConnidFechasQuery() {
    	List<String> skills = dr.getLstSkill();
    	String fechaDesde = dr.getFechaDesde();
    	String fechaHasta = dr.getFechaHasta();
    	String connid = dr.getConnid();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[%s+%s+ TO %s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	String filter1 = StringUtils.join(newList, " OR ");
    	String filter2 = String.format("connid:%s", connid);
    	String filters = String.format("(%s) AND (%s)", filter1, filter2);
    	return filters;
    }

    
    private String buildSkillFechasQuery() {
    	List<String> skills = dr.getLstSkill();
    	String fechaDesde = dr.getFechaDesde();
    	String fechaHasta = dr.getFechaHasta();
    	
    	List<String> newList = new ArrayList<>();
    	for (String sk : skills) {
    		newList.add(String.format("id:[%s+%s+ TO %s+%s+]", 
    				sk, fechaDesde, 
    				sk, fechaHasta));
    	}
    	return StringUtils.join(newList, " OR ");
    }
    
    @SuppressWarnings("unused")
	private String buildSkillQuery(List<String> skills) {
        List<String> newList = new ArrayList<>();
        for (String sk : skills) {
            newList.add(String.format("id:%s+*", sk));
        }

        return StringUtils.join(newList, " OR ");
    }

	
}
