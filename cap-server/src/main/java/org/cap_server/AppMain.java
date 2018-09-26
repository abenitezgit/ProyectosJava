package org.cap_server;

import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.model.AgeGroup;
import org.model.AgeGroupStat;
import org.services.FlowControl;
import org.utilities.GlobalParams;

import com.api.MysqlAPI;
import com.rutinas.Rutinas;

public class AppMain {
	static GlobalParams gParams = new GlobalParams();
	static Rutinas mylib = new Rutinas();
	
	static Map<String, AgeGroupStat> mapAgeGroupStat = new TreeMap<>();


	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		int initDay;
		
		MysqlAPI dbConn;
		
		dbConn = new MysqlAPI(	"wsecc", 
				"srvConf", 
				"3306",
				"process",
				"Process-01",
				10000);
		
		dbConn.open();
		
		if (dbConn.isConnected()) {
			
			String vSql0 = "call sp_get_ageGroupDay(0)";
			
			if (dbConn.executeQuery(vSql0)) {
				
				String vSql = "call srvConf.sp_get_ageGroupStat()";
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					
					boolean firstRow = true;
					
					Calendar cal = Calendar.getInstance();
					int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
					int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
					int minuteOfHour = cal.get(Calendar.MINUTE);
					
					while (rs.next()) {
						
						int day = rs.getInt("nDay");
						int hour = rs.getInt("nHour");
						int minute = rs.getInt("nMinute");
						int numGroups = rs.getInt("nCount");
						String uStatus = Objects.isNull(rs.getString("uStatus")) ? "NULL":rs.getString("uStatus") ;
						String typeExec = Objects.isNull(rs.getString("typeExec")) ? "NULL":rs.getString("typeExec") ;
						
						if (firstRow) {
							initDay = day;
							firstRow = false;
						}
	
						String key = String.format("%02d", day)+":"+String.format("%02d", hour);
						
						AgeGroupStat ags = new AgeGroupStat();
						
						if (mapAgeGroupStat.containsKey(key)) {
							ags = mapAgeGroupStat.get(key);
						}
						
						ags.setnGroups(ags.getnGroups()+numGroups);
						
						if (!uStatus.equals("SUCESS")) {
							if (day<dayOfMonth) {
								ags.setAlerts(ags.getAlerts()+numGroups);
							} else if (day==dayOfMonth) {
								if (hour<hourOfDay) {
									ags.setAlerts(ags.getAlerts()+numGroups);
								} else if (hour==hourOfDay) {
									if (minute<minuteOfHour) {
										ags.setAlerts(ags.getAlerts()+numGroups);
									}
								}
							}
						} else if (typeExec.equals("MANUAL")) {
							ags.setAlerts(ags.getAlerts()+numGroups);
						}
						
						ags.setnDay(day);
						ags.setnHour(hour);
						
						mapAgeGroupStat.put(key, ags);
						
						System.out.println(mylib.serializeObjectToJSon(mapAgeGroupStat.get(key), false));
						
					}
					System.out.println(dayOfMonth);
					
				}
			} else {
				System.out.println("Error");
			}
			dbConn.close();

			int itDay = 0;
			Map<String, AgeGroupStat> mapAgeGrStatFinal = new TreeMap<>();
			
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGroupStat.entrySet()) {
				int Day = Integer.valueOf(entry.getKey().split(":")[0]);
				
				if (itDay!=Day) {
					for (int i=0; i<=23; i++) {
						String getKey = entry.getKey().split(":")[0]+":"+String.format("%02d", i);
						System.out.println(getKey);
						if (!mapAgeGroupStat.containsKey(getKey)) {
							AgeGroupStat ags = new AgeGroupStat();
							ags.setAlerts(0);
							ags.setnDay(Day);
							ags.setnGroups(0);
							ags.setnHour(i);
							mapAgeGrStatFinal.put(getKey, ags);
						} else {
							mapAgeGrStatFinal.put(getKey, mapAgeGroupStat.get(getKey));
						}
					}
					itDay = Day;
				}
			}
			
			
			for(Map.Entry<String, AgeGroupStat> entry : mapAgeGrStatFinal.entrySet()) {
				System.out.println(mylib.serializeObjectToJSon(entry.getValue(), false));
			}
			
			
			
		}
		

	}
	
	static public void addAgeGroupCount(AgeGroup ag) {
		String key = String.format("%02d",ag.getnDay())+":"+String.format("%02d",ag.getnHour());
		List<AgeGroup> lstAgeGroups = new ArrayList<>(); 
				
		if (gParams.getMapAgeGroupCount().containsKey(key)) {
			int tmpCount = gParams.getMapAgeGroupCount().get(key);
			int finalCount = tmpCount + 1;
			gParams.getMapAgeGroupCount().put(key, finalCount);
			
			lstAgeGroups = gParams.getMapAgeGroup().get(key);		
			lstAgeGroups.add(ag);
			gParams.getMapAgeGroup().put(key, lstAgeGroups);
		} else {
			gParams.getMapAgeGroupCount().put(key, 1);
			gParams.getMapAgeGroup().put(key, lstAgeGroups);
		}
	}
	
	

}
