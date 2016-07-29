/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dataClass.AssignedTypeProc;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class srvRutinas {
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
    
    public String updateVar(JSONObject jData) {
        try {
            JSONArray jArrayVar = jData.getJSONArray("arrayVar");
            int items = jArrayVar.length();
            for (int i=0; i<items; i++) {
                if (jArrayVar.getJSONObject(i).names().get(i).equals("srvActive")) {
                    gDatos.setSrvActive(jArrayVar.getJSONObject(i).getInt("srvActive")==1);
                }
            }
            return sendOkTX();
            
        } catch (Exception e) {
            return sendError(1);
        }
    }
    
    public int sendRegisterService() {
        try {
            boolean processOK = false;

            Socket skCliente;
            String response;
            String dataSend;

            skCliente = new Socket(gDatos.getSrvMonHost(), Integer.valueOf(gDatos.getMonPort()));
            OutputStream aux = skCliente.getOutputStream(); 
            DataOutputStream flujo= new DataOutputStream( aux ); 
            
            dataSend = sendDataKeep("keep");
            
            logger.info("Enviando (tx)...: "+dataSend);
            flujo.writeUTF( dataSend ); 
            
            InputStream inpStr = skCliente.getInputStream();
            DataInputStream dataInput = new DataInputStream(inpStr);
            response = dataInput.readUTF();
            
            logger.info("Recibiendo (tx)...: "+response);
            /*
                Analiza la respuesta
            */
            
            JSONObject jHeader = new JSONObject(response);
            
            try {
                if (jHeader.getString("result").equals("OK")) {
                    JSONObject jData = jHeader.getJSONObject("data");
                    //Como es una repsuesta no se espera retorno de error del SP
                    //el mismo lo resporta internamente si hay alguno.
                    updateAssignedProcess(jData);
                    processOK = true;
                } else {
                    if (jHeader.getString("result").equals("error")) {
                        JSONObject jData = jHeader.getJSONObject("data");
                        logger.error("Error sendRegisterService result: "+jData.getString("errNum")+ " " +jData.getString("errMesg"));
                    }
                }
            } catch (Exception e) {
                logger.error("Error sendRegisterService en formato de respuesta...");
            }

            dataInput.close();
            inpStr.close();
            flujo.close();
            aux.close();
            skCliente.close();
            
            if (processOK) {
                return 0;
            } else {
                return 1;
            }
            
        } catch (NumberFormatException | IOException | JSONException e) {
            logger.error("Error sendRegisterService Conectando a Socket monitor: " + e.getMessage());
            return 1;
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
    
    public String sendPing() {
        
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
        
        jHeader.put("data", jData);
        jHeader.put("auth", gDatos.getAuthKey());
        jHeader.put("request", "sendPing");
            
        return jHeader.toString();
    }

    public String sendOkTX() {
        
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
        
        jHeader.put("data", jData);
        jHeader.put("result", "OK");
            
        return jHeader.toString();
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
        JSONObject jo;
        JSONArray ja = new JSONArray();
        try {
            int numProc = gDatos.getPoolProcess().size();
            if (numProc>0) {
                for (int i=0; i<numProc; i++) {
                    jo = new JSONObject();
                    jo.put("typeProc", gDatos.getPoolProcess().get(i).getString("typeProc"));
                    jo.put("procID", gDatos.getPoolProcess().get(i).getString("procID"));
                    jo.put("sendDate", gDatos.getPoolProcess().get(i).getString("sendDate"));
                    jo.put("receiveDate", gDatos.getPoolProcess().get(i).getString("receiveDate"));
                    jo.put("status", gDatos.getPoolProcess().get(i).getString("status"));
                    ja.put(jo);
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
        //Enviara en forma JSON
        // Parametros actuales del servicio
        // procesos asignados
        // procesos en ejecución
        //
        // Se requiere el acceso a dos listas
        //  assignedTypeProc - activeTypeProc
        //
        Runtime instance = Runtime.getRuntime();
        
        try {
            // Se genera la salida de la lista 
            JSONObject jHeader = new JSONObject();
            JSONObject jData = new JSONObject();
            
            //Si existem proceso activos
            //if (gDatos.getPoolProcess().size()>0) {
                //JSONArray jaPoolProcess = jaGetPoolProcess();
                jData.put("procActive", jaGetPoolProcess());
            //} else {
            //    response.put("procActive", mainja);
            //}
            
            jData.put("srvID", gDatos.getSrvID());
            jData.put("srvPort", gDatos.getSrvPort());
            jData.put("srvHost", gDatos.getSrvHost());
            jData.put("numTotalExec", String.valueOf(gDatos.getNumTotalExec()));
            jData.put("numProcMax", String.valueOf(gDatos.getNumProcMax()));
            jData.put("numProcExec", String.valueOf(gDatos.getNumProcExec()));
            jData.put("srvStart", gDatos.getSrvStart());
            jData.put("isRegisterService", gDatos.isIsRegisterService());
            jData.put("totalMemory", instance.totalMemory()/1024/1024);
            jData.put("cpuLoad", getProcessCpuLoad());
            
            //mainja.put(response);
            jHeader.put("data", jData);
            
            if (type.equals("keep")) {
                jHeader.put("auth", gDatos.getAuthKey());
                jHeader.put("request", "keepAlive");
            
            } else {
                jHeader.put("result", "OK");
            }
            
            return jHeader.toString();
        } catch (Exception e) {
            return sendError(10,e.getMessage());
        }
    }
    
    public void updateAssignedProcess(JSONObject jData) {
        try {

            String dCmd = jData.getString("cmd");
            JSONArray jArray = jData.getJSONArray("assignedTypeProc");
            ObjectMapper mapper = new ObjectMapper();
            
            String typeProc;
            String myTypeProc;
            int priority;
            int maxThread;
            boolean isFindItem;
            
            switch (dCmd) {
                case "update":
                    for (int i=0; i<jArray.length(); i++) {
                        //Crea objeto Data Class

                        //Asigna JSON to Data Class
                        AssignedTypeProc assignedTypeProc = mapper.readValue(jArray.getJSONObject(i).toString(), AssignedTypeProc.class);
                                                
                        int posFound = gDatos.getPosTypeProc(assignedTypeProc.getTypeProc());
                        if (posFound!=0) {
                            //TypeProc es encontrado en una posicion de la lista
                            gDatos.getLstAssignedTypeProc().set(posFound, assignedTypeProc);
                        } else {
                            //TypeProc no es encontrado en una posicion de la lista
                            gDatos.getLstAssignedTypeProc().add(assignedTypeProc);
                        }
                    }
                    break;
                case "delete":
                    //A partir de la Lista Guardada busca en lista informada para
                    //actualizar el dato o borrarlo si no se encuentra
                    for (int i=0; i<gDatos.getLstTypeProc().size(); i++) {
                        for (int j=0; j<jArray.length(); j++) {
                            AssignedTypeProc assignedTypeProc = mapper.readValue(jArray.getJSONObject(i).toString(), AssignedTypeProc.class);
                            
                            int posFound = getPosIndexJsonArray(dArrayTypeProc,"typeProc",gDatos.getLstTypeProc().get(i).getTypeProc());
                            if (posFound!=0) {
                                //Se referencia a un Data Class
                                AssignedTypeProc objTypeProc = new AssignedTypeProc();
                                objTypeProc.setTypeProc(dArrayTypeProc.getJSONObject(posFound).getString("typeProc"));
                                objTypeProc.setMaxThread(dArrayTypeProc.getJSONObject(posFound).getInt("maxThread"));
                                objTypeProc.setPriority(dArrayTypeProc.getJSONObject(posFound).getInt("priority"));
                                objTypeProc.setUsedThread(gDatos.getLstTypeProc().get(i).getUsedThread());
                                gDatos.getLstTypeProc().set(posFound, objTypeProc);
                            } else {
                                gDatos.getLstTypeProc().remove(i);
                            }
                        }
                    
                    }
                    
                    //A partir de la lista informada busca en lista guardada para actualizar o ingresar
                    //una nueva asignacion
                    
                    for (int i=0; i<dArrayTypeProc.length(); i++) {
                        for (int j=0; j<gDatos.getLstTypeProc().size(); j++) {
                            int posFound = gDatos.getPosTypeProc(dArrayTypeProc.getJSONObject(i).getString("typeProc"));
                            if (posFound==0) {
                                AssignedTypeProc objTypeProc = new AssignedTypeProc();
                                objTypeProc.setTypeProc(dArrayTypeProc.getJSONObject(i).getString("typeProc"));
                                objTypeProc.setMaxThread(dArrayTypeProc.getJSONObject(i).getInt("maxThread"));
                                objTypeProc.setPriority(dArrayTypeProc.getJSONObject(i).getInt("priority"));
                                objTypeProc.setUsedThread(0);
                                gDatos.getLstTypeProc().add(objTypeProc);
                            }
                        }
                    }
                    break;
                default:
                   break;
            }
        } catch (JSONException | IOException e) {
            sysOutln("Error: " + e.getMessage());
        }
    }
    
    public int getPosIndexJsonArray(JSONArray lstArray, String item, Object value) {
        int posFound = 0;
        if (lstArray!=null) {
            int numItems = lstArray.length();
            for (int i=0; i<numItems; i++) {
                if (lstArray.getJSONObject(i).getString(item).equals(value)) {
                    posFound=i;
                }
            }
            return posFound;
        } else {
            return 0;
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
            System.out.println("..........................................");
            System.out.println("..........................................");
            System.out.println(jData.toString());
            System.out.println("..........................................");
            System.out.println("..........................................");
            
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
    
    public String sendList(JSONObject jData) {
        try {
            JSONObject jHeader = new JSONObject();
            JSONArray ja = new JSONArray();
            
            String inputLista = jData.getString("lista");
            
            switch (inputLista) {
                case "pool":
                    for (int i=0; i<gDatos.getPoolProcess().size(); i++) {
                        ja.put(gDatos.getPoolProcess().get(i));
                    }
                    break;
                case "assignedProc":
                    for (int i=0; i<gDatos.getAssignedTypeProc().size(); i++) {
                        ja.put(gDatos.getAssignedTypeProc().get(i));
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
