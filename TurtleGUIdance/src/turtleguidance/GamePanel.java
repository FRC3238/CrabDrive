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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.Sound;
import resources.SoundManager;
import resources.ImageManager;

public class GamePanel extends JPanel implements Runnable
{
    //Double buffering
    private Image dbImage;
    private Graphics dbg;
    //JPanel
    static final int GWIDTH = 1296, GHEIGHT = 648;//these numbers actually do include the border
    //this means there is a 3 pixel border around the x
    //and a 14 pixel border around the y
    //on top of that, there is a 3 pixel viewing border around all axes
    //so if you draw something at 0,0 it will be up and left 3 pixels from where your inside border starts
    
    static final Dimension gameDim = new Dimension(GWIDTH,GHEIGHT);
    static final double PIXEL_RATIO = GWIDTH/648;
    //Game variables
    private Thread gameThread;
    private volatile boolean running = false;
    //Game loop
    public long previousThreadTime;
    
    ArrayList<Entity> gameObjects;
    
    SoundManager soundManager;
    ImageManager imageManager;
    
    public double instantFPS;
    
    public static GamePanel panel;
    
    public GamePanel(JFrame frame)
    {
    	panel = this;
        setPreferredSize(gameDim);
        VisualVector.boundX = GWIDTH-9;
        VisualVector.boundY = GHEIGHT-31;
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
        
        gameObjects.add(new Cursor(frame, gameObjects));
        gameObjects.add(new GameField(GWIDTH, GHEIGHT));
        
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
            	if (e.getKeyCode() == e.VK_N)
            	{
            		int lastVisualVecIndex = -1;
            		for(int i = gameObjects.size()-1; i >= 0; i--)
                    {
                        if(gameObjects.get(i) instanceof VisualVector)
                        {
                        	lastVisualVecIndex = i;
                            break;
                        }
                    }
            		
            		if(lastVisualVecIndex != -1)
            		{
            			VisualVector v = (VisualVector)gameObjects.get(lastVisualVecIndex);
            			gameObjects.add(new VisualVector(v.xCoord2, v.yCoord2, v.xCoord2+(v.xCoord2-v.xCoord1), v.yCoord2+(v.yCoord2-v.yCoord1)));
            			((VisualVector)gameObjects.get(gameObjects.size()-1)).setTailingVector(v);
            			v.setNextVector((VisualVector)gameObjects.get(gameObjects.size()-1));
            		}
            		else
            		{
            			//gameObjects.add(new VisualVector(16, 16, 32, 32));
            		    gameObjects.add(new VisualVector(Cursor.position.x, Cursor.position.y, Cursor.position.x+32, Cursor.position.y));
            		    System.out.println(Cursor.position);
            		}
            	}
            	
            	if(e.getKeyCode() == e.VK_S)
            	{
            		PrintWriter writer = null;
            		File fileToSave = null;
            		
            		JFileChooser chooser = new JFileChooser();
            	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Save as a Text File", "txt");
            	    chooser.setFileFilter(filter);
            	    int returnVal = chooser.showOpenDialog(GamePanel.panel);
            	    if(returnVal == JFileChooser.APPROVE_OPTION) 
            	    {
            	       System.out.println("You chose to save this file: " + chooser.getSelectedFile().getName());
            	       fileToSave = chooser.getSelectedFile();
            	    }
            		
            	    if(fileToSave != null)
            	    {
						try 
						{
		            	    writer = new PrintWriter(fileToSave);
							int findIndex = 0;
							VisualVector vec = null;
							for(int i = 0; i < gameObjects.size(); i++)
							{
								if(gameObjects.get(i) instanceof VisualVector)
								{
									vec = (VisualVector)gameObjects.get(i);
									if(vec.tailingVector == null)
									{
										break;
									}
								}
							}
							
							if(vec != null)
							{
								writer.println("" + (vec.xCoord1/PIXEL_RATIO) + " " + (vec.yCoord1/PIXEL_RATIO) + " " + (vec.xCoord2/PIXEL_RATIO) + " " + (vec.yCoord2/PIXEL_RATIO));
							}
							int saftey = 0;
							while(saftey < 10000 && vec != null)
							{
								writer.println("" + vec.theta + " " + vec.length/PIXEL_RATIO);
								System.out.println("" + vec.theta + " " + vec.length/PIXEL_RATIO);
								vec = vec.nextVector;
								saftey++;
							}
		            		writer.close();
						} 
						catch (FileNotFoundException e1) 
						{
							e1.printStackTrace();
						} 
            	    }
            	}
            	
            	if(e.getKeyCode() == e.VK_O)
            	{
            		File file = null;
            		
            		JFileChooser chooser = new JFileChooser();
            	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            	    chooser.setFileFilter(filter);
            	    int returnVal = chooser.showOpenDialog(GamePanel.panel);
            	    if(returnVal == JFileChooser.APPROVE_OPTION) 
            	    {
            	       System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            	       file = chooser.getSelectedFile();
            	    }
            	    
            	    if(file != null)
            	    {
	            		try 
	            		{
							Scanner scan = new Scanner(file);
							for(int i = 0; i < gameObjects.size(); i++)
							{
								if(gameObjects.get(i) instanceof VisualVector)
								{
									gameObjects.remove(i);
									i--;
								}
							}
							
							//ArrayList<Integer[]> data = new ArrayList<Integer[]>();
							if(scan.hasNextLine())
							{
							    gameObjects.add(new VisualVector(scan.nextDouble()*PIXEL_RATIO, scan.nextDouble()*PIXEL_RATIO, scan.nextDouble()*PIXEL_RATIO, scan.nextDouble()*PIXEL_RATIO));
	                            scan.nextLine();
	                            scan.nextLine();
							}
							while(scan.hasNextLine())
							{
							    VisualVector prevvec = (VisualVector)gameObjects.get(gameObjects.size()-1);
							    double temptheta = scan.nextDouble();
							    double templength = scan.nextDouble()*PIXEL_RATIO;
								VisualVector tempvec = new VisualVector(prevvec.xCoord2, prevvec.yCoord2, prevvec.xCoord2+Math.cos(temptheta-Math.PI/2)*templength, prevvec.yCoord2-Math.sin(temptheta-Math.PI/2)*templength);
								gameObjects.add(tempvec);
								scan.nextLine();
							}
							scan.close();
							
							boolean foundFirstVV = false;
							for(int i = 0; i < gameObjects.size(); i++)
							{
								if(gameObjects.get(i) instanceof VisualVector)
								{  
									if(i < gameObjects.size()-1)
									{
										((VisualVector)gameObjects.get(i)).nextVector = ((VisualVector)gameObjects.get(i+1));
									}
									
									if(foundFirstVV == true)
									{
										((VisualVector)gameObjects.get(i)).tailingVector = ((VisualVector)gameObjects.get(i-1));
									}
									
									foundFirstVV = true;
								}
							}
						}
	            		catch (FileNotFoundException e1) 
	            		{
							e1.printStackTrace();
						}
            	    }
            		
            	}
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
                ((Cursor)(gameObjects.get(0))).isPressed = true;

            }
            @Override
            public void mouseReleased(MouseEvent e)
            {

                ((Cursor)(gameObjects.get(0))).isPressed = false;
            }
            @Override
            public void mouseClicked(MouseEvent e)
            {

            }
        });
        
        addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {

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
        //g.drawString(Double.toString(instantFPS), 100, 100);
        for(int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).draw(g);
        }
        gameObjects.get(0).draw(g);
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
