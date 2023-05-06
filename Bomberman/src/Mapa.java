import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;

public class Mapa extends JPanel {

    private static final long serialVersionUID = 1L;
    private int[][] mapa;
    private int filas = 0;
    private int columnas = 0;
    private BufferedImage imagen;
    
    private int[][] matrizSuelo;
  
    //Ubicación inicial del jugador
    private int inicioFilaJugador;
    private int inicioColumnaJugador;
    
    //Ubicación del jugador
    private int filaJugador;
    private int columnaJugador;
    
    private boolean mapaCompletado = false;
    
    public Mapa(int[][] mapa, BufferedImage imagen) {
        this.mapa = mapa;
        this.imagen = imagen;
        this.filas = mapa.length;
        this.columnas = mapa[0].length;

        setLayout(new GridLayout(filas, columnas));
        setVisible(true);
		setFocusable(true);
		
		generarSuelo();
		encontrarJugador();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoCelda = getWidth() / columnas;
        int altoCelda = getHeight() / filas;
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

            	//Dibuja el mapa
            	if(mapa[i][j] == -1) {
                    g.setColor(Color.decode("#23a647")); //Color suelo 1
            	}
            	else {
            		Color colorPixel = new Color(imagen.getRGB(j, i));
            		g.setColor(colorPixel);
            	}
                //Dibuja el suelo con el patrón de la matriz suelo
                if (mapa[i][j] == 0 && i < filas-1 && j < columnas-1) {
                    switch (matrizSuelo[i][j]) {
                        case 1:
                            g.setColor(Color.decode("#13bc42")); // Color suelo 1
                            break;
                        case 2:
                            g.setColor(Color.decode("#23a647")); // Color suelo 2
                            break;
                    }
                }
                g.fillRect(j * anchoCelda, i * altoCelda, anchoCelda, altoCelda);
                
                //Pinta al jugador en la nueva posición
                g.setColor(Color.decode("#ff3d80")); //Color jugador
                g.fillRect(columnaJugador * anchoCelda, filaJugador * altoCelda, anchoCelda, altoCelda);
            }
        }
    }
    
    public void encontrarJugador() {
    	//Busca la posición inicial del jugador
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j] == -1) {
                	filaJugador = i;
                	columnaJugador = j;
                    break;
                }
            }
        }
    	inicioFilaJugador = filaJugador;
    	inicioColumnaJugador = columnaJugador;
    }
    
    public void generarSuelo() {
    	matrizSuelo = new int[filas-1][columnas-1];
		
    	//Genera un patrón aleatorio para el suelo
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
            	if(mapa[i][j] == 0) {
            		Random random = new Random();
            		matrizSuelo[i][j] = random.nextInt(2) + 1;
            	}
            }
        }
    }
    public boolean eleccionSalida() {
  	    int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas jugar otro nivel?", "Nuevo nivel", JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
        	return true;
        } else {
        	return false;
        }
    }
    
	public void reiniciarJuego() {
        filaJugador = inicioFilaJugador;
        columnaJugador = inicioColumnaJugador;
        mapaCompletado = false;
        repaint();
        requestFocusInWindow();
        
		generarSuelo();
    }
    
    public void actualizarMapa(int[][] mapa) {
        this.mapa = mapa;
        this.filas = mapa.length;
        this.columnas = mapa[0].length;
        this.filaJugador = this.columnaJugador = 1;
        this.mapaCompletado = false;
        
		encontrarJugador();		
        this.repaint();
    }
    
	public int[][] getMapa() {
		return mapa;
	}
	
	public int getColumnas() {
		return columnas;
	}

	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public void setColumnaJugador(int columnaJugador) {
		this.columnaJugador = columnaJugador;
	}

	public void setFilaJugador(int filaJugador) {
		this.filaJugador = filaJugador;
	}

	public int getFilaJugador() {
		return filaJugador;
	}

	public int getColumnaJugador() {
		return columnaJugador;
	}

	public void setMapaCompletado(boolean mapaCompletado) {
		this.mapaCompletado = mapaCompletado;
	}

	public boolean isMapaCompletado() {
		return mapaCompletado;
	}
}