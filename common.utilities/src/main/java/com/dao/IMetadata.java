package com.dao;

import java.sql.ResultSet;

public interface IMetadata {
	
	public void open() throws Exception;
	public void close() throws Exception;
	public int executeUpdate(String upd) throws Exception;
	public boolean executeQuery(String sql) throws Exception;
	public ResultSet getQuery() throws Exception;
	public boolean isExistRows() throws Exception;
	public boolean isConnected() throws Exception;

}
