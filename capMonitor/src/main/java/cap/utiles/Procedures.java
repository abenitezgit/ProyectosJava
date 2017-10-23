package cap.utiles;

import utiles.common.rutinas.Rutinas;

public class Procedures {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public Procedures(GlobalArea m) {
		gDatos = m;
	}
	
	public void getParamFileConfig() throws Exception {
		try {
			
		} catch (Exception e) {
			throw new Exception("Error getParamFileConfig: "+e.getMessage());
		}
	}
}
