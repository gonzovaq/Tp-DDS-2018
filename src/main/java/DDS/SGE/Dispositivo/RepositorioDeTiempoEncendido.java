package DDS.SGE.Dispositivo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class RepositorioDeTiempoEncendido {
	
	private LocalDateTime ultimaFechaDeEncendido;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<IntervaloActivo> intervalosDeActividad;
	
	public RepositorioDeTiempoEncendido() {
		ultimaFechaDeEncendido = LocalDateTime.now();
		intervalosDeActividad = new ArrayList<IntervaloActivo>(Arrays.asList());
	}
	
	public Stream<IntervaloActivo> getIntervalosDeActividad(){
		return intervalosDeActividad.stream();
	}
	
	public void setIntervalosDeActividad(List<IntervaloActivo> intervalosDeActividad){
		this.intervalosDeActividad = intervalosDeActividad;
	}
	
	public void setUltimaFechaDeEncendido(LocalDateTime fecha){
		this.ultimaFechaDeEncendido = fecha;
	}

	public void encender(LocalDateTime fechaEncendido) {
		ultimaFechaDeEncendido = fechaEncendido;
	}

	public void apagar(LocalDateTime fechaApagado) {
		intervalosDeActividad.add(new IntervaloActivo(ultimaFechaDeEncendido, fechaApagado));
	}
	
	public long tiempoTotalEncendidoHaceNHorasEnMinutos(int horas) {		
		
		LocalDateTime fechaSolicitada = LocalDateTime.now().minusHours(horas);

		return this.intervalosQueOcurrenEntre(fechaSolicitada, LocalDateTime.now())
				.mapToLong(intervalo -> intervalo.getIntervaloEncendidoEnMinutos()).sum()
				+ this.intervaloEncendidoActual().getIntervaloEncendidoEnMinutos();
	}
	
	public IntervaloActivo intervaloEncendidoActual() { 
		return new IntervaloActivo(ultimaFechaDeEncendido, LocalDateTime.now());
	}
	
	public Stream<IntervaloActivo> intervalosQueOcurrenEntre(LocalDateTime tiempoMinimo, LocalDateTime tiempoMaximo){
		return this.getIntervalosDeActividad().filter(intervalo -> intervalo.ocurreDespuesDe(tiempoMinimo) && intervalo.ocurreAntesDe(tiempoMaximo));
	}
		
	public double tiempoTotalEnUnPeriodoEnMinutos(LocalDateTime principioPeriodo, LocalDateTime finPeriodo) {
		return this.intervalosQueOcurrenEntre(principioPeriodo, finPeriodo).mapToLong(intervalo -> intervalo.getIntervaloEncendidoEnMinutos()).sum();
	}

}
