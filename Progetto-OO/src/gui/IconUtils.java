package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class IconUtils {
    private IconUtils() {}

    public static Icon getIconForText(String text, Color color) {
        final int SIZE = 20;
        BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Sfondo circolare
        g.setColor(color);
        g.fillOval(1, 1, SIZE-2, SIZE-2);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        String t = text.toLowerCase();
        switch (t) {
            case "pulisci" -> { // Scopa
                g.setStroke(new BasicStroke(2.2f));
                g.drawLine(7, 5, 13, 15);
                g.setStroke(new BasicStroke(2.8f));
                g.drawArc(10, 12, 6, 5, 20, 140);
                g.setStroke(new BasicStroke(1.2f));
                g.drawLine(12, 15, 15, 17);
                g.drawLine(11, 14, 13, 18);
            }
            case "nuovo ordine" -> { // Documento + freccia
                g.setStroke(new BasicStroke(2f));
                g.drawRect(4, 3, 10, 14);
                g.drawLine(4, 7, 14, 7);
                g.drawLine(4, 11, 14, 11);
                g.drawLine(12, 3, 14, 5);
                g.drawLine(14, 3, 14, 5);
                g.setStroke(new BasicStroke(2.2f));
                g.drawArc(10, 11, 6, 6, 180, 220);
                g.drawLine(13, 17, 16, 15);
            }
            case "filtra" -> { // Filtro
                g.drawRect(6, 5, 8, 3);
                g.drawLine(6, 8, 10, 15);
                g.drawLine(14, 8, 10, 15);
            }
            case "inserisci ordine", "inserisci" -> { // Più
                g.setStroke(new BasicStroke(2.5f));
                g.drawLine(10, 5, 10, 15);
                g.drawLine(5, 10, 15, 10);
            }
            case "login", "aggiungi", "conferma" -> { // Spunta
                g.drawLine(6, 12, 9, 15);
                g.drawLine(9, 15, 15, 7);
            }
            case "seleziona", "selezione" -> { // Freccia/cursore
                g.setStroke(new BasicStroke(2.5f));
                g.drawLine(10, 4, 10, 16);
                g.drawLine(10, 4, 6, 8);
                g.drawLine(10, 4, 14, 8);
            }
            case "statistiche" -> { // Grafico a barre
                g.fillRect(5, 13, 2, 3);
                g.fillRect(9, 9, 2, 7);
                g.fillRect(13, 5, 2, 11);
            }
            case "prodotti", "prodotto" -> { // Scatola
                g.drawRect(5, 7, 10, 7);
                g.drawLine(5, 7, 10, 4);
                g.drawLine(15, 7, 10, 4);
            }
            case "ordini", "ordine" -> { // Lista/documento
                g.drawRect(6, 4, 8, 12);
                g.drawLine(8, 7, 12, 7);
                g.drawLine(8, 10, 12, 10);
                g.drawLine(8, 13, 12, 13);
            }
            case "clienti", "cliente", "dipendenti", "dipendente" -> { // Utente
                g.drawOval(7, 5, 6, 6);
                g.drawArc(6, 10, 8, 4, 0, 180);
            }
            case "clear", "rimuovi", "elimina", "annulla" -> { // X
                g.drawLine(6, 6, 14, 14);
                g.drawLine(14, 6, 6, 14);
            }
            case "modifica" -> { // Matita
                g.setStroke(new BasicStroke(2f));
                g.drawLine(6, 14, 14, 6);
                g.drawLine(10, 14, 14, 10);
            }
            case "cerca", "ricerca" -> { // Lente
                g.setStroke(new BasicStroke(2.5f));
                g.drawOval(4, 4, 9, 9);
                g.setStroke(new BasicStroke(3.2f));
                g.drawLine(12, 12, 16, 16);
            }
            case "indietro", "back" -> { // Freccia sinistra
                g.setStroke(new BasicStroke(3.5f));
                g.drawLine(14, 10, 5, 10);
                g.drawLine(5, 10, 9, 6);
                g.drawLine(5, 10, 9, 14);
            }
            case "logout" -> { // Porta con freccia
                g.drawRect(5, 5, 6, 10);
                g.drawLine(11, 10, 16, 10);
                g.drawLine(14, 8, 16, 10);
                g.drawLine(14, 12, 16, 10);
            }
            default -> g.fillOval(8, 8, 4, 4); // Punto
        }
        g.dispose();
        return new ImageIcon(img);
    }
}