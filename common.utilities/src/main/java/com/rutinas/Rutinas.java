package com.rutinas;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONObject;

public class Rutinas {
	
	public void setLevelLogger(Logger logger, String level) {
		switch (level) {
		case "INFO":
			logger.setLevel(Level.INFO);
			break;
		case "DEBUG":
			logger.setLevel(Level.DEBUG);
			break;
		case "WARN":
			logger.setLevel(Level.WARN);
			break;
		case "ERROR":
			logger.setLevel(Level.ERROR);
			break;
		default:
			logger.setLevel(Level.INFO);
			break;
		}
	}
	
	public int getNumRows(Object[][] tbGrid) {
		int numRows=0;
		try {
			if (!Objects.isNull(tbGrid)) {
				for (int i=0; i<1000000; i++) {
					if (!Objects.isNull(tbGrid[i][0])) {
						numRows++;
					} else {
						return numRows;
					}
				}
				
			} else {
				return -1;
			}
			
			return numRows;
		} catch (Exception e) {
			return numRows;
		}
	}
	
	public int getNumColsOfRow(Object[][] matriz, int row) {
		int numCols=0;
		try {
			if (!Objects.isNull(matriz)) {
				for(int i=0; i<1000; i++) {
					if (!Objects.isNull(matriz[row][i])) {
						numCols++;
					} else {
						return numCols;
					}
				}
			} else {
				return -1;
			}
			return numCols;
		} catch (Exception e) {
			return numCols;
		}
	}
	
	public void setIfNullEmpty(String source, String update) throws Exception {
		if (isNullOrEmpty(source)) {
			source = new String(update);
		}
		
	}
	
	public boolean isWhitespace(String s) {
	    int length = s.length();
	    if (length > 0) {
	        for (int start = 0, middle = length / 2, end = length - 1; start <= middle; start++, end--) {
	            if (s.charAt(start) > ' ' || s.charAt(end) > ' ') {
	                return false;
	            }
	        }
	        return true;
	    }
	    return false;
	}
	
	public String nvlString(String obj) {
		if (obj==null) {
			return "";
		} else {
			return obj;
		}
	}
	
	public boolean isNull(String s) {
	    return s == null ;

	}
	
	public boolean isNull(Object o) {
	    return o == null;
	}
	
	public boolean isNullOrWhitespace(String s) {
	    return s == null || isWhitespace(s);

	}

	public boolean isNullOrEmpty(Object s) {
	    return s == null;
	}
	
	public boolean isNullOrEmpty(String s) {
	    return s == null || s.length() == 0;
	}
	
	public String getLoggerLevel(Logger logger) throws Exception {
		if (logger.isDebugEnabled()) {
			return "DEBUG";
		} else if (logger.isTraceEnabled()) {
				return "TRACE";
			} else if (logger.isInfoEnabled()) {
						return "INFO";
					} else {
						return "NOT FOUND";
					}
	}
	
