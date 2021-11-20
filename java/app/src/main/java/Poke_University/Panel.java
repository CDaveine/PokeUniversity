package Poke_University;

import java.awt.*;
import javax.swing.JPanel;

public class Panel extends JPanel{

    public Panel(){

    }

    public void paintComponent(Graphics g){                
        Font font = new Font("Courier", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.blue);          
        g.drawString("Poke-University", 10, 20);                
      }    
}
