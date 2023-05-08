import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemigo {
	
	//Dibujo del enemigo
	private BufferedImage dibujo;

    //Ubicación del enemigo
    private int filaEnemigo;
    private int columnaEnemigo;
    
    public Enemigo() {
    	try {
			dibujo = ImageIO.read(new File("resources/enemigo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public BufferedImage getDibujo() {
    	return dibujo;
    }
    
    public int getFilaEnemigo() {
    	return filaEnemigo;
    }

    public void setFilaEnemigo(int filaEnemigo) {
    	this.filaEnemigo = filaEnemigo;
    }
    
    public int getColumnaEnemigo() {
    	return columnaEnemigo;

    }
    public void setColumnaEnemigo(int columnaEnemigo) {
		this.columnaEnemigo = columnaEnemigo;
	}
}