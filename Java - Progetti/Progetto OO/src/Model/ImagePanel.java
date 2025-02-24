package Model;

import javax.swing.*;
import java.awt.*;

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
    }

    // Costruttore che riceve un componente da sovrapporre all'immagine di sfondo
    public ImagePanel(String imagePath, JComponent overlayComponent) {
        this(imagePath);
        add(overlayComponent);
    }

    // Metodo per aggiornare l'immagine in seguito (utile se l'immagine cambia dinamicamente)
    public void resetImage(Image newImage) {
        this.image = newImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imageWidth = image.getWidth(this);
            int imageHeight = image.getHeight(this);

            // Verifica che l'immagine sia stata caricata correttamente
            if (imageWidth > 0 && imageHeight > 0) {
                // Calcola i fattori di scala per larghezza e altezza
                double scaleX = (double) panelWidth / imageWidth;
                double scaleY = (double) panelHeight / imageHeight;
                // Per preservare le proporzioni, usiamo il minore dei due scale factor
                double scale = Math.min(scaleX, scaleY);

                int drawWidth = (int) (imageWidth * scale);
                int drawHeight = (int) (imageHeight * scale);

                // Calcola le coordinate per centrare l'immagine
                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                g.drawImage(image, x, y, drawWidth, drawHeight, this);
            }
        }
    }
}
