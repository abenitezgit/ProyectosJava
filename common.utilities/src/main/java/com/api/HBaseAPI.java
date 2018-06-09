package com.api;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.rutinas.Rutinas;

/**
 * 
 * @author admin
   Se deben incorporar las siguientes referencias en el pom.xml del cliente
   	<dependency>
	    <groupId>org.apache.hbase</groupId>
	    <artifactId>hbase-client</artifactId>
	    <version>1.3.0</version>
	      <exclusions>
	        <exclusion>
	          <groupId>com.sun.jersey</groupId> <!-- Exclude Project-E from Project-B -->
	          <artifactId>jersey-core</artifactId>
	        </exclusion>
	        <exclusion>
	          <groupId>com.sun.jersey</groupId> <!-- Exclude Project-E from Project-B -->
	          <artifactId>jersey-json</artifactId>
	        </exclusion>
	        <exclusion>
	          <groupId>com.sun.jersey</groupId> <!-- Exclude Project-E from Project-B -->
	          <artifactId>jersey-server</artifactId>
	        </exclusion>
	      </exclusions>
	</dependency>
	
	Version: 2.0.0
	NOTA: Se establece objeto generico Connection conn, para evitar abrir y cerrar conexiones por cada operacion
	      todos los programas que ejecuten esta api deben establecer el hbase.close() para cerrar la conexión, la 
	      cual es abierta en hbase.setConfig();
	      
	      Se elimina hbase.setConfig que no tiene tableName em su constructar

 */

public class HBaseAPI {
	Rutinas mylib = new Rutinas();
	Configuration hcfg  = HBaseConfiguration.create();
	Connection conn;
	String tbName;
	
	public Configuration getHcfg() throws Exception {
		return hcfg;
	}
	
