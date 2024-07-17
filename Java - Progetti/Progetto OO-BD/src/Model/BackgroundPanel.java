package Model;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	private Image backgroundImage;

	public BackgroundPanel(String imagePath) {
		// Carica l'immagine utilizzando getClassLoader().getResource(), che restituisce
		// un URL
		URL imageURL = getClass().getClassLoader().getResource(imagePath);
		if (imageURL == null) {
			throw new IllegalArgumentException("L'immagine non è stata trovata: " + imagePath);
		}

		try {
			backgroundImage = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
