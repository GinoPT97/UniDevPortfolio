package gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel {
    private transient Image image;
    private transient Image scaledImage;
    private int lastWidth = -1;
    private int lastHeight = -1;

    public ImagePanel(Image image) {
        this.image = image;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());
        setDoubleBuffered(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScaledImage();
                repaint();
            }
        });
    }

    public void resetImage(Image newImage) {
        this.image = newImage;
        updateScaledImage();
        repaint();
    }

    public void setBackgroundImage(String filePath) {
        try {
            this.image = ImageIO.read(new File(filePath));
            updateScaledImage();
            repaint();
        } catch (IOException e) {
            throw new IllegalArgumentException("Errore durante il caricamento dell'immagine: " + e.getMessage(), e);
        }
    }

    public void resizePanel(Dimension newSize) {
        setPreferredSize(newSize);
        revalidate();
        repaint();
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        if (parent != null) {
            return new Dimension(parent.getWidth() / 2, parent.getHeight());
        }
        return super.getPreferredSize();
    }

    private void updateScaledImage() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (panelWidth <= 0 || panelHeight <= 0 || image == null) {
            scaledImage = null;
            return;
        }
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
        if (image == null) return;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (scaledImage == null || panelWidth != lastWidth || panelHeight != lastHeight) {
            updateScaledImage();
        }
        if (scaledImage != null) {
            g.drawImage(scaledImage, 0, 0, this);
        }
    }
}
