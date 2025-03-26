package termostato;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class FinestraPrincipale extends JFrame implements PropertyChangeListener {

  JButton bottonePiu = new JButton("+");
  JButton bottoneMeno = new JButton("-");
  JSlider slider = new JSlider(0, 100, 0);

  public FinestraPrincipale(DigitalView digitale, AnalogView analogico, TermostatoModel model) {
    super("Termostato");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // view si registra come listener presso il model
    model.addPropertyChangeListener(this);

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
  public void propertyChange(PropertyChangeEvent evt) {
    if (SwingUtilities.isEventDispatchThread()) {
      // e' l'EDT, posso aggiornare GUI
      aggiorna((Integer) evt.getNewValue());
    } else {
      // non e' EDT, ma devo chiedere ad EDT di aggiornare GUI
      final int newValue = (Integer) evt.getNewValue();
      SwingUtilities.invokeLater(() -> aggiorna(newValue));
    }
  }

  private void aggiorna(int valore) {
    switch (valore) {
      case 100 -> bottonePiu.setEnabled(false);
      case 0 -> bottoneMeno.setEnabled(false);
      default -> {
        bottonePiu.setEnabled(true);
        bottoneMeno.setEnabled(true);
      }
    }
    slider.setValue(valore);
  }
}
