package cap.model;

import java.util.ArrayList;
import java.util.List;

public class SpeechRequest {
	
	private String id;
	private String status;
	private String uStatus;
	private String fecIns;
	private String fecUpdate;
	private String errMesg;
	private int numWords;
	private String watsonTrans;
	private String googleTrans;

	private List<AudioAnalisis> lstAnalisis = new ArrayList<>();

	
	public String getWatsonTrans() {
		return watsonTrans;
	}
	public void setWatsonTrans(String watsonTrans) {
		this.watsonTrans = watsonTrans;
	}
	public String getGoogleTrans() {
		return googleTrans;
	}
	public void setGoogleTrans(String googleTrans) {
		this.googleTrans = googleTrans;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNumWords() {
		return numWords;
	}
	public void setNumWords(int numWords) {
		this.numWords = numWords;
	}
	public List<AudioAnalisis> getLstAnalisis() {
		return lstAnalisis;
	}
	public void setLstAnalisis(List<AudioAnalisis> lstAnalisis) {
		this.lstAnalisis = lstAnalisis;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getuStatus() {
		return uStatus;
	}
	public void setuStatus(String uStatus) {
		this.uStatus = uStatus;
	}
	public String getFecIns() {
		return fecIns;
	}
	public void setFecIns(String fecIns) {
		this.fecIns = fecIns;
	}
	public String getFecUpdate() {
		return fecUpdate;
	}
	public void setFecUpdate(String fecUpdate) {
		this.fecUpdate = fecUpdate;
	}
	public String getErrMesg() {
		return errMesg;
	}
	public void setErrMesg(String errMesg) {
		this.errMesg = errMesg;
	}
}