	public java.sql.Date getSqlDate(String fecha, String format) throws Exception {
		try {
			
			
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat(format);
			
			java.util.Date utilDate = new java.util.Date();
			utilDate = formatter.parse(fecha);
			
			java.sql.Timestamp timeStamp = new java.sql.Timestamp(utilDate.getTime()); // your sql date
			
			java.sql.Date      sqlDate      = new java.sql.Date(timeStamp.getTime());
		    //java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		    
		    System.out.println("utilDate:" + utilDate);
		    System.out.println("sqlDate:" + sqlDate);
			
			return sqlDate;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public java.sql.Timestamp getSqlTimestamp(Date fecha)  {
		try {
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(fecha.getTime()); // your sql date

			return sqlDate;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Date getDate() throws Exception {
		try {
	        Date today;
	        SimpleDateFormat formatter;
	        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        today = new Date();
	        return formatter.parse(formatter.format(today));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Calendar getCalendarDiff(Calendar calendar, String tiempo, int valorDiff) throws Exception {
		try {
			switch(tiempo) {
				case "YEAR":
					calendar.add(Calendar.YEAR, valorDiff);
					break;
				case "MONTH":
					calendar.add(Calendar.MONTH, valorDiff);
					break;
				case "DAY_OF_MONTH":
					calendar.add(Calendar.DAY_OF_MONTH, valorDiff);
					break;
				case "HOUR_OF_DAY":
					calendar.add(Calendar.HOUR_OF_DAY, valorDiff);
					break;
				case "MINUTE":
					calendar.add(Calendar.MINUTE, valorDiff);
					break;
			}
			
			return calendar;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Calendar getCalendarOfTimeZone(String nameZone) throws Exception {
		/**
		 * Zonas
		 * America/Lima
		 * America/Santiago
		 */
		try {
			TimeZone timezone = TimeZone.getTimeZone(nameZone);

			Calendar calendar = new GregorianCalendar();

			calendar.setTimeZone(timezone);

			return calendar;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Date getDateAddDays(Date fecha, int days) throws Exception {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.DAY_OF_MONTH, days);
			return calendar.getTime();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Date getDate(String fecha) throws Exception {
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return formatter.parse(fecha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public int getMinuteDiff(Date fecFin, Date fecIni) throws Exception {
		try {
			int minutes=(int) ((fecFin.getTime()-fecIni.getTime())/86400000/24/60);
			
			return minutes;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public int getDaysDiff(Date fecFin, Date fecIni) throws Exception {
		try {
			int dias=(int) ((fecFin.getTime()-fecIni.getTime())/86400000);
			return dias;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Date getDate(String fecha, String xformat) throws Exception {
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat(xformat);
			return formatter.parse(fecha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String getDateString(String fecha, String formatIni, String formatFin) {
		try {
	    	SimpleDateFormat formatterIni;
	    	SimpleDateFormat formatterFin;
	    	formatterIni = new SimpleDateFormat(formatIni);
	    	formatterFin = new SimpleDateFormat(formatFin);
	    	Date tmp = formatterIni.parse(fecha);
	    	
	    	return formatterFin.format(tmp);
		} catch (Exception e) {
			return "";
		}
	}
	
    public String getDateNow() throws Exception {
        Date today;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        today = new Date();
        return formatter.format(today);  
    }
    
    public String getDateNow(String xformat) throws Exception {
        Date today;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(xformat);
        today = new Date();
        return formatter.format(today);  
    }
    
    public String getDateString(Date date, String format) throws Exception {
    	SimpleDateFormat formatter;
    	formatter = new SimpleDateFormat(format);
    	return formatter.format(date);
    }
    
    public boolean fileExist(String pathFile) throws Exception {
    	File f = new File(pathFile);
    	if (f.exists()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void console (int type, String msg) {
    	String stype = "INFO";
    	switch (type) {
	    	case 0:
	    		stype = "INFO";
	    		break;
	    	case 1:
	    		stype = "ERROR";
	    		break;
	    	case 2:
	    		stype = "WARNING";
	    		break;
    	}
    	try {
    		System.out.println(getDateNow()+" "+stype+" Console - " + msg);
    	} catch (Exception e) {
    		System.out.println(stype+" Console - " + msg);
    	}
    }

    public void console (int type, String proyecto, String msg) {
    	String stype = "INFO";
    	switch (type) {
	    	case 0:
	    		stype = "INFO";
	    		break;
	    	case 1:
	    		stype = "ERROR";
	    		break;
	    	case 2:
	    		stype = "WARNING";
	    		break;
    	}
    	try {
    		System.out.println(getDateNow()+" "+proyecto+" "+stype+" Console - " + msg);
    	} catch (Exception e) {
    		System.out.println(proyecto+" "+stype+" Console - " + msg);
    	}
    }

    public void console (String proyecto, String msg) {
    	String stype = "INFO";
    	try {
	    	System.out.println(getDateNow()+" "+proyecto+" "+stype+" Console - " + msg);
    	} catch (Exception e) {
    		System.out.println(proyecto+" "+stype+" Console - " + msg);
    	}
    }
    
    public void console (String msg) {
    	String stype = "INFO";
    	try {
	    	System.out.println(getDateNow()+" "+stype+" Console - " + msg);
    	} catch (Exception e) {
    		System.out.println(stype+" Console - " + msg);
    	}
    }
    
    public static double getProcessCpuLoad() throws Exception {
        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)      return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int)(value * 1000) / 10.0);
    }

    public String sendError(int errCode, String errMesg) {
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
    
        jData.put("errMesg", errMesg);
        jData.put("errCode", errCode);
        
        jHeader.put("data",jData);
        jHeader.put("result", "error");
            
        return jHeader.toString();
    }
    
    public String sendError(int errCode) {
        String errMesg;
        
        switch (errCode) {
            case 90: 
                    errMesg = "error de entrada";
                    break;
            case 80: 
                    errMesg = "servicio offlne";
                    break;
            case 60:
                    errMesg = "TX no autorizada";
                    break;
            case 61:
                errMesg = "Request no reconocido";
                break;
            default: 
                    errMesg = "error desconocido";
                    break;
        }
        
        JSONObject jData = new JSONObject();
        JSONObject jHeader = new JSONObject();
    
        jData.put("errMesg", errMesg);
        jData.put("errCode", errCode);
        
        jHeader.put("data",jData);
        jHeader.put("result", "error");
            
        return jHeader.toString();
    }

    
    public String serializeObjectToJSon (Object object, boolean formated) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, formated);

            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    public Object serializeJSonStringToObject (String parseJson, Class<?> className) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            return mapper.readValue(parseJson, className);
        } catch (Exception e) {
        		throw new Exception(e.getMessage());
        }
    }      
    
}
