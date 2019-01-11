package org.services;

import java.util.Calendar;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.dataAccess.DataAccess;
import org.utilities.GlobalParams;

public class TimerExecAgeWeek extends TimerTask{
	final String thName = "TimerExecWeek";
	Logger logger = Logger.getLogger(thName);
	GlobalParams gParams;
	
	public TimerExecAgeWeek(GlobalParams m) {
		// TODO Auto-generated constructor stub
		this.gParams = m;
	}

	@Override
    public void run() {
		//Set Thread Name
		Thread.currentThread().setName(thName);
		
		DataAccess da = new DataAccess(gParams);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
		
		logger.info("Iniciando Actualización de Agenda Semanal...");
		logger.info("Hora Ejecución: "+Calendar.getInstance().getTime());
		
		try {
			da.open();
			
			if (da.isConnected()) {
				if (da.getAgeGroupWeek()) {
					logger.info("Agenda Semanal Actualizada Exitosamente");
					logger.info("Próxima actualización: "+c.getTime());
				} else {
					logger.error("Agenda Semanal No ha podido ser actualizada");
				}
				da.close();
			}

			logger.info("Finalizando Actualización de Agenda Semanal");
		} catch (Exception e) {
			logger.error("No es posible actualiazar Agenda Semanal: "+e.getMessage());
		} finally {
			if (da.isConnected()) {
				da.close();
			}
		}
		
    }
}
