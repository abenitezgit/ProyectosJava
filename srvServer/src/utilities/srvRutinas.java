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
            case 10:
                    errDesc = "procID ya se encuentra en poolProcess...";
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
        
        mainjo.put("params",jo);
        mainjo.put("result", "error");
            
        return mainjo.toString();
    }
    
    public String sendOkTX() {

        JSONObject mainjo = new JSONObject();
        
        mainjo.put("result", "OK");
            
        return mainjo.toString();
    }
    
    public List<String> lstFindInListjSon (List<String> lstSource, String param, String valor) {
        try {
            List<String> lstReturn = new ArrayList<>();
            int numRows = lstSource.size();
            for (int i=0; i<numRows; i++) {
                JSONObject jo = new JSONObject(lstSource.get(i));
                if (jo.get(param).toString().equals(valor)) {
                    lstReturn.add(jo.toString());
                }
            }
            return lstReturn;
        } catch (Exception e) {
            sysOutln(e.getMessage());
            return null;
        }
    }
    
    public String jsonAddObject (String jsonSource, String jsonParam, Object jsonValor) {
        try {
            JSONObject jo = new JSONObject(jsonSource);
            jo.append(jsonParam, jsonValor);
            return jo.toString();
        } catch (Exception e) {
            sysOutln(e.getMessage());
            return jsonSource;
        }
    
    }
    
    public JSONArray jaGetAssignedTypeProc() {
        try {
            JSONArray ja = new JSONArray();
            int numProc = gDatos.getAssignedTypeProc().size();
            if (numProc>0) {
                for (int i=0; i<numProc; i++) {
                    ja.put(gDatos.getAssignedTypeProc().get(i));
                }
                return ja;
            } else {
                return ja;
            }
        } catch (Exception e) {
            sysOutln(e.getMessage());
            return null;
        }
    }

    public JSONArray jaGetActiveTypeProc() {
        try {
            JSONArray ja = new JSONArray();
            int numProc = gDatos.getPoolProcess().size();
            if (numProc>0) {
                for (int i=0; i<numProc; i++) {
                    if (!gDatos.getPoolProcess().get(i).getString("status").equals("queued")) {
                        ja.put(gDatos.getPoolProcess().get(i));
                    }
                }
            }
            return ja;
        } catch (Exception e) {
            sysOutln(e.getMessage());
            return null;
        }
    }        
    
    public List<String> lstGetAssignedTypeProc() {
        try {
            List<String> lstReturn = new ArrayList<>();
            int numProc = gDatos.getAssignedTypeProc().size();
            if (numProc>0) {
                for (int i=0; i<numProc; i++) {
                   // lstReturn.add(gDatos.getAssignedTypeProc().get(i));
                }
                return lstReturn;
            } else {
                return null;
            }
        } catch (Exception e) {
            sysOutln(e.getMessage());
            return null;
        }
    }
    
    public String sendDataKeep(String type) {
        //Enviara en forma JSON
        // Parametros actuales del servicio
        // procesos asignados
        // procesos en ejecución
        //
        // Se requiere el acceso a dos listas
        //  assignedTypeProc - activeTypeProc
        //
        try {
            // Se genera la salida de la lista 
            JSONObject response = new JSONObject();
            JSONArray mainja = new JSONArray();
            JSONObject mainjo = new JSONObject();
            
            //Si existen procesos asignados
            if (gDatos.getAssignedTypeProc().size()>0) {
                JSONArray jaAssignedTypeProc = jaGetAssignedTypeProc();
                response.put("procAssigned", jaAssignedTypeProc);
            } else {
                response.put("procAssigned", mainja);
            } 
            
            //Si existem proceso activos
            if (gDatos.getPoolProcess().size()>0) {
                JSONArray jaActiveTypeProc = jaGetActiveTypeProc();
                response.put("procActive", jaActiveTypeProc);
            } else {
                response.put("procActive", mainja);
            }
            
            response.put("srvName", gDatos.getSrvName());
            response.put("srvPort", gDatos.getSrvPort());
            response.put("numTotalExec", String.valueOf(gDatos.getNumTotalExec()));
            response.put("numProcMax", String.valueOf(gDatos.getNumProcMax()));
            response.put("numProcExec", String.valueOf(gDatos.getNumProcExec()));
            response.put("srvStart", gDatos.getSrvStart());
            response.put("isgetTypeProc", gDatos.isSrvGetTypeProc());
            
            //mainja.put(response);
            mainjo.put("params", response);
            
            if (type.equals("keep")) {
                mainjo.put("auth", gDatos.getAuthKey());
                mainjo.put("request", "keepAlive");
            
            } else {
                mainjo.put("result", "keepAlive");
            }
            
            return mainjo.toString();
        } catch (Exception e) {
            return sendError(0,e.getMessage());
        }
    }
    
    public int updateAssignedProcess(String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
            JSONArray rows = ds.getJSONArray("params");
            JSONObject row;
            
            //Lista para referenciar a Lista Actual
            //
            List<JSONObject> lstAssignedProc = new ArrayList<>();
            
            int numProcInput = rows.length();
            
            for (int i=0; i<numProcInput; i++) {
                //Extraigo el registro JSONObject
                row = rows.getJSONObject(i);
                lstAssignedProc.add(row);
            }
            gDatos.setAssignedTypeProc(lstAssignedProc);
            gDatos.setSrvGetTypeProc(true);
            return 0;
        } catch (Exception e) {
            sysOutln(e.getMessage());
            return 99;
        }
    }
    
    public String sendPoolProcess() {
        try {
            String output = null;
            JSONObject jo ;
            JSONArray ja = new JSONArray();
            JSONObject mainjo = new JSONObject();
            
            int numProc = gDatos.getPoolProcess().size();
            sysOutln("numproc: "+numProc);
            if (numProc>0) {
              for (int i=0; i<numProc; i++) {
                  jo = gDatos.getPoolProcess().get(i);
                  ja.put(jo);
                  
              }  
            }
            mainjo.put("params", ja);
            mainjo.put("result", "getPoolProcess");
            return mainjo.toString();
        } catch (Exception e) {
            return sendError(1, "error en send pool process: "+e.getMessage());
        }
    }
    
    public String enqueProcess(String inputData) {
        //inputData:
        //{
        //  "request":"executeProcess","auth":"querty0987","typeProc":"OSP","procID":"OSP00001","params":
        //  {
        //   "ospName":"sp_001",
        //   "ospUser":"process",
        //   "ospPass":"proc01",
        //   "ospOwner":"process",
        //   "ospServer":"localhost",
        //   "ospDBPort":"1521",
        //   "ospDBName":"oratest",
        //   "ospDBInstance":"default",
        //   "ospDBType":"ORA",
        //   "parametros":
        //      [
        //          {"value":"20160612","type":"string"},
        //          {"value":"10","type":"int"}
        //      ]
        //  }
        //}
        try {
            //Ingresa la peticion de ejecucion en una lista
            //
            int result; 
            JSONObject ds = new JSONObject(inputData);
            JSONObject params = ds.getJSONObject("params");
            
            String typeProc = ds.getString("typeProc");
            String procID = ds.getString("procID");
            
            JSONObject itemData = new JSONObject();
            
            itemData.put("params", params);
            itemData.put("status", "queued");
            itemData.put("procID", procID);
            itemData.put("typeProc", typeProc);
                        
            if (!gDatos.isExistPoolProcess(procID)) {
                result = gDatos.addPoolProcess(itemData);
            } else {
                result = 10;
            }
            
            if (result==0) {
                return sendOkTX();
            } else {
                return sendError(result);
            }
        } catch (Exception e) {
            return sendError(10, "error enque stack proc..");
        }
    
    }
    
    
    
    public String sendList(String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
            JSONArray ja = new JSONArray();
            
            String inputLista = ds.getJSONObject("params").getString("lista");
            
            switch (inputLista) {
                case "pool":
                    for (int i=0; i<gDatos.getPoolProcess().size(); i++) {
                        ja.put(gDatos.getPoolProcess().get(i));
                    }
                    break;
                default:
                    break;
            }
            
            return ja.toString();
        } catch (Exception e) {
            return null;
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
            
                List<String> lstActiveProcess = new ArrayList<>();
                List<String> newListActiveProcess = new ArrayList<>();
                String procType;
                String threadActive;
                String threadMax;
                boolean typeExist;

                lstActiveProcess = gDatos.getActiveTypeProc();
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
            mainjo.put("params", ja);
            mainjo.put("request", "response");
            return mainjo.toString();
        } catch (Exception e) {
            return sendError(99, e.getMessage());
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
    
    public String getRequest(String inputData) {
        try {
            JSONObject ds = new JSONObject(inputData);
            
            return ds.get("request").toString();
        } catch (Exception e) {
            return sendError(50); 
        }
    } 
    
}
