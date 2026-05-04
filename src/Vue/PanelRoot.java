package Vue;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanelRoot extends JPanel {

    private BufferedImage fond;

    public PanelRoot() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        try{
            fond = ImageIO.read(new File("res/images/fond.jpg"));
        }catch(IOException e){
            System.err.print("Probleme avec image");
            System.exit(1);
        }

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(fond, 0, 0, getWidth(), getHeight(), this);

    }

}
