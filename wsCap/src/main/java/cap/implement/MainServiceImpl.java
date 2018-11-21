package cap.implement;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import cap.model.Config;
import cap.services.IMainService;
import cap.utiles.GlobalParams;

public class MainServiceImpl implements IMainService {
	GlobalParams gParams;
	
	public MainServiceImpl(GlobalParams m) {
		gParams = m;
	}

	@Override
	public void initComponents() throws Exception {
		try {
		
			//Lee archivo de configuración
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("config.properties").getFile());
			
			Properties conf = new Properties();
			conf.load(new FileInputStream(file));
			
			gParams.setFileConfig(conf.getProperty("fileConfig"));
			gParams.setPathConfig(conf.getProperty("pathConfig"));
			
			Properties f = new Properties();
			f.load(new FileInputStream(gParams.getPathConfig()+"/"+gParams.getFileConfig()));
			
			Config config = new Config();
			
			config.setConnectTypeMon(f.getProperty("connectTypeMon"));
			config.setMonHostName(f.getProperty("monHostName"));
			config.setMonIP(f.getProperty("monIP"));
			config.setMonPort(f.getProperty("monPort"));
			config.setsConnectTypeMon(f.getProperty("sConnectTypeMon"));
			config.setsMonHostName(f.getProperty("sMonHostName"));
			config.setsMonIP(f.getProperty("sMonIP"));
			config.setsMonPort(f.getProperty("sMonPort"));
			config.setsUrlBase(f.getProperty("sUrlBase"));
			config.setsUrlPort(f.getProperty("sUrlPort"));
			config.setsUrlServer(f.getProperty("sUrlServer"));
			config.setUrlBase(f.getProperty("urlBase"));
			config.setUrlPort(f.getProperty("urlPort"));
			config.setUrlServer(f.getProperty("urlServer"));
			config.setDbHostName(f.getProperty("dbHostName"));
			config.setDbIP(f.getProperty("dbIP"));
			config.setDbName(f.getProperty("dbName"));
			config.setDbPort(f.getProperty("dbPort"));
			config.setDbUser(f.getProperty("dbUser"));
			config.setDbPass(f.getProperty("dbPass"));
			config.setDbTimeOut(Integer.valueOf(f.getProperty("dbTimeOut")));
			config.setDbType(f.getProperty("dbType"));
			
			gParams.setConfig(config);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	@Override
	public String getMapProc() {
		//IMonService ims = new MonServiceImpl(gParams);
		
		//String response = ims.getMapProc();
		
		return "";
	}
	
}
