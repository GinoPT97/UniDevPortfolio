package termostato;

import javax.swing.SwingUtilities;

public class Applicazione {

  public static void main(String[] args) {

    Runnable target = () -> {
        // creo model
        TermostatoModel model = new TermostatoModel();
        // view si registrano presso model come observer
        DigitalView digitalView = new DigitalView(model);
        AnalogView analogView = new AnalogView(); // Modifica il costruttore
        FinestraPrincipale mainFrame = new FinestraPrincipale(digitalView, analogView, model);
        // controller si registrano presso view come listener
        new ButtonsController(model, mainFrame); // Rimuovi la variabile locale inutilizzata
        mainFrame.setVisible(true);
    };
    SwingUtilities.invokeLater(target);
  }

}
