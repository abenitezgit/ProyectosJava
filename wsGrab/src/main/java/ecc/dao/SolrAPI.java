package ecc.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

public interface SolrAPI {
	
	public void connect(String strZkHost) throws Exception;
	public void close() throws Exception;
	public boolean connected() throws Exception;
	public void commit() throws Exception;
	public UpdateResponse add (SolrInputDocument doc) throws Exception;
	public UpdateResponse add(Collection<SolrInputDocument> docs) throws Exception;
	public Map<String, String> getRows(ModifiableSolrParams params) throws Exception;
	public Map<String, String> getRows(Map<String, String> filters) throws Exception;
	public List<String> getIds(Map<String, String> filters) throws Exception;
	public List<String> getIds(ModifiableSolrParams params) throws Exception;
}
