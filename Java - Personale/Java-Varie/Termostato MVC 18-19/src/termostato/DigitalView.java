package termostato;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DigitalView extends JPanel implements PropertyChangeListener {

  private JLabel tempLabel;

  public DigitalView(TermostatoModel model) {
    tempLabel = new JLabel("0°C");
    tempLabel.setFont(new Font("Arial", Font.BOLD, 150));
    this.add(tempLabel);
    // view si registra come listener presso il model
    model.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (SwingUtilities.isEventDispatchThread()) {
      // e' l'EDT, posso aggiornare GUI
      tempLabel.setText(evt.getNewValue() + "°C");
    } else {
      // non e' EDT, ma devo chiedere ad EDT di aggiornare GUI
      SwingUtilities.invokeLater(() -> tempLabel.setText(evt.getNewValue() + "°C"));
    }
  }
}
