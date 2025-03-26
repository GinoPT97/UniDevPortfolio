package termostato;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TermostatoModel {

  private int temperatura;
  private PropertyChangeSupport support;

  public TermostatoModel() {
    temperatura = 0;
    support = new PropertyChangeSupport(this);
  }

  public int getValoreTemperatura() {
    return temperatura;
  }

  public void setValoreTemperatura(int valore) {
    if (valore >= 0 && valore <= 100) {
      int oldValue = this.temperatura;
      temperatura = valore;
      support.firePropertyChange("temperatura", oldValue, temperatura);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }
}
