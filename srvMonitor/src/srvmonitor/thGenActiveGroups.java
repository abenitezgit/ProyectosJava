/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import dataClass.Agenda;
import dataClass.Grupo;
import dataClass.Process;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thGenActiveGroups extends Thread{
    static srvRutinas gSub;
    static globalAreaData gDatos;
    MetaData metadata;
    
//Carga Clase log4
    static Logger logger = Logger.getLogger("thGetAgendas");   
    
    public thGenActiveGroups(globalAreaData m) {
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

        
        //Setea Calendario en Base al TimeZone
        //
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

        int findHour    = gDatos.getServerInfo().getAgeShowHour();  //Cantidad de Horas definidas para la muestra de las agendas
        int findMinutes = gDatos.getServerInfo().getAgeGapMinute(); //GAP en minutos para encontrar agendas que deberían haberse activado

        //Genera las variables de Posicion a comparar con las guardadas en la base de datos
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
        String numSecExec = "0";
        
        
        /*
        Inicializa Lista de Agendas
        */
        gDatos.getLstShowAgendas().clear();     //Lista para el muestreo de agendas
        gDatos.getLstActiveAgendas().clear();   //Lista para las agendas que deben activar grupos de procesos
        
        //Data Class
        Agenda agenda;  //La clase de datos para agenda
        
        
        /**
         * Busca Todas las agenda en un rango de findHour (12) horas antes y después de la hora actual
         */
        logger.info("Buscando Agendas para Monitoreo...");
        
        for (int i=-findHour; i<=findHour; i++) {
            iteratorCalendar = new GregorianCalendar(tz);
            
            //Posiciona el iteratorCalendar tantas horas atrás como definido en findHour
            //Y extrae la hora correspondiente
            //
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
                        rs.close();
                    } else {
                        //Si para la hora buscada no encuentra agenda genera un objeto vacio
                        //solo con el valor de la hora correspondiente
                        //esto es para poder mostrar que en esa hora no hay agenda
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
                }            
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        
        logger.info("Se encontraron: "+gDatos.getLstShowAgendas().size()+ " Agendas para Monitoreo..");
        
        
        /**
         * Busca Todas las agendas que deberían ser activadas en el periodo de findMinutes (5) minutos de holgura
         */
        logger.info("Buscando Agendas Activas...");

        //Vuelve a inicializar las variables para realizar la busqueda de agendas que activaran grupos
        //
        iteratorCalendar = new GregorianCalendar(tz);
        iteratorHour = String.valueOf(iteratorCalendar.get(Calendar.HOUR_OF_DAY));
        posIteratorHour = String.valueOf(Integer.valueOf(iteratorHour)+1);
        
        for (int i=-findMinutes; i<=0; i++) {
            iteratorCalendar = new GregorianCalendar(tz);
            iteratorCalendar.add(Calendar.MINUTE, i);
            iteratorMinute = String.valueOf(iteratorCalendar.get(Calendar.MINUTE));
            posIteratorMinute = String.valueOf(Integer.valueOf(iteratorMinute)+1);
            
            //Para cada agenda que se encuentra en el periodo buscado se le asignara un numero unico de secuencia de ejecucion
            //Esto para no confundir las mismas agendas en periodos de ejecución buscados en el gap
            //
            numSecExec = String.format("%04d", year)+String.format("%02d", month+1)+String.format("%02d", dayOfMonth)+String.format("%02d", Integer.valueOf(iteratorHour))+String.format("%02d", Integer.valueOf(iteratorMinute));
            
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
                            agenda.setNumSecExec(Long.valueOf(numSecExec));
                            gDatos.updateLstActiveAgendas(agenda);
                        }
                        rs.close();
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        
        logger.info("Se han encontrado: "+gDatos.getLstActiveAgendas().size()+" Agendas activas.");
        
        /**
         * Busca paraa todas las agendas activas los grupos y procesos asignados
         */
        
        logger.info("Buscando Grupos de Procesos asociados a las agendas activas.");
        
        int numAgeActives = gDatos.getLstActiveAgendas().size();
        Grupo grupo;
        Process process;
        //gDatos.getLstActiveGrupos().clear();
        
        for (int i=0; i<numAgeActives; i++) {
        
            vSQL =  "select gr.GRPID, gr.GRPDESC, to_char(gr.UFECHAEXEC,'rrrr-mm-dd hh24:mi:ss') UFECHAEXEC, gr.USTATUS, gr.STATUS, gr.LASTNUMSECEXEC,  gr.CLIID, ha.HORID \n" +
                    "from \n" +
                    "  process.tb_HORAAGENDA ha,\n" +
                    "  process.tb_grupos gr\n" +
                    "where\n" +
                    "  ha.AGEID='"+gDatos.getLstActiveAgendas().get(i).getAgeID()+"'\n" +
                    "  AND ha.HORINCLUSIVE=1\n" +
                    "  AND gr.HORID = ha.HORID\n" +
                    "  AND gr.ENABLE=1";
            try (ResultSet rs = (ResultSet) metadata.getQuery(vSQL)) {
                if (rs!=null) {
                    while (rs.next()) {
                        grupo = new Grupo();
                        grupo.setGrpID(rs.getString("GRPID"));
                        grupo.setGrpDESC(rs.getString("GRPDESC"));
                        grupo.setGrpCLIID(rs.getString("CLIID"));
                        grupo.setGrpHORID(rs.getString("HORID"));
                        grupo.setGrpUFechaExec(rs.getString("UFECHAEXEC"));
                        grupo.setuStatus(rs.getString("USTATUS"));
                        grupo.setStatus(rs.getString("STATUS"));
                        grupo.setNumSecExec(gDatos.getLstActiveAgendas().get(i).getNumSecExec());
                        grupo.setLastNumSecExec(Long.valueOf(rs.getString("LASTNUMSECEXEC")));
                        
                        /**
                         * Por cada Grupo encontrado se buscan los procesos respectivos
                         */
                        logger.info("Buscando procesos asociados al grupo: "+grupo.getGrpID());
                        vSQL =  "  select PROCID, NORDER, CRITICAL \n" +
                                "  from \n" +
                                "    PROCESS.TB_PROCGRUPO\n" +
                                "  where\n" +
                                "    ENABLE = 1\n" +
                                "    AND GRPID = '"+rs.getString("GRPID")+"'\n" +
                                "  order by\n" +    
                                "    norder";
                        
                        try (ResultSet rs2 = (ResultSet) metadata.getQuery(vSQL)) {
                            if (rs2!=null) {
                                while (rs2.next()) {
                                    process = new Process();
                                    process.setProcID(rs2.getString("PROCID"));
                                    process.setnOrder(rs2.getInt("NORDER"));
                                    process.setCritical(rs2.getInt("CRITICAL"));
                                    grupo.getLstProcess().add(process);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error ejecutando query busca Procesos del Grupo..."+e.getMessage());
                        }
                        
                        gDatos.updateLstActiveGrupos(grupo);
                    }
                }
            } catch (Exception e) {
                logger.error("Error ejecutando query busca Grupos Activos..."+e.getMessage());
            }
        }
        
        logger.info("Se encontraron: "+gDatos.getLstActiveGrupos().size()+" Grupos para Activar...");
        
        try {
            for (int i=0; i<gDatos.getLstActiveAgendas().size(); i++) {
                logger.debug("Agenda para activar: "+ gSub.serializeObjectToJSon(gDatos.getLstActiveAgendas().get(i), true));
            }

            for (int i=0; i<gDatos.getLstActiveGrupos().size(); i++) {
                logger.info("Grupos para activar: "+ gSub.serializeObjectToJSon(gDatos.getLstActiveGrupos().get(i), true));
            }

            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //Date fecha = gDatos.getLstActiveGrupos().get(0).getGrpUFechaExec();
            //logger.info("UfechaExec: "+formatter.format(fecha));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(thGenActiveGroups.class.getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("Finaliza busquenda agendas activas...");
        
        
        /**
         * 
         * Actualiza la lista de PoolProcess con la información de grupo, proceso y detalle del proceso
         */
        
        
        //Inicia Recorriendo la lista de Grupos Activos y validando si cada proceso interno se encuentra o no en la poolProcess
        //para no tener que ir a buscar su información nuevamente a la base de datos.
        //La llave de acceso a busquedas es el grpID, procID, numSecExec
        
        
        int numLstGrupos = gDatos.getLstActiveGrupos().size();
        
        if (numLstGrupos>0) {
            for (int i=0; i<numLstGrupos; i++) {
                String grpID = gDatos.getLstActiveGrupos().get(i).getGrpID();
                //String numSecExec = gDatos.getLstActiveGrupos().get(i).getNumSecExec();
                int numProcAssigned = gDatos.getLstActiveGrupos().get(i).getLstProcess().size();
                for (int j=0; j<numProcAssigned; j++) {
                    String procID = gDatos.getLstActiveGrupos().get(i).getLstProcess().get(j).getProcID();
                }
            }
        } else {
            logger.info("No hay Grupos asociados a Agendas activas.");
        }
        
        
        
        metadata.closeConnection();
    }
}
