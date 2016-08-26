/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srvclientmonitor;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author andresbenitez
 */
public class frmMain extends javax.swing.JFrame {
    static globalAreaData gDatos = new globalAreaData();
    Thread thgetStatus = new thGetStatusServices(gDatos);

    /**
     * Creates new form frmMain
     */
    public frmMain() {
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlMonMain = new javax.swing.JPanel();
        pnlStatMon = new javax.swing.JPanel();
        lblMon = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblTimeValue0 = new javax.swing.JLabel();
        pnlStatMon1 = new javax.swing.JPanel();
        lblMon1 = new javax.swing.JLabel();
        lblTime1 = new javax.swing.JLabel();
        lblTimeValue1 = new javax.swing.JLabel();
        pnlStatMon2 = new javax.swing.JPanel();
        lblMon2 = new javax.swing.JLabel();
        lblActive1 = new javax.swing.JLabel();
        lblTimeNow2 = new javax.swing.JLabel();
        lblTime2 = new javax.swing.JLabel();
        lblTimeValue2 = new javax.swing.JLabel();
        pnlStatMon3 = new javax.swing.JPanel();
        lblMon3 = new javax.swing.JLabel();
        lblActive2 = new javax.swing.JLabel();
        lblTimeNow3 = new javax.swing.JLabel();
        lblTime3 = new javax.swing.JLabel();
        lblTimeValue3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnStartStop = new javax.swing.JButton();
        btnStop = new javax.swing.JToggleButton();
        jToolBar1 = new javax.swing.JToolBar();
        lblTimeNow = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaTypeProc = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaPoolProcess = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(java.awt.Color.lightGray);

        pnlMain.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        pnlMonMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        pnlStatMon.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));

        lblMon.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblMon.setForeground(new java.awt.Color(255, 255, 255));
        lblMon.setText("Monitor");

        lblTime.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setText("Time:");

        lblTimeValue0.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeValue0.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeValue0.setText("      ");
        lblTimeValue0.setToolTipText("");

        javax.swing.GroupLayout pnlStatMonLayout = new javax.swing.GroupLayout(pnlStatMon);
        pnlStatMon.setLayout(pnlStatMonLayout);
        pnlStatMonLayout.setHorizontalGroup(
            pnlStatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStatMonLayout.createSequentialGroup()
                        .addComponent(lblMon)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlStatMonLayout.createSequentialGroup()
                        .addComponent(lblTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTimeValue0, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlStatMonLayout.setVerticalGroup(
            pnlStatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMon)
                .addGap(25, 25, 25)
                .addGroup(pnlStatMonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTimeValue0)
                    .addComponent(lblTime))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlStatMon1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));

        lblMon1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblMon1.setForeground(new java.awt.Color(255, 255, 255));
        lblMon1.setText("Metadata");

        lblTime1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTime1.setForeground(new java.awt.Color(255, 255, 255));
        lblTime1.setText("Time:");

        lblTimeValue1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeValue1.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeValue1.setText("       ");
        lblTimeValue1.setToolTipText("");

        javax.swing.GroupLayout pnlStatMon1Layout = new javax.swing.GroupLayout(pnlStatMon1);
        pnlStatMon1.setLayout(pnlStatMon1Layout);
        pnlStatMon1Layout.setHorizontalGroup(
            pnlStatMon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMon1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatMon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStatMon1Layout.createSequentialGroup()
                        .addComponent(lblMon1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlStatMon1Layout.createSequentialGroup()
                        .addComponent(lblTime1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTimeValue1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnlStatMon1Layout.setVerticalGroup(
            pnlStatMon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMon1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMon1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlStatMon1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime1)
                    .addComponent(lblTimeValue1))
                .addContainerGap())
        );

        pnlStatMon2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));

        lblMon2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblMon2.setForeground(new java.awt.Color(255, 255, 255));
        lblMon2.setText("Services");

        lblActive1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblActive1.setForeground(new java.awt.Color(255, 255, 255));
        lblActive1.setText("Actives:");

        lblTimeNow2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeNow2.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeNow2.setText("0");
        lblTimeNow2.setToolTipText("");

        lblTime2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTime2.setForeground(new java.awt.Color(255, 255, 255));
        lblTime2.setText("Time:");

        lblTimeValue2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeValue2.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeValue2.setText("            ");
        lblTimeValue2.setToolTipText("");

        javax.swing.GroupLayout pnlStatMon2Layout = new javax.swing.GroupLayout(pnlStatMon2);
        pnlStatMon2.setLayout(pnlStatMon2Layout);
        pnlStatMon2Layout.setHorizontalGroup(
            pnlStatMon2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMon2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatMon2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMon2)
                    .addGroup(pnlStatMon2Layout.createSequentialGroup()
                        .addComponent(lblActive1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTimeNow2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStatMon2Layout.createSequentialGroup()
                        .addComponent(lblTime2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTimeValue2)))
                .addContainerGap())
        );
        pnlStatMon2Layout.setVerticalGroup(
            pnlStatMon2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMon2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMon2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatMon2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActive1)
                    .addComponent(lblTimeNow2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatMon2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime2)
                    .addComponent(lblTimeValue2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlStatMon3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));

        lblMon3.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblMon3.setForeground(new java.awt.Color(255, 255, 255));
        lblMon3.setText("Process");

        lblActive2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblActive2.setForeground(new java.awt.Color(255, 255, 255));
        lblActive2.setText("Actives:");

        lblTimeNow3.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeNow3.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeNow3.setText("0");
        lblTimeNow3.setToolTipText("");

        lblTime3.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTime3.setForeground(new java.awt.Color(255, 255, 255));
        lblTime3.setText("Time:");

        lblTimeValue3.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeValue3.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeValue3.setText("             ");
        lblTimeValue3.setToolTipText("");

        javax.swing.GroupLayout pnlStatMon3Layout = new javax.swing.GroupLayout(pnlStatMon3);
        pnlStatMon3.setLayout(pnlStatMon3Layout);
        pnlStatMon3Layout.setHorizontalGroup(
            pnlStatMon3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMon3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatMon3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMon3)
                    .addGroup(pnlStatMon3Layout.createSequentialGroup()
                        .addComponent(lblActive2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTimeNow3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStatMon3Layout.createSequentialGroup()
                        .addComponent(lblTime3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTimeValue3)))
                .addContainerGap())
        );
        pnlStatMon3Layout.setVerticalGroup(
            pnlStatMon3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatMon3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMon3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatMon3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActive2)
                    .addComponent(lblTimeNow3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlStatMon3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime3)
                    .addComponent(lblTimeValue3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMonMainLayout = new javax.swing.GroupLayout(pnlMonMain);
        pnlMonMain.setLayout(pnlMonMainLayout);
        pnlMonMainLayout.setHorizontalGroup(
            pnlMonMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlStatMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlStatMon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStatMon2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStatMon3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMonMainLayout.setVerticalGroup(
            pnlMonMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMonMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlStatMon3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlStatMon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMonMainLayout.createSequentialGroup()
                        .addGroup(pnlMonMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlStatMon1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlStatMon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jButton1.setText("Grafico");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 387, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        btnStartStop.setText("Start");
        btnStartStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartStopActionPerformed(evt);
            }
        });

        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addComponent(pnlMonMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnStartStop))
                            .addComponent(btnStop, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                        .addGap(0, 435, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnStartStop)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStop))
                    .addComponent(pnlMonMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jToolBar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N

        lblTimeNow.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lblTimeNow.setForeground(new java.awt.Color(255, 255, 255));
        lblTimeNow.setText("    ");
        jToolBar1.add(lblTimeNow);

        jTextLog.setBackground(java.awt.Color.black);
        jTextLog.setColumns(20);
        jTextLog.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jTextLog.setForeground(java.awt.Color.orange);
        jTextLog.setRows(5);
        jScrollPane1.setViewportView(jTextLog);

        jTextAreaTypeProc.setBackground(java.awt.Color.darkGray);
        jTextAreaTypeProc.setColumns(20);
        jTextAreaTypeProc.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jTextAreaTypeProc.setForeground(new java.awt.Color(255, 255, 204));
        jTextAreaTypeProc.setRows(5);
        jScrollPane2.setViewportView(jTextAreaTypeProc);

        jTextAreaPoolProcess.setBackground(java.awt.Color.darkGray);
        jTextAreaPoolProcess.setColumns(20);
        jTextAreaPoolProcess.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jTextAreaPoolProcess.setForeground(new java.awt.Color(255, 255, 204));
        jTextAreaPoolProcess.setRows(5);
        jScrollPane3.setViewportView(jTextAreaPoolProcess);

        jMenu1.setText("General");
        jMenu1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16_Login Manager.png"))); // NOI18N
        jMenuItem1.setText("Login");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16_Log Out.png"))); // NOI18N
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Schedule");
        jMenu4.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jMenuBar1.add(jMenu4);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16_report_01.png"))); // NOI18N
        jMenu3.setText("Report");
        jMenu3.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jMenuBar1.add(jMenu3);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16_info_01.png"))); // NOI18N
        jMenu2.setText("About");
        jMenu2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            this.setEnabled(false);
            frmLogin _frmLogin = new frmLogin(gDatos);
            _frmLogin.setVisible(true);
            _frmLogin.setAlwaysOnTop(true);
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        } 
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
       System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        //DefaultPieDataset data = new DefaultPieDataset();  //Para el Grafico PIE
        
        data.addValue(20, "OSP", "HOY");
        data.addValue(99, "ETL", "HOY");
        data.addValue(25, "LOR", "HOY");
        data.addValue(12, "MOV", "HOY");
        
        // Creando el Grafico
        //JFreeChart chart = ChartFactory.createPieChart(
        //JFreeChart chart = ChartFactory.createBarChart("Ejemplo Rapido de Grafico en un ChartFrame", "Mes", "Valor", data);
        
        JFreeChart chart = ChartFactory.createBarChart("", "", "", data, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(Color.BLACK);
        chart.setTitle("");
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setRangePannable(true);
            plot.setRangeGridlinesVisible(false);
            plot.setBackgroundAlpha(1);
            plot.setBackgroundPaint(Color.BLACK);
            plot.setForegroundAlpha(1);
            plot.setDomainCrosshairPaint(Color.WHITE);
            plot.setNoDataMessagePaint(Color.WHITE);
            plot.setOutlinePaint(Color.WHITE);
            plot.setRangeCrosshairPaint(Color.WHITE);
            plot.setRangeMinorGridlinePaint(Color.WHITE);
            plot.setRangeZeroBaselinePaint(Color.WHITE);
            
    //        Paint p = new GradientPaint(0, 0, Color.white, 1000, 0, Color.green);
    //        plot.setBackgroundPaint(p);


            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setLabelPaint(Color.WHITE);
            rangeAxis.setAxisLinePaint(Color.WHITE);
            rangeAxis.setTickLabelPaint(Color.WHITE);
            rangeAxis.setVerticalTickLabels(true);
            
            //ChartUtilities.applyCurrentTheme(chart);


        // Crear el Panel del Grafico con ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(300, 150);
        chartPanel.setBackground(Color.BLACK);
        chartPanel.setOpaque(false);
        chartPanel.setDomainZoomable(true);
        this.jPanel1.setSize(300, 200);
        this.jPanel1.setBackground(Color.DARK_GRAY);
        this.jPanel1.add(chartPanel);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnStartStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartStopActionPerformed
        
        thgetStatus.start();
        
        
    }//GEN-LAST:event_btnStartStopActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        thgetStatus.interrupt();
    }//GEN-LAST:event_btnStopActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            frmMain _frmMain = new frmMain();
            
            //Setea Color del Frame
            _frmMain.getContentPane().setBackground(Color.BLACK);
            _frmMain.setLocationRelativeTo(null);
            _frmMain.setTitle("Servicio de Ejecución de Procesos - Log OUT: (Ingrese Login para activar)");
            _frmMain.setVisible(true);
            
            //Setea color del Menu
            _frmMain.jMenuBar1.setBackground(Color.BLACK);
            _frmMain.jMenuBar1.setForeground(Color.WHITE);
            
            //Setea Color del Panel Principal
            _frmMain.pnlMain.setBackground(Color.BLACK);
            _frmMain.pnlMonMain.setBackground(Color.BLACK);
            _frmMain.pnlStatMon.setBackground(Color.black);
            _frmMain.pnlStatMon1.setBackground(Color.black);
            _frmMain.pnlStatMon2.setBackground(Color.black);
            _frmMain.pnlStatMon3.setBackground(Color.black);
            
            //Inicializa Componentes del Panel Principal
            _frmMain.lblMon.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_SPHERE_OFF_STATUS()));
            _frmMain.lblMon1.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_SPHERE_OFF_STATUS()));
            _frmMain.lblMon2.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_SPHERE_OFF_STATUS()));
            _frmMain.lblMon3.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_SPHERE_OFF_STATUS()));
            
            _frmMain.lblTime.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_TIME_STATUS()));
            _frmMain.lblTime1.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_TIME_STATUS()));
            _frmMain.lblActive1.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_TIME_STATUS()));
            _frmMain.lblActive2.setIcon(new ImageIcon(gDatos.getDIR_ICON_BASE()+gDatos.getICO_TIME_STATUS()));
            
            //Asocia a variables Globales
            gDatos.setLblMonDesc(_frmMain.lblMon);
            gDatos.setjTextAreaLog(_frmMain.jTextLog);
            gDatos.setjTextAreaTypeProc(_frmMain.jTextAreaTypeProc);
            gDatos.setjTextAreaPoolProcess(_frmMain.jTextAreaPoolProcess);
            
            //Habilita Time Hour
            Thread _thTime = new thTime(_frmMain.lblTimeNow);
            _thTime.start();
            
        });
    }
        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStartStop;
    private javax.swing.JToggleButton btnStop;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaPoolProcess;
    private javax.swing.JTextArea jTextAreaTypeProc;
    private javax.swing.JTextArea jTextLog;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblActive1;
    private javax.swing.JLabel lblActive2;
    private javax.swing.JLabel lblMon;
    private javax.swing.JLabel lblMon1;
    private javax.swing.JLabel lblMon2;
    private javax.swing.JLabel lblMon3;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTime1;
    private javax.swing.JLabel lblTime2;
    private javax.swing.JLabel lblTime3;
    private javax.swing.JLabel lblTimeNow;
    private javax.swing.JLabel lblTimeNow2;
    private javax.swing.JLabel lblTimeNow3;
    private javax.swing.JLabel lblTimeValue0;
    private javax.swing.JLabel lblTimeValue1;
    private javax.swing.JLabel lblTimeValue2;
    private javax.swing.JLabel lblTimeValue3;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMonMain;
    private javax.swing.JPanel pnlStatMon;
    private javax.swing.JPanel pnlStatMon1;
    private javax.swing.JPanel pnlStatMon2;
    private javax.swing.JPanel pnlStatMon3;
    // End of variables declaration//GEN-END:variables
}
