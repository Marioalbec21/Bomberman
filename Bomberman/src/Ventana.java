import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class Ventana extends JFrame {
	
	private JPanel contentPane;
	
	public Ventana() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 403, 461);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(128, 255, 255));
		contentPane.add(panel_1, BorderLayout.WEST);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(128, 255, 255));
		contentPane.add(panel_2, BorderLayout.EAST);
		
		Reloj reloj = new Reloj();
		reloj.setBackground(new Color(128, 255, 255));
		contentPane.add(reloj, BorderLayout.NORTH);
		
		//Mapas
		Carga nivel1 = new Carga("resources/nivel1.png");

		reloj.iniciar();
		Mapa mapa = new Mapa(nivel1.getMatriz());
		contentPane.add(mapa, BorderLayout.CENTER);
		
		//Metodos teclado
        mapa.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			    char tecla = e.getKeyChar();
			    if (!mapa.isMapaCompletado()) {

				    //Mueve al jugador 1 casilla a la izquierda
				    if (tecla == 'a') {
				        if (mapa.getJugador().getColumnaJugador() > 0 && 
				        	mapa.getMapa()[mapa.getJugador().getFilaJugador()][mapa.getJugador().getColumnaJugador() - 1] != 1) {
				        	mapa.getJugador().setColumnaJugador(mapa.getJugador().getColumnaJugador()-1);
				            repaint();
				        }
				    }
				    //Mueve al jugador 1 casilla abajo
				    if (tecla == 's') {
				        if (mapa.getJugador().getFilaJugador() < mapa.getFilas() - 1 && 
				        	mapa.getMapa()[mapa.getJugador().getFilaJugador() + 1][mapa.getJugador().getColumnaJugador()] != 1) {
				        	mapa.getJugador().setFilaJugador(mapa.getJugador().getFilaJugador()+1);
				            repaint();
				        }
				    }
				    //Mueve al jugador 1 casilla arriba
				    if (tecla == 'w') {
				    	if (mapa.getJugador().getFilaJugador() > 0 && 
				    		mapa.getMapa()[mapa.getJugador().getFilaJugador() - 1][mapa.getJugador().getColumnaJugador()] != 1) {
				        	mapa.getJugador().setFilaJugador(mapa.getJugador().getFilaJugador()-1);
				    		repaint();
				    	}
				    }
				    //Mueve al jugador 1 casilla a la derecha
				    if (tecla == 'd') {
				        if (mapa.getJugador().getColumnaJugador() < mapa.getColumnas() - 1 && 
				        	mapa.getMapa()[mapa.getJugador().getFilaJugador()][mapa.getJugador().getColumnaJugador() + 1] != 1) {
				        	mapa.getJugador().setColumnaJugador(mapa.getJugador().getColumnaJugador()+1);
				            repaint();
				        }
			        }
			    }
			    else {
			    	reloj.detener();
		    	    JOptionPane.showMessageDialog(null, "¡Felicidades, has ganado!");

		        	if(mapa.eleccionSalida()) {	
		        		reloj.reiniciar();
		        		//juego.actualizarMapa(nivel2.getMatriz());
		        	}
			    }
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		requestFocus(true);
		JPanel opciones = new JPanel();
		opciones.setBackground(new Color(128, 255, 128));
		contentPane.add(opciones, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Reiniciar");
		btnNewButton.setFont(new Font("Brock", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        mapa.reiniciarJuego();
		        reloj.reiniciar();
			}
		});
		opciones.add(btnNewButton);
	}
}