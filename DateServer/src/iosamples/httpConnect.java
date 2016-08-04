/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iosamples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class httpConnect {
    
    
    public static void main(String args[]) throws MalformedURLException, IOException {
    
        //URL url = new URL("http://10.33.66.30:8090/Service1.svc/rest/getAuthenticate/11111111/1234");
        //URL url = new URL("https://api.github.com/events");
        //URL url = new URL("https://api.github.com//users/abenitezgit/gists");
        //URL url = new URL("https://api.github.com/users/abenitezgit/repos");
        //URL url = new URL("https://api.github.com/search/repositories?q=ProyectosJava&sort=stars&order=desc");
        //URL url = new URL("https://api.github.com/repos/abenitezgit/ProyectosJava/branches");
        URL url = new URL("https://api.github.com/repos/abenitezgit/ProyectosJava");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(
        (conn.getInputStream())));
        
        System.out.println(conn.getContentType());
        
        System.out.println(conn.toString());

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
                System.out.println(output);
        }
        
        //JSONArray headerArray = new JSONArray(output);
        //JSONObject header = new JSONObject(output);
        //JSONArray header = new JSONArray(output);
        
        //System.out.println(header.toString());
        
        
        

        conn.disconnect();
    }
}
