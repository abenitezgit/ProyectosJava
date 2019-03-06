package cap.model;

public class AudioAnalisis {
	private String id;
	private String catID;
	private String catDesc;
	private int freq;
	private int numWords;

	public int getNumWords() {
		return numWords;
	}
	public void setNumWords(int numWords) {
		this.numWords = numWords;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCatID() {
		return catID;
	}
	public void setCatID(String catID) {
		this.catID = catID;
	}
	public String getCatDesc() {
		return catDesc;
	}
	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
}
