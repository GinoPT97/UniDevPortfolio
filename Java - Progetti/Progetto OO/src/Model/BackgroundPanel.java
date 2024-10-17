package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class BackgroundPanel extends JPanel {
    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>(); // Cache delle immagini
    private Image backgroundImage;
    private boolean isImageLoaded = false;
    private final String imagePath;

    public BackgroundPanel(String imagePath) {
        this.imagePath = imagePath;
        backgroundImage = imageCache.get(imagePath);
        if (backgroundImage != null) {
            isImageLoaded = true;
            repaint();
        } else {
            loadBackgroundImage();
        }

        // Aggiungi un listener per il ridimensionamento del pannello
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isImageLoaded && backgroundImage != null) {
                    backgroundImage = resizeImage(backgroundImage, getWidth(), getHeight());
                    repaint();
                }
            }
        });
    }

    private void loadBackgroundImage() {
        SwingUtilities.invokeLater(() -> new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() {
                try {
                    URL imageURL = getClass().getClassLoader().getResource(imagePath);
                    if (imageURL == null) {
                        throw new IllegalArgumentException("L'immagine non è stata trovata: " + imagePath);
                    }
                    return ImageIO.read(imageURL);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    backgroundImage = get();
                    if (backgroundImage != null) {
                        imageCache.put(imagePath, backgroundImage); // Memorizza l'immagine nella cache
                        isImageLoaded = true;
                        backgroundImage = resizeImage(backgroundImage, getWidth(), getHeight());
                        repaint();
                    } else {
                        System.err.println("Errore nel caricamento dell'immagine: " + imagePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute());
    }

    private Image resizeImage(Image originalImage, int width, int height) {
        if (width > 0 && height > 0) {
            return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
        return originalImage; // Restituisce l'immagine originale se le dimensioni non sono valide
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isImageLoaded && backgroundImage != null) {
            // Disegna l'immagine ridimensionata per adattarsi al pannello
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Placeholder: colore di sfondo o immagine di segnaposto
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

