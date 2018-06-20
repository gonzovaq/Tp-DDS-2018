package Fabricante;

public class Televisor implements Fabricante {

	public enum TipoTelevisor {
		LED, LCD, TUBO
	}
	
	TipoTelevisor tipo;
	int pulgadas;
	int usoMensualMinimo = 90;
	int usoMensualMaximo = 360;
	
	public Televisor(int pulgadas,TipoTelevisor tipo) {
		this.pulgadas = pulgadas;
		this.tipo = tipo;
	}
	
	@Override
	public int Medir() {		
		return 0;
	}

	@Override
	public void Actuar() {

	}

}
