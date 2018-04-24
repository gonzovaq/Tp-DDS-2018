package DDS.SGE;

import static org.junit.Assert.*;
import com.google.gson.*;

import java.util.Arrays;
import java.time.LocalDate;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import DDS.SGE.Cliente.TipoDni;
import junit.framework.Assert;

public class testsGenericos {

	// Hago algunas instancias para los casos de prueba
	String jsonDisp = "{nombre:dispositivo x,consumoKWPorHora:12,encendido:true}";
	String jsonCliente = "{nombre:gonzalo,apellido:vaquero,tipoDni:dni,numeroDni:\"1254156153\",telefono:\"4444444\",domicilio:calle falsa 123,anio:2030,mes:01,dia:18}";
	String jsonAdmin = "{nombre:mati,apellido:giorda,domicilio:calle falsa 321, fechaAlta:20/15/2030,identificador:2}";
	Dispositivo nintendoDS = new Dispositivo(0.5, false);
	Dispositivo televisor = new Dispositivo(1, false);
	Dispositivo plasma = new Dispositivo(2, false);
	Dispositivo computadora = new Dispositivo(1, true);
	Dispositivo estufaElectrica = new Dispositivo(2, false);
	// No estoy muy seguro si conviene usar listas de esta manera
	List<Dispositivo> dispositivosDeAlejandro = Arrays.asList(nintendoDS,plasma);
	List<Dispositivo> dispositivosDeLucila = Arrays.asList(televisor, computadora);
	Cliente alejandro = new Cliente("Alejandro","Peralta",TipoDni.dni,"123456789","1144448888","Av siempre viva 123", LocalDate.now() , dispositivosDeAlejandro);
	Cliente lucila = new Cliente("Lucila","Salmeron",TipoDni.dni,"123456789","1144448888","Av siempre viva 123", LocalDate.now(), dispositivosDeLucila);

	@Test
	public void castearJson() {
		Dispositivo disp = new Dispositivo(10,true);
		disp.cargarDesdeJson(jsonDisp);
		
		assertEquals("dispositivo x", disp.getNombre());
	}
	
	@Test
	public void crearCliente() {
		Cliente unCliente = new Cliente(jsonCliente);
		
		assertEquals("gonzalo", unCliente.getNombre());
	}
	
	// Esto deberia crear un cliente directamente desde el json - hay que ver como quedarian los constructores y demas
	// Mientras no ande dejo la carga de json con json.org 
	@Test
	public void crearClienteConGson() {
		Gson gson = new GsonBuilder().create();	
		String gsonDisp = "{'nombre':'dispositivo x','consumoKWPorHora':'12','encendido':'true'}";
		Dispositivo disp= gson.fromJson(gsonDisp, Dispositivo.class);
		
		assertEquals("dispositivo x", disp.getNombre());
	}
	
	@Test
	public void crearDispositivoConGson() {
		Gson gson = new GsonBuilder().create();	
		String gsonCliente = "{'nombre':'gonzalo','apellido':'vaquero','tipoDni':'DNI','numeroDocumento':'123','telefono':'4444444','domicilio':'calle falsa 123', 'fecha':'15-15-2030','categoria':'R1'}";		
		Cliente cliente= gson.fromJson(gsonCliente, Cliente.class);
		
		assertEquals("gonzalo", cliente.getNombre());
	}
	
	@Test
	public void testAlejandroNoTieneNingunDispositivoEncendido() {
		assertFalse(alejandro.algunDispositivoEncendido());
	}

	@Test
	public void testLucilaTieneAlgunDispositivoEncendido() {
		assertTrue(lucila.algunDispositivoEncendido());
	}
	
	@Test
	public void testLucilaTiene1DispositivoEncendido() {
		assertEquals(1, lucila.dispositivosEncendidos());
	}

	@Test
	public void testAlEncenderseLosDispositivosDeAlejandroSuConsumoPorHoraEsLaSumaDelConsumoPorHoraDeAmbosDispositivos() {
		plasma.encender();
		nintendoDS.encender();
		assertEquals(plasma.getConsumoKWPorHora() + nintendoDS.getConsumoKWPorHora(), alejandro.consumoTotalPorHora(),0);
	}

	@Test
	public void testLaCategoriaDeLucilaEsR9() {
		assertEquals(Categoria.R9, lucila.getCategoria());
	}

	@Test
	public void testLaFacturacionEstimadaVariableDeLucilaEsCorrespondienteALaCategoria1() {
		assertEquals(lucila.consumoTotalPorHora() * Categoria.R1.getNormalVariable(), 0,
				lucila.getCategoria().estimarFacturacionCargoVariable(lucila));
	}
	
	@Test
	public void testLaFacturacionEstimadaVariableDeLucilaTrasEncenderLaImpresoraEsCorrespondienteALaCategoria2() {
		estufaElectrica.encender();
		lucila.categorizar();
		assertEquals(lucila.consumoTotalPorHora() * Categoria.R3.getNormalVariable(), 0,
				lucila.getCategoria().estimarFacturacionCargoVariable(lucila));
	}
	
	@Test
	public void testRecategorizarAR9AAlejandro() {
		List<Dispositivo> dispositivosNuevos = Arrays.asList(televisor);
		alejandro.setDispositivos(dispositivosNuevos);
		assertEquals(Categoria.R8,alejandro.getCategoria());
	}
}

