import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

public class Mapa extends JPanel {

	private Timer timerEnemigos;
	private Timer timerColision;
;
    private static final long serialVersionUID = 1L;
    private int[][] mapa;
    private int filas = 0;
    private int columnas = 0;
    
    private int[][] matrizSuelo;
    private int[][] matrizRompibles;
    
    //Iniciar recursos
    private Jugador jugador = new Jugador();
    private List<Enemigo> enemigos = new ArrayList<>();
    
    private Carga pasto1 = new Carga("resources/pasto1.png");;
    private Carga pasto2 = new Carga("resources/pasto2.png");;
    private Carga paredes = new Carga("resources/pared1.png");
    private Carga paredesRompibles = new Carga("resources/pared2.png");;
    
    private boolean mapaCompletado = false;
    		
    //Variables
    private float probabilidadParedesRompibles = 0f; //Probabilidad de generar una pared rompible (entre 0 y 1)
    private int cantidadEnemigos = 3; //Cantidad de enemigos en el mapa
    private int intervaloMovimientoEnemigos = 500; //Velocidad de enemigos (mientras mas bajo, mas rapidos serán)

    public Mapa(int[][] mapa) {
        this.mapa = mapa;
        this.filas = mapa.length;
        this.columnas = mapa[0].length;

        setLayout(new GridLayout(filas, columnas));
        setVisible(true);
		setFocusable(true);
		
		//Generacion del mapa
		generarSuelo();
		generarParedesRompibles();
		generarJugador();
		generarEnemigos();
		iniciarTimerEnemigos();
		iniciarTimerColision();
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int anchoCelda = getWidth() / columnas;
        int altoCelda = getHeight() / filas;
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                //Dibuja el suelo con el patrón de la matriz suelo
                if (mapa[i][j] == 0) {
                    switch (matrizSuelo[i][j]) {
                        case 1:
                        	//Color suelo 1
	                		g.drawImage(pasto1.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                            break;
                        case 2:
                        	//Color suelo 2
	                		g.drawImage(pasto2.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                            break;
                    }
                }
                //Dibuja las paredes no rompibles
                if (mapa[i][j] == 1 && matrizRompibles[i][j] == 0) {
                	switch (mapa[i][j]) {
	                	case 1:
	                		g.drawImage(paredes.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
	                		break;
                	}
                }
                //Dibuja las paredes rompibles
                if (mapa[i][j] == 1 && matrizRompibles[i][j] == 1) {
                    switch (matrizRompibles[i][j]) {
                        case 1:
                        	g.drawImage(paredesRompibles.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                            break;
                    }
                }
                
                if (jugador.isVisible()) {
                	//Dibuja la imagen en la posición del jugador
                	g.drawImage(jugador.getDibujo(), jugador.getColumnaJugador() * anchoCelda, jugador.getFilaJugador() * altoCelda, anchoCelda, altoCelda, null);
                }
               
                //Dibuja la imagen de cada enemigo en su posición correspondiente
                for (Enemigo enemigo : enemigos) {
                	g.drawImage(enemigo.getDibujo(), enemigo.getColumnaEnemigo() * anchoCelda, enemigo.getFilaEnemigo() * altoCelda, anchoCelda, altoCelda, null);
                } 
            }
        }
    }
    
    private void iniciarTimerEnemigos() {
        timerEnemigos = new Timer(intervaloMovimientoEnemigos, e -> {
            moverEnemigos();
        });
        timerEnemigos.start();
    }
    
    private void iniciarTimerColision() {
        timerColision = new Timer(100, e -> {
        	if(detectarColisionJugador()) {
            	if(eleccionSalida()) {
            		reiniciarJuego();
            		jugador.setVisible(true);
            	}
            	else {
            		System.exit(0);
            	}                	
            }
        });
        timerColision.start();
    }
    
    public void generarSuelo() {
    	matrizSuelo = new int[filas][columnas];
    	
    	//Genera un patrón aleatorio para el suelo
    	for (int i = 0; i < filas; i++) {
    		for (int j = 0; j < columnas; j++) {
    			if (mapa[i][j] == 0) {
    				Random random = new Random();
    				matrizSuelo[i][j] = random.nextInt(2) + 1;
    			}
    		}
    	}
    }
    
    public void generarParedesRompibles() {
    	matrizRompibles = new int[filas][columnas];

    	//Genera las paredes rompibles
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if(mapa[i][j] == 0) {
                    Random random = new Random();   
                    
                    if (random.nextFloat() <= probabilidadParedesRompibles) {
                        matrizRompibles[i][j] = 1;
                    	mapa[i][j] = 1;
                    }
                }
            }
        }
    }
    
