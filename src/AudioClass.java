import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioClass {
    public void AudioPlay(File audioFile) throws IOException {
        try {
            AudioInputStream audiostream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audiostream);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException AudioError) {
            throw new RuntimeException(AudioError);
        }

    }
}
