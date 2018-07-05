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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ABT
 */
public class MysqlAPI {

	private Connection connection;
	private Statement stm;
	private CallableStatement cs; 
    
	private String dbHost;
	private String dbName;
	private String dbPort;
	private String dbUser;
	private String dbPass;
	private int timeOut; 
    
    public MysqlAPI(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int timeOut) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.timeOut = timeOut;
    }
    
    /**
     * Getter and Setter
     * @return
     */
    
    
    public String getDbHost() {
		return dbHost;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isConnected() throws Exception {
		try {
			boolean result = !connection.isClosed(); 
			return result;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Procedimientos Internos
	 * @throws SQLException
	 */
	
    public void setConfig(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int timeOut) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.timeOut = timeOut;
    }

    public MysqlAPI open() throws Exception {
        
	    	//Define la clase de conexion
	    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Register class: "+e.getMessage());
		}
        
        //Crea String de Conexion
    		String StringConnection = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName;

        //Setea TimeOut en Driver de Conexion
        DriverManager.setLoginTimeout(timeOut);
        
        //Establece la conexion a la BD
        try {
			connection = DriverManager.getConnection(StringConnection,dbUser,dbPass);
		} catch (SQLException e) {
			throw new Exception("DriverManager ("+StringConnection+"): "+e.getMessage());
		}
			
        return this;
    }

    public MysqlAPI open2() throws Exception {
        
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    
    		//Crea String de Conexion
			String StringConnection = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName;

		    //Setea TimeOut en Driver de Conexion
		    DriverManager.setLoginTimeout(timeOut);
    
		    connection = DriverManager.getConnection(StringConnection,dbUser,dbPass);
		    
		    return this;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }

	public void close() throws Exception {
		try {
			if (!Objects.isNull(stm)) {
				if (!stm.isClosed()) {
					stm.close();
				}
			}
			
			if (Objects.isNull(connection)) {
				if (!connection.isClosed()) {
					connection.close();
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
    
    public int executeUpdate(String upd) throws Exception {
    		//Operaciones update, delete, insert
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
    		//Operaciones select
	    	try {
	    		stm = connection.createStatement();
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
