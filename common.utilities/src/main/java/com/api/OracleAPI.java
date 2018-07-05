/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.api;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author NEO
 */
public class OracleAPI {

	private Connection connection;
	private Statement stm;
	private CallableStatement cs; 
        
    private String dbHost;
    private String dbName;
    private String dbPort;
    private String dbUser;
    private String dbPass;
    private String dbOwner;
    private int timeOut;
    
    public OracleAPI() { }

    public OracleAPI(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int timeOut) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.timeOut = timeOut;
    }
    
    public void setDBOwner(String dbOwner) {
    	this.dbOwner = dbOwner;
    }
    
    public String getDbOwner() {
    	return this.dbOwner;
    }
    
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean isConnected() throws Exception {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

    public OracleAPI open() throws Exception  {
        
    	//Define la clase de la conexión
        try {
        	Class.forName("oracle.jdbc.OracleDriver");
        } catch (Exception e) {
        	throw new Exception(e.getMessage());
        }
        
        //Crea string de conexión
        
        String StringConnection = "jdbc:oracle:thin:@"+dbHost+":"+dbPort+":"+dbName;
        
        //Setea TimeOut en Driver de Conexion
        DriverManager.setLoginTimeout(timeOut);
        
        //Establece conexión a la BD
        try {
            connection = DriverManager.getConnection(StringConnection, dbUser, dbPass);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    
        return this;
    } 
    
	public void close() throws Exception {
		try {
			if(!stm.isClosed()) {
				stm.close();
			}
			
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public int executeUpdate(String upd) throws Exception {
		try {
			stm = connection.createStatement();
	    		stm.executeUpdate(upd);
	    		int result = stm.getUpdateCount();
	    		stm.close();
	    		return result;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
    
	public boolean executeQuery(String sql) throws Exception {
	    	try {
	    		stm = connection.createStatement(); //.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	    		return stm.execute(sql);
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	}
	 
	public ResultSet getQuery() throws Exception {
		try {
	    		return stm.getResultSet();
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	 }

	 public boolean isExistRows() throws Exception {
	    	try {
	    		boolean result;
	    		if (stm.getResultSet()!=null) {
	    			if (stm.getResultSet().next()) {
	    				result = true;
	    			} else {
	    				result = false;
	    			}
	    		} else {
	    			result = false;
	    		}
	    		
	    		return result;
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
    }
	 
	public boolean executeProcedure(String spName, List<String> spParams) throws Exception {
		try {
			boolean isExistParams = false;
			int numParams =0;
			
			if (!Objects.isNull(spParams)) {
				if (spParams.size()>0) {
					isExistParams = true;
					numParams = spParams.size();
				}
			}
			
			if (!isExistParams) {
				String strSpName = "{ call "+spName+" }";
				cs = connection.prepareCall(strSpName);
			} else {
				String cad = "";
				for (int i=0; i<numParams; i++) {
					if (i<numParams-1) {
						cad = cad + "?,";
					} else {
						cad = cad + "?";
					}
				}
				String strSpName = "{ call "+spName+"("+cad+") }";
				cs = connection.prepareCall(strSpName);
			}
			
			int numParam=1;
			for (String param : spParams) {
				String[] items = param.split("&");
				String paramInOut = items[0];
				String paramType = items[1];
				String paramValue = "";
				
				try {
					paramValue = items[2];
				} catch (Exception e) {
					paramValue = "";
				}

				switch(paramType) {
					case "VARCHAR":
						cs.setString(numParam, paramValue);
						break;
					case "INTEGER":
						cs.setInt(numParam, Integer.valueOf(paramValue));
						break;
					default:
						break;
				}
				numParam++;
			}
			
			/*
			 If SQL stored procedure raises any error then it does not throw any exception in java code
			JDBC drivers should throw error is with severity greater than 10. But it does not, if you are 
			using CallableStatement.execute()
			If you need to catch error then use executeUpdate() method. But executeUpdate() does not return result sets. 
			You can use executeUpdate() if you are not returning anything.
			
			true if the next result is a ResultSet object; false if it is an update count or there are no more results 
			So, if you are doing an insert, then the return would always be false.
			If you want to know if you were successfull with your insert, you can use executeUpdate instead. This will return 
			the number of rows that were updated, so if it's > 0 then you were succesful. "
			may be it will help other people
			
			 */
			
			boolean response = cs.execute();

			if (!response) {
				ResultSet rs = cs.getResultSet();
			}
			
			if (cs!=null) {
				cs.close();
			}
			
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
