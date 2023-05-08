import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

public class Mapa extends JPanel {

	private Timer timerEnemigos;
	private Timer timerBombas;

	private Musica fondo = new Musica();
	private Musica soltar = new Musica();
	private Musica explosion = new Musica();
	private Musica muerte = new Musica();

    private static final long serialVersionUID = 1L;
    private int[][] mapa;
    private int filas = 0;
    private int columnas = 0;
    
    private int[][] matrizSuelo;
    private int[][] matrizRompibles;
    private int[][] matrizBombas;

    //Iniciar recursos
    private Jugador jugador = new Jugador();
    private List<Enemigo> enemigos = new ArrayList<>();
    
    private Carga pasto1 = new Carga("Bomberman/resources/pasto1.png");;
    private Carga pasto2 = new Carga("Bomberman/resources/pasto2.png");;
    private Carga paredes = new Carga("Bomberman/resources/pared1.png");
    private Carga paredesRompibles = new Carga("Bomberman/resources/pared2.png");;
    private Carga bomba = new Carga("Bomberman/resources/bomba.png");;
    private Carga fuego = new Carga("Bomberman/resources/fuego.png");;

    private boolean mapaCompletado = false;
    		
    //Variables
    private float rompibles = 0f;
    private int cantidadEnemigos = 0;
    private float movimientoEnemigo = 0f;
    private float tiempoFuegoBombas = 0f;
    private float volumenGeneral = 0f;
    private float volumenEfectos = 0f;

    public Mapa(int[][] mapa, float rompibles, int cantidadEnemigos, 
    		float movimientoEnemigo, float tiempoFuegoBombas, float volumenGeneral, float volumenEfectos) {
    	        
        this.mapa = mapa;
        this.filas = mapa.length;
        this.columnas = mapa[0].length;
        this.rompibles = rompibles;
        this.cantidadEnemigos = cantidadEnemigos;
        this.movimientoEnemigo = movimientoEnemigo;
        this.tiempoFuegoBombas = tiempoFuegoBombas;
        this.volumenGeneral = volumenGeneral;
        this.volumenEfectos = volumenEfectos;
        
        setLayout(new GridLayout(filas, columnas));
        setVisible(true);
		setFocusable(true);
		
		//Generacion del mapa
		fondo.play(volumenGeneral);
		generarSuelo();
		generarParedesRompibles();
		generarJugador();
		generarEnemigos();
		iniciarTimerEnemigos();
		iniciarTimerBombas();
		
	    matrizBombas = new int[filas][columnas];
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
            		g.drawImage(paredes.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                }
                //Dibuja las paredes rompibles
                if (mapa[i][j] == 1 && matrizRompibles[i][j] == 1) {
                	g.drawImage(paredesRompibles.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                }
                //Dibuja las bombas
                if (mapa[i][j] == 0 && matrizBombas[i][j] == 1) {
                    g.drawImage(bomba.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                } 
                //Dibuja la imagen en la posición del jugador
                if (jugador.isVisible()) {
                	g.drawImage(jugador.getDibujo(), jugador.getColumnaJugador() * anchoCelda, jugador.getFilaJugador() * altoCelda, anchoCelda, altoCelda, null);
                }
                //Dibuja la imagen de cada enemigo en su posición correspondiente
                for (Enemigo enemigo : enemigos) {
                	g.drawImage(enemigo.getDibujo(), enemigo.getColumnaEnemigo() * anchoCelda, enemigo.getFilaEnemigo() * altoCelda, anchoCelda, altoCelda, null);
                } 
                //Dibuja el fuego de las bombas
                if (mapa[i][j] == 0 && matrizBombas[i][j] == 2) {
                	g.drawImage(fuego.getDibujo(), j * anchoCelda, i * altoCelda, anchoCelda, altoCelda, null);
                }
               
            }
        }
    }
    
    private void iniciarTimerEnemigos() {
        timerEnemigos = new Timer(Math.round(movimientoEnemigo * 1000 / 10), e -> {
            moverEnemigos();
        });
        timerEnemigos.start();
    }
    
