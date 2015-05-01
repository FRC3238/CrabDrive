package turtleguidance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import resources.Sound;
import resources.SoundManager;
import resources.ImageManager;

public class GamePanel extends JPanel implements Runnable
{
    //Double buffering
    private Image dbImage;
    private Graphics dbg;
    //JPanel
    static final int GWIDTH = 960, GHEIGHT = 540;
    static final Dimension gameDim = new Dimension(GWIDTH,GHEIGHT);
    //Game variables
    private Thread gameThread;
    private volatile boolean running = false;
    //Game loop
    public long previousThreadTime;
    
    ArrayList<Entity> gameObjects;
    
    SoundManager soundManager;
    ImageManager imageManager;
    
    public double instantFPS;
    
    public GamePanel()
    {
        setPreferredSize(gameDim);
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();
        previousThreadTime = System.nanoTime();
        instantFPS = 0;
        soundManager = new SoundManager()
        {
            @Override
            public void initSounds()
            {
                sounds.add(new Sound("test", Sound.getURL("BoxBreak.wav")));
            }
        };
        soundManager.playSound("test");
        
        imageManager = new ImageManager();
        ImageManager.populateImageList();
        
        gameObjects = new ArrayList<Entity>();
        
        gameObjects.add(new Cursor());
        
        gameObjects.add(new SolidWall(32,32));
        gameObjects.add(new SolidWall(32,64));
        gameObjects.add(new SolidWall(32,96));
        gameObjects.add(new SolidWall(32,128));
        gameObjects.add(new SolidWall(0,160));
        
        gameObjects.add(new VisualVector(16,12,45,57));
        gameObjects.add(new VisualVector(45,57,64,128));
        
        
        //Handle all key inputs
        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {

            }
            @Override
            public void keyReleased(KeyEvent e)
            {

            }
            @Override
            public void keyTyped(KeyEvent e)
            {

            }
        });
        
        addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                ((Cursor)(gameObjects.get(0))).justPressed = true;
                ((Cursor)(gameObjects.get(0))).justReleased = false;
            }
            @Override
            public void mouseReleased(MouseEvent e)
            {
                ((Cursor)(gameObjects.get(0))).justReleased = true;
                //((Cursor)(gameObjects.get(0))).isClicked = false;
                ((Cursor)(gameObjects.get(0))).justPressed = false;
            }
            @Override
            public void mouseClicked(MouseEvent e)
            {
                //((Cursor)(gameObjects.get(0))).isClicked = true;
            }
        });
        
        addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                ((Cursor)(gameObjects.get(0))).setPosition(e.getPoint());
            }
            @Override
            public void mouseDragged(MouseEvent e)
            {

            }
            @Override
            public void mouseEntered(MouseEvent e)
            {

            }
            @Override
            public void mouseExited(MouseEvent e)
            {

            }
        });
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            //Main game loop, hard coded to 60fps
            if(System.nanoTime()-previousThreadTime >= 16666666)
            {
                instantFPS = 3.6*((System.nanoTime()-previousThreadTime)/1000000.0);
                gameUpdate();
                gameRender();
                paintScreen();
                previousThreadTime = System.nanoTime();
            }
        }
    }
    
    private void gameUpdate()
    {
        if(running && gameThread != null)
        {
            //call game objects step events
            for(int i = 0; i < gameObjects.size(); i++)
            {
                gameObjects.get(i).step();
            }
        }
    }
    
    private void gameRender()
    {
        if(dbImage == null)//Create the buffer
        {
            dbImage = createImage(GWIDTH, GHEIGHT);
            if(dbImage == null)
            {
                System.err.println("dbImage is still null!");
                return;
            }
            else
            {
                dbg = dbImage.getGraphics();
            }
        }
        //Clear the screen
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0,0,GWIDTH, GHEIGHT);
        //Draw game elements
        drawGameElements(dbg);
    }
    
    //Draw all of the game stuff
    public void drawGameElements(Graphics g)
    {
        g.setColor(Color.BLACK);
        //g.drawString("Hello World!", 100, 100);
        g.drawString(Double.toString(instantFPS), 100, 100);
        for(int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).draw(g);
        }
    }
    
    private void paintScreen()
    {
        Graphics g;
        try
        {
           g = this.getGraphics();
           if(dbImage != null && g != null)
           {
               g.drawImage(dbImage, 0, 0, this);
           }
           Toolkit.getDefaultToolkit().sync();//For linux users
           g.dispose();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }
    
    @Override
    public void addNotify()
    {
        super.addNotify();
        startGame();
    }
    
    private void startGame()
    {
        if(gameThread == null || !running)
        {
            gameThread = new Thread(this);
            gameThread.start();
            running = true;
        }
    }
    
    public void stopGame()
    {
        if(running)
        {
            running = false;
        }
    }
    
    public void log(String s)
    {
        System.out.println(s);
    }
}
