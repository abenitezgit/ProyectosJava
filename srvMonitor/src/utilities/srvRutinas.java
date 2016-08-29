/*
 * To change this license header, choose License Headers in Project Properties.nnn
 */
package utilities;

import dataClass.AssignedTypeProc;
import dataClass.PoolProcess;
import dataClass.ServiceStatus;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import srvmonitor.MetaData;

/**
 *
 * @author andresbenitez
 */
public class srvRutinas {
    globalAreaData gDatos;
    Logger logger = Logger.getLogger("srvRutinas");
    
    /**
     * 
     * Inicia Constructor
     * @param m 
     */

    public srvRutinas(globalAreaData m) {
        try {
            gDatos = m;
            gDatos.getServerStatus().setIsLoadRutinas(true);
        } catch (Exception e) {
            gDatos.getServerStatus().setIsLoadRutinas(false);
        }
    }
    
    
    /**
     * 
     * Rutinas Publicas
     * @return 
     */
    
    public String sendFTPservices(String ftpID) {
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
        JSONObject jo;
        JSONArray ja = new JSONArray();
        MetaData metadata = new MetaData(gDatos);
        try {
            if (metadata.isConnected()) {
                String vSQL = "select * from process.tb_ftp where ftpEnable=1 order by ftpID";
                ResultSet rs = (ResultSet) metadata.getQuery(vSQL);
                if (rs!=null) {
                    while (rs.next()) {
                        jo = new JSONObject();
                        jo.put("ftpID",rs.getString("ftpID"));
                        jo.put("ftpDESC", rs.getString("ftpDESC"));
                        jo.put("fileSourceName", rs.getString("fileSourceName"));
                        ja.put(jo);
                    }
                }
                jData.put("details", ja);
                jHeader.put("data", jData);
                jHeader.put("request", "OK");
                return jHeader.toString();
            } else {
                logger.error("Se perdió conexión a Metadata");
                return sendError(0);
            }
            
        } catch (SQLException | JSONException e) {
            return sendError(0);
        }
    }
    