	public Connection getConnection() throws Exception {
		try {
			return conn;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void close() throws Exception {
		try {
			conn.close();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void setConfig(String filePropertiesPath, String HBProperty, String tbName ) throws Exception {
		try {
			Properties fileProperties = new Properties();
			String [] confFiles;
			String pathFiles;
			
			fileProperties.load(new FileInputStream(filePropertiesPath));
			confFiles = fileProperties.getProperty(HBProperty+".hadoopConfs").split(",");
			pathFiles = fileProperties.getProperty(HBProperty+".hadoopPath");
            
            for (int i=0; i< confFiles.length; i++  ) {
                hcfg.addResource(new Path(pathFiles+"/"+confFiles[i]));
            }
            this.tbName = tbName;
            
            conn = ConnectionFactory.createConnection(hcfg);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public boolean isTableAvailable() throws Exception {
    	try {
    		
    		Admin admin = conn.getAdmin();
    		
    		boolean exist = admin.isTableAvailable(TableName.valueOf(tbName));
    		
    		admin.close();
    		return exist;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
	
	public boolean isTableAvailable(String tableName) throws Exception {
    	try {

    		Admin admin = conn.getAdmin();
    		
    		boolean exist = admin.isTableAvailable(TableName.valueOf(tableName));
    		
    		admin.close();
    		return exist;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }

	public boolean isRowFound(String key) throws Exception {
		try {
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			Result r = table.get(get);
			
			boolean isFound;
			
			if (r.isEmpty()) {
				isFound = false;
			} else {
				isFound = true;
			}

			table.close();
			
            return isFound;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deleteTimeRange(String fecIni, String fecEnd, int maxRowsRange) throws Exception {
		try {
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	    	Date dFecIni = formatter.parse(fecIni);
	    	Date dFecEnd = formatter.parse(fecEnd);
	    	
	    	Calendar calStart = Calendar.getInstance();
	    	Calendar calEnd = Calendar.getInstance();
	    	
	    	calStart.setTime(dFecIni);
	    	calEnd.setTime(dFecEnd);
	    	
	    	long startTime = calStart.getTimeInMillis();
	    	long endTime = calEnd.getTimeInMillis();
	    	
	    	Table table = conn.getTable(TableName.valueOf(tbName));
	    	List<Delete> listOfBatchDeletes = new ArrayList<Delete>();
	    	long recordCount = 0;
	    	
	    	//Set all scan related properties
        Scan scan = new Scan();
        //Most important part of code set it properly!
        //here my purpose it to delete everthing Present Time - 6 hours
        scan.setTimeRange(startTime, endTime);
        scan.setCaching(10000);

      //Scan the table and get the row keys
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result scanResult : resultScanner) {
        		Delete delete = new Delete(scanResult.getRow());

        		//Create batches of Bult Delete
        		listOfBatchDeletes.add(delete);
        		recordCount++;
        		if (listOfBatchDeletes.size() == maxRowsRange) {
        			mylib.console("Firing Batch Delete Now ("+maxRowsRange+")......");
        			table.delete(listOfBatchDeletes);
        			//don't forget to clear the array list
        			listOfBatchDeletes.clear();
        		}
        	}
        
        mylib.console("Firing Final Batch of Deletes ("+listOfBatchDeletes.size()+").....");
        table.delete(listOfBatchDeletes);
        mylib.console("Total Records Deleted are.... " + recordCount);
        
        resultScanner.close();
        table.close();
			
		} catch (Exception e) {
			throw new Exception("Error deleteTimeRange: "+e.getMessage());
		}
	}

	public void deleteByDateRangeKey(String fecStart, String fecStop, int maxRowsRange) throws Exception {
		try {
	    	
	    	Table table = conn.getTable(TableName.valueOf(tbName));
	    	List<Delete> listOfBatchDeletes = new ArrayList<Delete>();
	    	long recordCount = 0;
	    	
	    	//Set all scan related properties
        Scan scan = new Scan();
        //Most important part of code set it properly!
        //here my purpose it to delete everthing Present Time - 6 hours
        scan.setCaching(10000);
        
        scan.setStartRow(Bytes.toBytes(fecStart));
        scan.setStopRow(Bytes.toBytes(fecStop));

      //Scan the table and get the row keys
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result scanResult : resultScanner) {
        		Delete delete = new Delete(scanResult.getRow());

        		//Create batches of Bult Delete
        		listOfBatchDeletes.add(delete);
        		recordCount++;
        		if (listOfBatchDeletes.size() == maxRowsRange) {
        			mylib.console("Firing Batch Delete Now ("+maxRowsRange+")......");
        			table.delete(listOfBatchDeletes);
        			//don't forget to clear the array list
        			listOfBatchDeletes.clear();
        		}
        	}
        
        mylib.console("Firing Final Batch of Deletes ("+listOfBatchDeletes.size()+").....");
        table.delete(listOfBatchDeletes);
        mylib.console("Total Records Deleted are.... " + recordCount);
        
        resultScanner.close();
        table.close();
			
		} catch (Exception e) {
			throw new Exception("Error deleteTimeRange: "+e.getMessage());
		}
	}

	public void deleteKeys(List<String> lstKeys) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			List<Delete> lstDel = new ArrayList<>();
			
			for (int i=0; i<lstKeys.size(); i++) {
				Delete delKey = new Delete(lstKeys.get(i).getBytes());
				lstDel.add(delKey);
			}
			
			table.delete(lstDel);

			table.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void deleteKey(String key) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Delete delKey = new Delete(key.getBytes());
			
			table.delete(delKey);

			table.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public boolean isRowFound(String key, String tbName) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			Result r = table.get(get);
			
			boolean isFound;
			
			if (r.isEmpty()) {
				isFound = false;
			} else {
				isFound = true;
			}
			
			table.close();
			
            return isFound;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void putRow(Map<String, List<colModel>> rows) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			List<Put> lstPut = new ArrayList<>();
			
			for (Entry<String, List<colModel>> entry : rows.entrySet()) {
				Put p = new Put(Bytes.toBytes(entry.getKey()));
				
				for (int i=0; i<entry.getValue().size(); i++) {
					String cf = entry.getValue().get(i).getFamily();
					String cq = entry.getValue().get(i).getColumn();
					String vu = entry.getValue().get(i).getValue();
					p.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cq),Bytes.toBytes(vu));
				}
				lstPut.add(p);
			}
			table.put(lstPut);
			
			table.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, Map<String,String>> getRows(List<String> keys) throws Exception {
		Map<String, Map<String, String>> mapKeyValue = new HashMap<>();
		
		Table table = conn.getTable(TableName.valueOf(tbName));
		
		for (String key : keys) {
			Get get = new Get(key.getBytes());
			
			Result r = table.get(get);
			
			Map<String, String> mapRowValue = new HashMap<>();
			
			for (Cell cell : r.listCells()) {
				mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
			}
			
			mapKeyValue.put(key, mapRowValue);
		}

		table.close();
		
		return mapKeyValue;
	}

	public Map<String, Map<String,String>> getRows(List<String> keys, String[] family) throws Exception {
		Map<String, Map<String, String>> mapKeyValue = new HashMap<>();
		
		Table table = conn.getTable(TableName.valueOf(tbName));
		
		for (String key : keys) {
			Get get = new Get(key.getBytes());
			
			//Add family filters
			if (family.length>0) {
				for (String cf : family) {
					get.addFamily(cf.getBytes());
				}
			}
			
			Result r = table.get(get);
			
			Map<String, String> mapRowValue = new HashMap<>();
			
			for (Cell cell : r.listCells()) {
				mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
			}
			
			mapKeyValue.put(key, mapRowValue);

		}

		table.close();
		
		return mapKeyValue;
	}
	
	
	public Map<String, Map<String,String>> getRows(Map<String,String> mapKey, String[] family) throws Exception {
		try {
			Map<String, Map<String, String>> mapKeyValue = new HashMap<>();
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			for (Map.Entry<String, String> entry : mapKey.entrySet()) {
				
				String key = entry.getKey();
				
				Get get = new Get(key.getBytes());
				
				//Add family filters
				if (family.length>0) {
					for (String cf : family) {
						get.addFamily(cf.getBytes());
					}
				}
				
				Result r = table.get(get);
				
				Map<String, String> mapRowValue = new HashMap<>();
				
				for (Cell cell : r.listCells()) {
					mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
				}
				
				mapKeyValue.put(key, mapRowValue);
			}
			
			table.close();
			
			return mapKeyValue;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, Map<String,String>> getRow(String key) throws Exception {
		try {
			mylib.console("HBase Creando conector...");
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			mylib.console("HBase Recuperando Key...");
			Result r = table.get(get);
			
			Map<String, String> mapRowValue = new HashMap<>();
			Map<String, Map<String, String>> mapKeyValue = new HashMap<>();
			
			if (r!=null) {
				if (!r.isEmpty()) {
				
					mylib.console("HBase Parseando Key...");
					for (Cell cell : r.listCells()) {
						mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
					}
				}
			}
			
			mylib.console("HBase Guardando key...");
			mapKeyValue.put(key, mapRowValue);
			
			table.close();
			
			mylib.console("HBase retornando Key...");
			return mapKeyValue;
		} catch (Exception e) {
			mylib.console(1,"Error getRow: "+e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public Map<String, Map<String,String>> getRow(String key, String[] family) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Get get = new Get(key.getBytes());
			
			//Add family filters
			if (family.length>0) {
				for (String cf : family) {
					get.addFamily(cf.getBytes());
				}
			}
			
			Result r = table.get(get);
			
			Map<String, String> mapRowValue = new HashMap<>();
			Map<String, Map<String, String>> mapKeyValue = new HashMap<>();
			
			for (Cell cell : r.listCells()) {
				mapRowValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
			}
			
			mapKeyValue.put(key, mapRowValue);
			
			table.close();
			
			return mapKeyValue;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<String> getKeys(long limit) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			List<String> lstKeys = new ArrayList<>();
			
			Scan scan = new Scan();
			
			scan.setCaching(10000);
			scan.setMaxVersions(1);
			scan.setMaxResultSize(limit);
			scan.setFilter(new PageFilter(limit));
			
			ResultScanner rs = table.getScanner(scan);
			
			Result r = rs.next();

			while (r!=null) {
				String key = Bytes.toString(r.getRow());
				lstKeys.add(key);
				r = rs.next();
			}
			
			table.close();

			return lstKeys;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public Map<String, Map<String,String>> scan(String[] family, int limit) throws Exception {
		try {
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Scan scan = new Scan();
			
			scan.setCaching(10000);
			scan.setMaxVersions(1);
			scan.setMaxResultSize(limit);
			scan.setFilter(new PageFilter(limit));

			//Add filters
			FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ALL); //AND
			//FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ONE); //OR
			
			SingleColumnValueFilter colValFilter1 = new SingleColumnValueFilter(Bytes.toBytes("cf1"), Bytes.toBytes("fecgrab")
	                ,CompareFilter.CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes("201801010000")));

			SingleColumnValueFilter colValFilter2 = new SingleColumnValueFilter(Bytes.toBytes("cf1"), Bytes.toBytes("fecgrab")
	                ,CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("20180101235959")));
			
			list.addFilter(colValFilter1);
			list.addFilter(colValFilter2);
			
			scan.setFilter(list);
			
			//Add family filters
			if (family!=null) {
				for (String cf : family) {
					scan.addFamily(cf.getBytes());
				}
			}

			ResultScanner rs = table.getScanner(scan);
			
			Result r = rs.next();
			
			Map<String, Map<String, String>> mapKeyValue = new HashMap<>();
			
			int ncount=0;

			while (r!=null) {
				
				ncount++;
				
				Map<String, String> mapColValue = new HashMap<>();
				String key = null;
				
				for (Cell cell : r.listCells()) {
					key = new String(CellUtil.cloneRow(cell));
					mapColValue.put("id", key);
					mapColValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
				}
				
				System.out.println(ncount+" -- key: "+key);
				mapKeyValue.put(key, mapColValue);
				
				r = rs.next();
			}
			
			table.close();
			
			return mapKeyValue;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, Map<String,String>> scanConnid(String connid) throws Exception {
		try {
			
			Table table = conn.getTable(TableName.valueOf(tbName));
			
			Scan scan = new Scan();
			
			scan.setCaching(10000);
			scan.setMaxVersions(1);
			scan.setMaxResultSize(100);
			scan.setFilter(new PageFilter(100));

			//Add filters
			FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ALL);
			
			SingleColumnValueFilter colValFilter1 = new SingleColumnValueFilter(Bytes.toBytes("cf0"), Bytes.toBytes("04")
					,CompareFilter.CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes(connid)));

//			SingleColumnValueFilter colValFilter2 = new SingleColumnValueFilter(Bytes.toBytes("f"), Bytes.toBytes("fechagrab")
//	                ,CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("20170106_0000")));
			
			
			list.addFilter(colValFilter1);
//			list.addFilter(colValFilter2);
			
			scan.setFilter(list);
			
			//Add family filters
//			for (String cf : family) {
//				scan.addFamily(cf.getBytes());
//			}

			ResultScanner rs = table.getScanner(scan);
			
			Result r = rs.next();
			
			Map<String, Map<String, String>> mapKeyValue = new HashMap<>();

			while (r!=null) {
			
				Map<String, String> mapColValue = new HashMap<>();
				String key = null;
				
				for (Cell cell : r.listCells()) {
					key = new String(CellUtil.cloneRow(cell));
					mapColValue.put("id", key);
					mapColValue.put(new String(CellUtil.cloneQualifier(cell)), new String(CellUtil.cloneValue(cell)));
				}
				
				mapKeyValue.put(key, mapColValue);
				
				r = rs.next();
			}
			
			table.close();
			
			return mapKeyValue;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
