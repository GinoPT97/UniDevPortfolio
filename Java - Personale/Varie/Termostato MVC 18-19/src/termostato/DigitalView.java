package termostato;

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DigitalView extends JPanel implements Observer {

  private JLabel tempLabel;

  public DigitalView(TermostatoModel model) {
    tempLabel = new JLabel("0°C");
    tempLabel.setFont(new Font("Arial", Font.BOLD, 150));
    this.add(tempLabel);
    // view si registra come observer presso il model
    model.addObserver(this);
  }

  @Override
  public void update(Observable model, Object newValue) {
    if (SwingUtilities.isEventDispatchThread()) {
      // e' l'EDT, posso aggiornare GUI
      tempLabel.setText((Integer)newValue + "°C");
    }
    else {
      // non e' EDT, ma devo chiedere ad EDT di aggiornare GUI
      final int valore = (Integer)newValue;
      Runnable target = new Runnable() {

        @Override
        public void run() {
          tempLabel.setText(valore + "°C");
        }
      };
      SwingUtilities.invokeLater(target);
    }
  }
}
