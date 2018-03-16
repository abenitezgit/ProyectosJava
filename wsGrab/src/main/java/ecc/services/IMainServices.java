package ecc.services;

public interface IMainServices {
	
	public void gestionaOpcionConsulta() throws Exception;
	public void genSolrFilterMapping() throws Exception;
	public String getUrisGrab(String connID) throws Exception;

}
