package ecc.services;

import java.util.List;

import ecc.model.Chat;

public interface IMainService {
	
	public void initComponents() throws Exception;
	public List<Chat> getInteractsChatID(int chatType) throws Exception;
	public Chat getLastInteractionChatID(int chatType) throws Exception;
	public void putChat(int chatType) throws Exception;
	public void parseDataRequest(String dataInput) throws Exception;
	public void validaDataRequest(int opcion) throws Exception;
	public int lastStatus();
	public List<Chat> getChatText(int chatType);
}
