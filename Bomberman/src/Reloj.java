import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Reloj extends JPanel {

    private int segundos;
    private Timer timer;
    private JLabel tiempoLabel;
    private DecimalFormat formato = new DecimalFormat("00");
    private Font fuente = new Font("Arial", Font.BOLD, 24);

    public Reloj() {
        segundos = 0;
        tiempoLabel = new JLabel("00:00");
        tiempoLabel.setFont(fuente);
        tiempoLabel.setForeground(Color.BLACK);
        tiempoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tiempoLabel.setHorizontalAlignment(JLabel.CENTER);
        setBackground(new Color(128, 255, 255));
        setLayout(new BorderLayout());
        add(tiempoLabel, BorderLayout.CENTER);
    }

    public void iniciar() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                segundos++;
                int minutos = segundos / 60;
                int segundosRestantes = segundos % 60;
                tiempoLabel.setText(formato.format(minutos) + ":" + formato.format(segundosRestantes));
            }
        }, 1000, 1000);
    }

    public void detener() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void reiniciar(){
    	detener();
    	segundos = 0;
        tiempoLabel.setText("00:00");
        iniciar();
    }
}
