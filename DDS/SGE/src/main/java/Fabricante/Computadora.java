package Fabricante;

public class Computadora implements Fabricante {

	int usoMensualMinimo = 60;
	int usoMensualMaximo = 360;
	Boolean notebook;
	
	public Computadora(Boolean notebook) {
		this.notebook = notebook;
	}
	
	public int medir() {
		return 0;
	}

	public void actuar() {
		
	}

	public boolean hayQueActuar(double temperatura) {
		return false;
	}

	public double usoMensualMinimo() {
		return usoMensualMinimo;
	}

	public double usoMensualMaximo() {
		return usoMensualMaximo;
	}

	@Override
	public double getConsumoKWPorHora() {
		return 0;
	}
}
