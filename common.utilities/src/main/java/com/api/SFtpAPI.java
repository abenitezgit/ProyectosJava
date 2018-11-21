package com.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SFtpAPI {
	private String serverIP;
	private String userName;
	private String userPass;
	private Session session=null;
	
	public boolean upload(String remotePathFile, String localPathFile) throws Exception {
		try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            sftpChannel.put(localPathFile, remotePathFile);
            sftpChannel.exit();

			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean download(String remotePathFile, String localPathFile) throws Exception {
		try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            sftpChannel.get(remotePathFile, localPathFile);
            sftpChannel.exit();

			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<String> getFiles(String remoteDir) throws Exception {
		try {
			List<String> files = new ArrayList<>();
			
            Channel channel = session.openChannel("sftp");
            channel.connect();
            
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            sftpChannel.cd(remoteDir);
            
            Vector<?> filelist = sftpChannel.ls(remoteDir);
            for(int i=0; i<filelist.size();i++){
                LsEntry entry = (LsEntry) filelist.get(i);
                files.add(entry.getFilename());
            }
            
            sftpChannel.disconnect();
            channel.disconnect();

            return files;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public List<String> getFiles(String remoteDir, String pattern) throws Exception {
		try {
			List<String> files = new ArrayList<>();
			
            Channel channel = session.openChannel("sftp");
            channel.connect();
            
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            sftpChannel.cd(remoteDir);
            
            Vector<?> filelist = sftpChannel.ls(remoteDir+"/"+pattern);
            for(int i=0; i<filelist.size();i++){
                LsEntry entry = (LsEntry) filelist.get(i);
                files.add(entry.getFilename());
            }
            
            sftpChannel.disconnect();
            channel.disconnect();

            return files;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void disconnect() throws Exception {
		try {
			session.disconnect();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isConnect() throws Exception {
		try {
			return session.isConnected();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void connect() throws Exception {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, serverIP, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(userPass);
            session.connect();
            
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void connectNoPass() throws Exception {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, serverIP, 22);
            session.setConfig("StrictHostKeyChecking", "yes");
            //session.setPassword(userPass);
            session.connect();
            
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	
}
