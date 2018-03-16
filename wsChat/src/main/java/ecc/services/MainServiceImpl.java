package ecc.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.rutinas.Rutinas;

import ecc.model.Chat;
import ecc.model.DataRequest;
import ecc.model.Info;
import ecc.utiles.GlobalParams;

public class MainServiceImpl implements IMainService {
	Logger logger = Logger.getLogger("wsChat");
	Rutinas mylib = new Rutinas();
	GlobalParams gParams;
	ChatService srvChat;
	
	/*
	 * Variables de control
	 */
	private int lastStatus=0;
	
	public MainServiceImpl(GlobalParams m) {
		gParams = m;
		srvChat = new ChatService(gParams);
		
	}
	
	//Getter and Setter

	public int getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(int lastStatus) {
		this.lastStatus = lastStatus;
	}


	@Override
	public void initComponents() throws Exception {
		try {
			setLastStatus(100);
			
			//Lee archivo de configuración
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("config.properties").getFile());
			
			Info info = new Info();
			
			Properties conf = new Properties();
			conf.load(new FileInputStream(file));
			
			info.setConfigPath(conf.getProperty("configPath"));
			info.setConfigFile(conf.getProperty("configFile"));
			info.setCloudName(conf.getProperty("cloudName"));
			
			Properties f = new Properties();
			String fileProperties = info.getConfigPath()+"/"+info.getConfigFile();
			String cloudName = info.getCloudName();
			
			f.load(new FileInputStream(fileProperties));
			
			info.setZkSolr(f.getProperty(cloudName+".zkSolr"));
			
			gParams.setInfo(info);
			
			setLastStatus(0);
						
		} catch (Exception e) {
			throw new Exception("initComponents: "+e.getMessage());
		}

	}

	@Override
	public List<Chat> getInteractsChatID(int chatType)  {
		// 1: internas
		// 2: conversacion
		// 3: salones (room)
		// 4: grupos 
		try {
			List<Chat> lstChats = new ArrayList<>();
			lstChats = srvChat.getChatData("collchatint","chatint",chatType);
			return lstChats;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
			//throw new Exception("getInteractsChatID: "+e.getMessage());
		}
	}

	@Override
	public void parseDataRequest(String dataInput) throws Exception {
		try {
			DataRequest dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
			
			//Valida Fecha y la separa
			if (dr.getFecha()!=null) {
				Date fecha = mylib.getDate(dr.getFecha(), "yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				cal.setTime(fecha);
				dr.setYear(String.valueOf(cal.get(Calendar.YEAR)));
				dr.setMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
				dr.setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
				dr.setHour(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
				dr.setMinute(String.valueOf(cal.get(Calendar.MINUTE)));
				dr.setSecond(String.valueOf(cal.get(Calendar.SECOND)));
			}

			gParams.setDr(dr);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public void validaDataRequest(int opcion) throws Exception {
		try {
			switch(opcion) {
				case 1:
					//Select Chat
					
					break;
				case 2:
					break;
			}
			
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public void putChat(int chatType) throws Exception {
		try {
			srvChat.executeUpdate(chatType);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public Chat getLastInteractionChatID(int chatType)  {
		// 1: internas
		// 2: conversacion
		// 3: salones (room)
		// 4: grupos 
		try {
			Chat chat = new Chat();
			chat = srvChat.getLastChatData("collchatint","chatint",chatType);
			return chat;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
			//throw new Exception("getInteractsChatID: "+e.getMessage());
		}
	}

	@Override
	public int lastStatus() {
		return getLastStatus();
	}

	@Override
	public List<Chat> getChatText(int chatType) {
		List<Chat> lstChats = new ArrayList<>();
		try {
			ChatService cs = new ChatService(gParams);
			
			setLastStatus(101);
			
			lstChats = cs.getChatText("collchatint","chatint",chatType);
			
			setLastStatus(0);
			return lstChats;
		} catch (Exception e) {
			logger.error("getChatText: "+e.getMessage());
			return lstChats;
		}
	}

}
