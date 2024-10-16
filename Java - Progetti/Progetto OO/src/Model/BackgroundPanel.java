package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    }

    private void loadBackgroundImage() {
        SwingUtilities.invokeLater(() -> new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() {
                try {
                    // Usa getResource con un percorso relativo corretto
                    URL imageURL = getClass().getResource("/Immagini/" + imagePath);
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
                        repaint();
                    } else {
                        // Gestisci l'errore se l'immagine non è stata caricata
                        System.err.println("Errore nel caricamento dell'immagine: " + imagePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute());
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

