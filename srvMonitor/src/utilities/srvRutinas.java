/*
 * To change this license header, choose License Headers in Project Properties.
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
            String srvName = jData.get("srvName").toString();
            int myNumItems = gDatos.getServiceStatus().size();
            
            if (myNumItems>0) {
                for (int i=0; i<myNumItems; i++) {
                    if (gDatos.getServiceStatus().get(i).getString("srvName").equals(srvName)) {
                        gDatos.getServiceStatus().remove(i);
                    }
                    gDatos.getServiceStatus().add(jData);
                }
            } else {
                gDatos.getServiceStatus().add(jData);
            }
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
    
    public String sendAssignedProc(String srvName) {
        try {
            JSONObject jData = new JSONObject();
            JSONObject jHeader = new JSONObject();
            JSONArray arrayAssignedProc = new JSONArray();
            String respuesta;
                        
            int myNumItems = gDatos.getAssignedServiceTypeProc().size();
            if (myNumItems>0) {
                for (int i=0; i<myNumItems; i++) {
                    if (gDatos.getAssignedServiceTypeProc().get(i).get("srvName").equals(srvName)) {
                        arrayAssignedProc = gDatos.getAssignedServiceTypeProc().get(i).getJSONArray("ArrayTypeProc");
                    }
                }
            }
            jData.put("ArrayTypeProc", arrayAssignedProc);
            jData.put("cmd", "update");
            jHeader.put("data",jData);
            jHeader.put("result", "keepAlive");
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
            Connection conn = gDatos.getMetadataConnection();
            String vSQL = "select srvName, srvDesc, srvActive, srvTypeProc from process.tb_services order by srvName";
            Statement sentencia;
            sentencia = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            List<JSONObject> lstTemp = new ArrayList<>();
            lstTemp.clear();

            ResultSet rs = sentencia.executeQuery(vSQL);
            if (rs!=null) {
                JSONObject jo;
                JSONArray ja;
                while (rs.next()) {
                    jo = new JSONObject();
                    ja = new JSONArray(rs.getString("srvTypeProc"));
                    jo.put("procAssigned", ja);
                    jo.put("srvActive", rs.getInt("srvActive"));
                    jo.put("srvDesc", rs.getString("srvDesc"));
                    jo.put("srvName", rs.getString("srvName"));
                    lstTemp.add(jo);
                }
            }
            gDatos.getAssignedServiceTypeProc().clear();
            gDatos.setAssignedServiceTypeProc(lstTemp);
            return 0;
            
        } catch (SQLException | JSONException e) {
            sysOutln(e.getMessage());
            return 1;
        }
    
    }
}
