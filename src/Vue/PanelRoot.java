package Vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRoot extends JPanel {

    private Image fond;

    public PanelRoot() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        fond = new ImageIcon("img1.jpg").getImage();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(fond, 0, 0, getWidth(), getHeight(), this);

    }

}
