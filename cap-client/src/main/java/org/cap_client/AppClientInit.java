package org.cap_client;

import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.services.ThMain;
import org.utilities.GlobalParams;

import com.rutinas.Rutinas;


/**
 * Copyrigth GBData Ltda.
 * AppName: AppClientInit
 * Version: 3.0.0
 * Fecha Actualización: 22-05-2018
 * Parametros de Ejecucion: $ java -DsrvID=<role> -DpathConfig=<path> -DfileConfig=<file.properties>
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
    		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
    		
    		//Startup cap-server
    		mylib.console("Iniciando Dispatcher cap-client");
    		
    		//Reading External Params
    		mylib.console("Leyendo parametros externos...");
    		getExternalParams();
    		mylib.console("Parametros externos OK!");
    		mylib.console("External PathConfig: "+gParams.getAppConfig().getPathConfig());
    		mylib.console("External FileConfig: "+gParams.getAppConfig().getFileConfig());
    		mylib.console("External srvID: "+gParams.getAppConfig().getSrvID());
    		

    		//Reading FileConfig
    		mylib.console("Leyendo Archivo de Properties del Servicio...");
    		getFileProperties();
    		mylib.console("Archivo de Properties del Servicio OK!");
    		mylib.console("ConnectTypeMon: "+gParams.getAppConfig().getConnectTypeMon());

    		//Enable Logger
    		mylib.console("Configurando Log4j...");
    		String pathFileName = gParams.getAppConfig().getPathConfig()+"/"+gParams.getAppConfig().getLog4jName();
    		mylib.setupLog4j(pathFileName);
    		
    		//Levanta Main Services
    		mylib.console("Scheduling thMain - Proceso de Control Principal");
    		//ScheduledExecutorService executorThMain = Executors.newSingleThreadScheduledExecutor();
			
    		Runnable thMain = new ThMain(gParams);
			gParams.getExecutorThMain().scheduleWithFixedDelay(thMain, 1000, gParams.getAppConfig().getTxpMain(), TimeUnit.MILLISECONDS);
			
			//executorThMain.execute(thMain);
			mylib.console("Proceso de Control Principal ha sido Iniciando!");
			
			mylib.console("Finalizando dispatcher cap-client!");

    	} catch (Exception e) {
    		mylib.console(1, "No es posible iniciar cap-client: "+e.getMessage());
    		mylib.console(1, "Finalizando cap-client");
    	}

    }
    
    private static void getExternalParams() throws Exception {
    	try {
    		gParams.getAppConfig().setSrvID(System.getProperty("srvID"));
    		gParams.getAppConfig().setPathConfig(System.getProperty("pathConfig"));
    		gParams.getAppConfig().setFileConfig(System.getProperty("fileConfig"));
    		
    		if ( 	mylib.isNullOrEmpty(gParams.getAppConfig().getSrvID()) ||
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
    		
    		gParams.getAppConfig().setConnectTypeMon(conf.getProperty("connectTypeMon"));
    		gParams.getAppConfig().setMonHostName(conf.getProperty("monHostName"));
    		gParams.getAppConfig().setMonIP(conf.getProperty("monIP"));
    		gParams.getAppConfig().setMonPort(conf.getProperty("monPort"));
    		gParams.getAppConfig().setsMonHostName(conf.getProperty("sMonHostName"));
    		gParams.getAppConfig().setsMonIP(conf.getProperty("sMonIP"));
    		gParams.getAppConfig().setsMonPort(conf.getProperty("sMonPort"));
    		gParams.getAppConfig().setsUrlBase(conf.getProperty("sUrlBase"));
    		gParams.getAppConfig().setsUrlPort(conf.getProperty("sUrlPort"));
    		gParams.getAppConfig().setsUrlServer(conf.getProperty("sUrlServer"));
    		gParams.getAppConfig().setUrlBase(conf.getProperty("urlBase"));
    		gParams.getAppConfig().setUrlPort(conf.getProperty("urlPort"));
    		gParams.getAppConfig().setUrlServer(conf.getProperty("urlServer"));
    		gParams.getAppConfig().setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
    		gParams.getAppConfig().setAuthKey(conf.getProperty("authKey"));
    		gParams.getAppConfig().setLog4jLevel(conf.getProperty("log4jLevel"));
    		gParams.getAppConfig().setLog4jName(conf.getProperty("log4jName"));
    		gParams.getAppConfig().setWorkPath(conf.getProperty("workPath"));
    		
    	} else {
    		throw new Exception("Error leyendo archivo de parametros: "+gParams.getAppConfig().getPathConfig()+"/"+gParams.getAppConfig().getFileConfig());
    	}
    }
}
