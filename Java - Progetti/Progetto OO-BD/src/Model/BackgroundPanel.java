package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class BackgroundPanel extends JPanel {
	private Image backgroundImage;
	private boolean isImageLoaded = false;

	public BackgroundPanel(String imagePath) {
		loadBackgroundImage(imagePath);
	}

	private void loadBackgroundImage(String imagePath) {
		new SwingWorker<Image, Void>() {
			protected Image doInBackground() throws Exception {
				URL imageURL = getClass().getClassLoader().getResource(imagePath);
				if (imageURL == null) {
					throw new IllegalArgumentException("L'immagine non è stata trovata: " + imagePath);
				}
				return ImageIO.read(imageURL);
			}

			protected void done() {
				try {
					backgroundImage = get();
					isImageLoaded = true;
					repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isImageLoaded && backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		} else {
			// Placeholder: colore di sfondo o immagine di segnaposto
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
