package org.model;

public class TypeProc {
	String typeProc;
	int priority;
	int maxThread;
	int usedThread;
	
	public String getTypeProc() {
		return typeProc;
	}
	public void setTypeProc(String typeProc) {
		this.typeProc = typeProc;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getMaxThread() {
		return maxThread;
	}
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	public int getUsedThread() {
		return usedThread;
	}
	public void setUsedThread(int usedThread) {
		this.usedThread = usedThread;
	}
}
