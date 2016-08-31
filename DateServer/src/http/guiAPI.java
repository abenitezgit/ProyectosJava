/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class guiAPI {
    static int conectionTimeout;
    static int readTimeout;
    static Logger logger = Logger.getLogger("guiAPP");
    
    public guiAPI() {
        conectionTimeout = 15000;
        readTimeout = 15000;
    }
    
    
    public static void main(String args[]) throws MalformedURLException, ProtocolException, IOException {
    
        /**
         * GET Method
         */
        String requestURLget="http://10.33.16.254:8090/Service1.svc/rest/getAuthenticate/11111111/1234";
        
        URL urlGet;
        urlGet = new URL(requestURLget);
        HttpURLConnection httpGet = (HttpURLConnection) urlGet.openConnection();
//        //conn.setReadTimeout(readTimeout);
//        //conn.setConnectTimeout(conectionTimeout);
        httpGet.setRequestMethod("GET");
//        httpGet.setDoInput(true);
//        httpGet.setDoOutput(true);
        httpGet.setRequestProperty("Accept", "application/json");
//        
        BufferedReader br = new BufferedReader(new InputStreamReader(httpGet.getInputStream(), "UTF-8"));
//        
        String output = "";

        if ((output = br.readLine()) != null){
            System.out.println("peformGetCallResultado = "+output);
        }
//
        httpGet.disconnect();
        
        
        
        HashMap<String, Object> data = new HashMap<String, Object>();
        
        
        URL url;
        String response = "";
        
//        data.put("treeID", "PERSONAS");
//        String requestURL="http://10.33.16.254:8090/Service1.svc/rest/getPostTreeInfo";

        data.put("treeID", "PERSONAS");
        data.put("nodeID", "1");
        //String requestURL="http://10.33.16.254:8090/Service1.svc/rest/getPostNode";
        String requestURL="http://10.33.16.254:8090/Service1.svc/rest/getPostNodeActions";


        
        url = new URL(requestURL);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        //conn.setReadTimeout(readTimeout);
        //conn.setConnectTimeout(conectionTimeout);
        http.setRequestMethod("POST");
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        
        //byte[] out = "{\"treeID\":\"PERSONAS\"}" .getBytes(StandardCharsets.UTF_8);
        
        
        System.out.println(data.toString());
        
        byte[] out = serializeObjectToJSon(data, false) .getBytes(StandardCharsets.UTF_8);
        
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        
        int code = http.getResponseCode();
        
        //if code!=200 error
        
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
        

    
    }
    
        /*
    * Metodo General para realizar la consulta de un Web Service REST (VIA POST)
    * */
    public static String performPostCall(String requestURL,
        HashMap<String, Object> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

//            System.out.println("peformPostCallURL = "+requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(conectionTimeout);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


            OutputStream os = conn.getOutputStream();
            if (postDataParams != null){
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams.toString());
                //String urlParameters = "treeID=PERSONAS";
                
                JSONObject jo = new JSONObject();
                
                jo.put("treeID", "PERSONAS");
                writer.write(jo.toString());

                writer.flush();
                writer.close();
            }

            os.close();
            int responseCode=conn.getResponseCode();

//            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
//            }else {
//                System.out.println(responseCode);
//                response="";
//                throw new Exception(responseCode+"");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    
    //Metodo General para Adjuntar la Data VIA POST
    private static String getPostDataString(HashMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, Object> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            if (entry.getValue() instanceof List){
                List<String> lista = (List) entry.getValue();
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                String valores = "";

                for(String valor : lista) valores += valor+"|";

                result.append(URLEncoder.encode(valores, "UTF-8"));
            }else{
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }

        }

        return result.toString();
    }    
    
    /*
    * Metodo General para realizar la consulta de un Web Service REST (VIA GET)
    *
    * */
    public static String performGetCall(String strUrl){
        String output = "";
        try {

            URL url = new URL(strUrl);//new URL("http://localhost:8080/RESTfulExample/json/product/get");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("peformGetCallFailed : HTTP error code : "+ conn.getResponseCode());
                throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            if ((output = br.readLine()) != null){
                System.out.println("peformGetCallResultado = "+output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            System.err.println("peformGetCall Failed : MalformedURLException : " + e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("peformGetCall Failed : IOException : " + e.getMessage());
            e.printStackTrace();

        }
        return output;
    }    
    
    public static String serializeObjectToJSon (Object object, boolean formated) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);

            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error serializeObjectToJson: "+e.getMessage());
            return null;
        }
    }
    
    public static Object serializeJSonStringToObject (String parseJson, Class className) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(parseJson, className);
        } catch (Exception e) {
            logger.error("Error serializeJSonStringToObject: "+e.getMessage());
            return null;
        }
    }        

    
}
