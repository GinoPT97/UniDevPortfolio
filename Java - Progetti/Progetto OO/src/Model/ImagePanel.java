package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;
    private Image scaledImage;

    public ImagePanel(String imagePath) {
        this(new ImageIcon(ImagePanel.class.getResource(imagePath)).getImage());
    }

    public ImagePanel(Image image) {
        this.image = image;
        setLayout(new BorderLayout());
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Se le dimensioni sono cambiate, aggiorna l'immagine scalata
            if (scaledImage == null || scaledImage.getWidth(this) != panelWidth || scaledImage.getHeight(this) != panelHeight) {
                scaledImage = image.getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);
            }

            g.drawImage(scaledImage, 0, 0, this);
        }
    }
}
