/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClass;

/**
 *
 * @author andresbenitez
 */
public class AssignedTypeProc {
    String typeProc;
    int maxThread;
    int priority;
    
    public AssignedTypeProc() {
    }
    
    public AssignedTypeProc (String typeProc, int maxThread, int priority) {
        this.typeProc = typeProc;
        this.maxThread = maxThread;
        this.priority = priority;
    }

    public String getTypeProc() {
        return typeProc;
    }

    public void setTypeProc(String typeProc) {
        this.typeProc = typeProc;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    
}
