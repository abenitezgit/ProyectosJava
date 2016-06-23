/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateserver;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.json.JSONObject;

/**
 *
 * @author andresbenitez
 */
public class globalAreaData {
    
    private List<JSONObject> serviceStatus = new ArrayList<>();
    private JTextArea txtAreaLog = new JTextArea();
    private JTable tblMonServices =  new JTable();
    private JTable tblMonProcActive = new JTable();
    private JTable tblMonProcAssigned = new JTable();
    private JList lstMonService = new JList();
    private JList lstMonProcAssigned = new JList();
    private JList lstMonProcActive = new JList();

    public JList getLstMonProcAssigned() {
        return lstMonProcAssigned;
    }

    public void setLstMonProcAssigned(JList lstMonProcAssigned) {
        this.lstMonProcAssigned = lstMonProcAssigned;
    }

    public JList getLstMonProcActive() {
        return lstMonProcActive;
    }

    public void setLstMonProcActive(JList lstMonProcActive) {
        this.lstMonProcActive = lstMonProcActive;
    }
    
    public JList getLstMonService() {
        return lstMonService;
    }

    public void setLstMonService(JList lstMonService) {
        this.lstMonService = lstMonService;
    }
    
    public JTextArea getTxtAreaLog() {
        return txtAreaLog;
    }

    public void setTxtAreaLog(JTextArea txtAreaLog) {
        this.txtAreaLog = txtAreaLog;
    }

    public JTable getTblMonServices() {
        return tblMonServices;
    }

    public void setTblMonServices(JTable tblMonServices) {
        this.tblMonServices = tblMonServices;
    }

    public JTable getTblMonProcActive() {
        return tblMonProcActive;
    }

    public void setTblMonProcActive(JTable tblMonProcActive) {
        this.tblMonProcActive = tblMonProcActive;
    }

    public JTable getTblMonProcAssigned() {
        return tblMonProcAssigned;
    }

    public void setTblMonProcAssigned(JTable tblMonProcAssigned) {
        this.tblMonProcAssigned = tblMonProcAssigned;
    }

    public List<JSONObject> getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(List<JSONObject> serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
