package resources;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound 
{
    
    private static Sound staticSound = new Sound();
    
    public String name;
    public AudioClip sound;
    
    private Sound()
    {
        
    }
    
    public Sound(String name, URL url)
    {
        this.name = name;
        try
        {
            sound = Applet.newAudioClip(url);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
    
    public void play()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if(sound != null)
                {
                    sound.play();
                }
            }
        }).start();
    }
    
    public void loop()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if(sound != null)
                {
                    sound.loop();
                }
            }
        }).start();
    }
    
    public void stop()
    {
        if(sound != null)
        {
            sound.stop();
        }
    }
    
    public static URL getURL(String filename)
    {
        return staticSound.getClass().getResource("sounds/" + filename);
    }
}