    public void generarJugador() {
        Random random = new Random();
        
        while (true) {
            jugador.setFilaJugador(random.nextInt(filas));
            jugador.setColumnaJugador(random.nextInt(columnas));
            
            if (mapa[jugador.getFilaJugador()][jugador.getColumnaJugador()] == 0 
            		&& matrizRompibles[jugador.getFilaJugador()][jugador.getColumnaJugador()] == 0) {
                break;
            }
        }
    }
    
    public void generarEnemigos() {
        Random random = new Random();
        enemigos.clear(); //Limpia la lista de enemigos

        for (int i = 0; i < cantidadEnemigos; i++) {
            Enemigo enemigo = new Enemigo();

            while (true) {
                int fila = random.nextInt(filas);
                int columna = random.nextInt(columnas);

                if (mapa[fila][columna] == 0 && matrizRompibles[fila][columna] == 0 &&
                    !(fila == jugador.getFilaJugador() && columna == jugador.getColumnaJugador())) {
                    enemigo.setFilaEnemigo(fila);
                    enemigo.setColumnaEnemigo(columna);
                    break;
                }
            }

            enemigos.add(enemigo); //Agrega el enemigo generado a la lista de enemigos
        }
    }

    public void moverEnemigos() {
        Random random = new Random();

        for (Enemigo enemigo : enemigos) {
            int filaActual = enemigo.getFilaEnemigo();
            int columnaActual = enemigo.getColumnaEnemigo();

            int direccion = random.nextInt(4); //0: arriba, 1: abajo, 2: izquierda, 3: derecha

            switch (direccion) {
                case 0: //Arriba
                    if (filaActual > 0 && mapa[filaActual - 1][columnaActual] == 0 && 
                    	matrizRompibles[filaActual - 1][columnaActual] == 0) {
                        enemigo.setFilaEnemigo(filaActual - 1);
                    }
                    break;
                case 1: //Abajo
                    if (filaActual < filas - 1 && mapa[filaActual + 1][columnaActual] == 0 && 
                    	matrizRompibles[filaActual + 1][columnaActual] == 0) {
                        enemigo.setFilaEnemigo(filaActual + 1);
                    }
                    break;
                case 2: //Izquierda
                    if (columnaActual > 0 && mapa[filaActual][columnaActual - 1] == 0 && 
                    	matrizRompibles[filaActual][columnaActual - 1] == 0) {
                        enemigo.setColumnaEnemigo(columnaActual - 1);
                    }
                    break;
                case 3: //Derecha
                    if (columnaActual < columnas - 1 && mapa[filaActual][columnaActual + 1] == 0 && 
                    	matrizRompibles[filaActual][columnaActual + 1] == 0) {
                        enemigo.setColumnaEnemigo(columnaActual + 1);
                    }
                    break;
            }
        }
        repaint();
    }

    public boolean detectarColisionJugador() {
    	boolean colision = false;
        if (jugador.isVisible()) {
	        for (Enemigo enemigo : enemigos) {
	            if (jugador.getFilaJugador() == enemigo.getFilaEnemigo() && 
	            		jugador.getColumnaJugador() == enemigo.getColumnaEnemigo()) {
	            	
	            	jugador.setVisible(false);
	            	colision = true;
	            	JOptionPane.showMessageDialog(null, "¡Game Over!");
	            }
	        }
        }
        return colision;
    }

    public void limpiarMapa() {
    	
    	for (int i = 0; i < filas; i++) {
    		for (int j = 0; j < columnas; j++) {
    			if (mapa[i][j] == 1 && matrizRompibles[i][j] == 1) {
    				mapa[i][j] = 0;
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
        mapaCompletado = false;
        requestFocusInWindow();
        repaint();
        
        limpiarMapa();
		generarSuelo();
		generarParedesRompibles();
        generarJugador();
		generarEnemigos();
    }
    
    public void actualizarMapa(int[][] mapa) {
        this.mapa = mapa;
        this.filas = mapa.length;
        this.columnas = mapa[0].length;
        this.mapaCompletado = false;
        
        this.repaint();
    }
    
	public int[][] getMapa() {
		return mapa;
	}
	
	public int getFilas() {
		return filas;
	}
	
	public void setFilas(int filas) {
		this.filas = filas;
	}
	
	public int getColumnas() {
		return columnas;
	}

	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setMapaCompletado(boolean mapaCompletado) {
		this.mapaCompletado = mapaCompletado;
	}

	public boolean isMapaCompletado() {
		return mapaCompletado;
	}
}