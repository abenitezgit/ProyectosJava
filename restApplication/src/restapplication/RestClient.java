package restapplication;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;



public class RestClient {
	
	static int CONNECTION_TIMEOUT = 8000;
	static int READ_TIMEOUT = 8000;
	static String BASE_URL_1 = "http://10.33.66.30:8090/Service1.svc/rest";
        //static String BASE_URL_1 = "https://wcfguimodel.e-contact.cl/Service1.svc/rest";
	static String BASE_URL_2 = "http://gesys-demo.e-contact.cl:8080/WS_IVRNodos/services";
	
	public static void main(String args[]){
		RestClient cliente = new RestClient();
		//cliente.ejemploGET();
		
		cliente.ejemploPOSTInsert();
	}
	
	public void ejemploGET(){
		ConexionHTTP conexion = new ConexionHTTP(CONNECTION_TIMEOUT, READ_TIMEOUT);
		
		String rut = "11111111";
		String clave = "1234";
		String strUrl = BASE_URL_1+"/getAuthenticate/"+rut+"/"+clave;
				
		try {
			System.out.println("****** EJEMPLO GET ******");
			
			JSONObject json = new JSONObject(conexion.performGetCall(strUrl));
			
			System.out.println("**** RESULTADO OBTENIDO: "+json.toString());
			
			JSONObject json2 = json.getJSONObject("getRestAuthenticateResult");
			System.out.println("**** getRestAuthenticateResult: "+json2);
			
			
		} catch (JSONException e) {
		}
	}
	
	
	public void ejemploPOST(){
		ConexionHTTP conexion = new ConexionHTTP(CONNECTION_TIMEOUT, READ_TIMEOUT);
		
		String strUrl = BASE_URL_2+"/authenticate";
                
                for (int i=1;i<10000;i++) {
				
                    try {
                            System.out.println("****** EJEMPLO POST : "+i);

                            HashMap<String, Object> postDataParams = new HashMap<>();		
                            postDataParams.put("token", "11111111");
                            postDataParams.put("subtoken", "1");
                            postDataParams.put("secret", "1234");

                            JSONObject json = new JSONObject(conexion.performPostCall(strUrl, postDataParams));

                            System.out.println(" RESULTADO OBTENIDO: "+json.toString());


                    } catch (JSONException e) {
                    }
                }
	}

	public void ejemploPOSTInsert(){
		ConexionHTTP conexion = new ConexionHTTP(CONNECTION_TIMEOUT, READ_TIMEOUT);
		
		String strUrl = BASE_URL_1+"/testInsert";
                
                for (int i=1;i<10;i++) {
				
                    try {
                            System.out.println("****** EJEMPLO POST Insert: "+i);
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
                            
                            System.out.println(" RESULTADO OBTENIDO: "+conexion.performPostCallJSon(strUrl, jo.toString()));


                    } catch (JSONException e) {
                        System.out.println("Errot: "+e.getMessage());
                    }
                }
	}
        
        
}
