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
    
    public void playSoltar(float volume) {
        try {
            File archivoAudio = new File("Bomberman/resources/soltar.wav");
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(archivoAudio));

            // Obtener el control de ganancia y ajustar el volumen
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido de efecto: " + e.getMessage());
        }
    }
    
    public void playExplosion(float volume) {
        try {
            File archivoAudio = new File("Bomberman/resources/explosion.wav");
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(archivoAudio));

            // Obtener el control de ganancia y ajustar el volumen
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido de efecto: " + e.getMessage());
        }
    }
    
    public void playMuerte(float volume) {
        try {
            File archivoAudio = new File("Bomberman/resources/muerte.wav");
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(archivoAudio));

            // Obtener el control de ganancia y ajustar el volumen
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido de efecto: " + e.getMessage());
        }
    }
    
    public void detener() {
        // Detener la reproducción de la música y cerrar el Clip
        clip.stop();
        clip.close();
    }
}
