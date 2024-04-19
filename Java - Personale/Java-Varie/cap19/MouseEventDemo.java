package com.pellegrinoprincipe;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class MouseEventDemo extends JFrame
{
    private JButton button;
    private JTextArea area;
    private JTextArea wheel_area;
    private JScrollPane sp;

    public MouseEventDemo()
    {
        super("MouseEvent Demo");
        
        setLayout(null); // posizionameto assoluto

        // componenti
        button = new JButton("...sono a tua disposizione...");
        area = new JTextArea();
        wheel_area = new JTextArea();

        sp = new JScrollPane(area);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setBounds(1, 100, 580, 250);

        // aggiungo i componenti al container
        add(button);
        add(sp);
        add(wheel_area);

        button.setBounds(1, 1, 200, 60);
        wheel_area.setBounds(210, 1, 360, 90);

        class MyMouseAdapter extends MouseAdapter
        {            
            public void mouseEntered(MouseEvent e) // mouse enter
            {
                Cursor c1 = new Cursor(Cursor.HAND_CURSOR);
                button.setCursor(c1);
                area.append("[Enter] --> (x)" + e.getX() + ", (y)" + e.getY() + "\n");
            }
            
            public void mouseExited(MouseEvent e) // mouse exit
            {
                area.append("[Exit]\n");
            }
            
            public void mousePressed(MouseEvent e) // pressione del mouse
            {
                int nr_click = e.getClickCount();
                String msg = "numero di click " + nr_click;
                area.append("[Press] --> " + msg + "\n");
            }
            
            public void mouseReleased(MouseEvent e) // rilascio del mouse
            {
                area.append("[Release] --> " + "\n");
            }
            
            public void mouseClicked(MouseEvent e) // click del mouse
            {
                int b = e.getButton();
                String what = "";
                
                switch (b) // quale pulsante è stato premuto?
                {
                    case MouseEvent.BUTTON1:
                        what = "pulsante sinistro...";
                        break;
                    case MouseEvent.BUTTON2:
                        what = "pulsante centrale...";
                        break;
                    case MouseEvent.BUTTON3:
                        what = "pulsante destro...";
                        break;
                    default:
                        what = "pulsante sconosciuto...";
                }

                area.append("[Click] --> " + what + "\n");
            }

            public void mouseDragged(MouseEvent e)
            {
                Point p_l = e.getLocationOnScreen();
                area.append("[Dragging] --> (x)" + p_l.x + ", (y)" + p_l.y + "\n");
            }
           
            public void mouseMoved(MouseEvent e)  // muovo il mouse
            {
                String msg = "";

                // test di alcuni modificatori
                if ((e.getModifiersEx() == (e.ALT_DOWN_MASK | e.SHIFT_DOWN_MASK)))
                    msg += "premuti contemporaneamente SHIFT+ALT";
                area.append("[Moving] --> " + msg + "\n");
            }

            public void mouseWheelMoved(MouseWheelEvent e)
            {
                String st[] = {"PER UNITA'", "PER BLOCCO"};

                int unit = e.getUnitsToScroll();

                // calcolo per la scrollbar verticale l'attuale posizione
                // e l'unità di incremento per scroll
                JScrollBar sb = sp.getVerticalScrollBar();
                int y = sb.getValue();
                int y1 = e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL
                        ? sb.getUnitIncrement(1) : sb.getBlockIncrement(1);
                 
                sb.setValue(y + y1 * unit); // aggiorna la view della textarea  

                int a = e.getScrollAmount();
                int b = e.getScrollType();
                int c = e.getUnitsToScroll();
                int d = e.getWheelRotation();
                double f = e.getPreciseWheelRotation();
                String g = "Unità da scorrere per giro di tacca: " + a
                        + "\nTipo di scroll: " + st[e.getScrollType()]
                        + "\nUnità che scorreranno verso l'alto: " + c
                        + "\nNumero di tacche ruotate: " + d
                        + "\nNumero di tacche precisamente ruotate: " + f;

                wheel_area.setText(g);
            }
        }

        MyMouseAdapter mma = new MyMouseAdapter();

        // listeners
        button.addMouseListener(mma);
        button.addMouseMotionListener(mma);
        button.addMouseWheelListener(mma);
    }

    public static void main(String args[])
    {
        // creo la finestra
        MouseEventDemo window = new MouseEventDemo();

        window.setSize(600, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
