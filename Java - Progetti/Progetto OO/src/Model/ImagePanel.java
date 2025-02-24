package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;

    // Costruttore che riceve il percorso dell'immagine (da usare se l'immagine è inclusa nelle risorse)
    public ImagePanel(String imagePath) {
        this(new ImageIcon(ImagePanel.class.getResource(imagePath)).getImage());
    }

    // Costruttore che riceve direttamente un oggetto Image
    public ImagePanel(Image image) {
        this.image = image;
        setLayout(new BorderLayout());
        // Rimuovo setPreferredSize per evitare che il pannello imponga dimensioni troppo grandi
        // e si adatti dinamicamente in base allo spazio disponibile.
    }

    // Costruttore che riceve un componente da sovrapporre all'immagine di sfondo
    public ImagePanel(String imagePath, JComponent overlayComponent) {
        this(imagePath);
        overlayComponent.setOpaque(false); // Assicura che l'overlay sia trasparente
        add(overlayComponent, BorderLayout.CENTER);
    }

    // Metodo statico per creare un ImagePanel con overlay
    public static ImagePanel createWithOverlay(String imagePath, JComponent overlayComponent) {
        ImagePanel panel = new ImagePanel(imagePath);
        overlayComponent.setOpaque(false);
        panel.add(overlayComponent, BorderLayout.CENTER);
        return panel;
    }

    // Metodo per aggiornare l'immagine in seguito (utile se l'immagine cambia dinamicamente)
    public void resetImage(Image newImage) {
        this.image = newImage;
        revalidate();
        repaint();
    }

    // Metodo per impostare un'immagine di sfondo da un file
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
            int imageWidth = image.getWidth(this);
            int imageHeight = image.getHeight(this);

            if (imageWidth > 0 && imageHeight > 0) {
                // Calcola i fattori di scala per larghezza e altezza
                double scaleX = (double) panelWidth / imageWidth;
                double scaleY = (double) panelHeight / imageHeight;
                // Utilizza Math.max per far sì che l'immagine copra interamente il pannello
                double scale = Math.max(scaleX, scaleY);

                int drawWidth = (int) (imageWidth * scale);
                int drawHeight = (int) (imageHeight * scale);

                // Calcola le coordinate per centrare l'immagine nel pannello
                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                // Rendering di qualità con Graphics2D
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(image, x, y, drawWidth, drawHeight, this);
                g2d.dispose();
            }
        }
    }
}
