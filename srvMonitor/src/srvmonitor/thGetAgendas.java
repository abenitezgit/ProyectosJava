/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilities.globalAreaData;
import utilities.oracleDB;

/**
 *
 * @author andresbenitez
 */
public class thGetAgendas extends Thread{
    oracleDB conn = new oracleDB();
    globalAreaData gDatos;
    
    public thGetAgendas(globalAreaData m) {
        gDatos = m;
        conn.setDbName("oratest");
        conn.setDbUser("process");
        conn.setDbPass("proc01");
        conn.setHostIp("oradb01");
        conn.setDbPort("1521");

    }
    
    @Override
    public void run() {
        /*
            Recupera Parametros Fecha Actual
        */
        
        String[] ids = TimeZone.getAvailableIDs(-4 * 60 * 60 * 1000);
        String clt = ids[0];
        SimpleTimeZone tz = new SimpleTimeZone(-4 * 60 * 60 * 1000, clt);
        tz.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        tz.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        Calendar calendar = new GregorianCalendar(tz);
        
	int year       = calendar.get(Calendar.YEAR);
	int month      = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
	int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); 
	int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
	int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
	int weekOfMonth= calendar.get(Calendar.WEEK_OF_MONTH);

	int hour       = calendar.get(Calendar.HOUR);        // 12 hour clock
	int hourOfDay  = calendar.get(Calendar.HOUR_OF_DAY); // 24 hour clock
	int minute     = calendar.get(Calendar.MINUTE);
	int second     = calendar.get(Calendar.SECOND);
	int millisecond= calendar.get(Calendar.MILLISECOND);
        
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        int hourBefore = calendar.get(Calendar.HOUR_OF_DAY);
        
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        int hourAfter = calendar.get(Calendar.HOUR_OF_DAY);
        
        String posmonth = String.valueOf(month+1);
        String posdayOfMonth = String.valueOf(dayOfMonth);
        String posdayOfWeek = String.valueOf(dayOfWeek);
        String posweekOfYear = String.valueOf(weekOfYear);
        String posweekOfMonth = String.valueOf(weekOfMonth);
        String poshourOfDay = String.valueOf(hourOfDay);
        String poshourBefore = String.valueOf(hourBefore);
        String poshourAfter = String.valueOf(hourAfter);
        String posminute = String.valueOf(minute);
        String possecond = String.valueOf(second);
        String posmillisecond = String.valueOf(millisecond);

        
        String vSQL = "select "+poshourOfDay+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from process.tb_agenda where "
                + "     ageEnable=1 "
                + "     and ageInclusive=1"
                + "     and substr(month,"+posmonth+",1) = '1'"
                + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
                + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
                + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
                + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
                + "     and substr(hourOfDay,"+poshourOfDay +",1) = '1'"
                + "     union"
                + "     select "+poshourBefore+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from process.tb_agenda where "
                + "     ageEnable=1 "
                + "     and ageInclusive=1"
                + "     and substr(month,"+posmonth+",1) = '1'"
                + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
                + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
                + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
                + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
                + "     and substr(hourOfDay,"+poshourBefore +",1) = '1'"
                + "     union"
                + "     select "+poshourAfter+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from process.tb_agenda where "
                + "     ageEnable=1 "
                + "     and ageInclusive=1"
                + "     and substr(month,"+posmonth+",1) = '1'"
                + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
                + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
                + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
                + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
                + "     and substr(hourOfDay,"+poshourAfter +",1) = '1'"
                + "     ";
        try {
            Statement stm;
            stm = gDatos.getMetadataConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            JSONObject jData = new JSONObject();
            JSONArray jArray = new JSONArray();

            ResultSet rs = stm.executeQuery(vSQL);
            if (rs!=null) {
                while (rs.next()) {
                    jData.put("horaAgenda", rs.getString("horaAgenda"));
                    jData.put("ageID", rs.getString("ageID"));
                    jData.put("month", rs.getString("month"));
                    jData.put("dayOfMonth", rs.getString("dayOfMonth"));
                    jData.put("weekOfYear", rs.getString("weekOfYear"));
                    jData.put("weekOfMonth", rs.getString("weekOfMonth"));
                    jData.put("hourOfDay", rs.getString("hourOfDay"));
                    jArray.put(jData);
                }
            } else {
                System.out.println("No hay registros");
            }

            System.out.println(jArray.toString());

        } catch (SQLException | JSONException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
    
}
