package ecc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONObject;

import com.api.HBaseAPI;
import com.api.SolrAPI;
import com.rutinas.Rutinas;

import ecc.model.Grabacion;
import ecc.model.UriGrab;
import ecc.utiles.GlobalArea;

public class QueryService {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public QueryService(GlobalArea m) {
		gDatos = m;
	}
	
	public String getUrisGrab(List<String> interactionIds) throws Exception {
		SolrAPI solrConn = new SolrAPI();
		try {
			UriGrab uriGrab = new UriGrab();
			
			solrConn.connect(gDatos.getZkHost(), gDatos.getCollectionBase());
			
			if (solrConn.connected()) {
				List<String> filterInterac = new ArrayList<>();
				
				for (String interaction : interactionIds) {
					filterInterac.add("interactionid:"+interaction);
				}
				
				String fqFilter = StringUtils.join(filterInterac, " OR ");
				
				Map<String, String> filters = new HashMap<>();
				filters.put("q", "*:*");
				filters.put("fq", fqFilter);
				filters.put("rows", "10000");
				filters.put("fl", "id, interactionid, connid, fecgrab, tiponteract");
				filters.put("key", "id");

				Map<String, String> mapIds = new HashMap<>();
				mapIds = solrConn.getRows(filters);
				
				if (mapIds.size()>0) {
					
					List<String> uris = new ArrayList<>();
					for (Map.Entry<String, String> entry : mapIds.entrySet()) {
						JSONObject jo = new JSONObject(entry.getValue());
						uris.add("http://10.240.8.15:8080/wsGrab/webapi/getWav/"+jo.getString("id"));
					}
					uriGrab.setNumGrab(mapIds.size());
					uriGrab.setUrlGrab(uris);
				} else {
					uriGrab.setNumGrab(0);
				}
			}

			return mylib.serializeObjectToJSon(uriGrab, false);
		} catch (Exception e) {
			throw new Exception("getUrisGrab: "+e.getMessage());
		} finally {
			try {
				solrConn.close();
			} catch (Exception e) {}
		}
	}
	
	public List<String> getInteractionIDs(String connID) throws Exception {
		SolrAPI solrConn = new SolrAPI();
		try {
			List<String> interactionIds = new ArrayList<>();
			
			solrConn.connect(gDatos.getZkHost(), gDatos.getCollectionBase());
			
			if (solrConn.connected()) {
				Map<String, String> filters = new HashMap<>();
				filters.put("q", "*:*");
				filters.put("fq", "connid:"+connID);
				filters.put("rows", "10000");
				filters.put("fl", "interactionid");
				filters.put("key", "id");

				Map<String, String> mapIds = new HashMap<>();
				mapIds = solrConn.getRows(filters);
				
				if (mapIds.size()>0) {
					for (Map.Entry<String, String> entry : mapIds.entrySet()) {
						JSONObject jo = new JSONObject(entry.getValue());
						interactionIds.add(jo.getString("interactionid"));
					}
				}
			}
			
			return interactionIds;
		} catch (Exception e) {
			throw new Exception("getInteractionIDs: "+e.getMessage());
		} finally {
			try {
				solrConn.close();
			} catch (Exception e) {}
		}
	}

	
	public Map<String, Grabacion> getRow(String connid) throws Exception {
		SolrAPI solrConn = new SolrAPI();
		HBaseAPI hbConn = new HBaseAPI();
		try {
			
			Map<String, Grabacion> mapGrab = new HashMap<>();
			
			solrConn.connect(gDatos.getZkHost(), gDatos.getCollectionBase());
			
			if (solrConn.connected()) {
				
				//Aplica filtros de consulta
				Map<String,String> filters = new HashMap<>();
				filters.put("q", "*:*");
				filters.put("fq", "connid:"+connid);
				filters.put("rows", "1");
				filters.put("key", "id");
				
				//Ejecutando query y obteniendo respuesta
				List<String> keys = new ArrayList<>();
				keys = solrConn.getIds(filters);
				
				if (!keys.isEmpty()) {
					//Consulta Datos a HBase
					
					hbConn.setConfig(gDatos.getFileConfig(), gDatos.getClusterName(),gDatos.getHbTableName());
					
					Table table = hbConn.getConnection().getTable(TableName.valueOf(gDatos.getHbTableName()));
					
					Get g = new Get(Bytes.toBytes(keys.get(0)));
					Result rs = table.get(g);
					
					Grabacion grabacion = new Grabacion();
					
					for (Cell cell : rs.listCells()) {
						setValue(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), grabacion);
					}
					
					mapGrab.put(keys.get(0), grabacion);
					
					table.close();
				}
			} 
			
			return mapGrab;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				solrConn.close();
			} catch (Exception e) {}
			try {
				hbConn.close();
			} catch (Exception e) {}
		}
	}
	
    public void setValue(String cq, String valor, Grabacion grab) {
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

}
