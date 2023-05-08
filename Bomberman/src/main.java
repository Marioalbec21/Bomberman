import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Musica musicaDeFondo = new Musica();
        musicaDeFondo.play(0.2f);
		try {
			Ventana juego = new Ventana();
			juego.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
