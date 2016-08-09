/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import dataClass.Agenda;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thGetAgendas extends Thread{
    static srvRutinas gSub;
    static globalAreaData gDatos;
    static MetaData metadata;
    
//Carga Clase log4
    static Logger logger = Logger.getLogger("thGetAgendas");   
    
    public thGetAgendas(globalAreaData m) {
        try {
            gDatos = m;
            gSub = new srvRutinas(gDatos);
            metadata = new MetaData(gDatos);
        } catch (Exception e) {
            logger.error("Error en Constructor: "+e.getMessage());
        }
    }
    
    @Override
    public void run() {
        /*
            Recupera Parametros Fecha Actual
        */
        logger.info("Buscando Agendas Activas");

        String[] ids = TimeZone.getAvailableIDs(-4 * 60 * 60 * 1000);
        String clt = ids[0];
        SimpleTimeZone tz = new SimpleTimeZone(-4 * 60 * 60 * 1000, clt);
        tz.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        tz.setEndRule(Calendar.AUGUST, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        Calendar calendar = new GregorianCalendar(tz);

        int year       = calendar.get(Calendar.YEAR);
        int month      = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); 
        int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int weekOfMonth= calendar.get(Calendar.WEEK_OF_MONTH);

        int findHour=12;
        int findMinutes=5;

        String posmonth = String.valueOf(month+1);
        String posdayOfMonth = String.valueOf(dayOfMonth);
        String posdayOfWeek = String.valueOf(dayOfWeek);
        String posweekOfYear = String.valueOf(weekOfYear);
        String posweekOfMonth = String.valueOf(weekOfMonth);
        
        Calendar iteratorCalendar;
        String vSQL;
        String iteratorHour;
        String iteratorMinute;
        String posIteratorHour;
        String posIteratorMinute;
        
        
        /*
        Inicializa Lista de Agendas
        */
        gDatos.getLstShowAgendas().clear();
        gDatos.getLstActiveAgendas().clear();
        
        //Data Class
        Agenda agenda;
        
        
        /**
         * Busca Todas las agenda en un rango de findHour (12) horas antes y después de la hora actual
         */
        logger.info("Buscando Agendas para Monitoreo...");
        
        for (int i=-findHour; i<=findHour; i++) {
            iteratorCalendar = new GregorianCalendar(tz);
            iteratorCalendar.add(Calendar.HOUR_OF_DAY, i);
            iteratorHour = String.valueOf(iteratorCalendar.get(Calendar.HOUR_OF_DAY));
            posIteratorHour = String.valueOf(Integer.valueOf(iteratorHour)+1);
            
            vSQL = "select "+iteratorHour+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from process.tb_agenda where "
                    + "     ageEnable=1 "
                    + "     and substr(month,"+posmonth+",1) = '1'"
                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'";
            try {
                try (ResultSet rs = (ResultSet) metadata.getQuery(vSQL)) {
                    if (rs!=null) {
                        while (rs.next()) {
                            agenda = new Agenda();
                            agenda.setHoraAgenda(rs.getString("horaAgenda"));
                            agenda.setAgeID(rs.getString("ageID"));
                            agenda.setMonth(rs.getString("month"));
                            agenda.setDayOfMonth(rs.getString("dayOfMonth"));
                            agenda.setWeekOfYear(rs.getString("weekOfYear"));
                            agenda.setWeekOfMonth(rs.getString("weekOfMonth"));
                            agenda.setHourOfDay(rs.getString("hourOfDay"));
                            gDatos.getLstShowAgendas().add(agenda);
                        }
                    } else {
                        agenda = new Agenda();
                        agenda.setHoraAgenda(iteratorHour);
                        agenda.setAgeID("");
                        agenda.setMonth("");
                        agenda.setDayOfMonth("");
                        agenda.setWeekOfYear("");
                        agenda.setWeekOfMonth("");
                        agenda.setHourOfDay("");
                        gDatos.getLstShowAgendas().add(agenda);
                    }
                    rs.close();
                }            
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        
        logger.info("Se encontraron: "+gDatos.getLstShowAgendas().size()+ " Agendas para Monitoreo..");
        
        iteratorCalendar = new GregorianCalendar(tz);
        iteratorHour = String.valueOf(iteratorCalendar.get(Calendar.HOUR_OF_DAY));
        posIteratorHour = String.valueOf(Integer.valueOf(iteratorHour)+1);
        
        /**
         * Busca Todas las agendas que deberían ser activadas en el periodo de findMinutes (5) minutos de holgura
         */
        logger.info("Buscando Agendas Activas...");

        for (int i=-findMinutes; i<=0; i++) {
            iteratorCalendar = new GregorianCalendar(tz);
            iteratorCalendar.add(Calendar.MINUTE, i);
            iteratorMinute = String.valueOf(iteratorCalendar.get(Calendar.MINUTE));
            posIteratorMinute = String.valueOf(Integer.valueOf(iteratorMinute)+1);
            
            vSQL = "select "+iteratorMinute+" horaAgenda,ageID, month, dayOfMonth, dayOfWeek, weekOfYear, weekOfMonth, hourOfDay from process.tb_agenda where "
                    + "     ageEnable=1 "
                    + "     and substr(month,"+posmonth+",1) = '1'"
                    + "     and substr(dayOfMonth,"+posdayOfMonth+",1) = '1'"
                    + "     and substr(dayOfWeek,"+posdayOfWeek+",1) = '1'"
                    + "     and substr(weekOfYear,"+posweekOfYear+",1) = '1'"
                    + "     and substr(weekOfMonth,"+posweekOfMonth+",1) = '1'"
                    + "     and substr(hourOfDay,"+posIteratorHour +",1) = '1'"
                    + "     and substr(minute,"+posIteratorMinute +",1) = '1'";
            try {
                try (ResultSet rs = (ResultSet) metadata.getQuery(vSQL)) {
                    if (rs!=null) {
                        while (rs.next()) {
                            agenda = new Agenda();
                            agenda.setHoraAgenda(rs.getString("horaAgenda"));
                            agenda.setAgeID(rs.getString("ageID"));
                            agenda.setMonth(rs.getString("month"));
                            agenda.setDayOfMonth(rs.getString("dayOfMonth"));
                            agenda.setWeekOfYear(rs.getString("weekOfYear"));
                            agenda.setWeekOfMonth(rs.getString("weekOfMonth"));
                            agenda.setHourOfDay(rs.getString("hourOfDay"));
                            gDatos.getLstActiveAgendas().add(agenda);
                        }
                    }
                    rs.close();
                }
            } catch (SQLException | JSONException e) {
                logger.error(e.getMessage());
            }
        }
        
        logger.info("Se encontraron: "+gDatos.getLstActiveAgendas()+" Aegndas para Activar...");

        logger.info("Finaliza busquenda agendas activas...");
        
        metadata.closeConnection();
    }
}
