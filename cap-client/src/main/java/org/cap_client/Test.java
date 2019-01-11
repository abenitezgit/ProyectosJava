package org.cap_client;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.json.JSONArray;

import com.api.HBase2API;
import com.api.Solr6API;
import com.rutinas.Rutinas;

public class Test {
	Logger logger = Logger.getLogger("test");
	static Rutinas mylib = new Rutinas();

	public static void main(String[] args) {
		
		try {
			
			String pathFileName = "/usr/local/capProject/work/RP2812201801.TXT";

		    List<String> lines = Collections.emptyList();
		    
		    Charset charset = Charset.forName("ISO-8859-1");
		    
		    lines = Files.readAllLines(
		    			Paths.get(pathFileName), charset);
		    
		    System.out.println(lines.size());
			
		} catch (Exception e) {
			System.out.println("Exception: "+ e.getMessage());
			
		}

	}

}
