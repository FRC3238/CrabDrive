package resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ResourceLoader 
{
    static ResourceLoader rl = new ResourceLoader();
    
    public static Image getImage(String filename)
    {
        String orig = rl.getClass().getResource("images/" + filename).toString();
        String newfilepath = orig.substring(6, orig.length());
        Image dummy = null;
        
        try 
        {
            dummy = ImageIO.read(new File(newfilepath));
        } 
        catch (IOException e) 
        {
            System.out.println("Error: could not file image " + newfilepath);
        }
        return dummy;
    }
}
