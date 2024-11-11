package termostato;

import java.util.Observable;

public class TermostatoModel extends Observable {
  
  private int temperatura;
  
  public TermostatoModel() {
    temperatura = 0;
  }
  
  public int getValoreTemperatura() {
    return temperatura;
  }
  
  public void setValoreTemperatura(int valore) {
    if (valore >= 0 && valore <= 100) {
      temperatura = valore;
      setChanged();
      notifyObservers(temperatura);
    }
  }

}
