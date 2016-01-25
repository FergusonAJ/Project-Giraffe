package framework;

/**
 *
 * @author Andrew Polanco
 */

import java.io.BufferedInputStream;
import java.io.InputStream;
import javazoom.jl.player.Player;

//public class Audio 
//{
//    private String filename;
//    private Player player;
//    
//    public Audio(String filename)
//    {
//        this.filename = filename;
//    }
//
////public void close()
////{
////    if(player != null)
////        player.close();
////}
//   
//public void play()
//{
//    try
//    {
//        InputStream fis = this.getClass().getResourceAsStream(filename);
//        BufferedInputStream bis = new BufferedInputStream(fis);
//        player = new Player(bis);
//        
//    }
//    catch (Exception e)
//    {
//        System.out.println("Problem playing file " + filename);
//        System.out.println(e);
//    }
//    
//    new Thread()
//    {
//        public void run()
//        {
//            try
//            { 
//                player.play(); 
//            }
//            catch(Exception e)
//            {
//                System.out.println(e);
//            } 
//        }
//    } .start();
//}
//    
//}

//
//import javazoom.jl.player.advanced.*;
//
//public class Audio extends PlaybackListener implements Runnable
//{
//    private String filePath;
//    private AdvancedPlayer player;
//    private Thread playerThread;    
//
//    public Audio (String filePath)
//    {
//        this.filePath = filePath;
//    }
//
//    public void play()
//    {
//        try
//        {
//            String urlAsString = 
//                "file:///" 
//                + new java.io.File(".").getCanonicalPath() 
//                + "/" 
//                + this.filePath;
//
//            this.player = new AdvancedPlayer
//            (
//                new java.net.URL(urlAsString).openStream(),
//                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice()
//            );
//
//            this.player.setPlayBackListener(this);
//
//            this.playerThread = new Thread(this, "AudioPlayerThread");
//
//            this.playerThread.start();
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//
//    // PlaybackListener members
//
//    public void playbackStarted(PlaybackEvent playbackEvent)
//    {
//        System.out.println("playbackStarted()");
//    }
//
//    public void playbackFinished(PlaybackEvent playbackEvent)
//    {
//        System.out.println("playbackEnded()");
//    }    
//
//    // Runnable members
//
//    public void run()
//    {
//        try
//        {
//            this.player.play();
//        }
//        catch (javazoom.jl.decoder.JavaLayerException ex)
//        {
//            ex.printStackTrace();
//        }
//
//    }
//}

