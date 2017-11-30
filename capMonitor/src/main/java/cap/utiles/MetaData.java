/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.utiles;

import java.sql.Connection;
import java.sql.ResultSet;

import dataAccess.MySQLDB;
import utiles.common.rutinas.Rutinas;


/**
 *
 * @author andresbenitez
 */
public class MetaData {
    GlobalArea gDatos;
    Rutinas mylib = new Rutinas();
    
    private MySQLDB myConn; 
    
    public MetaData (GlobalArea m) {
        gDatos = m;
    }
    
    public void open() throws Exception{
        switch (gDatos.getInfo().getDbType()) {
            case "mySQL":
                try {
                    myConn = new MySQLDB(
                    						gDatos.getInfo().getDbHostName(), 
                    						gDatos.getInfo().getDbName(), 
                    						String.valueOf(gDatos.getInfo().getDbPort()), 
                    						gDatos.getInfo().getDbUser(), 
                    						gDatos.getInfo().getDbPass(),
                    						10);
                    myConn.open();
                } catch (Exception e) {
                	//mylib.console(1,"Error de conexion a MetaData: "+e.getMessage());
                	throw new Exception(e.getMessage());
                }
                break;
            default:
                break;
        }
    }
    
    public Connection getConnection() {
	    	switch (gDatos.getInfo().getDbType()) {
	    		case "mySQL":
	    			return myConn.getConnection();
				default:
					return null;
	    	} 
    }
    
    public boolean isConnected() throws Exception {
	    	try {
		        switch (gDatos.getInfo().getDbType()) {
		            case "mySQL":
		                return myConn.isConnected();
		            default:
		                return false;
		        }
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
    }
    
    public boolean executeQuery(String vSQL) throws Exception {
	    	try {
		        switch (gDatos.getInfo().getDbType()) {
		        	case "mySQL":
		    			return myConn.executeQuery(vSQL);
		    		default:
		    			return false;
		        }
	    	}
	        catch (Exception e) {
	        	throw new Exception(e.getMessage());
	    	}
    }
    
    public ResultSet getQuery() throws Exception{
	    	try {
		        switch (gDatos.getInfo().getDbType()) {
		            case "mySQL":
	                    return myConn.getQuery();
		            default:
		                return null;
		        }
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
    }
    
    public int executeUpdate(String upd) throws Exception{
	    	try {
		        switch (gDatos.getInfo().getDbType()) {
		            case "mySQL":
	                    return myConn.executeUpdate(upd);
		            default:
		                return 0;
		        }
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
    }
    
    public boolean isExistRows() throws Exception {
	    	try {
		        switch (gDatos.getInfo().getDbType()) {
	            case "mySQL":
	                return myConn.isExistRows();
	            default:
	                return false;
		        }
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
    }
    
    
    public void close() throws Exception {
	    	try {
		        switch (gDatos.getInfo().getDbType()) {
		            case "mySQL":
		                myConn.close();
		                break;
		            default:
		                break;
		        }
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
    }
}
