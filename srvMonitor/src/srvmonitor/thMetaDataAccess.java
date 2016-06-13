
import utilities.globalAreaData;
import utilities.srvRutinas;

public class thMetaDataAccess extends Thread {
    static srvRutinas gSub;
    static globalAreaData gDatos;
    
    //Carga constructor para inicializar los datos
    public thMetaDataAccess(globalAreaData m) {
        gDatos = m;
        gSub = new srvRutinas(gDatos);
    }
    
    @Override
    public void run() {
        try {

        } catch (Exception e) {


        }
    }
}
