import java.awt.image.BufferedImage;

public class Bomba {
	
	//Dibujo de la bomba
	private BufferedImage dibujo;
	
    private int filaBomba;
    private int columnaBomba;
    private int tiempoExplosion;
    
    public Bomba(int filaBomba, int columnaBomba, int tiempoExplosion) {
        this.filaBomba = filaBomba;
        this.columnaBomba = columnaBomba;
        this.tiempoExplosion = tiempoExplosion;
    }
    
    public BufferedImage getDibujo() {
		return dibujo;
	}
    
    public int getFilaBomba() {
		return filaBomba;
	}

	public int getColumnaBomba() {
		return columnaBomba;
	}

	public int getTiempoExplosion() {
        return tiempoExplosion;
    }
}
