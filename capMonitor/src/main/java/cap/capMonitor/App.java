package cap.capMonitor;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import cap.model.Info;
import cap.service.ThMain;
import cap.utiles.GlobalParams;

/**
 * srvService
 *
 */
public class App {
	static Logger logger = Logger.getLogger("capMonitor");
	static GlobalParams gParams = new GlobalParams();
	static Rutinas mylib = new Rutinas();
	static ScheduledExecutorService executorThMain = Executors.newSingleThreadScheduledExecutor();
	
    public static void main( String[] args ) {
        try {
	        	logger.info("Iniciando Servcio capMonitor");
	        	logger.info("Recuperando Parámetros Globales...");
	        	
	        	//Recupera archivo de configuracion ingresado por parametros
	        	gParams.setPathConfig(System.getProperty("path"));
	        	gParams.setFileConfig(System.getProperty("fileConfig"));
	        	
	        	logger.info("PathConfig:  "+gParams.getPathConfig());
	        	logger.info("FileConfig: "+gParams.getFileConfig());
	        	
	        	if (!mylib.isNullOrEmpty(gParams.getPathConfig()) && !mylib.isNullOrEmpty(gParams.getFileConfig())) {
	        		//Recupera valores de archivo de configuracion
	        		switch(initComponents()) {
	        		case 0:
	        			logger.info("Parametros leidos correctamente!!");
	        			logger.info("Iniciando Modulo Controlador Principal");
	        			
        				Runnable thMain = new ThMain(gParams);
        				executorThMain.scheduleWithFixedDelay(thMain, 1000, gParams.getInfo().getTxpMain(), TimeUnit.MILLISECONDS);
					
        				logger.info("Se ha iniciado correctamente el Thread ThMain");
	        			
	        			break;
	        		case 98:
	            		logger.error("No se pudo leer parametros de configuración");
	            		logger.error("Abortanto servicio!");
	        			break;
	        		case 99:
	        			logger.error("No se puede abrir archivo de config");
	        			logger.error("Abortanto servicio!");
	        			break;
	        		}
	        		
	        	} else {
	        		logger.error("Debe ingresar ubicacion de archivo properties");
	        		logger.error("Abortanto servicio!");
	        	}
	        	logger.info("Finalizando exitosamente el Startup");
        } catch (Exception e) {
        		logger.error("Error Iniciando Servicio capMonitor: "+e.getMessage());
        		logger.error("Abortanto servicio!");
        }
    }
    
    private static int initComponents() {
	    	int errCode=99;
	    	try {
	    		Info  info = new Info();
	    		
	    		//Abriendo archivo de Config
	    		String pathFileConfig = gParams.getPathConfig()+"/"+gParams.getFileConfig();
	    		
			if (mylib.fileExist(pathFileConfig)) {
				//Abriendo archivo de configuracion
				Properties conf = new Properties();
				conf.load(new FileInputStream(pathFileConfig));
				
				//Recuperando parametros
				errCode=98;
				info.setAgeGapMinute(Integer.valueOf(conf.getProperty("ageGapMinute")));
				info.setAgeShowHour(Integer.valueOf(conf.getProperty("ageShowHour")));
				info.setApiIP(conf.getProperty("apiIP"));
				info.setApiPort(conf.getProperty("apiPort"));
				info.setApiURLBase(conf.getProperty("apiURLBase"));
				info.setAuthKey(conf.getProperty("authKey"));
				info.setConnectType(conf.getProperty("connectType"));
				info.setDbHostName(conf.getProperty("dbHostName"));
				info.setDbIP(conf.getProperty("dbIP"));
				info.setDbName(conf.getProperty("dbName"));
				info.setDbPass(conf.getProperty("dbPass"));
				info.setDbPort(Integer.valueOf(conf.getProperty("dbPort")));
				info.setDbUser(conf.getProperty("dbUser"));
				info.setFileProperties(gParams.getFileConfig());
				info.setMonHostName(conf.getProperty("monHostName"));
				info.setMonIP(conf.getProperty("monIP"));
				info.setMonPort(Integer.valueOf(conf.getProperty("monPort")));
				info.setPathProperties(gParams.getPathConfig());
				info.setSmonHostName(conf.getProperty("smonHostName"));
				info.setSmonIP(conf.getProperty("smonIP"));
				info.setSmonPort(Integer.valueOf(conf.getProperty("smonPort")));
				info.setTxpDB(Integer.valueOf(conf.getProperty("txpDB")));
				info.setTxpProcess(Integer.valueOf(conf.getProperty("txpProcess")));
				info.setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
				info.setTxpSync(Integer.valueOf(conf.getProperty("txpSync")));
				info.setMonID(conf.getProperty("monID"));
				info.setSmonID(conf.getProperty("SmonID"));
				info.setDbTimeOut(Integer.valueOf(conf.getProperty("dbTimeOut")));
				info.setDbType(conf.getProperty("dbType"));
				
				gParams.setInfo(info);
				gParams.setMainAvtive(true);
				
				errCode=0;
			} 
	    		
	    		return errCode;
	    	} catch (Exception e) {
	    		logger.error("initComponents: "+e.getMessage());
	    		return errCode;
	    	}
    }
}
