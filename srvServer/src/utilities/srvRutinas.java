/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataClass.AssignedTypeProc;
import dataClass.PoolProcess;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.htrace.fasterxml.jackson.core.JsonProcessingException;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class srvRutinas {
    /**
     * Clase de Traspaso de Datos
     */
    static globalAreaData gDatos;
        
    //Carga Clase log4
    Logger logger = Logger.getLogger("srvRutinas");    
    
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
            //System.out.println(formatter.getTimeZone());
            today = new Date();
            return formatter.format(today);  
        } catch (Exception e) {
            return null;
        }
    }
    
    public String sendError(int errCode, String errMesg) {
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
    
        jData.put("errMesg", errMesg);
        jData.put("errCode", errCode);
        
        jHeader.put("data",jData);
        jHeader.put("result", "error");
            
        return jHeader.toString();
    }
    
    public String sendError(int errCode) {
        String errMesg;
        
        switch (errCode) {
            case 90: 
                    errMesg = "error de entrada";
                    break;
            case 80: 
                    errMesg = "servicio offlne";
                    break;
            case 60:
                    errMesg = "TX no autorizada";
                    break;
            case 10:
                    errMesg = "procID ya se encuentra en poolProcess...";
                    break;
            default: 
                    errMesg = "error desconocido";
                    break;
        }
        
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
    
        jData.put("errMesg", errMesg);
        jData.put("errCode", errCode);
        
        jHeader.put("data",jData);
        jHeader.put("result", "error");
            
        return jHeader.toString();
    }
    
    public String sendOkTX() {
        
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
        
        jHeader.put("data", jData);
        jHeader.put("result", "OK");
            
        return jHeader.toString();
    }
    
    public static double getProcessCpuLoad() throws Exception {

        
        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }    
    
    public String sendDataKeep(String type) {
        //
        Runtime instance = Runtime.getRuntime();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            
            //LLenando Datos de Prueba
//                PoolProcess pool = new PoolProcess();
//
//                pool.setTypeProc("ETL");
//                pool.setProcID("ETL00001");
//                pool.setuStatus("Success");
//                pool.setStatus("Finished");
//
//                gDatos.getLstPoolProcess().clear();
//                gDatos.getLstPoolProcess().add(pool);
            //
            
            //Actualiza Fecha de UpdateTime
            gDatos.getServiceStatus().setSrvUpdateTime(new Date());
            
            // Se genera la salida de la lista 
            JSONObject jHeader = new JSONObject();
            JSONObject jData = new JSONObject();
            
            JSONObject jo = new JSONObject(mapper.writeValueAsString(gDatos.getServiceStatus()));
            JSONArray jaAss = new JSONArray(mapper.writeValueAsString(gDatos.getLstAssignedTypeProc()));
            JSONArray jaAct = new JSONArray(mapper.writeValueAsString(gDatos.getLstActiveTypeProc()));
            JSONArray jaPool = new JSONArray(mapper.writeValueAsString(gDatos.getLstPoolProcess()));
            
            jo.put("lstAssignedTypeProc", jaAss);
            jo.put("lstActiveTypeProc", jaAct);
            jo.put("lstPoolProcess", jaPool);
            
            jData.put("ServiceStatus", jo);
            
            jHeader.put("data", jData);
            
            if (type.equals("keep")) {
                jHeader.put("auth", gDatos.getServiceInfo().getAuthKey());
                jHeader.put("request", "keepAlive");
            
            } else {
                jHeader.put("result", "OK");
            }
            
            return jHeader.toString();
        } catch (JsonProcessingException | JSONException e) {
            return sendError(10,e.getMessage());
        }
    }
    
    public synchronized void updateAssignedProcess(JSONObject jData) {
        try {
            AssignedTypeProc assignedTypeProc;
            
            JSONArray jArray = jData.getJSONArray("AssignedTypeProc");
            int numItems = jArray.length();
            
            gDatos.getLstAssignedTypeProc().clear();
            
            for (int i=0; i<numItems; i++) {
                assignedTypeProc = (AssignedTypeProc) serializeJSonString(jArray.get(i).toString(), AssignedTypeProc.class);
                gDatos.getLstAssignedTypeProc().add(assignedTypeProc);
            }
              
            int numItemsList = gDatos.getLstAssignedTypeProc().size();
            if (numItemsList>0) {
                gDatos.getServiceStatus().setIsAssignedTypeProc(true);
            } else {
                gDatos.getServiceStatus().setIsAssignedTypeProc(false);
            }
            
        } catch (JSONException | IOException e) {
            logger.error("Error actualizando Asignacion de Procesos: " + e.getMessage());
        }
    }
    
    public String sendPoolProcess() {
        try {

            return null;
        } catch (Exception e) {
            return sendError(1, "error en send pool process: "+e.getMessage());
        }
    }
    
    public int enqueProcess(JSONObject jData) {
        try {
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
    
    public void updateActiveProcess(String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
            JSONArray rows = ds.getJSONArray("params");
            JSONObject row;
            
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            
            if (rows.length()>0) {
            
                List<String> lstActiveProcess;
                List<String> newListActiveProcess = new ArrayList<>();
                String procType;
                String threadActive;
                String threadMax;
                boolean typeExist;

                lstActiveProcess = null; //gDatos.getActiveTypeProc();
                int numProc = lstActiveProcess.size();

                for (int j=0; j<rows.length(); j++) {
                    row = rows.getJSONObject(j);
                    typeExist = false;
                    for (int i=0; i<numProc; i++) {
                        JSONObject proc = new JSONObject(lstActiveProcess.get(i));
                        procType = proc.get("procType").toString();
                        threadActive = proc.get("threadActive").toString();
                        threadMax = proc.get("threadMax").toString();
                        if (row.get("procType").toString().equals(procType)) {
                            jo.put("procType", procType);
                            jo.put("threadActive", threadActive);
                            jo.put("threadMax",row.get("threadMax").toString());
                            typeExist = true;
                        }
                    }
                    //Si NO enconctró un tipo de proceso en la lista actual
                    //deja el que se esta informando.
                    if (!typeExist) {
                        jo.put("procType", row.get("procType").toString());
                        jo.put("threadActive", row.get("threadActive").toString());
                        jo.put("threadMax",row.get("threadMax").toString());
                    }
                    ja.put(jo);
                }
            }   
        } catch (Exception e) {
            sysOutln(e.getMessage());
        }
    }
    
    public String sendDate() {
        try {
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();

            jo.put("fecha", getDateNow("yyyy-MM-dd HH:mm:ss"));
            ja.put(jo);
            mainjo.put("data", ja);
            mainjo.put("result", "OK");
            return mainjo.toString();
        } catch (Exception e) {
            return sendError(99, e.getMessage());
        }
    }
    
    public String serializeObjectToJSon (Object object, boolean formated) throws IOException {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);
        
        return mapper.writeValueAsString(object);
    }
    
    public Object serializeJSonString (String parseJson, Class className) throws IOException {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        
        return mapper.readValue(parseJson, className);
    }    
}
