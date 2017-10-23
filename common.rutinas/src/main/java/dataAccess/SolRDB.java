package dataAccess;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.JSONObject;

import utiles.common.rutinas.Rutinas;

/**
 * 
 * @author admin
 * Agregar las dependencias al archivo pom.xml del cliente
   	<dependency>
	    <groupId>org.apache.solr</groupId>
	    <artifactId>solr-solrj</artifactId>
	    <version>5.2.0</version>
	</dependency>
 */

public class SolRDB {
	String[] zkFiles;
	CloudSolrClient server;
	ModifiableSolrParams solrParams;
	Rutinas mylib = new Rutinas();
	boolean connected;
	String keyValue;
	String solrCollection;
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setConfig(String filePropertiesPath, String HBProperty) throws Exception {
		try {
			Properties fileProperties = new Properties();
			
			fileProperties.load(new FileInputStream(filePropertiesPath));
			zkFiles = fileProperties.getProperty(HBProperty+".solr").split(",");
			solrCollection = fileProperties.getProperty(HBProperty+".solr.collection");
			
			if (zkFiles.length==0) {
				throw new Exception("No se han configurado server zookeper");
			}
            
		} catch (Exception e) {
			throw new Exception("solrdb - setConfig - "+e.getMessage());
		}
	}
	
	public void close() throws Exception {
		try {
			server.close();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void open() throws Exception {
		try {
			connected = false;
			for (int i=0; i<zkFiles.length; i++) {
				server = new CloudSolrClient(zkFiles[i]);
				server.setDefaultCollection(solrCollection);
				SolrPingResponse response = server.ping();
				if (response.getStatus()==0) {
					connected = true;
					mylib.console("Conectado a Zookeeper: "+zkFiles[i]);
					break;
				} else {
					mylib.console(1,"Unable to connect zookeeper: "+zkFiles[i]);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void open(String collection) throws Exception {
		try {
			solrCollection = collection;
			connected = false;
			for (int i=0; i<zkFiles.length; i++) {
				server = new CloudSolrClient(zkFiles[i]);
				server.setDefaultCollection(collection);
				SolrPingResponse response = server.ping();
				if (response.getStatus()==0) {
					connected = true;
					mylib.console("Conectado a Zookeeper: "+zkFiles[i]);
					break;
				} else {
					mylib.console(1,"Unable to connect zookeeper: "+zkFiles[i]);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private void setSolrParams(ModifiableSolrParams params) throws Exception {
		solrParams = new ModifiableSolrParams();
		solrParams = params;
		keyValue = "id";
	}
	
	private void setSolrParams(Map<String, String> filters) throws Exception {
		try {
			solrParams = new ModifiableSolrParams();
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				switch (entry.getKey()) {
					case "q":
						solrParams.set("q", entry.getValue());
						break;
					case "fq":
						solrParams.set("fq", entry.getValue());
						break;
					case "fl":
						solrParams.set("fl", entry.getValue());
						break;
					case "sort":
						solrParams.set("sort", entry.getValue());
						break;
					case "rows":
						solrParams.set("rows", Integer.valueOf(entry.getValue()));
						break;
					case "key":
						keyValue = entry.getValue();
						break;
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public List<String> getIds(Map<String, String> filters) throws Exception {
		try {
			List<String> ids = new ArrayList<>();
			
			setSolrParams(filters);
			
			SolrDocumentList docs = getQueryDocumentList();
			
			for (SolrDocument doc: docs) {
				ids.add((String) doc.getFieldValue(keyValue));
			}
			
			docs.clear();
			return ids;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public List<String> getIds(ModifiableSolrParams params) throws Exception {
		try {
			List<String> ids = new ArrayList<>();
			
			setSolrParams(params);
			
			SolrDocumentList docs = getQueryDocumentList();
			
			for (SolrDocument doc: docs) {
				ids.add((String) doc.getFieldValue(keyValue));
			}
			
			docs.clear();
			return ids;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, String> getRows(Map<String, String> filters) throws Exception {
		try {
			Map<String, String> response = new HashMap<>();
			
			setSolrParams(filters);
			
			SolrDocumentList docs = getQueryDocumentList();
			
			for (SolrDocument doc: docs) {
				JSONObject jo = new JSONObject(doc);
				String keymap = (String) doc.getFieldValue(keyValue);
				response.put(keymap, jo.toString());
			}
			
			docs.clear();
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, String> getRows(ModifiableSolrParams params) throws Exception {
		try {
			Map<String, String> response = new HashMap<>();
			
			setSolrParams(params);
			
			SolrDocumentList docs = getQueryDocumentList();
			
			for (SolrDocument doc: docs) {
				JSONObject jo = new JSONObject(doc);
				String keymap = (String) doc.getFieldValue(keyValue);
				response.put(keymap, jo.toString());
			}
			
			docs.clear();
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private SolrDocumentList getQueryDocumentList() throws Exception {
		try {
			SolrDocumentList idKeys = new SolrDocumentList();
			QueryResponse response = server.query(solrParams);
			idKeys = response.getResults();
			return idKeys;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
}
