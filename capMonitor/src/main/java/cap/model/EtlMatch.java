/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.model;

/**
 *
 * @author andresbenitez
 */
public class EtlMatch {
    int etlOrder;
    int etlEnable;
    String etlSourceField;
    int etlSourceLength;
    String etlSourceType;
    String etlDestField;
    int etlDestLength;
    String etlDestType;
    
    //Getter and setter
    
	public int getEtlOrder() {
		return etlOrder;
	}
	public void setEtlOrder(int etlOrder) {
		this.etlOrder = etlOrder;
	}
	public int getEtlEnable() {
		return etlEnable;
	}
	public void setEtlEnable(int etlEnable) {
		this.etlEnable = etlEnable;
	}
	public String getEtlSourceField() {
		return etlSourceField;
	}
	public void setEtlSourceField(String etlSourceField) {
		this.etlSourceField = etlSourceField;
	}
	public int getEtlSourceLength() {
		return etlSourceLength;
	}
	public void setEtlSourceLength(int etlSourceLength) {
		this.etlSourceLength = etlSourceLength;
	}
	public String getEtlSourceType() {
		return etlSourceType;
	}
	public void setEtlSourceType(String etlSourceType) {
		this.etlSourceType = etlSourceType;
	}
	public String getEtlDestField() {
		return etlDestField;
	}
	public void setEtlDestField(String etlDestField) {
		this.etlDestField = etlDestField;
	}
	public int getEtlDestLength() {
		return etlDestLength;
	}
	public void setEtlDestLength(int etlDestLength) {
		this.etlDestLength = etlDestLength;
	}
	public String getEtlDestType() {
		return etlDestType;
	}
	public void setEtlDestType(String etlDestType) {
		this.etlDestType = etlDestType;
	}
    
    
}
