package DDS.SGE;

import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.json.*;

import DDS.SGE.Dispositivo.Dispositivo;
import DDS.SGE.Dispositivo.DispositivoInteligente;
import DDS.SGE.Dispositivo.Estado.Encendido;

public class Cliente {
	private String nombre;
	private String apellido;
	private TipoDni tipoDni;
	private String numeroDni;
	private String telefono;
	private String domicilio;
	private LocalDateTime fechaAltaServicio;
	private Categoria categoria;
	private List<Dispositivo> dispositivos;
	private NotificadorNuevoDispositivo nuevoDisp;
	int puntos;

	public Cliente(String nombre, String apellido, TipoDni tipoDni, String numeroDni, String telefono, String domicilio,
			LocalDateTime fechaAltaServicio, List<Dispositivo> dispositivos) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.tipoDni = tipoDni;
		this.numeroDni = numeroDni;
		this.telefono = telefono;
		this.domicilio = domicilio;
		this.fechaAltaServicio = fechaAltaServicio;
		this.categoria = Categoria.R1;
		this.setDispositivos(dispositivos);
		this.nuevoDisp = new NotificadorNuevoDispositivo();
	}

	public enum TipoDni {
		DNI, CI, LE, LC
	}

	public String getTipoDni() {
		return this.tipoDni.toString();
	}

	public String getNombre() {
		return this.nombre;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setTipoDni(String string) {
		tipoDni = TipoDni.valueOf(string);
	}

	public void setFechaAltaServicio(int anio, int mes, int dia) {
		this.fechaAltaServicio = this.fechaAltaServicio.withDayOfMonth(dia).withMonth(mes).withYear(anio);
	}

	public Stream<Dispositivo> getDispositivos() {
		return dispositivos.stream();
	}

	public void setDispositivos(List<Dispositivo> dispositivos) {
		for (Dispositivo disp : dispositivos){
			this.dispositivos.add(disp);
			nuevoDisp.SeAgregoDispositivo(this,disp);
		}
	}

	public boolean algunDispositivoEncendido() {
		return getDispositivos().anyMatch(dispositivo -> dispositivo.estaEncendido());
	}

	public int cantidadDispositivos() {
		return dispositivos.size();
	}

	public int cantidadDispositivosEncendidos() {
		return (int) this.dispositivosEncendidos().count(); //Hago el casteo a Int porque el .count() me devuelve long.
	}
	
	public Stream<Dispositivo> dispositivosEncendidos() {
		return getDispositivos().filter(dispositivo ->dispositivo.estaEncendido());
	}
	
	public int cantidadDispositivosApagados() {
		return this.cantidadDispositivos() - this.cantidadDispositivosEncendidos();
	}

	public DoubleStream consumoDispositivosDiarioEstimado() {
		return getDispositivos().mapToDouble(dispositivo -> dispositivo.consumoDiarioEstimado());
	}

	public double consumoTotalEstimadoDiario() {
		return this.consumoDispositivosDiarioEstimado().sum();
	}

	public double consumoTotalPorMes() {
		LocalDate localDate = LocalDate.now();
		int diasDelMesActual = localDate.lengthOfMonth();
		return this.consumoFinalEstimado(diasDelMesActual);
	}

	public double consumoTotalDeUnMesEspecifico(LocalDate fecha) {
		int diasDeTalMes = fecha.lengthOfMonth();
		return this.consumoFinalEstimado(diasDeTalMes);
	}

	public double consumoFinalEstimado(int diasDelMes) {
		return this.consumoTotalEstimadoDiario() * diasDelMes;
	}

	public void recategorizar() {
		categoria = Arrays.stream(Categoria.values())
				.filter(categorias -> categorias.pertenece(this.consumoTotalPorMes())).findFirst().get();
	}
	
	public void agregarModuloAdaptadorA(Dispositivo dispositivo) {
		if(lePerteneceDispositivo(dispositivo)) {
			dispositivo.adaptarConModulo();
		}
	}
	
	public boolean lePerteneceDispositivo(Dispositivo dispositivo) {
		return this.getDispositivos().anyMatch(unDispositivo->unDispositivo.equals(dispositivo));
	}

	public void sumarPuntos(int puntos) {
		this.puntos += puntos;
		
	}
}