    public String getDateNow() {
        try {
            //Extrae Fecha de Hoy
            //
            Date today;
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(formatter.getTimeZone());
            today = new Date();
            return formatter.format(today);  
        } catch (Exception e) {
            return null;
        }
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
        
    public String sendPing() {
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
            
        jHeader.put("data",jData);
        jHeader.put("auth",gDatos.getServerInfo().getAuthKey());
        jHeader.put("request", "ping");
            
        return jHeader.toString();
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
    
    public String sendStatusServices() {
        try {
            JSONObject jData = new JSONObject();
            JSONObject jHeader = new JSONObject();
            JSONArray jArray;
            
            jArray = new JSONArray(serializeObjectToJSon(gDatos.getLstServiceStatus(), false));
            
            jData.put("servicios", jArray);
            jHeader.put("data",jData);
            jHeader.put("result", "OK");

            return jHeader.toString();
        } catch (IOException | JSONException e) {
            return sendError(0,e.getMessage());
        }
    }
    
    public int updateStatusService(JSONObject jData) {
        try {
            ServiceStatus serviceStatus;
            List<AssignedTypeProc> lstAssignedTypeProc;
            
            serviceStatus = (ServiceStatus) serializeJSonStringToObject(jData.toString(), ServiceStatus.class);
            
            int numItems = gDatos.getLstServiceStatus().size();
            boolean itemFound = false;
            
            for (int i=0; i<numItems; i++) {
                if (gDatos.getLstServiceStatus().get(i).getSrvID().equals(serviceStatus.getSrvID())) {
                    lstAssignedTypeProc = gDatos.getLstServiceStatus().get(i).getLstAssignedTypeProc();
                    serviceStatus.setLstAssignedTypeProc(lstAssignedTypeProc);
                    gDatos.getLstServiceStatus().set(i, serviceStatus);
                    itemFound = true;
                }
            }
            
            if (!itemFound) {
                gDatos.getLstServiceStatus().add(serviceStatus);
            }
            
            logger.info("Se genera lista serviceStatus: "+ serializeObjectToJSon(gDatos.getLstServiceStatus(), true));
            
            return 0;
        } catch (JSONException | IOException e) {
            return 1;
        }
    }
    
    public String sendAssignedProc(String srvID) throws SQLException {
        try {
            List<PoolProcess> lstPoolProcess = new ArrayList<>();
            List<AssignedTypeProc> lstAssignedTypeProc = null;
            JSONObject jData = new JSONObject();
            JSONObject jHeader = new JSONObject();

            int exitCode = getMDprocAssigned();
            
            if (exitCode!=0) {
                logger.warn("No es posible conectarse a la Metdata para extraer los Procesos Asignados..se utilizará lista actual.");
            } 
            
            int numItems = gDatos.getLstServiceStatus().size();

            for (int i=0; i<numItems; i++) {
                if (gDatos.getLstServiceStatus().get(i).getSrvID().equals(srvID)) {
                    lstAssignedTypeProc = gDatos.getLstServiceStatus().get(i).getLstAssignedTypeProc();
                }
            }
            
            
            int numPools = gDatos.getLstPoolProcess().size();
            for (int i=0; i<numPools; i++) {
                if (gDatos.getLstPoolProcess().get(i).getSrvID().equals(srvID)) {
                    lstPoolProcess.add(gDatos.getLstPoolProcess().get(i));
                }
            }
            
            JSONArray assignedTypeProc = new JSONArray(serializeObjectToJSon(lstAssignedTypeProc, false));

            jData.put("AssignedTypeProc", assignedTypeProc);
            jData.put("poolProcess", lstPoolProcess);
            jHeader.put("data",jData);
            jHeader.put("result", "OK");
            
            return jHeader.toString();
        } catch (IOException | JSONException e) {
            return sendError(1,e.getMessage());
        }
    }
    
    public String sendDate() {
        try {
            JSONObject jData = new JSONObject();
            JSONArray ja = new JSONArray();
            JSONObject jHeader = new JSONObject();

            jData.put("fecha", getDateNow("yyyy-MM-dd HH:mm:ss"));
            jHeader.put("data", jData);
            jHeader.put("result", "OK");
            return jHeader.toString();
        } catch (Exception e) {
            return sendError(99, e.getMessage());
        }
    }
                  
    public int getMDprocAssigned() throws SQLException, IOException {
        try {
            MetaData metadata = new MetaData(gDatos);
            if (gDatos.getServerStatus().isIsValMetadataConnect()) {
                JSONArray ja;
                JSONObject jo = new JSONObject();
                ServiceStatus serviceStatus;

                String vSQL = "select srvID, srvDesc, srvEnable, srvTypeProc "
                        + "     from process.tb_services"
                        + "     order by srvID";
                try (ResultSet rs = (ResultSet) metadata.getQuery(vSQL)) {
                    if (rs!=null) {
                        while (rs.next()) {

                            ja = new JSONArray(rs.getString("srvTypeProc"));
                            jo.put("lstAssignedTypeProc", ja);
                            jo.put("srvID", rs.getString("srvID"));
                            jo.put("srvEnable", rs.getInt("srvEnable"));

                            serviceStatus = (ServiceStatus) serializeJSonStringToObject(jo.toString(), ServiceStatus.class);

                            gDatos.updateLstServiceStatus(serviceStatus);
                        }
                    }
                    rs.close();
                }
                metadata.closeConnection();
            }
            return 0;
        } catch (SQLException | JSONException e) {
            logger.error("Error recuperando Procesos Asignados. "+ e.getMessage());
            return 1;
        }
    }
    
    public String serializeObjectToJSon (Object object, boolean formated) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);

            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error serializeObjectToJson: "+e.getMessage());
            return null;
        }
    }
    
    public Object serializeJSonStringToObject (String parseJson, Class className) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(parseJson, className);
        } catch (Exception e) {
            logger.error("Error serializeJSonStringToObject: "+e.getMessage());
            return null;
        }
    }        
}
