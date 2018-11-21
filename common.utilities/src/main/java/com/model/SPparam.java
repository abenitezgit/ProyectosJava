package com.model;


/**
 * @author admin
 *
 */
public class SPparam {
	
	private String inOutType; // IN OUT
	private Object value;
	
	
	public SPparam() {
		super();
	}
	
	public SPparam(Object value) {
		super();
		this.inOutType = "IN";
		this.value = value;
	}

	public SPparam(String inOutType, Object value) {
		super();
		this.inOutType = inOutType;
		this.value = value;
	}

	public String getInOutType() {
		return inOutType;
	}
	public void setInOutType(String inOutType) {
		this.inOutType = inOutType;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
