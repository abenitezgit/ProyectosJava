/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class thExecPost extends Thread{
    static int CONNECTION_TIMEOUT = 8000;
    static int READ_TIMEOUT = 8000;
    static String BASE_URL_1;
    //"http://10.33.66.30:8090/Service1.svc/rest";
    //static String BASE_URL_1 = "https://wcfguimodel.e-contact.cl/Service1.svc/rest";
    static String BASE_URL_2 = "http://gesys-demo.e-contact.cl:8080/WS_IVRNodos/services";
    
    globalDataArea gDatos;
    
    
    public thExecPost(globalDataArea m) {
        gDatos = m;
        thExecPost.BASE_URL_1 = gDatos.getURLBase();
    }
    
    @Override
    public void run() {
                ConexionHTTP conexion = new ConexionHTTP(CONNECTION_TIMEOUT, READ_TIMEOUT);
		
		String strUrl = BASE_URL_1+"/testInsert";
                
                for (int i=1;i<=1000;i++) {
				
                    try {
                            String msg = "****** EJEMPLO POST request: "+i;
                            gDatos.addDLMProc(msg);
                            //gDatos.getDlmProc().addElement(msg);
                            
                            JSONObject jo = new JSONObject();
                            
                            jo.put("nombre", "nomrbe  sdsdsdsdsd");
                            jo.put("datos", "kjdkasd asdlaskjdas asdklasjd asdjasldkas sadakljd " );
                            jo.put("otros", "otros otros dskjdlkasdlsajdlasdlsajdas sdasdasds" );

                            HashMap<String, Object> postDataParams = new HashMap<>();		
                            postDataParams.put("nombre", "AAAAAAAAAAAAAAAAAAA");
                            postDataParams.put("datos", "datos datos datos");
                            postDataParams.put("otros", "otros otros otros");

                            //JSONObject json = new JSONObject(conexion.performPostCall(strUrl, postDataParams));
                            //JSONObject json = new JSONObject(conexion.performPostCallJSon(strUrl, jo.toString()));
                            String result = conexion.performPostCallJSon(strUrl, jo.toString());
                            //String result = "{\"testInsertResult\":\"OK\"}";
                            if (result.equals("{\"testInsertResult\":\"OK\"}")) {
                                //gDatos.getDlmProc().addElement("RequestID: "+i+" resultado Exitoso: "+result);
                                gDatos.addDLMProc("RequestID: "+i+" resultado Exitoso: "+result);
                                gDatos.addNumInserts();
                            } else {
                                gDatos.addDLMError("RequestID: "+i+" Error en retorno: "+result);
                                gDatos.addNumErrors();
                            }
                            
                            gDatos.setTxtInserted(String.valueOf(gDatos.getNumInserts()));
                            gDatos.setTxtError(String.valueOf(gDatos.getNumErrors()));
                                                        
                            
                            //System.out.println(" RESULTADO OBTENIDO: "+conexion.performPostCallJSon(strUrl, jo.toString()));

                    } catch (JSONException e) {
                        System.out.println("Errot: "+e.getMessage());
                    }
                }
    }
}