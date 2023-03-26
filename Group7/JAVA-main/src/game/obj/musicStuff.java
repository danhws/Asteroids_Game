package game.obj;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class musicStuff{
    public void playMusic(String musicLocation){
        try
        {
            // if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(musicLocation).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(clip.LOOP_CONTINUOUSLY);


            // }else{
            //     System.out.println("Can't find music file");
            // }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();

        }
    }

}
