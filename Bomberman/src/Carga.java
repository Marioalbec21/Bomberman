import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Carga{
	
	private int[][] matriz;
	private BufferedImage imagen;
	
	public Carga(String ruta) throws IOException {		

		imagen = ImageIO.read(new File(ruta));
		
		int ancho = imagen.getWidth();
		int alto = imagen.getHeight();
		
		matriz = new int[alto][ancho];
		
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				Color colorPixel = new Color(imagen.getRGB(j, i));
				if (colorPixel.equals(Color.WHITE)) {
					matriz[i][j] = 0;
				} 
				//Color paredes
				if (colorPixel.equals(Color.BLACK) || colorPixel.equals(new Color(88,88,88))) {
					matriz[i][j] = 1;
				}
				//Color del jugador
				if (colorPixel.equals(Color.BLUE)) {
					matriz[i][j] = -1;
				}
			}
		}
	}

	public int[][] getMatriz() {
		return matriz;
	}

	public BufferedImage getImagen() {
		return imagen;
	}
}
