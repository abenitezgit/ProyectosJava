package dataAccess;
import java.sql.*;

/**
 *
 * @author NEO
 */
public class sqlDB {
    
	//Variables
	private Connection connection;
	private Statement stm;
        
	private String dbHost;
	private String dbName;
	private String dbPort;
	private String dbUser;
	private String dbPass;
	private int timeOut; 
    
    //Getter and setter
    
    public Statement getStm() {
		return stm;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
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

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isConnected() throws Exception {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public String getDbName() {
		return dbName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}
    
	
    //Constructor
    
    public sqlDB() {
    }


	public sqlDB(String dbHost, String dbName, String dbPort, String dbUser, String dbPass, int timeOut) {
    		this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.timeOut = timeOut;
    }
    

	//Procedimientos internos
    public sqlDB open() throws Exception {
        
    	//Define la clase de conexion
    		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			throw new Exception(e.getMessage());
		}
        
        //Crea String de Conexion
    		String StringConnection = "jdbc:sqlserver://"+getDbHost()+";databaseName="+getDbName()+";user="+getDbUser()+";password="+getDbPass();

        
        //Setea TimeOut en Driver de Conexion
        DriverManager.setLoginTimeout(timeOut);
        
        //Establece la conexion a la BD
        try {
			connection = DriverManager.getConnection(StringConnection,dbUser,dbPass);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
			
        return this;
    }
	
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
	    		/*
	    		 * Considerar ciertos parametros que podrian utilizarse
	    		 * createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    		 */
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
}
