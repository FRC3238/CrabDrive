package turtleguidance;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.ImageManager;

public class Cursor extends Entity
{   
    public static Point position;
    public boolean previousPressed;
    public boolean isPressed;
    private JFrame myFrame;
    private ArrayList<Entity> gameObjects;
    private VisualVector latchedVector;
    private String latchedEnd = "default";
    private Image loadImage;
    private Image saveImage;
    
    
    public Cursor(JFrame frame, ArrayList<Entity> ar)
    {
        super();
        position = new Point();
        previousPressed = false;
        isPressed = false;
        myFrame = frame;
        gameObjects  = ar;
        latchedVector = null;
        loadImage = ImageManager.staticImageManager.getImage("sprIconOpen");
        saveImage = ImageManager.staticImageManager.getImage("sprIconSave");
    }
    
    public void step()
    {
        setPosition((double)MouseInfo.getPointerInfo().getLocation().x-myFrame.getLocationOnScreen().x-3, 
                    (double)MouseInfo.getPointerInfo().getLocation().y-myFrame.getLocationOnScreen().y-25);
        
        if(isPressed == true && previousPressed == false)
        {
            for(int i = 0; i < gameObjects.size(); i++)
            {
                if(gameObjects.get(i) instanceof VisualVector)
                {
                    VisualVector v = (VisualVector)gameObjects.get(i);
                    if(Math.sqrt(Math.pow(position.x - v.xCoord2, 2)+Math.pow(position.y - v.yCoord2, 2)) < 16)
                    {
                        latchedVector = v;
                        latchedVector.beingManipulated = true;
                        latchedEnd = "Front";
                        break;
                    }
                    
                    if(Math.sqrt(Math.pow(position.x - v.xCoord1, 2)+Math.pow(position.y - v.yCoord1, 2)) < 16)
                    {
                        latchedVector = v;
                        latchedVector.beingManipulated = true;
                        latchedEnd = "Back";
                        break;
                    }
                }
            }
        }
        
        if(isPressed == true)
        {
            if(latchedVector != null)
            {
            	if(latchedEnd.equals("Front"))
            	{
            		latchedVector.xCoord2 = position.x;
                	latchedVector.yCoord2 = position.y;
            	}
            	
            	if(latchedEnd.equals("Back"))
            	{
            		latchedVector.xCoord1 = position.x;
                	latchedVector.yCoord1 = position.y;
            	}
            }
        }
        
        if(isPressed == false && previousPressed == true)
        {
        	if(latchedVector!=null)
        	{
	        	for(int i = 0; i < gameObjects.size(); i++)
	            {
	                if(gameObjects.get(i) instanceof VisualVector)
	                {
	                    VisualVector v = (VisualVector)gameObjects.get(i);
	                    
	                    if(latchedEnd.equals("Front"))
	                    {
		                    if(Math.sqrt(Math.pow(position.x - v.xCoord1, 2)+Math.pow(position.y - v.yCoord1, 2)) < 16)
		                    {
		                    	if(latchedVector.nextVector == null && v.tailingVector == null)
		                    	{
			                        latchedVector.nextVector = v;
			                        v.tailingVector = latchedVector;
			                        break;
		                    	}
		                    }
	                    }
	                    
	                    if(latchedEnd.equals("Back"))
	                    {
	                    	if(Math.sqrt(Math.pow(position.x - v.xCoord2, 2)+Math.pow(position.y - v.yCoord2, 2)) < 16)
		                    {
	                    		if(latchedVector.tailingVector == null && v.nextVector == null)
		                    	{
	                    			latchedVector.tailingVector = v;
		                        	v.nextVector = latchedVector;
		                        	break;
		                    	}
		                    }
	                    }
	                }
	            }
	        	latchedVector.beingManipulated = false;
	            latchedVector = null;
        	}
        	
        	if(position.y >= 0 && position.y < 32)
        	{
        		if(position.x >= 0 && position.x < 32)
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
								writer.println("" + (vec.xCoord1/GamePanel.PIXEL_RATIO) + " " + (vec.yCoord1/GamePanel.PIXEL_RATIO) + " " + (vec.xCoord2/GamePanel.PIXEL_RATIO) + " " + (vec.yCoord2/GamePanel.PIXEL_RATIO));
							}
							int saftey = 0;
							while(saftey < 10000 && vec != null)
							{
								writer.println("" + vec.theta + " " + vec.length/GamePanel.PIXEL_RATIO);
								System.out.println("" + vec.theta + " " + vec.length/GamePanel.PIXEL_RATIO);
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
        		if(position.x >= 32 && position.x < 64)
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
							    gameObjects.add(new VisualVector(scan.nextDouble()*GamePanel.PIXEL_RATIO, scan.nextDouble()*GamePanel.PIXEL_RATIO, scan.nextDouble()*GamePanel.PIXEL_RATIO, scan.nextDouble()*GamePanel.PIXEL_RATIO));
	                            scan.nextLine();
	                            scan.nextLine();
							}
							while(scan.hasNextLine())
							{
							    VisualVector prevvec = (VisualVector)gameObjects.get(gameObjects.size()-1);
							    double temptheta = scan.nextDouble();
							    double templength = scan.nextDouble()*GamePanel.PIXEL_RATIO;
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
        		
        }
    }
    
    public void setPosition(double newX, double newY)
    {
        if((Double.valueOf(newX) != null) && (Double.valueOf(newY) != null))
        {
            position.setLocation(newX, newY);
        }
    }
    
    public void setPosition(Point p)
    {
        if(p != null)
        {
            position.setLocation(p.x, p.y);
        }
    }
    
    public void draw(Graphics g)
    {
        //g.drawOval(this.position.x-16, this.position.y-16, 32, 32);

        if(isPressed!=previousPressed)
        {
            //g.fillOval(0, 0, 32, 32);
        }
        g.drawImage(saveImage, 0, 0, 32, 32, null);
        g.drawImage(loadImage, 32, 0, 32, 32, null);
        //g.drawImage(loadImage, 64, 64, null);
        
        //g.drawString(position.toString(), (int)position.x, (int)position.y);
        
        //g.drawString("     isPressed = "+isPressed+"    previousPressed = "+previousPressed, 16, 16);
        previousPressed = isPressed;
    }
}
