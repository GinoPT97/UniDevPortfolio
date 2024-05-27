package termostato;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderController implements ChangeListener {
  
  private TermostatoModel termoModel;
  private FinestraPrincipale frame;
  
  public SliderController(TermostatoModel model, FinestraPrincipale view) {
    termoModel = model;
    frame = view;
    // controller si registra come listener presso il view
    frame.slider.addChangeListener(this);
  }
  
  @Override
  public void stateChanged(ChangeEvent e) {
    termoModel.setValoreTemperatura(frame.slider.getValue());
  }

}
