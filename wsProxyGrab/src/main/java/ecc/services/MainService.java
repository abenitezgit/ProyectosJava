package ecc.services;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import ecc.model.Info;
import ecc.utiles.GlobalParams;

public class MainService {
	Rutinas mylib = new Rutinas();
	Logger logger = Logger.getLogger("MainService");
	GlobalParams gParams;
	
	public MainService(GlobalParams m) {
		gParams = m;
	}
	
	public boolean initComponents() {
		boolean isExitOk = false;
		try {
			if (mylib.fileExist(gParams.getFileConfig())) {
				//Abriendo archivo de configuracion
				Properties conf = new Properties();
				conf.load(new FileInputStream(gParams.getFileConfig()));

				//Recuperando datos de parametros
				
				//Usando varables definidas en constructor de globalArea
				String clusterName = gParams.getClusterName();
				String appName = gParams.getAppName();
				
				//Definiendo keys para buscar variables
				String keyBase = clusterName+"."+appName+".";
				
				//Recupera datos para Rest API
				
				Info info = new Info();
				
				info.setRestServer(conf.getProperty(keyBase+"restServer"));
				info.setRestPort(conf.getProperty(keyBase+"restPort"));
				info.setRestUrlBase(conf.getProperty(keyBase+"restUrlBase"));
				info.setRestUrlExtract(conf.getProperty(keyBase+"restUrlExtract"));
				info.setRestContentType(conf.getProperty(keyBase+"restContentType"));
				info.setRestMediaWork(conf.getProperty(keyBase+"restMediaWork"));
				
				gParams.setInfo(info);
				
				isExitOk = true;
			} else {
				mylib.console(1,"No se encuentra archivo de config");
				throw new Exception("No se encuentra archivo de config "+gParams.getFileConfig());
			}
			
			return isExitOk;
		} catch (Exception e) {
			mylib.console(1,"Error initcomponents(): "+e.getMessage());
			return false;
		}
	}

}
