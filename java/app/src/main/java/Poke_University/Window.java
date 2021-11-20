package Poke_University;

import javax.swing.JFrame;

public class Window extends JFrame{

    public Window(int height, int width){
        JFrame window = new JFrame();
        window.setTitle("Poke-University");
        window.setSize(height, width);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

}
