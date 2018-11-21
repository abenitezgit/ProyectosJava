/**
 * Author: ABT
 * Version: 6-0.0
 * Modified Date: 2018-11-07
 * Maven Release: 6.6.0
 */
package com.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.RangeFacet.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.JSONObject;

public class Solr6API  {
	Logger logger = Logger.getLogger("Solr6API");
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
			throw new Exception("getQueryDocumentList(): "+e.getMessage());
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
			throw new Exception("setSolrParams(): "+e.getMessage());
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
			
			//Deprecated
			//solrClient = new CloudSolrClient(zkHost,"/solr");
			
			//Use new Connection from solr 6
			solrClient  = new CloudSolrClient.Builder().withZkHost(zkHost).build();
			
			if (getCollectionBase()==null || getCollectionBase().equals("")) {
				throw new Exception("Collection debe definirse antes del Connect");
			}
			solrClient.setDefaultCollection(getCollectionBase());
			solrStatus = solrClient.ping();
			
		} catch (Exception e) {
			throw new Exception("connect"+e.getMessage());
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
			
			//Add /solr
			zkHost.add("/solr");
			
			//Deprecated
			//solrClient = new CloudSolrClient(zkHost,"/solr");
			
			//Use new Connection from solr 6 
			solrClient  = new CloudSolrClient.Builder().withZkHost(zkHost).build();
			solrClient.setDefaultCollection(getCollectionBase());
			solrStatus = solrClient.ping();
			
		} catch (Exception e) {
			throw new Exception("connect"+e.getMessage());
		}

	}

	public boolean connected()  {
		try {
			if (solrStatus.getStatus()==0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public void close()  {
		try {
			solrClient.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void commit() throws Exception {
		try {
			solrClient.commit();
		} catch (Exception e) {
			throw new Exception("commit(): "+e.getMessage());
		}
	}

	public UpdateResponse add(SolrInputDocument doc) throws Exception {
		try {
			UpdateResponse response = solrClient.add(doc);
			return response;
		} catch (Exception e) {
			throw new Exception("add(): "+e.getMessage());
		}
	}

	public UpdateResponse add(Collection<SolrInputDocument> docs) throws Exception {
		try {
			UpdateResponse response = solrClient.add(docs);
			return response;
		} catch (Exception e) {
			throw new Exception("add(): "+e.getMessage());
		}
	}
	
	public UpdateResponse deleteById(List<String> lstIds) throws Exception {
		try {
			UpdateResponse response = solrClient.deleteById(lstIds);
			return response;
		} catch (Exception e) {
			throw new Exception("deleteById(): "+e.getMessage());
		}
	}
	
	public UpdateResponse deleteById(String id) throws Exception {
		try {
			UpdateResponse response = solrClient.deleteById(id);
			return response;
		} catch (Exception e) {
			throw new Exception("deleteById(): "+e.getMessage());
		}
	}

	/**
	 * 
	 * @param params (ModifiableSolrParams)
	 * @return Map<String,String>
	 * @throws Exception
	 */
	@Deprecated
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
			throw new Exception("getRows(): "+e.getMessage());
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
			throw new Exception("getRows(): "+e.getMessage());
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
			throw new Exception("getIds(): "+e.getMessage());
		}
	}

	public List<String> getIds(List<String> fqs, int limit) throws Exception {
		try {
			SolrQuery query = new SolrQuery();
			List<String> ids = new ArrayList<>();
			
			query.setQuery("*:*");
			
			if (fqs.size()>0) {
				for (String fq : fqs) {
					query.addFilterQuery(fq);
				}
			}
			
			if (limit>0) {
				query.setRows(limit);
			} else {
				query.setRows(10000);
			}
			
			query.setFields("id");
			
			QueryResponse qr = solrClient.query(query, METHOD.POST);
			SolrDocumentList docs = qr.getResults();
			
			for (SolrDocument doc: docs) {
				ids.add((String) doc.getFieldValue("id"));
			}
			
			docs.clear();
			return ids;
		} catch (Exception e) {
			throw new Exception("getIds(): "+e.getMessage());
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
			throw new Exception("getIds(): "+e.getMessage());
		}
	}

	public List<Map<String,Object>> getCountRangeDate(String rangeField, Date start, Date end, String gap) throws Exception {
		try {
			SolrQuery query = new SolrQuery();
			List<Map<String,Object>> lstMap = new ArrayList<>();
			Map<String,Object> mapCountRange = new TreeMap<>();
			
			query.setFacet(true);
			query.setRows(0);
			query.setQuery("*:*");
			
			//Formato gap: +1DAY
			
			query.addDateRangeFacet(rangeField, start, end, gap);
			
			QueryResponse response = solrClient.query(query);
			
			@SuppressWarnings("rawtypes")
			List<RangeFacet> ranges = response.getFacetRanges();
			
			for (@SuppressWarnings("rawtypes") RangeFacet range : ranges) {
				
				for (int i=0; i<range.getCounts().size(); i++) {
					Count count = (Count) range.getCounts().get(i);
					mapCountRange = new HashMap<>();
					mapCountRange.put(count.getValue(), count.getCount());
					lstMap.add(mapCountRange);
				}
			}

			return lstMap;
		} catch (Exception e) {
			throw new Exception("getCountRangeDate(): "+e.getMessage());
		}
	}

	public List<Map<String,Object>> getCountRangeDate(String fq, String rangeField, Date start, Date end, String gap) throws Exception {
		try {
			SolrQuery query = new SolrQuery();
			List<Map<String,Object>> lstMap = new ArrayList<>();
			Map<String,Object> mapCountRange = new HashMap<>();
			
			query.setFacet(true);
			query.setRows(0);
			query.setQuery("*:*");
			query.addFilterQuery(fq);
			
			//Formato gap: +1DAY
			
			query.addDateRangeFacet(rangeField, start, end, gap);
			
			QueryResponse response = solrClient.query(query);
			
			@SuppressWarnings("rawtypes")
			List<RangeFacet> ranges = response.getFacetRanges();
			
			for (@SuppressWarnings("rawtypes") RangeFacet range : ranges) {
				
				for (int i=0; i<range.getCounts().size(); i++) {
					Count count = (Count) range.getCounts().get(i);
					mapCountRange = new HashMap<>();
					mapCountRange.put(count.getValue(), count.getCount());
					lstMap.add(mapCountRange);
				}
			}

			return lstMap;
		} catch (Exception e) {
			throw new Exception("getCountRangeDate(): "+e.getMessage());
		}
	}
	
	public List<Map<String,Object>> getListDocs(List<String> fqs, int limit) throws Exception {
		try {
			SolrQuery q = new SolrQuery();
			List<Map<String,Object>> lstMap = new ArrayList<>();
			Map<String,Object> mapDoc = new HashMap<>();
			
			q.setQuery("*:*");
			
			if (limit>0) {
				q.setRows(limit);
			}
			
			for(String fq : fqs) {
				q.setFilterQueries(fq);
			}
			
			QueryResponse qr = solrClient.query(q, METHOD.POST);
			SolrDocumentList docs = qr.getResults();
			
			for (SolrDocument doc : docs) {
				JSONObject jo = new JSONObject(doc);
				mapDoc = jo.toMap();
//				String keymap = (String) doc.getFieldValue("id");
//				map.put(keymap, jo.toString());
				lstMap.add(mapDoc);
			}
			
			return lstMap;
		} catch (Exception e) {
			throw new Exception("getDocs(): "+e.getMessage());
		}
	}

	public List<SolrDocument> getSolrDocs(List<String> fqs, int limit) throws Exception {
		try {
			SolrQuery q = new SolrQuery();
			List<SolrDocument> lstDocs = new ArrayList<>();
			
			q.setQuery("*:*");
			
			if (limit>0) {
				q.setRows(limit);
			}
			
			for(String fq : fqs) {
				q.setFilterQueries(fq);
			}
			
			QueryResponse qr = solrClient.query(q, METHOD.POST);
			SolrDocumentList docs = qr.getResults();
			
			for (SolrDocument doc : docs) {
				lstDocs.add(doc);
			}
			
			return lstDocs;
		} catch (Exception e) {
			throw new Exception("getDocs(): "+e.getMessage());
		}
	}

	/**
	 * 
	 * @param fqs: Lista de String de fq
	 * @param limit: máxima cantidad de registros a consultar
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<SolrInputDocument> getSolrInputDocs(List<String> fqs, int limit) throws Exception {
		try {
			SolrQuery q = new SolrQuery();
			
			Collection<SolrInputDocument> colDocs = new ArrayList<>();
			
			q.setQuery("*:*");
			
			if (limit>0) {
				q.setRows(limit);
			}
			
			for(String fq : fqs) {
				q.setFilterQueries(fq);
			}
			
			QueryResponse qr = solrClient.query(q, METHOD.POST);
			SolrDocumentList docs = qr.getResults();
			
			for (SolrDocument doc : docs) {
				colDocs.add(toSolrInputDocument(doc));
			}
			
			return colDocs;
		} catch (Exception e) {
			throw new Exception("getDocs(): "+e.getMessage());
		}
	}

	public SolrInputDocument toSolrInputDocument(SolrDocument doc) throws Exception {
		try {
			SolrInputDocument iDoc = new SolrInputDocument();

			for (String name : doc.getFieldNames()) {
				if (!name.equals("_version_")) {
					iDoc.addField(name, doc.getFieldValue(name));
				}
			 }
			
			//Don't forget children documents
	        if(doc.getChildDocuments() != null) {
	            for(SolrDocument childDocument : doc.getChildDocuments()) {
	                //You can add paranoic check against infinite loop childDocument == solrDocument
	                iDoc.addChildDocument(toSolrInputDocument(childDocument));
	            }
	        }
			
			return iDoc;
		} catch (Exception e) {
			throw new Exception("toSolrInputDocument(): "+e.getMessage());
		}
	}
	
}
