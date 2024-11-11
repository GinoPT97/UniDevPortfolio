package termostato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsController implements ActionListener {
  
  private TermostatoModel termoModel;
  private FinestraPrincipale frame;
  
  public ButtonsController(TermostatoModel model, FinestraPrincipale view) {
    termoModel = model;
    frame = view;
    // controller si registra come listener presso il view
    frame.bottonePiu.addActionListener(this);
    frame.bottoneMeno.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    String action = event.getActionCommand();
    if (action.equals("PIU")) {
      termoModel.setValoreTemperatura(termoModel.getValoreTemperatura()+1);
    }
    else {
      termoModel.setValoreTemperatura(termoModel.getValoreTemperatura()-1);
    }
  }
  
  

}
