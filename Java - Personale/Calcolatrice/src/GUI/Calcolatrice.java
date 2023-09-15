package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import Model.Calc;

public class Calcolatrice extends JFrame{
	     private JPanel pane1 = new JPanel();
	     private JPanel pane2 = new JPanel();
	     private JPanel pane3 = new JPanel();
	     private JPanel pane4 = new JPanel();
	     private JPanel pane5 = new JPanel();
	     private JPanel pane6 = new JPanel();
	     private JPanel pane7 = new JPanel();
	     private JPanel pane8 = new JPanel();
         private JButton calc = new JButton("Calcola");
	     private JButton clear = new JButton("Clear");
	     private JButton backbutt = new JButton("Indietro");
	     private JLabel inser1 = new JLabel("Inserisci il primo operando :");
	     private JLabel inser2 = new JLabel("Inserisci il secondo operando :");
	     private JLabel risin = new JLabel("Ecco il risultato : ");
	     private JButton sommabutt = new JButton("Somma");
	     private JButton sottbutt = new JButton("Sottrazione");
	     private JButton moltbutt = new JButton("Moltiplicazione");
	     private JButton divbutt = new JButton("divisione");
         private JCheckBox sommacb = new JCheckBox("Somma");
         private JCheckBox sottcb = new JCheckBox("Sottrazione");
         private JCheckBox moltcb = new JCheckBox("Moltiplicazione");
         private JCheckBox divcb = new JCheckBox("Divisione");
         private String[] opcb = {"Somma","Sottrazione","Moltiplicazione","Divisione"};
         private JComboBox combo1 = new JComboBox(opcb);
	     private JTextField operando1 = new JTextField(15);
	     private JTextField operando2 = new JTextField(15);
	     private JLabel risout = new JLabel("0.0");
         
	     public void elem() {
	     pane1.add(inser1);
    	 pane1.add(operando1);
    	 pane2.add(inser2);
    	 pane2.add(operando2);
    	 pane3.add(risin);
    	 pane3.add(risout);
    	 pane4.add(sommacb);
    	 pane4.add(sottcb);
    	 pane4.add(moltcb);
    	 pane4.add(divcb);
    	 pane4.add(calc);
    	 pane5.add(sommabutt);
    	 pane5.add(sottbutt);
    	 pane5.add(moltbutt);
    	 pane5.add(divbutt);
    	 pane6.add(combo1);
    	 pane6.add(calc);
    	 pane7.add(clear);
    	 pane7.add(backbutt);
	     }
	     
         public void pan() {
        	 elem();
        	 pane8.add(pane1);
        	 pane8.add(pane2);
        	 pane8.add(pane3);
        	 //pane8.add(pane4);
        	 //pane8.add(pane5);
        	 pane8.add(pane6);
        	 pane8.add(pane7);
        	 pane8.setLayout(new BoxLayout(pane8, BoxLayout.Y_AXIS));
        	 this.add(pane8);
             this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             this.pack();
         }
         
         public float first () {
        	 String s = operando1.getText();
        	 float a = Float.parseFloat(s);  
			 return a;
         }
         public float second () {
        	 String s = operando2.getText();
        	 float b = Float.parseFloat(s);  
			 return b;
         }
         
         public void az(Controller c) {
        	 clear.addActionListener(new ActionListener() {
        		 public void actionPerformed(ActionEvent e) {
        			 operando1.setText("");
        			 operando2.setText("");
        			 risout.setText("0.0");
        		 }
        	 });
        	 
        	 calc.addActionListener(new ActionListener() {
        		 public void actionPerformed(ActionEvent e) {
        			int op = combo1.getSelectedIndex();
					switch(op) {
					case 0: 
						String s = c.somma();
						risout.setText(s);
						break;
					case 1: 
						String s1 = c.sottrazione();
						risout.setText(s1);
						break;
        		    case 2: 
						String s2 = c.moltiplicazione();
						risout.setText(s2);
						break;
        		    case 3: 
						String s3 = c.moltiplicazione();
						risout.setText(s3);
						break;
        		 }
        		}
        	 });
        	 
        	 calc.addActionListener(new ActionListener() {
        		 public void actionPerformed(ActionEvent e) {
        			 if(sommacb.isSelected()) {
        				String s = c.somma();
 						risout.setText(s);
        			 }
        			 if(sottcb.isSelected()) {
        				String s = c.sottrazione();
 						risout.setText(s);
        			 }
        			 if(moltcb.isSelected()) {
        				String s2 = c.moltiplicazione();
 						risout.setText(s2); 
        			 }
        			 if(divcb.isSelected()) {
         				String s2 = c.divisione();
  						risout.setText(s2); 
         			 }
        		 }
        	 });
             
             sommabutt.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
            		 String s = c.somma();
            		 risout.setText(s);
            	 }
             });
             
             sottbutt.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
            		 String s = c.sottrazione();
            		 risout.setText(s);
            	 }
             });
            
             moltbutt.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
            		 String s = c.moltiplicazione();
            		 risout.setText(s);
            	 }
             });
             
             divbutt.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
            		 String s = c.divisione();
            		 risout.setText(s);
            	 }
             });
             backbutt.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
            		 c.back1();
            	 }
             });
             
             calc.addActionListener(new ActionListener() {
            	 public void actionPerformed(ActionEvent e) {
                	 String s =(String) combo1.getSelectedItem();
                	 try {
                		float a = Float.parseFloat(inser1.getText());
                		float b = Float.parseFloat(inser2.getText());
                		float r = Float.parseFloat(risin.getText());
                		Calc cal = new Calc(a, b,  s , r);
						c.setop(cal);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
                 }
             });
         }
         
         public Calcolatrice(String title, Controller c) {
        	 super(title);
        	 this.pan();
        	 this.az(c);
       }
}