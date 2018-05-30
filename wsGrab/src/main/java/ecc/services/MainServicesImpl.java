package ecc.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import ecc.mapping.MappingService;
import ecc.mapping.MappingServiceImpl;
import ecc.model.UriGrab;
import ecc.utiles.GlobalArea;

public class MainServicesImpl implements IMainServices{
	Logger logger = Logger.getLogger("wsGrab");
	Rutinas mylib = new Rutinas();
	GlobalArea gParams;
	MappingService mapS;
	QueryService qs;
	
	public MainServicesImpl(GlobalArea m) {
		gParams = m;
		mapS = new MappingServiceImpl(gParams);
		qs = new QueryService(gParams);
	}

	@Override
	public void gestionaOpcionConsulta() throws Exception {
		try {
			
			logger.info("Seteando Mapping de Opcions de Consulta...");
			mapS.setOpcionMapping();
			
			logger.info("Generando Matriz Binaria de Opcion de Consulta...");
			String matriz = "";
			
			if (!mylib.isNullOrEmpty(gParams.getDr().getAni())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getDnis())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getConnid())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getAgente())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getUniqueid())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getCodigoservicio())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getInteractionid())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getFname())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getZone())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getRutagente())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getTipointeract())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getFechaDesde())) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getFechaHasta())) matriz += "1"; else matriz += "0";
			if (gParams.getDr().getLstSkill().size()!=0) matriz += "1"; else matriz += "0";
			if (gParams.getDr().getLimit()>0) matriz += "1"; else matriz += "0";
			if (!mylib.isNullOrEmpty(gParams.getDr().getFono())) matriz += "1"; else matriz += "0";
			
			logger.info("Matriz generada: "+matriz);
			logger.info("Setando Matriz Global...");
			gParams.setOpcionMatriz(matriz);
			
			logger.info("Buscando Matriz en Mapping Opcion de Consulta...");
			if (gParams.getMapOpcion().containsKey(matriz)) {
 				String descMatriz[] = gParams.getMapOpcion().get(matriz).split(":");
 				logger.info("Matriz validada. Opcion de Consulta: "+descMatriz[1]);
			} else {
				logger.error("getTipoConsulta: No es posible identificar una Opcion de Consulta");
				throw new Exception("getTipoConsulta: No es posible identificar una Opcion de Consulta");
			}
		} catch (Exception e) {
			logger.error("getTipoConsulta: "+e.getMessage());
			throw new Exception("getTipoConsulta: "+e.getMessage());
		}
	}

	@Override
	public void genSolrFilterMapping() throws Exception {
		try {
			mapS.getSolrFiltersMapping(gParams.getOpcionMatriz());
			
		} catch (Exception e) {
			logger.error("genSolrFilterMapping: "+e.getMessage());
			throw new Exception("genSolrFilterMapping: "+e.getMessage());
		}
		
	}

	@Override
	public String getUrisGrab(String connID) throws Exception {
		try {
			/**
			 * Recuperando InteractionID del connid consultado
			 */
			String result=null;
			List<String> interactionIds = new ArrayList<>();
			interactionIds = qs.getInteractionIDs(connID);
			
			if (interactionIds.size()>0) {
				logger.info("Total de Interactions para connID: "+connID+ " " + interactionIds.size());
				result = qs.getUrisGrab(interactionIds);
			} else {
				UriGrab uri = new UriGrab();
				uri.setNumGrab(0);
				result = mylib.serializeObjectToJSon(uri, false);
				logger.warn("No hay interacciones para connID: "+connID);
			}
			
			return result;
		} catch (Exception e) {
			logger.error("genSolrFilterMapping: "+e.getMessage());
			throw new Exception("genSolrFilterMapping: "+e.getMessage());
		}
	}

}
