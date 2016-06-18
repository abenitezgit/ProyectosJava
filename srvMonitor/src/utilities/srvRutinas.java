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
    
    public String sendError(int errCode, String errDesc) {
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainjo = new JSONObject();
        
        jo.put("errMesg", errDesc);
        jo.put("errCode", errCode);
        
        ja.put(jo);
        
        mainjo.put("params",ja);
        mainjo.put("error", "method");
            
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
        mainjo.put("error", "method");
            
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
        mainjo.put("result", "OK");
            
        return mainjo.toString();
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
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();

            for (int i=0; i<gDatos.getServiceStatus().size(); i++) {
                ja.put(gDatos.getServiceStatus().get(i));
            }
            mainjo.put("params", ja);
            mainjo.put("result", "getStatus");

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
    
    public int updateStatusServices(String inputData) {
        try {
            System.out.println("inicio......");
            JSONObject ds = new JSONObject(inputData);
            JSONObject row = ds.getJSONObject("params");
            String srvName = row.get("srvName").toString();
            int result;
            
            //Busca Registro a Modificar en Lista global serviceStatus
            //
            result = gDatos.deleteItemServiceStatus(srvName);
            
            //Agrega Servicio a la lista de servicios
            //
            result = gDatos.addItemServiceStatus(row);

            return 0;
        } catch (Exception e) {
            return 9;
        }
    }
    
    public String sendAssignedProc(String srvName) {
        try {
            String respuesta;
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();
            
            System.out.println("srvname: "+srvName);
            
            int numItems = gDatos.getAssignedServiceTypeProc().size();
            if (numItems>0) {
                for (int i=0; i<numItems; i++) {
                    if (gDatos.getAssignedServiceTypeProc().get(i).get("srvName").equals(srvName)) {
                        ja = gDatos.getAssignedServiceTypeProc().get(i).getJSONArray("procAssigned");
                    }
                }
            }
            mainjo.put("params",ja);
            mainjo.put("result", "keepAlive");
            return mainjo.toString();
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
    
    public String getSrvName(String inputData) {
        //Devuelve el srvName de la cadena de entrada
        //
        try {
            JSONObject ds = new JSONObject(inputData);
            JSONObject row = ds.getJSONObject("params");
            return row.get("srvName").toString();
        } catch (Exception e) {
            return sendError(60);
        }
    }
    
    public String getRequest(String inputData) {
        //Devuelve la Operacion request del mensaje JSON
        //
        try {
            JSONObject ds = new JSONObject(inputData);
            
            return ds.get("request").toString();
        } catch (Exception e) {
            return sendError(50); 
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
            List<JSONObject> lstAssignedProc = new ArrayList<>();
            Connection conn = gDatos.getMetadataConnection();
            String vSQL = "select srvName, srvDesc, srvActive, srvTypeProc from process.tb_services order by srvName";
            Statement sentencia;
            sentencia = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

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
                    System.out.println(jo.toString());
                    lstAssignedProc.add(jo);
                }
            }
            gDatos.setAssignedServiceTypeProc(lstAssignedProc);
            return 0;
            
        } catch (SQLException | JSONException e) {
            sysOutln(e.getMessage());
            return 1;
        }
    
    }
}
