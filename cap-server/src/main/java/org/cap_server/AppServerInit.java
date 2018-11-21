package org.cap_server;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.json.JSONArray;
import org.model.MonParams;
import org.services.ThMain;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;

/**
 * Copyrigth GBData Ltda.
 * AppName: AppServerInit
 * Version: 3.0.1
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
    		mylib.console("Iniciando Dispatcher cap-server");
    		
    		//Reading External Params
    		mylib.console("Leyendo parametros externos...");
    		getExternalParams();
    		mylib.console("Parametros externos OK!");
    		mylib.console("External PathConfig: "+gParams.getAppConfig().getPathConfig());
    		mylib.console("External FileConfig: "+gParams.getAppConfig().getFileConfig());
    		mylib.console("External monID: "+gParams.getAppConfig().getMonID());
    		
    		//Reading FileConfig
    		mylib.console("Leyendo Parametros de la Aplicacion...");
    		getFileProperties();
    		mylib.console("Parametros de la Aplicacion OK!");
    		mylib.console("Property dbHostName: "+gParams.getAppConfig().getDbHostName());
    		mylib.console("Property dbIP: "+gParams.getAppConfig().getDbIP());
    		mylib.console("Property dbName: "+gParams.getAppConfig().getDbName());
    		mylib.console("Property dbPort: "+gParams.getAppConfig().getDbPort());
    		mylib.console("Property dbTimeOut: "+gParams.getAppConfig().getDbTimeOut());
    		mylib.console("Property dbUser: "+gParams.getAppConfig().getDbUser());
    		mylib.console("Property dbPass: "+gParams.getAppConfig().getDbPass());
    		mylib.console("TxpMain: "+gParams.getAppConfig().getTxpMain());
    		
    		//Enable Logger
    		mylib.console("Configurando Log4j...");
    		String pathFileName = gParams.getAppConfig().getPathConfig()+"/"+gParams.getAppConfig().getLog4jName();
    		mylib.setupLog4j(pathFileName);
    		
    		//Valida conexion a Metadata
    		mylib.console("Validando Acceso a MetaData...");
    		valDbConnect();
    		mylib.console("Acceso a MetaData OK!");

    		
    		//Inicializa status de thread en false (no se están ejecutando)
    		initStatusThread();
    		
    		//Recupera parametros del servicio en MetaData
    		mylib.console("Leyendo Parametros de servicio desde Metadata...");
    		getDBParams();
    		mylib.console("Parametros de servicios OK!");
    		mylib.console("Parametros de Servcios MonServices: "+mylib.serializeObjectToJSon(gParams.getMapMonParams(), false));
    		
    		//Levanta Main Services
    		mylib.console("Scheduling thMain - Proceso Principal");
    		
			Runnable thMain = new ThMain(gParams);
			gParams.getMapThreadRunnig().put("thMain", true);
			gParams.getExecutorThMain().scheduleWithFixedDelay(thMain, 1000, gParams.getMapMonParams().get(gParams.getAppConfig().getMonID()).getTxpMain(), TimeUnit.MILLISECONDS);
			//executorThMain.execute(thMain);
			
			mylib.console("Proceso principal ha sido agendado!");
			
			mylib.console("Finalizando dispatcher cap-server!");

    	} catch (Exception e) {
    		mylib.console(1,"No es posible iniciar cap-server: "+e.getMessage());
    		mylib.console(1,"Finalizando cap-server");
    	}
    }
        
    private static void initStatusThread() throws Exception {
    	gParams.getMapThreadRunnig().put("thMain", false);
    	gParams.getMapThreadRunnig().put("thListener", false);
    	gParams.getMapThreadRunnig().put("thSync", false);
    	gParams.getMapThreadRunnig().put("thDBAccess", false);
    	gParams.getMapThreadRunnig().put("thProcess", false);
    	gParams.getMapThreadRunnig().put("TimerExecWeek", false);
    }
    
    private static void getExternalParams() throws Exception {
    	try {
	    	gParams.getAppConfig().setMonID(System.getProperty("monID"));
	    	gParams.getAppConfig().setPathConfig(System.getProperty("pathConfig"));
	    	gParams.getAppConfig().setFileConfig(System.getProperty("fileConfig"));
	    	
	    	if (	mylib.isNullOrEmpty(gParams.getAppConfig().getMonID()) || 
	    			mylib.isNullOrEmpty(gParams.getAppConfig().getPathConfig()) || 
	    			mylib.isNullOrEmpty(gParams.getAppConfig().getFileConfig())) {
	    		throw new Exception("Error recuperando parametros de entrada");
	    	}
    	} catch (Exception e) {
    		throw new Exception("getExternalParams(): "+e.getMessage());
    	}
    }
    
    private static void getFileProperties() throws Exception {
    	/**
    	 * Valida si archivo properties existe
    	 */
    	
    	if (mylib.fileExist(gParams.getAppConfig().getPathConfig()+"/"+gParams.getAppConfig().getFileConfig())) {
    		//Abre archivo de configuración
    		Properties conf = new Properties();
    		conf.load(new FileInputStream(gParams.getAppConfig().getPathConfig()+"/"+gParams.getAppConfig().getFileConfig()));
    		
    		gParams.getAppConfig().setAuthKey(conf.getProperty("authKey"));
    		gParams.getAppConfig().setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
    		gParams.getAppConfig().setDbHostName(conf.getProperty("dbHostName"));
    		gParams.getAppConfig().setDbIP(conf.getProperty("dbIP"));
    		gParams.getAppConfig().setDbName(conf.getProperty("dbName"));
    		gParams.getAppConfig().setDbPort(conf.getProperty("dbPort"));
    		gParams.getAppConfig().setDbUser(conf.getProperty("dbUser"));
    		gParams.getAppConfig().setDbPass(conf.getProperty("dbPass"));
    		gParams.getAppConfig().setLog4jLevel(conf.getProperty("log4jLevel"));
    		gParams.getAppConfig().setLog4jName(conf.getProperty("log4jName"));
    		gParams.getAppConfig().setDbTimeOut(Integer.valueOf(conf.getProperty("dbTimeOut")));

    		
    	} else {
    		throw new Exception("Error leyendo archivo de parametros: "+gParams.getAppConfig().getPathConfig()+"/"+gParams.getAppConfig().getFileConfig());
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
    		throw new Exception("Unable to Connect MetaData: "+e.getMessage());
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
    		
    		/**
    		 * Recupera valores de ID para logs
    		 */
    		
    		gParams.setMessageID(gParams.getMapMonParams().get(gParams.getAppConfig().getMonID()).getMessageID());
    		gParams.setLogID(gParams.getMapMonParams().get(gParams.getAppConfig().getMonID()).getLogID());
    		
    	} catch (Exception e) {
    		throw new Exception("No es posible leer parametros de inicio desde Metadata: "+e.getMessage());
    	} finally {
			if (da.isConnected()) {
				da.close(); 
			}

    	}
    }
    
}
