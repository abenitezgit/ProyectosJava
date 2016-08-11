/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appabtviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author ABT
 */
public class createWorkSpace {
    
    createWorkSpace() {
        JFrame frm = new JFrame("Titulo");
        
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        
        JLabel lbl1 = new JLabel("Ingrese Texto1", JLabel.CENTER);
        JLabel lbl2 = new JLabel("Ingrese Texto2", JLabel.CENTER);
        JLabel lbl3 = new JLabel("Ingrese Texto3", JLabel.CENTER);
        JLabel lbl4 = new JLabel("Ingrese Texto4", JLabel.CENTER);
        JLabel lbl5 = new JLabel("Ingrese Texto5", JLabel.CENTER);
        
        panel1.add(lbl1);
        panel2.add(lbl2);
        panel3.add(lbl3);
        panel4.add(lbl4);
        panel5.add(lbl5);
        
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Para Terminar ejecución cuando se presiona X
        frm.setSize(800, 600);
        frm.setLayout(new GridLayout(2,3));
        frm.setResizable(false);
        
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frm.getSize().width;
        int h = frm.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        
        frm.setLocation(x, y);
        
        frm.add(panel1);
        frm.add(panel2);
        frm.add(panel3);
        frm.add(panel4);
        frm.add(panel5);
        frm.pack();
        
        frm.setVisible(true);
    }
    
    public static void main(String args[]) {
        createWorkSpace createWorkSpace = new createWorkSpace();
    }
}
