package termostato;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AnalogView extends JPanel implements Observer {

  private static final int width = 20;
  private static final int top = 20;
  private static final int left = 100;
  private static final int right = 250;
  private static final int height = 200;
  
  private static final int MAX_TEMP = 100;
  private static final int MIN_TEMP = 0;
  
  private int valoreCorrente = 0;
  private Color colore = Color.GREEN;
  
  public AnalogView(TermostatoModel model) {
    // view si registra come observer presso il model
    model.addObserver(this);
    setPreferredSize(new Dimension(200, 550));
  }
  
  public void paintComponent(Graphics g) {
    // sempre eseguito da EDT
    super.paintComponent(g);
    g.setColor(Color.black);
    g.drawRect(left, top, width, height);
    g.setColor(colore);
    g.fillOval(left-width/2, top+height-width/3,width*2, width*2);
    g.setColor(Color.black);
    g.drawOval(left-width/2, top+height-width/3,width*2, width*2);
    g.setColor(Color.white);
    g.fillRect(left+1,top+1, width-1, height-1);
    g.setColor(colore);
    //Occhio
    long redtop = height*(valoreCorrente-MAX_TEMP)/(MIN_TEMP-MAX_TEMP);
    g.fillRect(left+1, top + (int)redtop, width-1, height-(int)redtop);
  }
  
  @Override
  public void update(Observable model, Object valore) {
    valoreCorrente = (Integer)valore;
    if (valoreCorrente <= 30)
      colore = Color.GREEN;
    else if (valoreCorrente <= 70)
      colore = Color.ORANGE;
    else
      colore = Color.RED;
    repaint();
  }

}
