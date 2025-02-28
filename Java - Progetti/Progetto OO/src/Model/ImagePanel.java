package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, panelWidth, panelHeight, this);
            g2d.dispose();
        }
    }
}
