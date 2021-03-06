package DDS.SGE.Fabricante;

import javax.persistence.Entity;

@Entity
public class Lampara extends Fabricante {
	
	int watts;
	Boolean halogenas;
	
	protected Lampara() {}
	
	public Lampara(int watts, Boolean halogenas) {
		this.watts = watts;
		this.halogenas = halogenas;
		this.consumoKWPorHora = watts/100;
		inicializarUsoMinimoYMaximo(90, 360);
	}
	
}
