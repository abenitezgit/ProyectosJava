/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author andresbenitez
 */
public class restClient {
    
    public static void main(String args[]) throws IOException {
        HttpClient client = new DefaultHttpClient();
        
        
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/status");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/version");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/version/hive?user.name=hive");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/version/hadoop?user.name=hadoop");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/ddl/database?user.name=hive&like=*");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/ddl/database/hgrab?user.name=hive");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/ddl/database/default?user.name=hbase&like=*");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/jobs/job_1471360327714_0013?user.name=hive");
        //HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/jobs?user.name=hive&showall=true");
        HttpGet request = new HttpGet("http://hwk10.e-contact.cl:50111/templeton/v1/jobs?user.name=hive&fields=*");
        
        
        HttpResponse response = client.execute(request);
        
        System.out.println(response.getStatusLine());
        System.out.println(response.getProtocolVersion());
        System.out.println(response.getEntity().getContentType());
        System.out.println(response.getEntity().getContent());
        
        
        
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = "";

        List<String> lstResponse = new ArrayList<>();
        
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            lstResponse.add(line);
          }
        
		URL url = new URL("http://hwk10.e-contact.cl:50111/templeton/v1/jobs?user.name=hive&fields=*");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");        
        
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

                String salida="";
		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
                        salida=output;
		}

		conn.disconnect();                
                
                JSONArray ja = new JSONArray(lstResponse.get(0));
                
                System.out.println(ja.toString());
                System.out.println(ja.length());
                System.out.println(ja.get(0).toString());
                System.out.println(ja.get(1).toString());
                System.out.println(ja.get(2).toString());
                
                String id = ja.getJSONObject(2).getString("id");
                String status = ja.getJSONObject(2).getJSONObject("detail").getJSONObject("status").getString("state");
                
                System.out.println("id: "+id);
                System.out.println("status: "+status);
                
                
                
        //JSONArray ja = new JSONArray(line);
//        System.out.println(ja.toString());
        
        
//        for (int i=0; i<ja.length(); i++) {
//            //String objID = ja.getJSONObject(i).getString("id");
//            //System.out.println("ObjID: "+objID);
//        }
        
        
    }

}