    private void iniciarTimerBombas() {
        timerBombas = new Timer(Math.round(tiempoFuegoBombas * 1000), e -> {
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    if (matrizBombas[i][j] == 2) {
                        matrizBombas[i][j] = 0;
                        
                    }
                }
            }
            repaint();
        });
        timerBombas.start();
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
                    
                    if (random.nextFloat() <= rompibles) {
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
            	
            	// Verificar que las posiciones adyacentes estén libres de paredes
                if (jugador.getFilaJugador() > 0 && matrizRompibles[jugador.getFilaJugador() - 1][jugador.getColumnaJugador()] == 1) {
                	mapa[jugador.getFilaJugador() - 1][jugador.getColumnaJugador()] = 0; //Borrar pared
                }
                if (jugador.getFilaJugador() < filas - 1 && matrizRompibles[jugador.getFilaJugador() + 1][jugador.getColumnaJugador()] == 1) {
                	mapa[jugador.getFilaJugador() + 1][jugador.getColumnaJugador()] = 0; //Borrar pared
                }
                if (jugador.getColumnaJugador() > 0 && matrizRompibles[jugador.getFilaJugador()][jugador.getColumnaJugador() - 1] == 1) {
                	mapa[jugador.getFilaJugador()][jugador.getColumnaJugador() - 1] = 0; //Borrar pared
                }
                if (jugador.getColumnaJugador() < columnas - 1 && matrizRompibles[jugador.getFilaJugador()][jugador.getColumnaJugador() + 1] == 1) {
                	mapa[jugador.getFilaJugador()][jugador.getColumnaJugador() + 1] = 0; //Borrar pared
                }

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

                //Valida que la posición generada no esté ocupada por el jugador
                if (mapa[fila][columna] == 0 && matrizRompibles[fila][columna] == 0 &&
                    !(fila == jugador.getFilaJugador() && columna == jugador.getColumnaJugador())) {
                   
                	//Borra las paredes rompibles a los lados de los enemigos
                	if (fila > 0 && matrizRompibles[fila - 1][columna] == 1) {
                		mapa[fila - 1][columna] = 0;
                    }
                    if (fila < filas - 1 && matrizRompibles[fila + 1][columna] == 1) {
                    	mapa[fila + 1][columna] = 0;
                    }
                    if (columna > 0 && matrizRompibles[fila][columna - 1] == 1) {
                    	mapa[fila][columna - 1] = 0;
                    }
                    if (columna < columnas - 1 && matrizRompibles[fila][columna + 1] == 1) {
                        mapa[fila][columna + 1] = 0;
                    }

                    enemigo.setFilaEnemigo(fila);
                    enemigo.setColumnaEnemigo(columna);
                    break;
                }
            }

            enemigos.add(enemigo); //Agrega el enemigo generado a la lista de enemigos
        }
    }

    public void agregarBomba(Bomba bomba) {
        int fila = bomba.getFilaBomba();
        int columna = bomba.getColumnaBomba();
        soltar.playSoltar(volumenEfectos);
        
        //Verificar si la posición es válida en el mapa
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            //Verificar si no hay otra bomba en la misma posición
            if (!hayBombaEnPosicion(fila, columna)) {
                //Agregar la bomba al mapa
            	matrizBombas[fila][columna] = 1;
            }
        }
        //Inicia el temporizador para eliminar la bomba después de 3 segundos
        Timer timer = new Timer(bomba.getTiempoExplosion()*1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarBomba(bomba);
                explotarBomba(bomba);
                explosion.playExplosion(volumenEfectos);
            }
        });
        timer.setRepeats(false); //Solo se ejecuta una vez
        timer.start();
    }

    private void eliminarBomba(Bomba bomba) {
        int fila = bomba.getFilaBomba();
        int columna = bomba.getColumnaBomba();
        
        //Verificar si la posición de la bomba aún es válida
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            //Verifica si la bomba todavía está en esa posición
            if (matrizBombas[fila][columna] == 1) {
            	matrizBombas[fila][columna] = 0; //Elimina la bomba del mapa
                repaint();
            }
        }
    }
    
    public void explotarBomba(Bomba bomba) {
        int fila = bomba.getFilaBomba();
        int columna = bomba.getColumnaBomba();

        for (int i = fila; i >= Math.max(fila - 3, 0); i--) {
            if (matrizRompibles[i][columna] == 1) {
                mapa[i][columna] = 0;
            } else if (mapa[i][columna] == 1) {
                break;
            }
            matrizBombas[i][columna] = 2;
        }

        for (int i = fila; i <= Math.min(fila + 3, filas - 1); i++) {
            if (matrizRompibles[i][columna] == 1) {
                mapa[i][columna] = 0;
            } else if (mapa[i][columna] == 1) {
                break;
            }
            matrizBombas[i][columna] = 2;
        }

        for (int j = columna; j >= Math.max(columna - 3, 0); j--) {
            if (matrizRompibles[fila][j] == 1) {
                mapa[fila][j] = 0;
            } else if (mapa[fila][j] == 1) {
                break;
            }
            matrizBombas[fila][j] = 2;
        }

        for (int j = columna; j <= Math.min(columna + 3, columnas - 1); j++) {
            if (matrizRompibles[fila][j] == 1) {
                mapa[fila][j] = 0;
            } else if (mapa[fila][j] == 1) {
                break;
            }
            matrizBombas[fila][j] = 2;
        }
        
        //Elimina enemigos en la posición de la explosión
        Iterator<Enemigo> iterator = enemigos.iterator();
        while (iterator.hasNext()) {
            Enemigo enemigo = iterator.next();
            int enemigoFila = enemigo.getFilaEnemigo();
            int enemigoColumna = enemigo.getColumnaEnemigo();

            //Verifica si el enemigo se encuentra en la cruz de la explosión o en el fuego
            if ((enemigoFila == fila && enemigoColumna >= columna - 1 && enemigoColumna <= columna + 1) ||
                (enemigoColumna == columna && enemigoFila >= fila - 1 && enemigoFila <= fila + 1) ||
                matrizBombas[enemigoFila][enemigoColumna] == 2) {
                iterator.remove();
            }
        }
    }

    private boolean hayBombaEnPosicion(int fila, int columna) {
        return mapa[fila][columna] == 1;
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
                    matrizRompibles[filaActual - 1][columnaActual] == 0 &&
                    matrizBombas[filaActual - 1][columnaActual] == 0 &&
                    !hayEnemigoEnPosicion(filaActual - 1, columnaActual)) {
                    enemigo.setFilaEnemigo(filaActual - 1);
                }
                break;
            case 1: //Abajo
                if (filaActual < filas - 1 && mapa[filaActual + 1][columnaActual] == 0 &&
                    matrizRompibles[filaActual + 1][columnaActual] == 0 &&
                    matrizBombas[filaActual + 1][columnaActual] == 0 &&
                    !hayEnemigoEnPosicion(filaActual + 1, columnaActual)) {
                    enemigo.setFilaEnemigo(filaActual + 1);
                }
                break;
            case 2: //Izquierda
                if (columnaActual > 0 && mapa[filaActual][columnaActual - 1] == 0 &&
                    matrizRompibles[filaActual][columnaActual - 1] == 0 &&
                    matrizBombas[filaActual][columnaActual - 1] == 0 &&
                    !hayEnemigoEnPosicion(filaActual, columnaActual - 1)) {
                    enemigo.setColumnaEnemigo(columnaActual - 1);
                }
                break;
            case 3: //Derecha
                if (columnaActual < columnas - 1 && mapa[filaActual][columnaActual + 1] == 0 &&
                    matrizRompibles[filaActual][columnaActual + 1] == 0 &&
                    matrizBombas[filaActual][columnaActual + 1] == 0 &&
                    !hayEnemigoEnPosicion(filaActual, columnaActual + 1)) {
                    enemigo.setColumnaEnemigo(columnaActual + 1);
                }
                break;
            }
        }
        repaint();
    }

    private boolean hayEnemigoEnPosicion(int fila, int columna) {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.getFilaEnemigo() == fila && enemigo.getColumnaEnemigo() == columna) {
                return true;
            }
        }
        return false;
    }

    public boolean detectarColisionJugador() {
    	boolean colision = false;
        if (jugador.isVisible()) {
	        for (Enemigo enemigo : enemigos) {
	            if (jugador.getFilaJugador() == enemigo.getFilaEnemigo() && 
	            		jugador.getColumnaJugador() == enemigo.getColumnaEnemigo()) {
	            	
	            	colision = true;
	            	muerte.playMuerte(volumenEfectos);
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
    			if (mapa[i][j] == 0 && matrizBombas[i][j] == 1) {
    				matrizBombas[i][j] = 0;
    			}
    		}
    	}
    }
    
    public boolean eleccionSalida() {
    	if(!jugador.isVisible()) {
    		fondo.detener();
    		muerte.playMuerte(volumenEfectos);
        	JOptionPane.showMessageDialog(null, "¡Game Over!");
    	} else {
    	    JOptionPane.showMessageDialog(null, "¡Felicidades, has ganado!");
    	}
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
        
        fondo.detener();
        fondo.play(volumenGeneral);
        limpiarMapa();
		generarSuelo();
		generarParedesRompibles();
        generarJugador();
    	jugador.setVisible(true);
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
	
	public int[][] getMatrizBombas() {
		return matrizBombas;
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
	
	public List<Enemigo> getEnemigos() {
		return enemigos;
	}

	public void setMapaCompletado(boolean mapaCompletado) {
		this.mapaCompletado = mapaCompletado;
	}

	public boolean isMapaCompletado() {
		return mapaCompletado;
	}

	public void setRompibles(float rompibles) {
		this.rompibles = rompibles;
	}

	public void setCantidadEnemigos(int cantidadEnemigos) {
		this.cantidadEnemigos = cantidadEnemigos;
	}

	public void setMovimientoEnemigo(float movimientoEnemigo) {
		this.movimientoEnemigo = movimientoEnemigo;
	}

	public void setTiempoFuegoBombas(float tiempoFuegoBombas) {
		this.tiempoFuegoBombas = tiempoFuegoBombas;
	}

	public void setVolumenGeneral(float volumenGeneral) {
		this.volumenGeneral = volumenGeneral;
	}
	
	public void setVolumenEfectos(float volumenEfectos) {
		this.volumenGeneral = volumenEfectos;
	}
}