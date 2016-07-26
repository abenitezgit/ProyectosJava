/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class random {
    
    public static void main(String args[]) {
        Random rnd = new Random();
        System.out.println(abs(rnd.nextInt()));
        
        JSONObject jo = new JSONObject();
        jo.put("fecha", "hoy");
        jo.put("procid", "osp1");
        jo.put("status","init");
        System.out.println(jo.toString());
    
        if (jo.getString("procid").equals("osp1")) {
            jo.put("status","fin");
        }
        System.out.println(jo.toString());
        
        List<JSONObject> lista = new ArrayList<>();
        
        lista.add(jo);
        
        System.out.println(lista.get(0).toString());
        
        lista.get(0).put("startDate", "hoy");
        
        System.out.println(lista.get(0).toString());
        
        product p = new product();
        
        
        
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new Gson();
        
        //System.out.println(gson.fromJson(jo, product.class));
        
    }
    
    static class product {
        private String fecha;
        private String procid;
        private String startDate;
        private String status;

        /**
         * @return the status
         */
        public String getStatus() {
            return status;
        }

        /**
         * @param status the status to set
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * @return the fecha
         */
        public String getFecha() {
            return fecha;
        }

        /**
         * @param fecha the fecha to set
         */
        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        /**
         * @return the procid
         */
        public String getProcid() {
            return procid;
        }

        /**
         * @param procid the procid to set
         */
        public void setProcid(String procid) {
            this.procid = procid;
        }

        /**
         * @return the startDate
         */
        public String getStartDate() {
            return startDate;
        }

        /**
         * @param startDate the startDate to set
         */
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }
    
}
