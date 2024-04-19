package com.pellegrinoprincipe;

import javax.swing.JFrame;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class JFrameContainer extends JFrame
{
    public JFrameContainer()
    {
        super("Una finestra");

        // scelgo l'icona da attribuire alla finestra
        URL icon_url = getClass().getResource("computer.png");
        Image icon = new ImageIcon(icon_url).getImage();
        setIconImage(icon);
    }

    public static void main(String args[])
    {
        JFrameContainer window = new JFrameContainer();
        
        // imposto le dimensioni della finestra
        window.setSize(350, 200);
        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // rendo visibile la finestra
        window.setVisible(true);
        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
