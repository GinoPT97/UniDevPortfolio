package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;
    private Image scaledImage;
    private int lastWidth = -1;
    private int lastHeight = -1;

    public ImagePanel(String imagePath) {
        this(new ImageIcon(ImagePanel.class.getResource(imagePath)).getImage());
    }

    public ImagePanel(Image image) {
        this.image = image;
        setLayout(new BorderLayout(0, 0)); // Imposta il layout senza gap
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Rimuovi eventuali bordi
        setDoubleBuffered(true); // Abilita il double buffering

        // Aggiorna l'immagine scalata quando il pannello viene ridimensionato
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScaledImage();
            }
        });
    }

    public ImagePanel(String imagePath, JComponent overlayComponent) {
        this(imagePath);
        overlayComponent.setOpaque(false);
        add(overlayComponent, BorderLayout.CENTER);
    }

    public static ImagePanel createWithOverlay(String imagePath, JComponent overlayComponent) {
        ImagePanel panel = new ImagePanel(imagePath);
        overlayComponent.setOpaque(false);
        panel.add(overlayComponent, BorderLayout.CENTER);
        return panel;
    }

    public void resetImage(Image newImage) {
        this.image = newImage;
        revalidate();
        repaint();
    }

    public void setBackgroundImage(String filePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filePath));
            this.image = bufferedImage;
            revalidate();
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resizePanel(Dimension newSize) {
        setPreferredSize(newSize);
        revalidate();
        repaint();
    }

    // Override per rimuovere eventuali margini
    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public Dimension getPreferredSize() {
        if (getParent() != null) {
            int parentWidth = getParent().getWidth();
            int parentHeight = getParent().getHeight();
            // Imposta la larghezza a metà del genitore
            return new Dimension(parentWidth / 2, parentHeight);
        }
        return super.getPreferredSize();
    }

    private void updateScaledImage() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (panelWidth <= 0 || panelHeight <= 0) {
            return;
        }
        // Aggiorna solo se le dimensioni sono cambiate
        if (panelWidth != lastWidth || panelHeight != lastHeight) {
            BufferedImage bufferedScaledImage = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedScaledImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, 0, 0, panelWidth, panelHeight, null);
            g2.dispose();
            scaledImage = bufferedScaledImage;
            lastWidth = panelWidth;
            lastHeight = panelHeight;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (panelWidth <= 0 || panelHeight <= 0) return;

        // Se non esiste una scaledImage o le dimensioni non corrispondono a quelle correnti, ricalcola
        if (scaledImage == null || scaledImage.getWidth(this) != panelWidth || scaledImage.getHeight(this) != panelHeight) {
            BufferedImage bufferedScaledImage = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedScaledImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, 0, 0, panelWidth, panelHeight, null);
            g2.dispose();
            scaledImage = bufferedScaledImage;
            lastWidth = panelWidth;
            lastHeight = panelHeight;
        }

        g.drawImage(scaledImage, 0, 0, this);
    }
}
