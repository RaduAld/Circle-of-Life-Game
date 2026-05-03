package Vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.*;
import java.awt.*;
import javax.swing.border.Border;

public class Utilities {


    //    @Override
//    public void toggleIA(boolean b){
//        if(b)
//            choixBoard.setText("Joueur VS : IA");
//        else
//            choixBoard.setText("Joueur VS : Joueur");
//    }
//
    public static JButton createJButton(String texte){
        JButton button = new JButton(texte);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setFont(new Font("Brush", Font.BOLD, 16));
        //button.setFont(new Font("Monserrat",Font.BOLD,16));
        button.setBackground(new Color(0x51511D));
        button.setForeground(new Color(200, 174, 73, 220));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        return button;
    }
//
//     public static JToggleButton createToggleButton(String texte){
//        JToggleButton toggleButton = new JToggleButton(texte);
//        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        toggleButton.setFocusable(false);
//        toggleButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, toggleButton.getPreferredSize().height));
//        toggleButton.setFont(new Font("Monserrat",Font.BOLD,16));
//        toggleButton.setBackground(new Color(224, 209, 197));
//        toggleButton.setBorderPainted(false);
//        return toggleButton;
//    }
//
    public static JLabel createJLabel(String texte, Color color){
        JLabel label = new JLabel(texte);

        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        label.setMinimumSize(new Dimension(Integer.MIN_VALUE, 40));
        //label.setPreferredSize(new Dimension(300, 20));
        //label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Monserrat",Font.BOLD,18));
        label.setForeground(color);
        label.setOpaque(true);
        label.setBackground(new Color(253, 222, 173, 148));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(207, 185, 106, 220), 2, true),
                new EmptyBorder(20, 30, 20, 30)
        ));
        return label;
    }
}
