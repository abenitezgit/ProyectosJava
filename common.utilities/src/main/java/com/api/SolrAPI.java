package com.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.JSONObject;

public class SolrAPI  {
	private CloudSolrClient solrClient;
	private SolrPingResponse solrStatus;
	private ModifiableSolrParams modifiableSolrParams;
	private String collectionBase;
	private String collectionAlt;
	private String keyValue;
	
	//Getter and Setter
	
	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public String getCollectionBase() {
		return collectionBase;
	}

	public void setCollectionBase(String collectionBase) {
		this.collectionBase = collectionBase;
	}

	public String getCollectionAlt() {
		return collectionAlt;
	}

	public void setCollectionAlt(String collectionAlt) {
		this.collectionAlt = collectionAlt;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

	//Private Methods
	private SolrDocumentList getQueryDocumentList() throws Exception {
		try {
			solrClient.setDefaultCollection(collectionBase);
			SolrDocumentList idKeys = new SolrDocumentList();
			
			QueryResponse qryResponse = solrClient.query(modifiableSolrParams,METHOD.POST);
			idKeys = qryResponse.getResults();
			
			if (idKeys.getNumFound()==0) {
				if (!(collectionAlt == null) && !(collectionAlt.isEmpty())) {
					solrClient.setDefaultCollection(collectionAlt);
					qryResponse = solrClient.query(modifiableSolrParams,METHOD.POST);
					idKeys = qryResponse.getResults();
				}
			}
			
			return idKeys;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private void setSolrParams(ModifiableSolrParams params) throws Exception {
		modifiableSolrParams = new ModifiableSolrParams();
		modifiableSolrParams = params;
		keyValue = "id";
	}
	
	private void setSolrParams(Map<String, String> filters) throws Exception {
		try {
			modifiableSolrParams = new ModifiableSolrParams();
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				switch (entry.getKey()) {
					case "q":
						modifiableSolrParams.set("q", entry.getValue());
						break;
					case "fq":
						modifiableSolrParams.set("fq", entry.getValue());
						break;
					case "fl":
						modifiableSolrParams.set("fl", entry.getValue());
						break;
					case "sort":
						modifiableSolrParams.set("sort", entry.getValue());
						break;
					case "wt":
						modifiableSolrParams.set("wt", entry.getValue());
						break;
					case "rows":
						modifiableSolrParams.set("rows", Integer.valueOf(entry.getValue()));
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

	//Public Methods

	/**
	 * Establece connect to solrCloud especificando los zookeeper servers
	 * @param strZkHost
	 * @throws Exception
	 */
	public void connect(String strZkHost) throws Exception {
		try {
			// Using a ZK Host String
			String[] arrayZk = strZkHost.split(",");
			Collection<String> zkHost = new ArrayList<>();
			
			for (String zkServer : arrayZk) {
				zkHost.add(zkServer);
			}
			solrClient = new CloudSolrClient(zkHost,"/solr");
			
			if (getCollectionBase()==null || getCollectionBase().equals("")) {
				throw new Exception("Collection debe definirse antes del Connect");
			}
			solrClient.setDefaultCollection(getCollectionBase());
			solrStatus = solrClient.ping();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * Establece connect to solrCloud especificando los zookeeper servers y collectionbase
	 * @param strZkHost
	 * @throws Exception
	 */
	public void connect(String strZkHost, String collection) throws Exception {
		try {
			//Set collectionBase
			setCollectionBase(collection);
			
			// Using a ZK Host String
			String[] arrayZk = strZkHost.split(",");
			Collection<String> zkHost = new ArrayList<>();
			
			for (String zkServer : arrayZk) {
				zkHost.add(zkServer);
			}
			solrClient = new CloudSolrClient(zkHost,"/solr");
			solrClient.setDefaultCollection(getCollectionBase());
			solrStatus = solrClient.ping();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	public boolean connected() throws Exception {
		try {
			if (solrStatus.getStatus()==0) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void close()  {
		try {
			solrClient.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void commit() throws Exception {
		try {
			solrClient.commit();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public UpdateResponse add(SolrInputDocument doc) throws Exception {
		try {
			UpdateResponse response = solrClient.add(doc);
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public UpdateResponse add(Collection<SolrInputDocument> docs) throws Exception {
		try {
			UpdateResponse response = solrClient.add(docs);
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public UpdateResponse deleteById(List<String> lstIds) throws Exception {
		try {
			UpdateResponse response = solrClient.deleteById(lstIds);
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public UpdateResponse deleteById(String id) throws Exception {
		try {
			UpdateResponse response = solrClient.deleteById(id);
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
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
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



}
