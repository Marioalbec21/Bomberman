import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Musica {
    private Clip clip;

    public void play(float volume) {
        try {
            File archivoAudio = new File("Bomberman/resources/fondoMusica.wav");
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(archivoAudio));

            // Obtener el control de ganancia y ajustar el volumen
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Error al reproducir la música de fondo: " + e.getMessage());
        }
    }
}