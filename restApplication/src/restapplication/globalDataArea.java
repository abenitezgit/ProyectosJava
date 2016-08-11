/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 *
 * @author andresbenitez
 */
public class globalDataArea {
    private JList listProc = new JList();
    private JList listError = new JList();
    private int numInserts;
    private int numErrors;
    private JTextField txtInserted;
    private JTextField txtError;
    private DefaultListModel dlmProc = new DefaultListModel();
    private DefaultListModel dlmError = new DefaultListModel();
    private String URLBase;
    
    public synchronized void addNumInserts() {
        numInserts++;
    }

    public synchronized void addNumErrors() {
        numErrors++;
    }

    public synchronized void addDLMProc(String item) {
        dlmProc.addElement(item);
    }
    
    public synchronized void addDLMError(String item) {
        dlmError.addElement(item);
    }
    
    public globalDataArea() {
        listProc.setModel(dlmProc);
        listError.setModel(dlmError);
    }

    public String getURLBase() {
        return URLBase;
    }

    public void setURLBase(String URLBase) {
        this.URLBase = URLBase;
    }
    
    public DefaultListModel getDlmProc() {
        return dlmProc;
    }

    public void setDlmProc(DefaultListModel dlmProc) {
        this.dlmProc = dlmProc;
    }

    public DefaultListModel getDlmError() {
        return dlmError;
    }

    public void setDlmError(DefaultListModel dlmError) {
        this.dlmError = dlmError;
    }

    public int getNumErrors() {
        return numErrors;
    }

    public synchronized void setNumErrors(int numErrors) {
        this.numErrors = numErrors;
    }

    public JTextField getTxtInserted() {
        return txtInserted;
    }
    
    public synchronized void linkTxtInserted(JTextField value) {
        this.txtInserted = value;
    }

    public synchronized void linkTxtError(JTextField value) {
        this.txtError = value;
    }
    
    public synchronized void setTxtInserted(String value) {
        this.txtInserted.setText(value);
    }

    public JTextField getTxtError() {
        return txtError;
    }

    public synchronized void setTxtError(String value) {
        this.txtError.setText(value);
    }
    
    public int getNumInserts() {
        return numInserts;
    }

    public synchronized void setNumInserts(int numInserts) {
        this.numInserts = numInserts;
    }
    
    public JList getListProc() {
        return listProc;
    }

    public void setListProc(JList listProc) {
        this.listProc = listProc;
    }

    public JList getListError() {
        return listError;
    }

    public void setListError(JList listError) {
        this.listError = listError;
    }
    
    
}
