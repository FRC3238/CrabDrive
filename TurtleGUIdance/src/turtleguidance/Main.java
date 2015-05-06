package turtleguidance;

import javax.swing.JFrame;

import resources.ImageManager;

public class Main extends JFrame
{
    GamePanel gp;
    
    public Main()
    {
        gp = new GamePanel(this);
        this.setTitle("Turtle GUIdance");
        this.setIconImage(ImageManager.staticImageManager.getImage("sprTurtle"));
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
