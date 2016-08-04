/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataClass;

import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class PoolProcess {
    String typeProc;
    String procID;
    String status;
    String updateTime;
    String StartTime;
    String EndTime;
    String ExitCode;
    
    JSONObject params;

    public String getExitCode() {
        return ExitCode;
    }

    public void setExitCode(String ExitCode) {
        this.ExitCode = ExitCode;
    }
    
    public String getTypeProc() {
        return typeProc;
    }

    public void setTypeProc(String typeProc) {
        this.typeProc = typeProc;
    }

    public String getProcID() {
        return procID;
    }

    public void setProcID(String procID) {
        this.procID = procID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
    
}
