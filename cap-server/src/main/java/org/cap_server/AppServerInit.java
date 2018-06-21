package org.cap_server;


import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.json.JSONArray;
import org.model.Info;
import org.model.MonParams;
import org.services.ThMain;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

/**
 * Copyrigth GBData Ltda.
 * AppName: AppServerInit
 * Version: 1.0.0
 * Fecha Actualización: 22-05-2018
 * Parametros de Ejecucion: $ java -DmonID=<role> -DpathConfig=<path> -DfileConfig=<file.properties>
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
    		logger.info("Iniciando Dispatcher cap-server");
    		
    		//Reading External Params
    		logger.info("Leyendo parametros externos...");
    		getExternalParams();
    		logger.info("Parametros externos OK!");
    		logger.info("External PathConfig: "+gParams.getPathConfig());
    		logger.info("External FileConfig: "+gParams.getFileConfig());
    		logger.info("External monID: "+gParams.getMonID());
    		
    		//Reading FileConfig
    		logger.info("Leyendo Parametros de la Aplicacion...");
    		getFileProperties();
    		logger.info("Parametros de la Aplicacion OK!");
    		logger.info("Property dbHostName: "+gParams.getInfo().getDbHostName());
    		logger.info("Property dbIP: "+gParams.getInfo().getDbIP());
    		logger.info("Property dbName: "+gParams.getInfo().getDbName());
    		logger.info("Property dbPort: "+gParams.getInfo().getDbPort());
    		logger.info("Property dbTimeOut"+gParams.getInfo().getDbTimeOut());
    		logger.info("Property dbUser: "+gParams.getInfo().getDbUser());
    		logger.info("Property dbPass: "+gParams.getInfo().getDbPass());
    		logger.info("TxpMain: "+gParams.getInfo().getTxpMain());
    		
    		//Valida conexion a Metadata
    		logger.info("Validando Acceso a MetaData...");
    		valDbConnect();
    		logger.info("Acceso a MetaData OK!");

    		
    		//Inicializa status de thread en false (no se están ejecutando)
    		initStatusThread();
    		
    		//Recupera parametros del servicio en MetaData
    		logger.info("Leyendo Parametros de servicio desde Metadata...");
    		getDBParams();
    		logger.info("Parametros de servicios OK!");
    		logger.info("Parametros de Servcios MonServices: "+mylib.serializeObjectToJSon(gParams.getMapMonParams(), false));
    		
    		//Levanta Main Services
    		logger.info("Scheduling thMain - Proceso Principal");
    		ScheduledExecutorService executorThMain = Executors.newSingleThreadScheduledExecutor();
			Runnable thMain = new ThMain(gParams);
			gParams.getMapThreadRunnig().put("thMain", true);
			executorThMain.scheduleWithFixedDelay(thMain, 1000, gParams.getMapMonParams().get(gParams.getMonID()).getTxpMain(), TimeUnit.MILLISECONDS);
			//executorThMain.execute(thMain);
			logger.info("Proceso principal ha sido agendado!");
			
			logger.info("Finalizando dispatcher cap-server!");

    	} catch (Exception e) {
    		logger.error("No es posible iniciar cap-server: "+e.getMessage());
    		logger.error("Finalizando cap-server");
    	}
    }
        
    private static void initStatusThread() throws Exception {
    	gParams.getMapThreadRunnig().put("thMain", false);
    	gParams.getMapThreadRunnig().put("thListener", false);
    	gParams.getMapThreadRunnig().put("thSync", false);
    	gParams.getMapThreadRunnig().put("thDBAccess", false);
    	gParams.getMapThreadRunnig().put("thProcess", false);
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
    		
    		info.setAuthKey(conf.getProperty("authKey"));
    		info.setDbHostName(conf.getProperty("dbHostName"));
    		info.setDbIP(conf.getProperty("dbIP"));
    		info.setDbName(conf.getProperty("dbName"));
    		info.setDbPass(conf.getProperty("dbPass"));
    		info.setDbPort(Integer.valueOf(conf.getProperty("dbPort")));
    		info.setDbUser(conf.getProperty("dbUser"));
    		info.setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
    		
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
    		
    	} catch (Exception e) {
    		throw new Exception("No es posible leer parametros de inicio desde Metadata: "+e.getMessage());
    	} finally {
			if (da.isConnected()) {
				da.close(); 
			}

    	}
    }
    
}
