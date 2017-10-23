package cap.capMonitor;

import utiles.common.rutinas.Rutinas;

/**
 * 
 *
 */
public class App {
	static Rutinas mylib = new Rutinas();
	
    public static void main( String[] args ) {
    		try {
    			/**
    			 * Validando parámetros de entrada
    			 */
    			String pathConfig = System.getProperty("pathConfig");
    			if (!mylib.isNullOrEmpty(pathConfig)) {
    				
    			} else {
    				mylib.console(1,"No se ha ingresado la ruta de archivos de configuración");
    				mylib.console(1,"Abortando servicios");
    			}
    			
    		} catch (Exception e) {
    			mylib.console(1,"Error general. Abortando servicio ("+e.getMessage()+")");
    		}
    }
}
