/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvmonitor;

import java.sql.ResultSet;
import org.apache.log4j.Logger;
import utilities.globalAreaData;
import utilities.hbaseDB;
import utilities.oracleDB;
import utilities.sqlDB;

/**
 *
 * @author andresbenitez
 */
public class MetaData {
    static Logger logger = Logger.getLogger("MetaData");
    globalAreaData gDatos;
    
    static oracleDB oraConn;
    static sqlDB sqlConn;
    static hbaseDB hbConn;
    
    public MetaData (globalAreaData m) {
        gDatos = m;
        
        switch (gDatos.getServerInfo().getDbType()) {
            case "ORA":
                try {
                    oraConn = new oracleDB(gDatos.getServerInfo().getDbOraHost(), gDatos.getServerInfo().getDbOraDBNAme(), String.valueOf(gDatos.getServerInfo().getDbOraPort()), gDatos.getServerInfo().getDbOraUser(), gDatos.getServerInfo().getDbOraPass());
                    oraConn.conectar();
                    if (oraConn.getConnStatus()) {
                        gDatos.getServerStatus().setIsValMetadataConnect(true);
                    }
                    else {
                        gDatos.getServerStatus().setIsValMetadataConnect(false);
                    }
                } catch (Exception e) {
                    logger.error("Error de conexion a MetaData: "+e.getMessage());
                    gDatos.getServerStatus().setIsValMetadataConnect(false);
                }
                break;
            case "SQL":
                break;
            case "HBASE":
                break;
            default:
                break;
        }
    }
    
    public Object getQuery(String vSQL) {
        switch (gDatos.getServerInfo().getDbType()) {
            case "ORA":
                try {
                    ResultSet rs = oraConn.consultar(vSQL);
                    return rs;
                } catch (Exception e) {
                    logger.error("Error de Ejecucion SQL: "+e.getMessage());
                }
                break;
            case "SQL":
                break;
            case "HBASE":
                break;
            default:
                break;
        }
        return null;
    }
    
    public void closeConnection() {
        switch (gDatos.getServerInfo().getDbType()) {
            case "ORA":
                try {
                    oraConn.closeConexion();
                } catch (Exception e) {
                    logger.error("Error Cerrando Conexion: "+e.getMessage());
                    gDatos.getServerStatus().setIsValMetadataConnect(false);
                }
                break;
            case "SQL":
                break;
            case "HBASE":
                break;
            default:
                break;
        }
    }
}
