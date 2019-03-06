package org.cap_server;

import java.util.Map;
import java.util.TreeMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Map<String,Object> cols = new TreeMap<>();
		
		cols.put("grpID", "GRP1");
		cols.put("numSecExec", "10");
		cols.put("nOrder", 3);
		cols.put("procID", "proc1");
		cols.put("fecUpdate", "2018-10-10");
		cols.put("status", "RUNNING");
		
		String strOrder = String.valueOf((int) cols.get("nOrder")); 
		
		System.out.println(String.format("%02d", 2));
		
		
		String key = cols.get("grpID")+":"+cols.get("numSecExec")+":"+String.format("%03d", (int) cols.get("nOrder"))+":"+cols.get("procID")+":"+cols.get("fecUpdate")+":"+cols.get("status");
		
		System.out.println(key);
		
	}

}
