package DDS.SGE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

public class Administrador implements Usuario {
	private String nombre;
	private String apellido;
	private String domicilio;
	private LocalDateTime fechaAltaSistema;
	private int idAdmin;

	public Administrador(String nombre, String apellido, String domicilio, LocalDateTime fechaAltaSistema, int idAdmin) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.domicilio = domicilio;
		this.fechaAltaSistema = fechaAltaSistema;
		this.idAdmin = idAdmin;
	}

	public String getNombre() {
		return this.nombre;
	}

	public Administrador(String json) {
		this.cargarDesdeJson(json);
	}
	
	public LocalDateTime getFechaAltaSistema() {
		return this.fechaAltaSistema;
	}

	public void setFechaAltaSistema(int anio, int mes, int dia) {
		this.fechaAltaSistema = this.fechaAltaSistema.withDayOfMonth(dia).withMonth(mes).withYear(anio);
	}

	public long cantidadDeMesesComoAdmin() {
		LocalDate localDate = LocalDate.now();
		return ChronoUnit.MONTHS.between(this.fechaAltaSistema, localDate);
	}

	public void cargarDesdeJson(String json) {
		JSONObject jsonObject = new JSONObject(json);
		this.nombre = jsonObject.getString("nombre");
		this.apellido = jsonObject.getString("apellido");
		this.domicilio = jsonObject.getString("domicilio");
		//this.setFechaAltaSistema(jsonObject.getInt("anio"), jsonObject.getInt("mes"), jsonObject.getInt("dia"));
		this.idAdmin = jsonObject.getInt("idAdmin");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy"); // Uso un parser
		this.fechaAltaSistema = LocalDateTime.parse(jsonObject.getString("fechaAltaServicio"), formatter);
	}

}