package ecc.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.api.HBaseAPI;
import com.api.SolrAPI;
import com.api.colModel;
import com.rutinas.Rutinas;

import ecc.model.Chat;
import ecc.model.DataRequest;
import ecc.model.Info;
import ecc.model.SelectRequest;
import ecc.utiles.GlobalParams;

public class ChatService {
	Logger logger = Logger.getLogger("wsChat");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	DataRequest dr = new DataRequest();
	SelectRequest sr = new SelectRequest();
	
	public ChatService(GlobalParams m) {
		gParams = m;
	}

    public void fillDataRequest(String dataInput) throws Exception {
        dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    }

    public void fillSelectRequest(String dataInput) throws Exception {
        sr = (SelectRequest) mylib.serializeJSonStringToObject(dataInput, SelectRequest.class);
    }

    public void initComponents(String dataInput) throws Exception {
        try {
            /**
             * fill Global DataRequest
             */
        		mylib.console("Parseando datos de entrada");
            fillDataRequest(dataInput);
            
            mylib.console("Validando datos de entrada");
            validaDataInput();
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void initSelectComponents(String dataInput) throws Exception {
        try {
            /**
             * fill Global DataRequest
             */
        		mylib.console("Parseando datos de entrada");
            fillSelectRequest(dataInput);
            
            mylib.console("Validando datos de entrada");
            validaSelectDataInput();
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ModifiableSolrParams buildSolrFiltersText(int chatType) throws Exception {
        ModifiableSolrParams parameters = new ModifiableSolrParams();
        String q="*:*";
        String fq="*:*";
        
    		String filter1 = buildChatTextQuery(chatType);
    		//String filter2 = 
        
        parameters.set("q", q);
        parameters.set("fq", fq);
        parameters.set("start", 0);
        parameters.set("rows", 500);
        parameters.set("fl", "id");
        
        logger.info("Filtro consulta q: "+q);
        logger.info("Filtro consulta fq: "+fq);
        
        return parameters;
    }
        
    public ModifiableSolrParams buildSolrFilters(int chatType) throws Exception {
        ModifiableSolrParams parameters = new ModifiableSolrParams();
        String q="*:*";
        String fq="*:*";
        
    		fq = buildChatIDQuery(chatType);
        
        parameters.set("q", q);
        parameters.set("fq", fq);
        parameters.set("start", 0);
        parameters.set("rows", 500);
        parameters.set("fl", "id");
        
        logger.info("Filtro consulta q: "+q);
        logger.info("Filtro consulta fq: "+fq);
        
        return parameters;
    }

    private String buildChatTextQuery(int chatType) {

        String filter1 = String.format("mensaje:%s", gParams.getDr().getChatID());
        String filter2 = "";
        switch(chatType) {
	        case 1:
	        		filter2 = "chatType:1";
	        		break;
	        case 2:
	        		filter2 = "chatType:2";
	        		break;
	        case 3:
	        		filter2 = "chatType:3";
	        		break;
	        case 4:
	        		filter2 = "chatType:4";
	        		break;
        }
        
        return filter1 + " AND " + filter2;
    }

    private String buildChatIDQuery(int chatType) {

        String filter1 = String.format("chatID:%s", gParams.getDr().getChatID());
        String filter2 = "";
        switch(chatType) {
	        case 1:
	        		filter2 = "chatType:1";
	        		break;
	        case 2:
	        		filter2 = "chatType:2";
	        		break;
	        case 3:
	        		filter2 = "chatType:3";
	        		break;
	        case 4:
	        		filter2 = "chatType:4";
	        		break;
        }
        
        return filter1 + " AND " + filter2;
    }

    private void setValue(String cq, String valor, Chat chat) {
        switch (cq) {
        	case "01":
        		chat.setFecha(mylib.getDateString(valor, "yyyy-MM-dd HH:mm:ss","dd-MM-yyyy HH:mm:ss"));
        		break;
        	case "02":
        		chat.setMensaje(valor);
        		break;
        	case "03":
        		chat.setUserID(valor);
        		break;
        	case "04":
            chat.setUserName(valor);
            break;
        	case "05":
        		chat.setUserType(valor);
        		break;
        	case "06":
        		chat.setChatID(valor);
        		break;
        	case "12":
        		chat.setFileName(valor);
        		break;
        	case "13":
            chat.setFileExt(valor);
            break;
        	case "14":
        		chat.setFileBin(valor);
        		break;
        	case "15":
        		chat.setRoom(valor);
        		break;
        	case "16":
        		chat.setServiceID(valor);
        		break;
        	case "17":
        		chat.setChatType(valor);
        		break;
        default:
            break;
        }
    }
    
    public List<Chat> getChatText(String collection, String tbName, int chatType) throws Exception {
    		try {
    			SolrAPI solrConn = new SolrAPI();
        	    solrConn.setCollectionBase(collection);
        	    solrConn.connect(gParams.getInfo().getZkSolr());
        	    
        	    List<Chat> lstChats = new ArrayList<>();
        	    
        	    if (solrConn.connected()) {
	        	    	logger.info("Conectado a solR");
	        	    	
	        	    	ModifiableSolrParams parameters = buildSolrFilters(chatType);
	        	    	logger.info("Se recuperaron los filtros para solR");
        	    }

    			
    			return lstChats;
    			
    		} catch (Exception e) {
    			throw new Exception(e.getMessage());
    		}
    }
    
    public List<Chat> getChatData(String collection, String tbName, int chatType) throws Exception {
    	SolrAPI solrConn = new SolrAPI();
    	try {
    	    
    	    List<Chat> lstChat = new ArrayList<>();
    	    
    	    solrConn.setCollectionBase(collection);
    	    solrConn.connect(gParams.getInfo().getZkSolr());
    	    
    	    
    	    if (solrConn.connected()) {
    	    	logger.info("Conectado a solR");
    	    	
    	    	ModifiableSolrParams parameters = buildSolrFilters(chatType);
    	    	logger.info("Se recuperaron los filtros para solR");
    	    	
    	    	List<String> keys = new ArrayList<>();
    	    	logger.info("Recuperando Ids de grabaciones");
    	    	keys = solrConn.getIds(parameters);
    	    	solrConn.close();
    	    	
    	    	logger.info("Se encontraron "+keys.size()+ " interacciones de chat");
    	    	
    	    	for (String key : keys) {
    	    		logger.debug("key: "+key);
    	    	}
    	    	
    	    	if (keys.size()>0) {
			//Consulta Datos a HBase
			HBaseAPI hbConn = new HBaseAPI();
			hbConn.setConfig(gParams.getInfo().getConfigPath()+"/"+gParams.getInfo().getConfigFile(), gParams.getInfo().getCloudName(),tbName);
			
			Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			logger.info("Conectado a HBase");
			
			Chat chat;
			
			logger.info("Recuperando rows desde Hbase");
			for (String key : keys) {
				logger.debug("get key: "+key);
	            Get g = new Get(Bytes.toBytes(key));
	            Result rs = table.get(g);
	            
	            chat = new Chat();
	            for (Cell cell : rs.listCells()) {
	              setValue(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), chat);
	            }
	            
	            lstChat.add(chat);
	    	    	}
			logger.info("Se recuperaron "+lstChat.size()+ " interacciones desde HBase");
	    	    	conn.close();
    	    	}
	    }
		
	    return lstChat;
	} catch (Exception e) {
		throw new Exception("getChatData: "+e.getMessage());
	}
}

    public Chat getLastChatData(String collection, String tbName, int chatType) throws Exception {
	    	SolrAPI solrConn = new SolrAPI();
	    	try {
	    	    Chat returnChat = new Chat();
	    	    List<Chat> lstChat = new ArrayList<>();
	    	    
	    	    solrConn.setCollectionBase(collection);
	    	    solrConn.connect(gParams.getInfo().getZkSolr());
	    	    
	    	    
	    	    if (solrConn.connected()) {
		    	    	logger.info("Conectado a solR");
		    	    	
		    	    	ModifiableSolrParams parameters = buildSolrFilters(chatType);
		    	    	logger.info("Se recuperaron los filtros para solR");
		    	    	
		    	    	List<String> keys = new ArrayList<>();
		    	    	logger.info("Recuperando Ids de grabaciones");
		    	    	keys = solrConn.getIds(parameters);
		    	    	solrConn.close();
		    	    	
		    	    	//Asigna resultados a un maptree para ordenarlo
		    	    	Map<String, String> mapKey = new TreeMap<>();
		    	    	String lastKey="";
		    	    	for(int i=0; i<keys.size(); i++) {
		    	    		lastKey=keys.get(i);
		    	    		mapKey.put(keys.get(i), "");
		    	    	}
		    	    	
		    	    	keys.clear();
		    	    	keys.add(lastKey);
		    	    	
		    	    	logger.info("Se encontraron "+keys.size()+ " interacciones de chat");
		    	    	
		    	    	for (String key : keys) {
		    	    		logger.debug("key: "+key);
		    	    	}
		    	    	
		    	    	if (keys.size()>0) {
					//Consulta Datos a HBase
					HBaseAPI hbConn = new HBaseAPI();
					hbConn.setConfig(gParams.getInfo().getConfigPath()+"/"+gParams.getInfo().getConfigFile(), gParams.getInfo().getCloudName(),tbName);
					
					Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
					
					Table table = conn.getTable(TableName.valueOf(tbName));
					
					logger.info("Conectado a HBase");
					
					Chat chat;
					
					logger.info("Recuperando rows desde Hbase");
					for (String key : keys) {
						logger.debug("get key: "+key);
			            Get g = new Get(Bytes.toBytes(key));
			            Result rs = table.get(g);
			            
			            chat = new Chat();
			            for (Cell cell : rs.listCells()) {
			              setValue(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), chat);
			            }
			            
			            lstChat.add(chat);
			            returnChat = chat;
			    	    	}
					logger.info("Se recuperaron "+lstChat.size()+ " interacciones desde HBase");
			    	    	conn.close();
		    	    	}
		    }
			
		    return returnChat;
		} catch (Exception e) {
			throw new Exception("getLastChatData: "+e.getMessage());
		}
    }

    
    public Map<String, Map<String,String>> getChat(String tbName) throws Exception {
    		try {
    			Map<String, Map<String,String>> mapRows = new TreeMap<>();
    			
    			HBaseAPI conn = new HBaseAPI();
    			
    			conn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecchdp1",tbName);

    			String key = sr.getChatID();
    			logger.info("Buscando chatID: "+key);
    			mapRows = conn.getRow(key);
    			
    			return mapRows;
    			
    		} catch (Exception e) {
    			throw new Exception("getChat: "+e.getMessage());
    		}
    }

    private void validaSelectDataInput() throws Exception {
    		try {
    			
    		} catch (Exception e) {
    			throw new Exception(e.getMessage());
    		}
    }
    
    private void validaDataInput() throws Exception {
    		try {
    			//Valida Fecha y la separa
    			Date fecha = mylib.getDate(dr.getFecha(), "yyyy-MM-dd HH:mm:ss");
    			Calendar cal = Calendar.getInstance();
    			cal.setTime(fecha);
    			dr.setYear(String.valueOf(cal.get(Calendar.YEAR)));
    			dr.setMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
    			dr.setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
    			dr.setHour(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
    			dr.setMinute(String.valueOf(cal.get(Calendar.MINUTE)));
    			dr.setSecond(String.valueOf(cal.get(Calendar.SECOND)));
    		} catch (Exception e) {
    			throw new Exception(e.getMessage());
    		}
    }
    
    public int executeUpdate(int chatType) throws Exception {
    		try {
    			Info info = gParams.getInfo();
    			HBaseAPI conn = new HBaseAPI();
    			
    			conn.setConfig(info.getConfigPath()+"/"+info.getConfigFile(), info.getCloudName(), "chatint");

    			Map<String,List<colModel>> row = new HashMap<>();
    			
    			row = genMapRow(chatType);
    			
    			conn.putRow(row);
    			
    			return 0;
    			
    		} catch (Exception e) {
    			throw new Exception("executeUpdate: "+e.getMessage());
    		}
    }
    
    private Map<String, List<colModel>> genMapRow(int chatType) throws Exception {
    		try {
    			colModel cm = new colModel();
    			List<colModel> col = new ArrayList<>();
    			Map<String, List<colModel>> row = new HashMap<>();
    			DataRequest dr = gParams.getDr();
    			
    			logger.info("FecIni: "+dr.getFecha());
    			logger.info("FecFin: "+mylib.getDateString(dr.getFecha(), "dd-MM-yyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss"));
    			
    			cm.setFamily("cf1");
    			cm.setColumn("01");
    			cm.setValue(mylib.getDateString(dr.getFecha(), "dd-MM-yyyy HH:mm:ss","yyyy-MM-dd HH:mm:ss"));
    			
    			col.add(cm);
    			
    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("02");
    			cm.setValue(dr.getMensaje());
    			col.add(cm);
    			
    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("03");
    			cm.setValue(dr.getUserID());
    			col.add(cm);
    			
    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("04");
    			cm.setValue(dr.getUserName());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("05");
    			cm.setValue(dr.getUserType());
    			col.add(cm);
    			
    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("06");
    			cm.setValue(dr.getChatID());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("07");
    			cm.setValue(dr.getYear());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("08");
    			cm.setValue(dr.getMonth());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("09");
    			cm.setValue(dr.getDay());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("10");
    			cm.setValue(dr.getHour());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("11");
    			cm.setValue(dr.getMinute());
    			col.add(cm);
    			
    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("12");
    			cm.setValue(dr.getFileName());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("13");
    			cm.setValue(dr.getFileExt());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("14");
    			cm.setValue(dr.getFileBin());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("15");
    			cm.setValue(dr.getRoom());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("16");
    			cm.setValue(dr.getServiceID());
    			col.add(cm);

    			cm = new colModel();
    			cm.setFamily("cf1");
    			cm.setColumn("17");  //chatType
    			cm.setValue(String.valueOf(chatType));
    			col.add(cm);

    			String year = String.format("%04d", Integer.valueOf(dr.getYear()));
    			String month = String.format("%02d", Integer.valueOf(dr.getMonth()));
    			String day = String.format("%02d", Integer.valueOf(dr.getDay()));
    			String hour = String.format("%02d", Integer.valueOf(dr.getHour()));
    			String minute = String.format("%02d", Integer.valueOf(dr.getMinute()));
    			String second = String.format("%02d", Integer.valueOf(dr.getSecond()));

    			String key = dr.getChatID() + "+" + year + month + day + hour + minute + second;
    			
    			row.put(key, col);
    			
    			return row;
    		} catch (Exception e) {
    			throw new Exception("genMapRow: "+e.getMessage());
    		}
    }
	
}
