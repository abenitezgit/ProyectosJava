/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvclientmonitor;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author andresbenitez
 */
public class globalAreaData {
    private JFrame frmMain = new JFrame();
    
    //Primer JPanel de Monitoreo
    private JPanel jpnMon = new JPanel();
    private JLabel lblMonDesc = new JLabel();
    private JLabel lblMonTime = new JLabel();
    private JLabel lblMonTimeNow = new JLabel();
    private final String DIR_ICON_BASE = "/Users/andresbenitez/Documents/Apps/NetBeansProjects3/srvClientMonitor/src/";
    private final String ICO_SPHERE_OFF_STATUS = "1469138256_stock_draw-sphere.png";
    private final String ICO_SPHERE_OFF_STATUS2 = "16x15_dialog-error.png";
    private final String ICO_TIME_STATUS = "16x16_emblem-urgent.png";
    private final String ICO_START_01 = "16x16_start.png";

    public String getICO_START_01() {
        return ICO_START_01;
    }

    public String getDIR_ICON_BASE() {
        return DIR_ICON_BASE;
    }

    public String getICO_SPHERE_OFF_STATUS2() {
        return ICO_SPHERE_OFF_STATUS2;
    }

    public String getICO_SPHERE_OFF_STATUS() {
        return ICO_SPHERE_OFF_STATUS;
    }

    public String getICO_TIME_STATUS() {
        return ICO_TIME_STATUS;
    }
    
    public JFrame getFrmMain() {
        return frmMain;
    }

    public void setFrmMain(JFrame frmMain) {
        this.frmMain = frmMain;
    }

    public JPanel getJpnMon() {
        return jpnMon;
    }

    public void setJpnMon(JPanel jpnMon) {
        this.jpnMon = jpnMon;
    }

    public JLabel getLblMonDesc() {
        return lblMonDesc;
    }

    public void setLblMonDesc(JLabel lblMonDesc) {
        this.lblMonDesc = lblMonDesc;
    }

    public JLabel getLblMonTime() {
        return lblMonTime;
    }

    public void setLblMonTime(JLabel lblMonTime) {
        this.lblMonTime = lblMonTime;
    }

    public JLabel getLblMonTimeNow() {
        return lblMonTimeNow;
    }

    public void setLblMonTimeNow(JLabel lblMonTimeNow) {
        this.lblMonTimeNow = lblMonTimeNow;
    }
    
    
    //Metodos de Gestion
    
    public void setPanelMonVisible(boolean visible) {
        this.jpnMon.setVisible(visible);
    }

    public void setPanelMonActive(boolean active) {
        if (active) {
            this.lblMonDesc.setIcon(new ImageIcon(DIR_ICON_BASE+ICO_START_01));
            this.lblMonTime.setIcon(new ImageIcon(DIR_ICON_BASE+ICO_TIME_STATUS));
        } else {
            this.lblMonDesc.setIcon(new ImageIcon(DIR_ICON_BASE+ICO_SPHERE_OFF_STATUS));
            this.lblMonTime.setIcon(new ImageIcon(DIR_ICON_BASE+ICO_TIME_STATUS));
            this.lblMonTimeNow.setText("0 hrs");
        }
    }
    
}
