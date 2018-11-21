package org.cap_client;

import com.api.SFtpAPI;

public class Test {

	public static void main(String[] args) {
		try {
			
			SFtpAPI sftp = new SFtpAPI();
			
			sftp.setServerIP("wsecc");
			sftp.setUserName("root");
			sftp.setUserPass("entel123");
			
			System.out.println("Conectando a Sitio SFTP...");
			sftp.connect();
			
			if (sftp.isConnect()) {
				
				String localPathFile = "/usr/local/capProject/work/ABASTIBLE_CAMP_BDIA_13112018.csva";
				String remotePathFile = "/usr/local/capProject/work/ABASTIBLE_CAMP_BDIA_13112018.csv";
				
				if (sftp.upload(remotePathFile, localPathFile)) {
					System.out.println("OK");
				} else {
					System.out.println("Error");
				}
				
			}

			
		} catch (Exception e) {
			System.out.println("Exception: "+ e.getMessage());
		}

	}

}
