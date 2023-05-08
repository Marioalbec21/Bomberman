import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Jugador {
	
	//Dibujo del jugador
	private BufferedImage dibujo;
	private boolean visible = true;

	//Ubicación del jugador
    private int filaJugador;
    private int columnaJugador;
    
    public Jugador() {
    	try {
			dibujo = ImageIO.read(new File("resources/jugador.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public BufferedImage getDibujo() {
    	return dibujo;
    }
    
    public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getFilaJugador() {
    	return filaJugador;
    }
    
    public void setFilaJugador(int filaJugador) {
    	this.filaJugador = filaJugador;
    }
    
    public int getColumnaJugador() {
    	return columnaJugador;
    }
    
    public void setColumnaJugador(int columnaJugador) {
		this.columnaJugador = columnaJugador;
	}
}
