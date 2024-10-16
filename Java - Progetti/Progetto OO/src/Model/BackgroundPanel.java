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

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>(); // Cache delle immagini
    private Image backgroundImage;
    private Image scaledImage; // Immagine ridimensionata
    private boolean isImageLoaded = false;
    private final String imagePath;

    public BackgroundPanel(String imagePath) {
        this.imagePath = imagePath;
        backgroundImage = imageCache.get(imagePath);
        if (backgroundImage != null) {
            isImageLoaded = true;
            resizeImage(); // Ridimensiona subito l'immagine
        } else {
            loadBackgroundImage();
        }
    }

    private void loadBackgroundImage() {
        new Thread(() -> {
            try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                if (imageStream == null) {
                    throw new IllegalArgumentException("L'immagine non è stata trovata: " + imagePath);
                }
                backgroundImage = ImageIO.read(imageStream);
                if (backgroundImage != null) {
                    imageCache.put(imagePath, backgroundImage); // Memorizza l'immagine nella cache
                    isImageLoaded = true;
                    resizeImage(); // Ridimensiona l'immagine dopo il caricamento
                    repaint(); // Richiama il repaint dopo il caricamento
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void resizeImage() {
        if (backgroundImage != null) {
            // Ridimensiona l'immagine per adattarsi al pannello
            scaledImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isImageLoaded && scaledImage != null) {
            // Disegna l'immagine ridimensionata per adattarsi al pannello
            g.drawImage(scaledImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Placeholder: colore di sfondo o immagine di segnaposto
            g.setColor(java.awt.Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        // Ridimensiona l'immagine quando il pannello cambia dimensione
        if (isImageLoaded) {
            resizeImage();
        }
    }
}
