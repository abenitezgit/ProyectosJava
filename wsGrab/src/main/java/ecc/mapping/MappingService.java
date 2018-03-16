package ecc.mapping;


public interface MappingService {

	public void setConfigParams() throws Exception;
	public void setOpcionMapping() throws Exception;
	public void getSolrFiltersMapping(String opcionMatriz) throws Exception;
	
}
