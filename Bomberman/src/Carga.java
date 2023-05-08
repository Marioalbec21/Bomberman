import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Carga{
	
	private int[][] matriz;
	private BufferedImage dibujo;
	
	public Carga(String ruta) {		

		try {
			dibujo = ImageIO.read(new File(ruta));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int ancho = dibujo.getWidth();
		int alto = dibujo.getHeight();
		
		matriz = new int[alto][ancho];
		
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				//Asigna valores a la matriz dependiendo el color
				Color colorPixel = new Color(dibujo.getRGB(j, i));
				
				//Suelo
				if (colorPixel.equals(Color.WHITE)) {
					matriz[i][j] = 0;
				} 
				//Color paredes
				if (colorPixel.equals(Color.BLACK)) {
					matriz[i][j] = 1;
				}
			}
		}
	}

	public int[][] getMatriz() {
		return matriz;
	}

	public BufferedImage getDibujo() {
		return dibujo;
	}
}
