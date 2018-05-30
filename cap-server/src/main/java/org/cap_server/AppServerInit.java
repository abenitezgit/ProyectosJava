package org.cap_server;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.json.JSONArray;
import org.model.Info;
import org.model.MonParams;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

/**
 * Copyrigth GBData Ltda.
 * AppName: AppServerInit
 * Version: 1.0.0
 * Fecha Actualización: 22-05-2018
 * Parametros de Ejecucion: $ java -Drole=<role> -DpathConfig=<path> -DfileConfig=<file.properties>
 *
 */


public class AppServerInit {
	//Inicializando el Logger
	static Logger logger = Logger.getLogger("cap-server");
	
	//Referenciando Rutinas
	static Rutinas mylib = new Rutinas();
	
	//Inicializa por Unica vez la GlobalParams()
	static GlobalParams gParams = new GlobalParams();
	
    public static void main( String[] args ) {
        
    	try {
    		//Startup cap-server
    		logger.info("Iniciando cap-server");
    		
    		//Reading External Params
    		logger.info("Leyendo parametros externos");
    		getExternalParams();
    		logger.info("Parametros externos OK!");
    		
    		//Reading FileConfig
    		logger.info("Leyendo Parametros de la Aplicacion");
    		getFileProperties();
    		logger.info("Parametros de la Aplicacion OK!");
    		
    		//Valida conexion a Metadata
    		logger.info("Validando Acceso a MetaData");
    		valDbConnect();
    		logger.info("Acceso a MetaData OK!");
    		
    		//Recupera parametros del servicio en MetaData
    		logger.info("Leyendo Parametros de Inicio");
    		getDBParams();
    		
    	} catch (Exception e) {
    		logger.error("No es posible iniciar cap-server: "+e.getMessage());
    		logger.error("Finalizando cap-server");
    	}
    }
    
    private static void getExternalParams() throws Exception {
    	gParams.setMonID(System.getProperty("monID"));
    	gParams.setPathConfig(System.getProperty("pathConfig"));
    	gParams.setFileConfig(System.getProperty("fileConfig"));
    	
    	if (	mylib.isNullOrEmpty(gParams.getMonID()) || 
    			mylib.isNullOrEmpty(gParams.getPathConfig()) || 
    			mylib.isNullOrEmpty(gParams.getFileConfig())) {
    		throw new Exception("Unable to read External params");
    		
    	}
    }
    
    private static void getFileProperties() throws Exception {
    	/**
    	 * Valida si archivo properties existe
    	 */
    	
    	if (mylib.fileExist(gParams.getPathConfig()+"/"+gParams.getFileConfig())) {
    		//Abre archivo de configuración
    		Properties conf = new Properties();
    		conf.load(new FileInputStream(gParams.getPathConfig()+"/"+gParams.getFileConfig()));
    		
    		//Carga la clase Info()
    		Info info = new Info();
    		
//    		info.setAgeGapMinute(Integer.valueOf(conf.getProperty("ageGapMinute")));
//    		info.setAgeShowHour(Integer.valueOf(conf.getProperty("ageShowHour")));
//    		info.setApiIP(conf.getProperty("apiIP"));
//    		info.setApiPort(conf.getProperty("apiPort"));
//    		info.setApiURLBase(conf.getProperty("apiURLBase"));
//    		info.setAuthKey(conf.getProperty("authKey"));
//    		info.setConnectType(conf.getProperty("connectType"));
    		info.setDbHostName(conf.getProperty("dbHostName"));
    		info.setDbIP(conf.getProperty("dbIP"));
    		info.setDbName(conf.getProperty("dbName"));
    		info.setDbPass(conf.getProperty("dbPass"));
    		info.setDbPort(Integer.valueOf(conf.getProperty("dbPort")));
    		info.setDbUser(conf.getProperty("dbUser"));
//    		info.setFileProperties(gParams.getFileConfig());
//    		info.setMonHostName(conf.getProperty("monHostName"));
//    		info.setMonIP(conf.getProperty("monIP"));
//    		info.setMonPort(Integer.valueOf(conf.getProperty("monPort")));
//    		info.setPathProperties(gParams.getPathConfig());
//    		info.setSmonHostName(conf.getProperty("smonHostName"));
//    		info.setSmonIP(conf.getProperty("smonIP"));
//    		info.setSmonPort(Integer.valueOf(conf.getProperty("smonPort")));
//    		info.setTxpDB(Integer.valueOf(conf.getProperty("txpDB")));
//    		info.setTxpProcess(Integer.valueOf(conf.getProperty("txpProcess")));
//    		info.setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
//    		info.setTxpSync(Integer.valueOf(conf.getProperty("txpSync")));
//    		info.setMonID(conf.getProperty("monID"));
//    		info.setSmonID(conf.getProperty("SmonID"));
//    		info.setDbTimeOut(Integer.valueOf(conf.getProperty("dbTimeOut")));
//    		info.setDbType(conf.getProperty("dbType"));
    		
    		gParams.setInfo(info);
    		
    	} else {
    		throw new Exception("Unable to read file Config: "+gParams.getPathConfig()+"/"+gParams.getFileConfig());
    	}
    }
    
    private static void valDbConnect() throws Exception {
    	try {
	    	DataAccess da = new DataAccess(gParams);
	    	
	    	da.open();
	    	
	    	if (!da.isConnected() ) {
	    		throw new Exception("Unable to Connect MetaData");
	    	} else {
	    		da.close();
	    	}
    	} catch (Exception e) {
    		throw new Exception("Unable to Connect MetaData");
    	}
    }
    
    private static void getDBParams() throws Exception {
    	DataAccess da = new DataAccess(gParams);
    	
    	try {
    		String resp = da.getDBParams();
    		JSONArray ja = new JSONArray(resp);
    		
    		for(int i=0; i<ja.length(); i++) {
    			MonParams mp = (MonParams) mylib.serializeJSonStringToObject(ja.get(i).toString(), MonParams.class);
    			gParams.getMapMonParams().put(mp.getMonID(), mp);
    		}
    		
    		logger.info("resp: "+mylib.serializeObjectToJSon(gParams.getMapMonParams(), true));
    		
    	} catch (Exception e) {
    		throw new Exception("No es posible leer parametros de inicio desde Metadata: "+e.getMessage());
    	} finally {
			if (da.isConnected()) {
				da.close(); 
			}

    	}
    }
    
}
