package DDS.SGE;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DDS.SGE.Cliente.TipoDni;
import DDS.SGE.Dispositivo.Dispositivo;
import DDS.SGE.Dispositivo.DispositivoEstandar;
import Geoposicionamiento.Transformador;

public class Reportes {
	Cliente clienteSinDispositivos;
	Cliente clienteConUnDispositivo;
	Dispositivo dispositivoSencillo;
	DispositivoEstandar estandar = new DispositivoEstandar(10, 20);
	EntityManager em = EntityManagerHelper.entityManager();
	Transformador unTransformador = new Transformador(1);
	EspecialistaEnReportes especialistaEnReportes;

	@Before
	public void Inicializar() {
		clienteSinDispositivos = new Cliente("Alejandro", "Peralta", TipoDni.DNI, "123456789", "1144448888",
				"Av siempre viva 742", LocalDateTime.now(), Arrays.asList());

		dispositivoSencillo = new Dispositivo(estandar);
		dispositivoSencillo.setNombre("Sencillo");

		clienteConUnDispositivo = new Cliente("Maxi", "Paz", TipoDni.DNI, "98765431", "1188884444", "Calle Falsa 123",
				LocalDateTime.now(), Arrays.asList(dispositivoSencillo));

		unTransformador.agregarCliente(clienteConUnDispositivo);
		unTransformador.agregarCliente(clienteSinDispositivos);

		EntityManagerHelper.beginTransaction();
		em.persist(clienteConUnDispositivo);
		em.persist(clienteSinDispositivos);
		em.persist(estandar);
		em.persist(dispositivoSencillo);
		em.persist(unTransformador);
		
		especialistaEnReportes  = new EspecialistaEnReportes();
	}

	@After
	public void after() {
		EntityManagerHelper.rollback();		
	}

	@Test
	public void esPosibleObtenerElConsumoTotalDeTodosLosClientesEnUnPeriodo() {
		int periodoEnDias = 50;
		assertEquals(10000, especialistaEnReportes.obtenerElConsumoTotalDeTodosLosClientesEnUnPeriodo(periodoEnDias), 0.0);
	}

	@Test
	public void esPosibleObtenerElConsumoPromedioPorDispositivoDeUnCliente() {

		assertEquals(200,especialistaEnReportes.obtenerElConsumoPromedioPorDispositivoDeUnCliente(clienteConUnDispositivo.getId()) , 0.0);
	}

	@Test
	public void esPosibleObtenerElConsumoPorTransformadorPorPeriodo() {

		assertEquals(200, especialistaEnReportes.obtenerElConsumoPorTransformadorPorPeriodo(unTransformador.getId()), 0.0);

	}
}
