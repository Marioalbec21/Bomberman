import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Puntuacion extends JPanel {

    private JLabel puntuacionLabel;
    private Font fuente = new Font("Arial", Font.BOLD, 24);

    private int puntaje = 0;
    
    public Puntuacion() {
        puntuacionLabel = new JLabel("Score: " + puntaje);
        puntuacionLabel.setFont(fuente);
        puntuacionLabel.setForeground(Color.BLACK);
        puntuacionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        puntuacionLabel.setHorizontalAlignment(JLabel.CENTER);
        setBackground(new Color(128, 255, 255));
        setLayout(new BorderLayout());
        add(puntuacionLabel, BorderLayout.CENTER);
    }

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntuacion(int puntaje) {
		this.puntaje = puntaje;
        puntuacionLabel.setText("Score: " + puntaje);
	}
	
	public void reiniciarPuntuacion() {
        puntuacionLabel.setText("Score: " + 0);
	}
}
