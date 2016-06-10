/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class srvRutinas {
    globalAreaData gDatos;
    
    //Constructor de la clase
    //
    public srvRutinas(globalAreaData m) {
        gDatos = m;
    }
    
    public void sysOutln(Object obj) {
        System.out.println(obj);
    }
    
    public String getDateNow(String xformat) {
        try {
            //Extrae Fecha de Hoy
            //
            Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat(xformat);
            System.out.println(formatter.getTimeZone());
            today = new Date();
            return formatter.format(today);  
        } catch (Exception e) {
            return null;
        }
    }
    
    public void incNumTotalExec() {
        gDatos.setNumTotalExec(gDatos.getNumTotalExec()+1);
    }
    
    public void decNumTotalExec() {
        gDatos.setNumTotalExec(gDatos.getNumTotalExec()-1);
    }

    public void incNumProcExec() {
        gDatos.setNumProcExec(gDatos.getNumProcExec()+1);
    }

    public void decNumProcExec() {
        gDatos.setNumProcExec(gDatos.getNumProcExec()-1);
    }
    
    public String appendJSonParam(String cadena, String param, String valor) {
        String output="{}";
        
        try {
            if (cadena==null) {cadena="{}";}
            
            JSONObject obj = new JSONObject(cadena);
            obj.append(param, valor);
            output = obj.toString();
        
            return output;
        } catch (Exception e) {
            return output;
        }
    }
    
    public String sendError(int errCode, String errDesc) {
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainjo = new JSONObject();
        
        jo.put("errMesg", errDesc);
        jo.put("errCode", errCode);
        
        ja.put(jo);
        
        mainjo.put("params",ja);
        mainjo.put("request", "response");
            
        return mainjo.toString();    
    }
    
    public String sendError(int errCode) {
        String errDesc;
        
        switch (errCode) {
            case 90: 
                    errDesc = "error de entrada";
                    break;
            case 80: 
                    errDesc = "servicio offlne";
                    break;
            case 60:
                    errDesc = "TX no autorizada";
                    break;
            default: 
                    errDesc = "error desconocido";
                    break;
        }
        
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainjo = new JSONObject();
        
        jo.put("errMesg", errDesc);
        jo.put("errCode", errCode);
        
        ja.put(jo);
        
        mainjo.put("params",ja);
        mainjo.put("request", "response");
            
        return mainjo.toString();
    }

    public String sendOkTX() {
        
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainjo = new JSONObject();
        
        jo.put("errMesg", "");
        jo.put("errCode", 0);
        
        ja.put(jo);
        
        mainjo.put("params",ja);
        mainjo.put("request", "response");
            
        return mainjo.toString();
    }


    public String getStatusServices() {
        try {
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();

            for (int i=0; i<gDatos.getServiceStatus().size(); i++) {
                ja.put(gDatos.getServiceStatus().get(i));
            }
            mainjo.put("params", ja);
            mainjo.put("request", "response");

            return mainjo.toString();
        } catch (Exception e) {
            return sendError(0,e.getMessage());
        }
    }
    
    public String getAuthData (String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
               
            return ds.get("auth").toString();
        } catch (Exception e) {
            return sendError(0,e.getMessage());
        }
    }
    
    public String updateStatusServices(String inputData) {
        try {
            
            List<String> inputList = new ArrayList<>();
            List<String> gDatosList = new ArrayList<>();
            
            inputList = getDataParams(inputData);
            gDatosList = gDatos.getServiceStatus();
            
            if (gDatosList.size()>0) {
                for (int i=0; i<inputList.size(); i++) {
                    JSONObject ds = new JSONObject(inputList.get(i));

                    List<String> findlist = gDatosList.stream().filter(item -> item.contains(ds.get("srvName").toString())).collect(Collectors.toList());

                    gDatosList.removeAll(findlist);
                }
            }
            
            for (int i=0; i<inputList.size(); i++) {
                gDatosList.add(inputList.get(i));
            }
            
            gDatos.setServiceStatus(gDatosList);

            return sendOkTX();
        } catch (Exception e) {
            return sendError(0,e.getMessage());
        }
    }
    
    public String sendDate() {
        try {
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();

            jo.put("fecha", getDateNow("yyyy-MM-dd HH:mm:ss"));
            ja.put(jo);
            mainjo.put("params", ja);
            mainjo.put("request", "response");
            return mainjo.toString();
        } catch (Exception e) {
            return sendError(99, e.getMessage());
        }
    }
    
    public String getRequest(String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
            
            return ds.get("request").toString();
        } catch (Exception e) {
            return sendError(50); 
        }
    } 
      
    public List<String> getDataParams(String inputData) {
        List<String> result = new ArrayList<>();
        
        JSONObject ds = new JSONObject(inputData);
        JSONArray rows = ds.getJSONArray("params");
        JSONObject row;
        
        for (int i=0; i<rows.length(); i++) {
            row = rows.getJSONObject(i);
            result.add(row.toString());
        }
        return result;
    }
}
