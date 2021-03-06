package DDS.SGE.Dispositivo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class IntervaloActivo {
	@Id
	@GeneratedValue
	private Long id;
	LocalDateTime tiempoInicial;
	LocalDateTime tiempoFinal;
	
	protected IntervaloActivo() {}
	
	public IntervaloActivo(LocalDateTime tiempoInicial, LocalDateTime tiempoFinal) {
		this.tiempoInicial = tiempoInicial;
		this.tiempoFinal = tiempoFinal;
	}
	
	public long getIntervaloEncendidoEnMinutos() {
		return ChronoUnit.MINUTES.between(tiempoInicial, tiempoFinal);
	}
	
	public boolean ocurreDespuesDe(LocalDateTime fecha) {
		return tiempoInicial.isAfter(fecha);
	}
	
	public boolean ocurreAntesDe(LocalDateTime fecha) {
		return !ocurreDespuesDe(fecha);
	}

	public LocalDateTime getTiempoInicial() {
		return this.tiempoInicial;
	}
	
	public LocalDateTime getTiempoFinal() {
		return this.tiempoFinal;
	}
}
