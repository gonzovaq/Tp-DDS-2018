package Fabricante;

public class Ventilador implements Fabricante {

	int usoMensualMinimo = 120;
	int usoMensualMaximo = 360;
	Boolean deTecho;
	
	public Ventilador(Boolean deTecho) {
		this.deTecho = deTecho;
	}
	
	@Override
	public int medir() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void actuar() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hayQueActuar(double temperatura) {
		// TODO Auto-generated method stub
		return false;
	}

}
