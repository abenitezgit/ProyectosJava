package org.cap_client;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.model.Config;
import org.services.ThMain;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;


/**
 * Copyrigth GBData Ltda.
 * AppName: AppClientInit
 * Version: 1.0.0
 * Fecha Actualización: 22-05-2018
 * Parametros de Ejecucion: $ java -DmonID=<role> -DpathConfig=<path> -DfileConfig=<file.properties>
 *
 */
public class AppClientInit {
	//Inicializando el Logger
	static Logger logger = Logger.getLogger("cap-client");
	
	//Referenciando Rutinas
	static Rutinas mylib = new Rutinas();
	
	//Inicializa por Unica vez la GlobalParams()
	static GlobalParams gParams = new GlobalParams();

	
    public static void main( String[] args ) {
    	try {
    		//Startup cap-server
    		logger.info("Iniciando Dispatcher cap-client");
    		
    		//Reading External Params
    		logger.info("Leyendo parametros externos...");
    		getExternalParams();
    		logger.info("Parametros externos OK!");
    		logger.info("External PathConfig: "+gParams.getPathConfig());
    		logger.info("External FileConfig: "+gParams.getFileConfig());
    		logger.info("External srvID: "+gParams.getSrvID());
    		
    		//Reading FileConfig
    		logger.info("Leyendo Archivo de Properties del Servicio...");
    		getFileProperties();
    		logger.info("Archivo de Properties del Servicio OK!");
    		logger.info("ConnectTypeMon: "+gParams.getCfgParams().getConnectTypeMon());
    		    		
    		//Inicializa status de thread en false (no se están ejecutando)
    		initStatusThread();
    		
    		
    		//Levanta Main Services
    		logger.info("Scheduling thMain - Proceso Principal");
    		ScheduledExecutorService executorThMain = Executors.newSingleThreadScheduledExecutor();
			Runnable thMain = new ThMain(gParams);
			gParams.getMapThreadRunnig().put("thMain", true);
			executorThMain.scheduleWithFixedDelay(thMain, 1000, gParams.getCfgParams().getTxpMain(), TimeUnit.MILLISECONDS);
			//executorThMain.execute(thMain);
			logger.info("Proceso principal ha sido agendado!");
			
			logger.info("Finalizando dispatcher cap-client!");

    	} catch (Exception e) {
    		logger.error("No es posible iniciar cap-client: "+e.getMessage());
    		logger.error("Finalizando cap-client");
    	}

    }
    
    private static void initStatusThread() throws Exception {
    	gParams.getMapThreadRunnig().put("thMain", false);
    }
    
    private static void getExternalParams() throws Exception {
    	gParams.setSrvID(System.getProperty("srvID"));
    	gParams.setPathConfig(System.getProperty("pathConfig"));
    	gParams.setFileConfig(System.getProperty("fileConfig"));
    	
    	if (	mylib.isNullOrEmpty(gParams.getSrvID()) || 
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
    		Config cfg = new Config();
    		
    		cfg.setConnectTypeMon(conf.getProperty("connectTypeMon"));
    		cfg.setMonHostName(conf.getProperty("monHostName"));
    		cfg.setMonIP(conf.getProperty("monIP"));
    		cfg.setMonPort(conf.getProperty("monPort"));
    		cfg.setsMonHostName(conf.getProperty("sMonHostName"));
    		cfg.setsMonIP(conf.getProperty("sMonIP"));
    		cfg.setsMonPort(conf.getProperty("sMonPort"));
    		cfg.setsUrlBase(conf.getProperty("sUrlBase"));
    		cfg.setsUrlPort(conf.getProperty("sUrlPort"));
    		cfg.setsUrlServer(conf.getProperty("sUrlServer"));
    		cfg.setUrlBase(conf.getProperty("urlBase"));
    		cfg.setUrlPort(conf.getProperty("urlPort"));
    		cfg.setUrlServer(conf.getProperty("urlServer"));
    		cfg.setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
    		cfg.setAuthKey(conf.getProperty("authKey"));
    		
    		gParams.setCfgParams(cfg);
    		
    	} else {
    		throw new Exception("Unable to read file Config: "+gParams.getPathConfig()+"/"+gParams.getFileConfig());
    	}
    }
}
