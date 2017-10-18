package ecc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import dataAccess.HBaseDB;
import dataAccess.SolRDB;
import ecc.model.Grabacion;
import ecc.utiles.GlobalArea;
import utiles.common.rutinas.Rutinas;

public class QueryService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	
	public Map<String, Grabacion> getRow(String connid) throws Exception {
		try {
			SolRDB solrConn = new SolRDB();
			Map<String, Grabacion> mapGrab = new HashMap<>();
			
			solrConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties());
			solrConn.open();
			
			if (solrConn.isConnected()) {
				
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
					HBaseDB hbConn = new HBaseDB();
					hbConn.setConfig(gDatos.getFileConfig(), gDatos.getHbProperties(),"grabaciones");
					
					Connection conn = ConnectionFactory.createConnection(hbConn.getHcfg());
					
					Table table = conn.getTable(TableName.valueOf("grabaciones"));
					
					Get g = new Get(Bytes.toBytes(keys.get(0)));
					Result rs = table.get(g);
					
					Grabacion grabacion = new Grabacion();
					
					for (Cell cell : rs.listCells()) {
						setValue(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)), grabacion);
					}
					
					mapGrab.put(keys.get(0), grabacion);
					
					table.close();
					conn.close();
				}
				solrConn.close();
			} 
			
			return mapGrab;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
