

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by atong on 9/30/2015.
 *
 */
public class conexionHTTP {

    private final int conectionTimeout;
    private final int readTimeout;

    public conexionHTTP(int conectionTimeout, int readTimeout){
        this.conectionTimeout = conectionTimeout;
        this.readTimeout = readTimeout;
    }

    public conexionHTTP(){
        this.conectionTimeout = 15000;
        this.readTimeout = 15000;
    }

    /*
    * Metodo General para realizar la consulta de un Web Service REST (VIA POST)
    * */
    public String performPostCall(String requestURL,
                                   HashMap<String, Object> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

//            System.out.println("peformPostCallURL = "+requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(this.readTimeout);
            conn.setConnectTimeout(this.conectionTimeout);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            try (OutputStream os = conn.getOutputStream()) {
                if (postDataParams != null){
                    try (BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"))) {
                        writer.write(getPostDataString(postDataParams));
                        
                        writer.flush();
                    }
                }
            }
            int responseCode=conn.getResponseCode();

//            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
//            }else {
//                System.out.println(responseCode);
//                response="";
//                throw new Exception(responseCode+"");
//            }
        } catch (Exception e) {
        }

        return response;
    }

    //Metodo General para Adjuntar la Data VIA POST
    private String getPostDataString(HashMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, Object> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            if (entry.getValue() instanceof List){
                List<String> lista = (List) entry.getValue();
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                String valores = "";

                for(String valor : lista) valores += valor+"|";

                result.append(URLEncoder.encode(valores, "UTF-8"));
            }else{
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }

        }

        return result.toString();
    }



    /*
    * Metodo General para realizar la consulta de un Web Service REST (VIA GET)
    *
    * */
    public String performGetCall(String strUrl){
        String output = "";
        try {

            URL url = new URL(strUrl);//new URL("http://localhost:8080/RESTfulExample/json/product/get");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("peformGetCallFailed : HTTP error code : "+ conn.getResponseCode());
                throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            if ((output = br.readLine()) != null){
                System.out.println("peformGetCallResultado = "+output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            System.err.println("peformGetCall Failed : MalformedURLException : " + e.getMessage());

        } catch (IOException e) {
            System.err.println("peformGetCall Failed : IOException : " + e.getMessage());

        }
        return output;
    }



    /*METODOS DEPRECATED*/
/*
    public String readUrlPost(String urloriginal, List<NameValuePair> parametros)
            throws MalformedURLException {
        String resultado = "";
        try {

            if(Build.VERSION.SDK_INT>8){
                //Version 2.3 o mas
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            // URL url = new URL(urloriginal);
            // HttpURLConnection urlConnection = (HttpURLConnection)
            // url.openConnection();

            // InputStream in = new
            // BufferedInputStream(urlConnection.getInputStream());
            //String inputLine;
            HttpResponse response = null;
            if (parametros == null) {

                HttpGet httprequest = new HttpGet(urloriginal);
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, conectionTimeout);
                HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
                HttpClient httpclient = new DefaultHttpClient(httpParameters);
                response = httpclient.execute(httprequest);
            } else {

                HttpPost httppost = new HttpPost(urloriginal);
                HttpParams params = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params, conectionTimeout);
                HttpConnectionParams.setSoTimeout(params,socketTimeout);
                HttpProtocolParams.setContentCharset(params, "utf-8");
                HttpClient httpclient = new DefaultHttpClient(params);
                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));
                response = httpclient.execute(httppost);
            }
            resultado = getResponseBody(response);


        } catch (Exception e) {

            Log.v("WS_C2C_BEP-E-SERVIDOR", e.getMessage());

            return "-1";
        }
        return resultado;
    }

    public static String _getResponseBody(HttpEntity entity)
            throws IOException, ParseException {

        if(Build.VERSION.SDK_INT>8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        InputStream instream = entity.getContent();

        if (instream == null) {
            return "";
        }

        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(

                    "HTTP entity too large to be buffered in memory");
        }

        // String charset = getContentCharSet(entity);
        String charset = HTTP.UTF_8;

        Reader reader = new InputStreamReader(instream, charset);

        StringBuilder buffer = new StringBuilder();

        try {

            char[] tmp = new char[1024];

            int l;

            while ((l = reader.read(tmp)) != -1) {

                buffer.append(tmp, 0, l);

            }

        } finally {

            reader.close();

        }

        return buffer.toString();

    }

    public static String getContentCharSet(final HttpEntity entity)
            throws ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        String charset = null;

        if (entity.getContentType() != null) {

            HeaderElement values[] = entity.getContentType().getElements();

            if (values.length > 0) {

                NameValuePair param = values[0].getParameterByName("charset");

                if (param != null) {

                    charset = param.getValue();

                }

            }

        }

        return charset;

    }

    public static String getResponseBody(HttpResponse response) {

        if(Build.VERSION.SDK_INT>8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String response_text = null;

        HttpEntity entity = null;

        try {

            entity = response.getEntity();

            response_text = _getResponseBody(entity);

        } catch (ParseException e) {

            e.printStackTrace();

        } catch (IOException e) {

            if (entity != null) {

                try {

                    entity.consumeContent();

                } catch (IOException e1) {

                }

            }

        }

        return response_text;

    }
    */

}
