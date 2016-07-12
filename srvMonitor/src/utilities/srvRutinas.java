/*
 * To change this license header, choose License Headers in Project Properties.nnn
 */
package utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
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
    
    public void putExecOSP(String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
            JSONArray rows = ds.getJSONArray("params");
            JSONObject row = rows.getJSONObject(0); //Recupera el primero registro de la lista
        } catch (Exception e) {
            sysOutln(e);
        }    
    }

    public String getStatusServices() {
        try {
            JSONObject jo = new JSONObject();
            JSONArray jaServicios = new JSONArray();
            JSONArray jaAssigned = new JSONArray();
            
            JSONObject mainjo = new JSONObject();

            for (int i=0; i<gDatos.getServiceStatus().size(); i++) {
                jaServicios.put(gDatos.getServiceStatus().get(i));
            }
            
            for (int i=0; i<gDatos.getAssignedServiceTypeProc().size(); i++) {
                jaAssigned.put(gDatos.getAssignedServiceTypeProc().get(i));
            }
            
            jo.put("procAssigned", jaServicios);
            jo.put("servicios", jaServicios);
            mainjo.put("params",jo);
            mainjo.put("result", "getStatus");

            return mainjo.toString();
        } catch (Exception e) {
            return sendError(0,e.getMessage());
        }
    }
        
    public int updateStatusServices(JSONObject jData) {
        try {            
            String srvID    = jData.get("srvID").toString();
            int myNumItems  = gDatos.getServiceStatus().size();
            
            if (myNumItems>0) {
                for (int i=0; i<myNumItems; i++) {
                    if (gDatos.getServiceStatus().get(i).getString("srvID").equals(srvID)) {
                        gDatos.getServiceStatus().remove(i);
                    }
                }
            } 
            gDatos.getServiceStatus().add(jData);
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
    
    public String sendAssignedProc(String srvID) {
        try {
            JSONObject jData = new JSONObject();
            JSONObject jHeader = new JSONObject();
            JSONArray arrayAssignedProc = new JSONArray();
            String respuesta;
                        
            int myNumItems = gDatos.getAssignedServiceTypeProc().size();
            if (myNumItems>0) {
                for (int i=0; i<myNumItems; i++) {
                    if (gDatos.getAssignedServiceTypeProc().get(i).get("srvID").equals(srvID)) {
                        arrayAssignedProc = gDatos.getAssignedServiceTypeProc().get(i).getJSONArray("ArrayTypeProc");
                    }
                }
            }
            jData.put("ArrayTypeProc", arrayAssignedProc);
            jData.put("cmd", "update");
            jHeader.put("data",jData);
            jHeader.put("result", "OK");
            return jHeader.toString();
        } catch (Exception e) {
            return sendError(1,e.getMessage());
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
            mainjo.put("result", "getDate");
            return mainjo.toString();
        } catch (Exception e) {
            return sendError(99, e.getMessage());
        }
    }
              
    public List<String> getDataParams(String inputData) {
        //Devuelve Lista de Parametros de un mensaje JSON
        //
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
    
    public int getMDprocAssigned() throws SQLException {
        try {
            String vSQL = "select srvID, srvDesc, srvEnable, srvTypeProc "
                    + "     from process.tb_services"
                    + "     order by srvID";
            Statement stm;
            stm = gDatos.getMetadataConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            List<JSONObject> lstTemp = new ArrayList<>();
            lstTemp.clear();

            ResultSet rs = stm.executeQuery(vSQL);
            if (rs!=null) {
                JSONObject jData;
                JSONArray jArray;
                while (rs.next()) {
                    jData = new JSONObject();
                    jArray = new JSONArray(rs.getString("srvTypeProc"));
                    jData.put("procAssigned", jArray);
                    jData.put("srvEnable", rs.getInt("srvEnable"));
                    jData.put("srvDesc", rs.getString("srvDesc"));
                    jData.put("srvID", rs.getString("srvID"));
                    lstTemp.add(jData);
                }
            }
            /*
                Actualiza la lista assignedServiceTypeProc como JSONObject()
            */
            gDatos.getAssignedServiceTypeProc().clear();
            gDatos.setAssignedServiceTypeProc(lstTemp);
            return 0;
            
        } catch (SQLException | JSONException e) {
            sysOutln(e.getMessage());
            return 1;
        }
    
    }
}
