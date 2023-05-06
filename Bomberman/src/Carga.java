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
				//Asigna valores a la matriz dependiendo el color
				Color colorPixel = new Color(imagen.getRGB(j, i));
				
				//Suelo
				if (colorPixel.equals(Color.WHITE)) {
					matriz[i][j] = 0;
				} 
				//Color paredes
				if (colorPixel.equals(Color.decode("#535d58"))) {
					matriz[i][j] = 1;
				}
				//Color del jugador
				if (colorPixel.equals(Color.decode("#ff3d80"))) {
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
