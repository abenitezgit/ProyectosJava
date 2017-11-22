package ecc.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import ecc.model.Chat;
import ecc.model.DataRequest;
import ecc.model.SelectRequest;
import ecc.utiles.GlobalArea;
import model.colModel;
import utiles.common.rutinas.Rutinas;

public class ChatService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataRequest dr = new DataRequest();
	SelectRequest sr = new SelectRequest();

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
    
    public ModifiableSolrParams buildSolrFilters(int tipoConsulta) throws Exception {
        ModifiableSolrParams parameters = new ModifiableSolrParams();
        String q="*:*";
        String fq="*:*";
        
        switch (tipoConsulta) {
        	case 1:
        		/**
        		 * Consulta todas las interaccion de un Chat
        		 */
        		fq = buildChatIDQuery();
        		break;
        }
        
        parameters.set("q", q);
        parameters.set("fq", fq);
        parameters.set("start", 0);
        parameters.set("rows", 500);
        parameters.set("fl", "id");
        
        mylib.console("Filtro consulta q: "+q);
        mylib.console("Filtro consulta fq: "+fq);
        
        return parameters;
    }
        
    private String buildChatIDQuery() {

        String filter1 = String.format("chatID:%s", sr.getChatID());
        
        return filter1;
    }

    private void setValue(String cq, String valor, Chat chat) {
        switch (cq) {
        	case "01":
        		chat.setFecha(valor);
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
        default:
            break;
        }
    }
    
    public List<Chat> getChatData(int tipoConsulta) throws Exception {
    	SolRDB solrConn = new SolRDB();
    	try {
    	    
    	    List<Chat> lstChat = new ArrayList<>();
    	    
    	    solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties());
    	    solrConn.setSolrCollection("collchat");
    	    solrConn.open();
    	    
    	    if (solrConn.isConnected()) {
    	    	mylib.console("Conectado a solR");
    	    	
    	    	ModifiableSolrParams parameters = buildSolrFilters(1);
    	    	mylib.console("Se recuperaron los filtros para solR");
    	    	
    	    	List<String> keys = new ArrayList<>();
    	    	mylib.console("Recuperando Ids de grabaciones");
    	    	keys = solrConn.getIds(parameters);
    	    	solrConn.close();
    	    	
    	    	mylib.console("Se encontraron "+keys.size()+ " interacciones de chat");
    	    	
    	    	for (String key : keys) {
    	    		mylib.console("key: "+key);
    	    	}
    	    	
    	    	if (keys.size()>0) {
			//Consulta Datos a HBase
			HBaseDB hbConn = new HBaseDB();
			hbConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties(),"chatdata");
			
			Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
			
			Table table = conn.getTable(TableName.valueOf("chatdata"));
			
			mylib.console("Conectado a HBase");
			
			Chat chat;
			
			mylib.console("Recuperando rows desde Hbase");
			for (String key : keys) {
		    		mylib.console("get key: "+key);
	            Get g = new Get(Bytes.toBytes(key));
	            Result rs = table.get(g);
	            
	            chat = new Chat();
	            for (Cell cell : rs.listCells()) {
	              setValue(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), chat);
	            }
	            
	            lstChat.add(chat);
	    	    	}
	    	    	mylib.console("Se recuperaron "+lstChat.size()+ " interacciones desde HBase");
	    	    	conn.close();
    	    	}
	    }
		
	    return lstChat;
	} catch (Exception e) {
		if (solrConn.isConnected()) {
			solrConn.close();
		}
		throw new Exception(e.getMessage());
	}
}

    
    public Map<String, Map<String,String>> getChat() throws Exception {
    		try {
    			Map<String, Map<String,String>> mapRows = new TreeMap<>();
    			
    			HBaseDB conn = new HBaseDB();
    			
    			conn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecchdp1","chatdata");

    			String key = sr.getChatID();
    			mylib.console("Buscando chatID: "+key);
    			mapRows = conn.getRow(key);
    			
    			return mapRows;
    			
    		} catch (Exception e) {
    			mylib.console(1,"Error getChat: "+e.getMessage());
    			throw new Exception(e.getMessage());
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
    			mylib.console(1,"Error validaDataInput: "+e.getMessage());
    			throw new Exception(e.getMessage());
    		}
    }
    
    public int executeUpdate() throws Exception {
    		try {
    			HBaseDB conn = new HBaseDB();
    			
    			conn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecchdp1","chatdata");

    			Map<String,List<colModel>> row = new HashMap<>();
    			
    			row = genMapRow();
    			
    			conn.putRow(row);
    			
    			return 0;
    			
    		} catch (Exception e) {
    			mylib.console(1,"Error executeUpdate: "+e.getMessage());
    			throw new Exception(e.getMessage());
    		}
    }
    
    private Map<String, List<colModel>> genMapRow() throws Exception {
    		try {
    			colModel cm = new colModel();
    			List<colModel> col = new ArrayList<>();
    			Map<String, List<colModel>> row = new HashMap<>();
    			
    			cm.setFamily("cf1");
    			cm.setColumn("01");
    			cm.setValue(dr.getFecha());
    			
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
    			mylib.console(1,"Error genMapRow: "+e.getMessage());
    			throw new Exception(e.getMessage());
    		}
    }
	
}
