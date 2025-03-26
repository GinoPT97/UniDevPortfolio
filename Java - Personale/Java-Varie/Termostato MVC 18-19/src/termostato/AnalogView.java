package termostato;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

public class AnalogView extends JPanel implements PropertyChangeListener {

  private static final int WIDTH = 20;
  private static final int TOP = 20;
  private static final int LEFT = 100;
  private static final int HEIGHT = 200;

  private static final int MAX_TEMP = 100;
  private static final int MIN_TEMP = 0;

  private int valoreCorrente = 0;
  private Color colore = Color.GREEN;
  private PropertyChangeSupport support;

  public AnalogView() {
    support = new PropertyChangeSupport(this);
    setPreferredSize(new Dimension(200, 550));
  }

  @Override
  public void paintComponent(Graphics g) {
    // sempre eseguito da EDT
    super.paintComponent(g);
    g.setColor(Color.black);
    g.drawRect(LEFT, TOP, WIDTH, HEIGHT);
    g.setColor(colore);
    g.fillOval(LEFT - WIDTH / 2, TOP + HEIGHT - WIDTH / 3, WIDTH * 2, WIDTH * 2);
    g.setColor(Color.black);
    g.drawOval(LEFT - WIDTH / 2, TOP + HEIGHT - WIDTH / 3, WIDTH * 2, WIDTH * 2);
    g.setColor(Color.white);
    g.fillRect(LEFT + 1, TOP + 1, WIDTH - 1, HEIGHT - 1);
    g.setColor(colore);
    // Occhio
    long redtop = HEIGHT * (valoreCorrente - MAX_TEMP) / (MIN_TEMP - MAX_TEMP);
    g.fillRect(LEFT + 1, TOP + (int) redtop, WIDTH - 1, HEIGHT - (int) redtop);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // Implementa il metodo per gestire i cambiamenti di proprietà
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }

}
