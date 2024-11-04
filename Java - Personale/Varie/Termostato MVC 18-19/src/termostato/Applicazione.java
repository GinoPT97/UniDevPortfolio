package termostato;

import javax.swing.SwingUtilities;

/*
 * NOTA: questo e' un semplice esempio didattico.
 * Spesso la separazione tra model-view-controller non e' netta e definita.
 * In questo esempio era possibile considerare la finestra principale come unica view
 * aggiornando i componenti grafici al variare del model.
 */

public class Applicazione {
  
  public static void main(String[] args) {
    
    Runnable target = new Runnable() {
      
      @Override
      public void run() {
        // creo model
        TermostatoModel model = new TermostatoModel();
        // creo le view passando il model;
        // view si registrano presso model come observer
        DigitalView digitalView = new DigitalView(model);
        AnalogView analogView = new AnalogView(model);
        FinestraPrincipale mainFrame = new FinestraPrincipale(digitalView, analogView, model);
        // creo controller passando model e view;
        // controller si registrano presso view come listener
        SliderController sliderController = new SliderController(model, mainFrame);
        ButtonsController buttonsController = new ButtonsController(model, mainFrame);
        mainFrame.setVisible(true);
      }
    };
    SwingUtilities.invokeLater(target);
  }

}
