/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import dataClass.ETL;
import dataClass.EtlMatch;
import dataClass.Interval;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import utilities.globalAreaData;
import utilities.srvRutinas;

/**
 *
 * @author andresbenitez
 */
public class thGetETL extends Thread{
    globalAreaData gDatos;
    srvRutinas gSub;
    static Logger logger = Logger.getLogger("thGetETL");
    MetaData conn;
    
    public thGetETL(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
        try{
            conn = new MetaData(gDatos);
        } catch (Exception e) {
            logger.error("Error conectando a Metadata..."+e.getMessage());
        }
    }
    
    @Override
    public void run() {
        logger.info("Iniciando Ciclo Ejecución Thread ETL...");
        
        ETL etl;
        EtlMatch etlMatch;
        List<EtlMatch> lstEtlMatch;
        Interval interval;
        
        //INITCIO ETAPA 1
        //Buscando programaciones de ETL con sus respexitvos Match de Campos
        //
        String vSQL =   "select  cfg.ETLID ETLID, cfg.ETLDESC ETLDESC, cfg.ETLENABLE ETLENABLE, cli.CLIDESC CLIDESC,\n" +
                        "        cfg.ETLINTERVALFIELDKEY FIELDKEY, cfg.ETLINTERVALFIELDKEYTYPE FIELDTYPE, cfg.ETLINTERVALTIMEGAP TIMEGAP, cfg.ETLINTERVALTIMEGENINTERVAL TIMEGEN,\n" +
                        "        cfg.ETLINTERVALTIMEPERIOD TIMEPERIOD, cfg.ETLINTERVALUNITMEASURE UNITMEASURE, cfg.ETLQUERYWHEREACTIVE WHEREACTIVE, cfg.ETLQUERYBODY QUERYBODY,\n" +
                        "        cfg.ETLSOURCETBNAME STBNAME,  cfg.ETLDESTTBNAME DTBNAME,\n" +
                        "        srv.SERVERIP SIP, \n" +
                        "        db.DBDESC SDBDESC, db.DBNAME SDBNAME, db.DBTYPE SDBTYPE, db.DBPORT SDBPORT, db.DBINSTANCE SDBINSTANCE, db.DBFILECONF SDBCONF, db.DBJDBCSTRING SDBJDBC,\n" +
                        "        usr.USERNAME SUSERNAME, usr.USERPASS SUSERPASS, usr.USERTYPE SUSERTYPE,\n" +
                        "        srvD.SERVERIP DIP,\n" +
                        "        dbD.DBDESC DDBDESC, dbD.DBNAME DDBNAME, dbD.DBTYPE DDBTYPE, dbD.DBPORT DDBPORT, dbD.DBINSTANCE DDBINSTANCE, dbD.DBFILECONF DDBCONF, dbD.DBJDBCSTRING DDBJDBC,\n" +
                        "        usrD.USERNAME DUSERNAME, usrD.USERPASS DUSERPASS, usrD.USERTYPE DUSERTYPE\n" +
                        "from\n" +
                        "  process.tb_etlConf cfg,\n" +
                        "  process.tb_servidor srv,\n" +
                        "  process.tb_basedatos db,\n" +
                        "  process.tb_cliente cli,\n" +
                        "  PROCESS.TB_USERS usr,\n" +
                        "  PROCESS.TB_SERVIDOR srvD,\n" +
                        "  PROCESS.TB_BASEDATOS dbD,\n" +
                        "  PROCESS.TB_USERS usrD\n" +
                        "where\n" +
                        "  cfg.ETLCLIID = cli.CLIID\n" +
                        "  And cfg.ETLSourceServerID = srv.SERVERID\n" +
                        "  And cfg.ETLSourceDBID = db.DBID\n" +
                        "  And cfg.ETLSOURCEUSERID = usr.USERID\n" +
                        "  And cfg.ETLDESTSERVERID = srvD.SERVERID\n" +
                        "  And cfg.ETLDESTDBID = dbD.DBID\n" +
                        "  And cfg.ETLDESTUSERID = usrD.USERID\n" +
                        "order by\n" +
                        "  ETLID";
        
        if (conn.isConnected()) {
            //Ejecuta Query de Consulta
            ResultSet rs = (ResultSet) conn.getQuery(vSQL);
            if (rs!=null) {
                etl = new ETL();
                try {
                    while (rs.next()) {
                        if (rs.getString("ETLID")!=null) {
                            etl.setETLID(rs.getString("ETLID")); 
                        }
                        if (rs.getString("ETLDESC")!=null) {
                            etl.setETLDesc(rs.getString("ETLDESC")); 
                        }
                        if (rs.getString("ETLENABLE")!=null) {
                            etl.setETLEnable(rs.getInt("ETLENABLE")); 
                        }
                        if (rs.getString("CLIDESC")!=null) {
                            etl.setCliDesc(rs.getString("CLIDESC")); 
                        }
                        if (rs.getString("FIELDKEY")!=null) {
                            etl.setFIELDKEY(rs.getString("FIELDKEY")); 
                        }
                        if (rs.getString("FIELDTYPE")!=null) {
                            etl.setFIELDTYPE(rs.getString("FIELDTYPE")); 
                        }
                        if (rs.getString("TIMEGAP")!=null) {
                            etl.setTIMEGAP(rs.getInt("TIMEGAP")); 
                        }
                        if (rs.getString("TIMEGEN")!=null) {
                            etl.setTIMEGEN(rs.getInt("TIMEGEN")); 
                        }
                        if (rs.getString("TIMEPERIOD")!=null) {
                            etl.setTIMEPERIOD(rs.getInt("TIMEPERIOD")); 
                        }
                        if (rs.getString("UNITMEASURE")!=null) {
                            etl.setUNITMEASURE(rs.getString("UNITMEASURE")); 
                        }
                        if (rs.getString("WHEREACTIVE")!=null) {
                            etl.setWHEREACTIVE(rs.getInt("WHEREACTIVE")); 
                        }
                        if (rs.getString("QUERYBODY")!=null) {
                            etl.setQUERYBODY(rs.getString("QUERYBODY")); 
                        }
                        if (rs.getString("STBNAME")!=null) {
                            etl.setSTBNAME(rs.getString("STBNAME")); 
                        }
                        if (rs.getString("DTBNAME")!=null) {
                            etl.setDTBNAME(rs.getString("DTBNAME")); 
                        }
                        if (rs.getString("SIP")!=null) {
                            etl.setSIP(rs.getString("SIP")); 
                        }
                        if (rs.getString("SDBNAME")!=null) {
                            etl.setSDBNAME(rs.getString("SDBNAME")); 
                        }
                        if (rs.getString("SDBDESC")!=null) {
                            etl.setSDBDESC(rs.getString("SDBDESC")); 
                        }
                        if (rs.getString("SDBTYPE")!=null) {
                            etl.setSDBTYPE(rs.getString("SDBTYPE")); 
                        }
                        if (rs.getString("SDBPORT")!=null) {
                            etl.setSDBPORT(rs.getString("SDBPORT")); 
                        }
                        if (rs.getString("SDBINSTANCE")!=null) {
                            etl.setSDBINSTANCE(rs.getString("SDBINSTANCE")); 
                        }
                        if (rs.getString("SDBCONF")!=null) {
                            etl.setSDBCONF(rs.getString("SDBCONF")); 
                        }
                        if (rs.getString("SDBJDBC")!=null) {
                            etl.setSDBJDBC(rs.getString("SDBJDBC")); 
                        }
                        if (rs.getString("SUSERNAME")!=null) {
                            etl.setSUSERNAME(rs.getString("SUSERNAME")); 
                        }
                        if (rs.getString("SUSERPASS")!=null) {
                            etl.setSUSERPASS(rs.getString("SUSERPASS")); 
                        }
                        if (rs.getString("SUSERTYPE")!=null) {
                            etl.setSUSERTYPE(rs.getString("SUSERTYPE")); 
                        }
                        if (rs.getString("DIP")!=null) {
                            etl.setDIP(rs.getString("DIP")); 
                        }
                        if (rs.getString("DDBDESC")!=null) {
                            etl.setDDBDESC(rs.getString("DDBDESC")); 
                        }
                        if (rs.getString("SDBNAME")!=null) {
                            etl.setDDBNAME(rs.getString("SDBNAME")); 
                        }
                        if (rs.getString("DDBTYPE")!=null) {
                            etl.setDDBTYPE(rs.getString("DDBTYPE")); 
                        }
                        if (rs.getString("DDBPORT")!=null) {
                            etl.setDDBPORT(rs.getString("DDBPORT")); 
                        }
                        if (rs.getString("DDBINSTANCE")!=null) {
                            etl.setDDBINSTANCE(rs.getString("DDBINSTANCE")); 
                        }
                        if (rs.getString("DDBCONF")!=null) {
                            etl.setDDBCONF(rs.getString("DDBCONF")); 
                        }
                        if (rs.getString("DDBJDBC")!=null) {
                            etl.setDDBJDBC(rs.getString("DDBJDBC")); 
                        }
                        if (rs.getString("DUSERNAME")!=null) {
                            etl.setDUSERNAME(rs.getString("DUSERNAME")); 
                        }
                        if (rs.getString("DUSERPASS")!=null) {
                            etl.setDUSERPASS(rs.getString("DUSERPASS")); 
                        }
                        if (rs.getString("DUSERTYPE")!=null) {
                            etl.setDUSERTYPE(rs.getString("DUSERTYPE")); 
                        }
                        
                        logger.debug("ETL: "+gSub.serializeObjectToJSon(etl, true));
                        
                        //Recupera detalle de Match de Campos para este ETL
                        String vSQL2 =  "select \n" +
                                        "  ETLORDER, ETLSOURCEFIELD, ETLSOURCELENGTH, ETLSOURCETYPE,\n" +
                                        "  ETLDESTFIELD, ETLDESTLENGTH, ETLDESTTYPE\n" +
                                        "from \n" +
                                        "  process.tb_etlMatch\n" +
                                        "where\n" +
                                        "  ETLID='"+ etl.getETLID()  +"'\n" +
                                        "  And ETLENABLE=1 order by ETLORDER";
                        try {
                            ResultSet rs2 = (ResultSet) conn.getQuery(vSQL2);
                            if (rs2!=null) {
                                lstEtlMatch = new ArrayList<>();
                                while (rs2.next()) {
                                    etlMatch = new EtlMatch();
                                    if (rs2.getString("ETLORDER")!=null) {
                                        etlMatch.setEtlOrder(rs2.getInt("ETLORDER"));
                                    }
                                    if (rs2.getString("ETLSOURCEFIELD")!=null) {
                                        etlMatch.setEtlSourceField(rs2.getString("ETLSOURCEFIELD"));
                                    }
                                    if (rs2.getString("ETLSOURCELENGTH")!=null) {
                                        etlMatch.setEtlSourceLength(rs2.getInt("ETLSOURCELENGTH"));
                                    }
                                    if (rs2.getString("ETLSOURCETYPE")!=null) {
                                        etlMatch.setEtlSourceType(rs2.getString("ETLSOURCETYPE"));
                                    }
                                    if (rs2.getString("ETLDESTFIELD")!=null) {
                                        etlMatch.setEtlDestField(rs2.getString("ETLDESTFIELD"));
                                    }
                                    if (rs2.getString("ETLDESTLENGTH")!=null) {
                                        etlMatch.setEtlDestLength(rs2.getInt("ETLDESTLENGTH"));
                                    }
                                    if (rs2.getString("ETLDESTTYPE")!=null) {
                                        etlMatch.setEtlDestType(rs2.getString("ETLDESTTYPE"));
                                    }
                                    etl.getLstEtlMatch().add(etlMatch);
                                }
                                rs2.close();
                            } else {
                                logger.info("No hay match de campos para esta configuracion de ETL");
                            }
                        } catch (Exception e) {
                            logger.error("Error Ejecutando extraccion de Match de campos");
                        }
                        gDatos.updateLstEtlConf(etl);
                    }
                    rs.close();
                    logger.debug("Desc List ETL: "+gSub.serializeObjectToJSon(gDatos.getLstETLConf(), true));
                } catch (SQLException | IOException ex) {
                    logger.error("Error recorriendo recorset Query..."+ex.getMessage());
                }
            } else {
                logger.info("No se recuperaron datos de ETL");
            }
            //conn.closeConnection();
        } else {
            logger.error("Error no se pudo conectar a Metadata");
        }
        
        //FINALIZA ETAPA 1 (Busqueda de Configuraciones de ETL con sus Match de Campos)
        
        

        
        //INICIO ETAPA 2
        //Recuperando desde BD Intervalos Pendientes
        //
        vSQL =  "select\n" +
                "  ETLID, INTERVALID, FECINS, FECUPDATE, STATUS, USTATUS, NUMEXEC\n" +
                "from\n" +
                "  process.tb_etlinterval\n" +
                "where\n" +
                "  status='Sleeping'\n" +
                "order by\n" +
                "  ETLID,\n" +
                "  INTERVALID";
        try {
            ResultSet rs = (ResultSet) conn.getQuery(vSQL);
            if (rs!=null) {
                while (rs.next()) {
                    interval = new Interval();
                    if (rs.getString("ETLID")!=null) {
                        interval.setETLID(rs.getString("ETLID"));
                    }
                    if (rs.getString("INTERVALID")!=null) {
                        interval.setIntervalID(rs.getString("INTERVALID"));
                    }
                    if (rs.getString("FECINS")!=null) {
                        interval.setFechaIns(rs.getString("FECINS"));
                    }
                    if (rs.getString("FECUPDATE")!=null) {
                        interval.setFechaUpdate(rs.getString("FECUPDATE"));
                    }
                    if (rs.getString("STATUS")!=null) {
                        interval.setStatus(rs.getString("STATUS"));
                    }
                    if (rs.getString("NUMEXEC")!=null) {
                        interval.setNumExec(rs.getInt("NUMEXEC"));
                    }
                    gDatos.updateLstInterval(interval);
                }
                rs.close();
            } else {
                logger.debug("No hay intervalos pendientes en BD");
            }
        } catch (Exception e) {
            logger.error("Error recuperando Intevalos desde BD..."+ e.getMessage());
        }
        
        
        try {
            logger.debug("List interval: "+ gSub.serializeObjectToJSon(gDatos.getLstInterval().get(0), true));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(thGetETL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        //Extre Fecha Actual
        Date today;
        Date fecGap;
        Date fecIni;
        Date fecItera;
        Date fecIntervalIni;
        Date fecIntervalFin;

        int MinItera;
        int HoraItera;
        int DiaItera;
        int MesItera;
        int AnoItera;

        long numInterval;
        String localIntervalID;
        String todayChar;


        //Setea Fecha Actual
        //
        today = new Date();


        //Setea Fecha GAP - Desface de tiempo en extraccion
        //
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -(gDatos.getLstETLConf().get(0).getTIMEGAP()+gDatos.getLstETLConf().get(0).getTIMEPERIOD()));
        fecGap = c.getTime();

        //Setea Fecha Inicio Inscripcion/Revision de Intervalos

        c.setTime(today);
        c.add(Calendar.MINUTE, -gDatos.getLstETLConf().get(0).getTIMEGEN());
        fecIni = c.getTime();

        logger.debug("Fecha Actual: "+ today);
        logger.debug("Fecha GAP   : "+ fecGap);
        logger.debug("Fecha IniIns: "+ fecIni);


        fecItera = fecIni;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfToday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String IntervalIni;
        String IntervalFin;
        List<String> qualifier = new ArrayList<>();

        while (fecItera.compareTo(fecGap) < 0) {
            //Extrae Intervalo para Fecha fecItera
            //
            c.setTime(fecItera);
            AnoItera = c.get(Calendar.YEAR);
            MesItera = c.get(Calendar.MONTH);
            DiaItera = c.get(Calendar.DAY_OF_MONTH);
            HoraItera = c.get(Calendar.HOUR_OF_DAY);
            MinItera = c.get(Calendar.MINUTE);

            //Valida si el intervalo de extraccion (cETL_INTERVALUNIDAD) es por:
            //  Minutos     : 0
            //  Horas       : 1
            //  Dias        : 2
            //  Semanas     : 3
            //  Mensuales   : 4
            //  Anuales     : 5

            switch (gDatos.getLstETLConf().get(0).getUNITMEASURE()) {
                case "MINUTE":
                    fecIntervalIni = null;
                    fecIntervalFin = null;
                    numInterval = 60/gDatos.getLstETLConf().get(0).getTIMEPERIOD();
                    for (int i=1;i<=numInterval;i++) {
                        c.set(AnoItera, MesItera, DiaItera, HoraItera, (i)*gDatos.getLstETLConf().get(0).getTIMEPERIOD(),0);
                        fecIntervalFin = c.getTime();
                        if (fecIntervalFin.compareTo(fecItera) >0 ) {
                            c.set(AnoItera, MesItera, DiaItera, HoraItera, (i-1)*gDatos.getLstETLConf().get(0).getTIMEPERIOD(),0);
                            fecIntervalIni = c.getTime();
                            break;
                        }
                    }
                    c.setTime(fecItera);
                    c.add(Calendar.MINUTE, gDatos.getLstETLConf().get(0).getTIMEPERIOD());
                    fecItera = c.getTime();


                    IntervalIni = sdf.format(fecIntervalIni);
                    IntervalFin = sdf.format(fecIntervalFin);
                    localIntervalID = IntervalIni+'-'+IntervalFin;
                        
                    logger.debug(fecIntervalIni);
                    logger.debug(fecIntervalFin);                        


                    logger.debug("Inscribiendo Intervalos: "+localIntervalID);
                    
//                        if (connHB.isKeyValueExist("tb_etlInterval", vProc+"|"+localIntervalID)) {
//                            gDatos.writeLog(0, cCLASS_NAME, cMETHOD_NAME, "rowKey: "+ vProc+"|"+localIntervalID +" Existe!!");
//                            
//                        } else {
//                            gDatos.writeLog(0, cCLASS_NAME, cMETHOD_NAME, "rowKey: "+ vProc+"|"+localIntervalID +" NO Existe!!");
//                            
//                            //Preperando Datos para Inscribir
//                            //
//                            qualifier.add("data,intervalID,"+localIntervalID);
//                            qualifier.add("data,fecIns,"+sdfToday.format(today));
//                            qualifier.add("data,status,Ready");
//                            qualifier.add("data,numExec,0");
//                            
//                            connHB.putDataRows("tb_etlInterval", vProc+"|"+localIntervalID, qualifier);
//                            if (connHB.getStatusCode()==0) {
//                                gDatos.writeLog(0, cCLASS_NAME, cMETHOD_NAME, "Intervalo: "+localIntervalID +" Inscrito Correctamente");
//                            } else {
//                                gDatos.writeLog(2, cCLASS_NAME, cMETHOD_NAME, "Error Inscribiendo Intervalo: "+localIntervalID);
//                            }
//                        }

                        break;

                case "1":
                    fecIntervalIni = null;
                    fecIntervalFin = null;
                    numInterval = 24/gDatos.getLstETLConf().get(0).getTIMEPERIOD();
                    for (int i=1;i<=numInterval;i++) {
                        c.set(AnoItera, MesItera, DiaItera, (i)*gDatos.getLstETLConf().get(0).getTIMEPERIOD(), 0, 0);
                        fecIntervalFin = c.getTime();
                        if (fecIntervalFin.compareTo(fecItera) >0 ) {
                            c.set(AnoItera, MesItera, DiaItera, (i-1)*gDatos.getLstETLConf().get(0).getTIMEPERIOD(), 0, 0);
                            fecIntervalIni = c.getTime();
                            break;
                        }
                    }
                    c.setTime(fecItera);
                    c.add(Calendar.HOUR_OF_DAY, gDatos.getLstETLConf().get(0).getTIMEPERIOD());
                    fecItera = c.getTime();

                    System.out.println(fecIntervalIni);
                    System.out.println(fecIntervalFin);

                    break;

                case "2":
                case "3":
                case "4":
                case "5":
                default:
            }

        }
        
        
   
        logger.info("Terminando Ciclo Ejecución Thread ETL");
    }
}
