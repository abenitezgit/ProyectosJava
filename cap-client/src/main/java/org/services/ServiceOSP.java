package org.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.model.Osp;
import org.model.OspParam;
import org.utilities.MyLogger;

import com.rutinas.Rutinas;

public class ServiceOSP {
	Logger logger;
	MyLogger mylog;
	Rutinas mylib = new Rutinas();
	Osp osp;
	
	public ServiceOSP(Osp osp, MyLogger mylog) {
		this.osp = osp;
		this.mylog = mylog;
		this.logger = mylog.getLogger();
	}
	
	//variables de intercambio
	private boolean existParams;
	private List<String> parsedParams = new ArrayList<>();
	
	public boolean execute() throws Exception {
		try {
			boolean exitStatus = false;
			
			mylog.info("Instanciando Clase Metadata...");
			MetaData md = new MetaData(osp.getDbType(), mylog);

			mylog.info("Conectando a Base de Datos Cliente...");
			md.open(osp.getServerIP(), osp.getDbName(), osp.getDbPort(), osp.getDbLoginUser(), osp.getDbLoginPass(), 10000);
			
			if (md.isConnected()) {
				mylog.info("Conectado a Database Cliente");
				String ospOwner = osp.getDbOwnerUser();
				String ospName = osp.getOspName();
				List<String> ospParams = parsedParams;
				
				mylog.info("Executing "+ospName+"...");
				if (md.executeProcedure(ospOwner, ospName, ospParams)) {
					exitStatus = true;
					mylog.info("Executing "+ospName+" SUCCESS");
				} else {
					mylog.info("Executing "+ospName+" ERROR");
				}
			} else {
				mylog.error("No es posible conectarse a Database cliente");
			}
			
			return exitStatus;
		} catch (Exception e) {
			throw new Exception("execute(): "+e.getMessage());
		}
	}
	
    public void genParsedOspParams() throws Exception {
    	try {
    		mylog.info("Analizando Parametros del Store procedure...");
    		
    		Map<String, OspParam> mapParams = osp.getMapOspParam();
    		
    		this.existParams = false;
    		
    		if (mapParams.size()>0) {
    			mylog.info("Numero de Parametros leidos: "+mapParams.size());

    			mylog.info("Parseando parametros leidos...");
    			for (Map.Entry<String, OspParam> entry : mapParams.entrySet())  {
    				
    				mylog.info("Param: "+entry.getKey()+" "+entry.getValue().getDesc()+" "+entry.getValue().getType()+" "+entry.getValue().getDataType()+" "+entry.getValue().getValue());
    				
					String type = entry.getValue().getType(); //IN OUT INOUT FN
					String dataType = entry.getValue().getDataType(); //VARCHAR INTEGER
					String value = entry.getValue().getValue();
					
					if (type.equals("FN")) {
						type = "IN";
						value = mylib.parseFnParam(value);
					}
					
					mylog.info("Parsed: "+type+"&"+dataType+"&"+value);
					parsedParams.add(type+"&"+dataType+"&"+value);
    			}
    			
    			if (parsedParams.size()>0) {
    				this.existParams = true;
    			} 
    		} else {
    			mylog.info("Store Procedure sin parametros informados!!");
    		}
    		
    	} catch (Exception e) {
    		throw new Exception("genParsedOspParams(): "+e.getMessage());
    	}
    }
    

	public boolean isExistParams() {
		return existParams;
	}

	public void setExistParams(boolean existParams) {
		this.existParams = existParams;
	}

}