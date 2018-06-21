package DDS.SGE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.Before;

import DDS.SGE.Cliente.TipoDni;
import DDS.SGE.Dispositivo.Dispositivo;
import DDS.SGE.Dispositivo.DispositivoEstandar;
import Fabricante.Computadora;
import Geoposicionamiento.Zona;

public class TestCalcularConsumoEstimado {

	Computadora unFabricante = new Computadora(true);
	
	int diasDelMes = LocalDate.now().lengthOfMonth();
	Dispositivo unDispositivo = new Dispositivo(0.78, new DispositivoEstandar(24), unFabricante);
	Cliente unCliente = new Cliente("Un", "Cliente", TipoDni.DNI, "111111111", "1123456789",
			"Una Calle", LocalDateTime.now(), Arrays.asList(unDispositivo), new Zona());	
	
	@Before
	public void initialize() {
		unCliente.recategorizar();
	}
	
	@Test
	public void testElConsumoMensualDeUnClienteEsElCorrespondienteAlConsumoDelDispositivoConvertidoALaDuracionDelMesActual() {
		assertEquals(unDispositivo.consumoDiarioEstimado() * diasDelMes,
				unCliente.consumoTotalPorMes(), 0);
	}
	
	@Test
	public void testUnClienteTiene3DispositivosIgualesYSuConsumoTotalDiarioEsElTripleDelConsumoDelDispositivo() {
		List<Dispositivo> tresDispositivosIguales = Arrays.asList(unDispositivo, unDispositivo, unDispositivo);
		unCliente.setDispositivos(tresDispositivosIguales);

		assertEquals(74.88, unCliente.consumoTotalEstimadoDiario(), 0);
	}
	
	@Test
	public void testEnAbrilElConsumoTotalPorMesDeUnClienteEsMayorQueEnFebrero() {
		LocalDate abril = LocalDate.of(2018, 4, 22);
		LocalDate febrero = LocalDate.of(2018, 2, 27);

		assertTrue(unCliente.consumoTotalDeUnMesEspecifico(abril) > unCliente.consumoTotalDeUnMesEspecifico(febrero));
	}
	
	@Test
	public void testLaFacturacionEstimadaVariableDelClienteEsCorrespondienteASuCategoria() {
		Categoria categoriaDelCliente = unCliente.getCategoria();
		
		assertEquals(unCliente.consumoTotalEstimadoDiario() * categoriaDelCliente.getNormalVariable(),
				categoriaDelCliente.estimarFacturacionCargoVariable(unCliente), 0);
	}
}
