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

    public JSONArray jaGetPoolProcess() {
        JSONArray ja = new JSONArray();
        try {
            int numProc = gDatos.getPoolProcess().size();
            if (numProc>0) {
                for (int i=0; i<numProc; i++) {
                    //if (!gDatos.getPoolProcess().get(i).getString("status").equals("queued")) {
                        ja.put(gDatos.getPoolProcess().get(i));
                    //}
                }
            }
            return ja;
        } catch (Exception e) {
            return ja;
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
            JSONObject jHeader = new JSONObject();
            JSONObject jData = new JSONObject();
            
            //Si existem proceso activos
            //if (gDatos.getPoolProcess().size()>0) {
                //JSONArray jaPoolProcess = jaGetPoolProcess();
                jData.put("procActive", jaGetPoolProcess());
            //} else {
            //    response.put("procActive", mainja);
            //}
            
            jData.put("srvName", gDatos.getSrvName());
            jData.put("srvPort", gDatos.getSrvPort());
            jData.put("numTotalExec", String.valueOf(gDatos.getNumTotalExec()));
            jData.put("numProcMax", String.valueOf(gDatos.getNumProcMax()));
            jData.put("numProcExec", String.valueOf(gDatos.getNumProcExec()));
            jData.put("srvStart", gDatos.getSrvStart());
            jData.put("isgetTypeProc", gDatos.isSrvGetTypeProc());
            
            //mainja.put(response);
            jHeader.put("data", jData);
            
            if (type.equals("keep")) {
                jHeader.put("auth", gDatos.getAuthKey());
                jHeader.put("request", "keepAlive");
            
            } else {
                jHeader.put("result", "keepAlive");
            }
            
            return jHeader.toString();
        } catch (Exception e) {
            return sendError(10,e.getMessage());
        }
    }
    
    public void updateAssignedProcess(JSONObject jData) {
        try {
            /* Request JSON jData
            // update: solo actualiza lo informado
            // delete: borra y registra lo nuevo
            {
                "cmd":"update/delete",
                "ArrayTypeProc":
                  [´
                    {
                      "typeProc":"<string value>",
                      "priority":<int value>,
                      "maxThread":<int values>
                    }
                  ]
            }
            
            */

            String dCmd = jData.getString("cmd");
            JSONArray dArrayTypeProc = jData.getJSONArray("ArrayTypeProc");
            
            String typeProc;
            String myTypeProc;
            int myUsedThread;
            int priority;
            int maxThread;
            boolean isFindItem;
            
            switch (dCmd) {
                case "update":
                    for (int i=0; i<dArrayTypeProc.length(); i++) {
                        //Busca en cada item de la lista
                        typeProc = dArrayTypeProc.getJSONObject(i).getString("typeProc");
                        priority = dArrayTypeProc.getJSONObject(i).getInt("priority");
                        maxThread = dArrayTypeProc.getJSONObject(i).getInt("maxThread");
                        isFindItem = false;
                        for (int j=0; j<gDatos.getAssignedTypeProc().size(); j++) {
                            myTypeProc = gDatos.getAssignedTypeProc().get(j).getString("typeProc");
                            if (typeProc.equals(myTypeProc)) {
                                gDatos.getAssignedTypeProc().get(j).put("priority", priority);
                                gDatos.getAssignedTypeProc().get(j).put("maxThread", maxThread);
                                isFindItem = true;
                            }
                        }
                        if (!isFindItem) {
                            gDatos.getAssignedTypeProc().add(dArrayTypeProc.getJSONObject(i));
                        }
                    }
                    break;
                case "delete":
                    for (int i=0; i<gDatos.getAssignedTypeProc().size(); i++) {
                        //Busca en cada Item nuevo informado
                        myTypeProc = gDatos.getAssignedTypeProc().get(i).getString("typeProc");
                        myUsedThread = gDatos.getAssignedTypeProc().get(i).getInt("usedThread");
                        isFindItem = false;
                        for (int j=0; j<dArrayTypeProc.length(); j++) {
                            typeProc = dArrayTypeProc.getJSONObject(i).getString("typeProc");
                            priority = dArrayTypeProc.getJSONObject(i).getInt("priority");
                            maxThread = dArrayTypeProc.getJSONObject(i).getInt("maxThread");
                            if (typeProc.equals(myTypeProc)) {
                                gDatos.getAssignedTypeProc().get(j).put("priority", priority);
                                gDatos.getAssignedTypeProc().get(j).put("maxThread", maxThread);
                                isFindItem = true;
                            }
                        }
                        if (!isFindItem) {
                            gDatos.getAssignedTypeProc().remove(i);
                        }
                    } 
                    for (int i=0; i<dArrayTypeProc.length(); i++) {
                        //Busca en cada item de la lista
                        typeProc = dArrayTypeProc.getJSONObject(i).getString("typeProc");
                        isFindItem = false;
                        for (int j=0; j<gDatos.getAssignedTypeProc().size(); j++) {
                            myTypeProc = gDatos.getAssignedTypeProc().get(j).getString("typeProc");
                            if (typeProc.equals(myTypeProc)) {
                                isFindItem = true;
                            }
                        }
                        if (!isFindItem) {
                            gDatos.getAssignedTypeProc().add(dArrayTypeProc.getJSONObject(i));
                        }
                    }
                    break;
                default:
                   break;
            }
        } catch (Exception e) {
            sysOutln("Error: " + e.getMessage());
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
    
    public int enqueProcess(JSONObject jData) {
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
            String procID = jData.getString("procID");
                        
            if (!gDatos.isExistPoolProcess(procID)) {
                jData.put("receiveDate", getDateNow("yyyy-MM-dd HH:mi:ss"));
                jData.put("status","queued");
                gDatos.poolProcess.add(jData);
                return 0;
            } else {
                return 2;
            }
        } catch (Exception e) {
            return 1;
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
            mainjo.put("result", "sendDate");
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
