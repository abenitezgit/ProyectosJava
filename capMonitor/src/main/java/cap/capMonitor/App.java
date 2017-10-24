package cap.capMonitor;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import cap.model.Info;
import cap.utiles.GlobalArea;
import utiles.common.rutinas.Rutinas;

/**
 * srvService
 *
 */
public class App {
	final static String serviceName = "capMonitor";
	static GlobalArea gDatos = new GlobalArea(serviceName);
	static Rutinas mylib = new Rutinas();
	
    public static void main( String[] args ) {
        try {
        	mylib.console("Iniciando Servcio "+serviceName);
        	mylib.console("Recuperando Parámetros Globales...");
        	
        	//Recupera archivo de configuracion ingresado por parametros
        	///usr/local/srvProject/conf/srvService/srvService.properties
        	gDatos.setPathConfig(System.getProperty("path"));
        	
        	if (!mylib.isNullOrEmpty(gDatos.getPathConfig())) {
        		//Recupera valores de archivo de configuracion
        		switch(initComponents(gDatos.getPathConfig())) {
        		case 0:
        			mylib.console("Parametros leidos correctamente!!");
        			
					//Inician Thread ThMain
					Thread thMain = new ThMain(gDatos);
					thMain.setName("thMain");
					thMain.start();
					
					mylib.console("Se ha iniciado correctamente el Thread ThMain");
        			
        			break;
        		case 97:
            		mylib.console(1,"No se pudo leer log4j properties");
            		mylib.console(1,"Abortanto "+serviceName);
        			break;
        		case 98:
            		mylib.console(1,"No se pudo leer parametros de configuración");
            		mylib.console(1,"Abortanto "+serviceName);
        			break;
        		case 99:
            		mylib.console(1,"No se puede abrir archivo de config");
            		mylib.console(1,"Abortanto "+serviceName);
        			break;
        		}
        		
        	} else {
        		mylib.console(1,"Debe ingresar ubicacion de archivo properties");
        		mylib.console(1,"Abortanto "+serviceName);
        	}
        	
        } catch (Exception e) {
        	mylib.console(1,"Error Iniciando Servicio "+serviceName+": "+e.getMessage());
        	mylib.console(1,"Abortanto "+serviceName);
        }
    }
    
    private static int initComponents(String path) {
    	int errCode=99;
    	try {
    		Info  info = new Info();
    		
    		//Abriendo archivo de Config
    		String pathFileConfig = path+"/"+gDatos.getFileConfig();
    		String pathLogConfig = path+"/"+gDatos.getLogConfig();
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
			info.setFileProperties(gDatos.getFileConfig());
			info.setLogProperties(gDatos.getLogConfig());
			info.setMonHostName(conf.getProperty("monHostName"));
			info.setMonIP(conf.getProperty("monIP"));
			info.setMonPort(Integer.valueOf(conf.getProperty("monPort")));
			info.setPathProperties(gDatos.getPathConfig());
			info.setSmonHostName(conf.getProperty("SmonHostName"));
			info.setSmonIP(conf.getProperty("SmonIP"));
			info.setSmonPort(Integer.valueOf(conf.getProperty("SmonPort")));
			info.setTxpDB(Integer.valueOf(conf.getProperty("txpDB")));
			info.setTxpIns(Integer.valueOf(conf.getProperty("txpIns")));
			info.setTxpMain(Integer.valueOf(conf.getProperty("txpMain")));
			info.setTxpSync(Integer.valueOf(conf.getProperty("txpSync")));
			
			//Habilitando Logger
			errCode=97;
			if (mylib.fileExist(pathLogConfig)) {
				PropertyConfigurator.configure(pathLogConfig);
				gDatos.getLogger().setLevel(Level.DEBUG);
//					logger.info("Logger SET Level: "+mylib.getLoggerLevel(logger));
//					logger.trace("Logger Trace Enable");
//					logger.debug("Logger DEBUG Enable");
//					logger.info("Logger INFO Enable");
				
				
				//Habilita ejecución de Threads
//				gDatos.setEnableThMain(true);
//				gDatos.setEnableThExec(true);
				
				errCode=0;
			}
		} 
    		
    		return errCode;
    	} catch (Exception e) {
    		mylib.console(1,"Error en initComponents: "+e.getMessage());
    		return errCode;
    	}
    }
}
