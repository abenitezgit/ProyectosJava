package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JMySql implements IMetadata {
	
	private Connection connection;
	private Statement stm;
    
	private String dbHost;
	private String dbName;
	private int dbPort;
	private String dbUser;
	private String dbPass;
	private int timeOut;
	
	public JMySql() {
	}
    
    public JMySql(String dbHost, String dbName, int dbPort, String dbUser, String dbPass, int timeOut) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.timeOut = timeOut;
    }
    
    //Getter and Setter

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStm() {
		return stm;
	}

	public void setStm(Statement stm) {
		this.stm = stm;
	}

	public String getDbHost() {
		return dbHost;
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

	public int getDbPort() {
		return dbPort;
	}

	public void setDbPort(int dbPort) {
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

	@Override
	public void open() throws Exception {
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
	}

	@Override
	public void close() throws Exception {
		try {
			if (!stm.isClosed()) {
				stm.close();
			}
			
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
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

	@Override
	public boolean executeQuery(String sql) throws Exception {
	    	try {
	    		stm = connection.createStatement();
	    		return stm.execute(sql);
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	}

	@Override
	public ResultSet getQuery() throws Exception {
	    	try {
	    		return stm.getResultSet();
	    	} catch (Exception e) {
	    		throw new Exception(e.getMessage());
	    	}
	}

	@Override
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

	@Override
	public boolean isConnected() throws Exception {
		try {
			boolean result = !connection.isClosed(); 
			return result;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
