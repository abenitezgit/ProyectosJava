package cap.model;

public class Module {
	String name;
	String type;	//THREAD - TIMERTASK
	int txp;
	String lastFecIni;
	String lastFecFin;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTxp() {
		return txp;
	}
	public void setTxp(int txp) {
		this.txp = txp;
	}
	public String getLastFecIni() {
		return lastFecIni;
	}
	public void setLastFecIni(String lastFecIni) {
		this.lastFecIni = lastFecIni;
	}
	public String getLastFecFin() {
		return lastFecFin;
	}
	public void setLastFecFin(String lastFecFin) {
		this.lastFecFin = lastFecFin;
	}
	
	
}
