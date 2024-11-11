package termostato;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class FinestraPrincipale extends JFrame implements Observer {
  
  JButton bottonePiu = new JButton("+");
  JButton bottoneMeno = new JButton("-");
  JSlider slider = new JSlider(0, 100, 0);
  
  public FinestraPrincipale(DigitalView digitale, AnalogView analogico, TermostatoModel model) {
    super("Termostato");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    // view si registra come observer presso il model
    model.addObserver(this);
    
    bottonePiu.setActionCommand("PIU");
    bottoneMeno.setActionCommand("MENO");
    bottoneMeno.setEnabled(false);
    
    JPanel pannelloBottoni = new JPanel();
    pannelloBottoni.add(bottoneMeno);
    pannelloBottoni.add(bottonePiu);
    JPanel pannelloCentrale = new JPanel(new BorderLayout());
    pannelloCentrale.add(digitale, BorderLayout.CENTER);
    pannelloCentrale.add(pannelloBottoni, BorderLayout.PAGE_END);
    
    slider.setMajorTickSpacing(10);
    slider.setMinorTickSpacing(2);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);
    slider.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
    
    getContentPane().add(pannelloCentrale, BorderLayout.CENTER);
    getContentPane().add(analogico, BorderLayout.LINE_END);
    getContentPane().add(slider, BorderLayout.PAGE_END);
    
    setSize(650, 350);
    setLocationRelativeTo(null);
  }

  @Override
  public void update(Observable model, Object valore) {
    if (SwingUtilities.isEventDispatchThread()) {
      // e' l'EDT, posso aggiornare GUI
      aggiorna((Integer)valore);
    }
    else {
      // non e' EDT, ma devo chiedere ad EDT di aggiornare GUI
      final int newValue = (Integer)valore;
      Runnable target = new Runnable() {
        
        @Override
        public void run() {
          aggiorna(newValue);
        }
      };
      SwingUtilities.invokeLater(target);
    }
  }
  
  private void aggiorna(int valore) {
    if (valore == 100) {
      bottonePiu.setEnabled(false);
    }
    else if (valore == 0) {
      bottoneMeno.setEnabled(false);
    }
    else {
      bottonePiu.setEnabled(true);
      bottoneMeno.setEnabled(true);
    }
    slider.setValue(valore);
  }
}
