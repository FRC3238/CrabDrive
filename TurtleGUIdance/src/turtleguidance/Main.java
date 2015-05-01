package turtleguidance;

import javax.swing.JFrame;

public class Main extends JFrame
{
    GamePanel gp;
    
    public Main()
    {
        gp = new GamePanel();
        setSize(gp.GWIDTH,gp.GHEIGHT);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        add(gp);
    }
    
    public static void main(String[] args) 
    {
        Main m = new Main();
    }
    
}
