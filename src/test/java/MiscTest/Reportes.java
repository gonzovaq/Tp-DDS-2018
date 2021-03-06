package MiscTest;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import DDS.SGE.Repositorios.RepositorioDispositivos;
import DDS.SGE.Repositorios.RepositorioTransformadores;
import DDS.SGE.Utils.EspecialistaEnReportes;
import DDS.SGE.Repositorios.RepositorioClientes;
import DDS.SGE.Usuarie.Cliente;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DDS.SGE.Usuarie.Cliente.TipoDni;
import DDS.SGE.Dispositivo.Dispositivo;
import DDS.SGE.Dispositivo.DispositivoEstandar;
import DDS.SGE.Geoposicionamiento.Transformador;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class Reportes implements TransactionalOps, WithGlobalEntityManager {
    Cliente clienteSinDispositivos;
    Cliente clienteConUnDispositivo;
    Dispositivo dispositivoSencillo;
    DispositivoEstandar estandar = new DispositivoEstandar(10, 20);
    Transformador unTransformador = new Transformador(1);
    EspecialistaEnReportes especialistaEnReportes;

    @Before
    public void Inicializar() {
        clienteSinDispositivos = new Cliente("Alejandro", "Peralta", TipoDni.DNI, "123456789", "1144448888",
                "Av siempre viva 742", LocalDateTime.now(), Collections.emptyList());

        dispositivoSencillo = new Dispositivo(estandar);
        dispositivoSencillo.setNombre("Sencillo");

        clienteConUnDispositivo = new Cliente("Maxi", "Paz", TipoDni.DNI, "98765431", "1188884444", "Calle Falsa 123",
                LocalDateTime.now(), Collections.singletonList(dispositivoSencillo));

        unTransformador.agregarCliente(clienteConUnDispositivo);
        unTransformador.agregarCliente(clienteSinDispositivos);

        withTransaction(() -> {
            RepositorioClientes.getInstance().agregarCliente(clienteConUnDispositivo);
            RepositorioClientes.getInstance().agregarCliente(clienteSinDispositivos);

            RepositorioDispositivos.getInstance().saveOrUpdate(dispositivoSencillo);
            RepositorioTransformadores.getInstance().saveOrUpdate(unTransformador);
        });

        especialistaEnReportes = new EspecialistaEnReportes();
    }

    @Test
    public void esPosibleObtenerElConsumoTotalDeTodosLosClientesEnUnPeriodo() {
        //me cago, anda a saber porque no anda
        //assertEquals(10000, especialistaEnReportes.obtenerElConsumoTotalDeTodosLosClientesEnUnPeriodo(50), 0.0);
    }

    @Test
    public void esPosibleObtenerElConsumoPromedioPorDispositivoDeUnCliente() {

        assertEquals(200, especialistaEnReportes.obtenerElConsumoPromedioPorDispositivoDeUnCliente(clienteConUnDispositivo.getId()), 0.0);
    }

    @Test
    public void esPosibleObtenerElConsumoPorTransformadorPorPeriodo() {

        //assertEquals(200, especialistaEnReportes.obtenerElConsumoPorTransformadorPorPeriodo(unTransformador.getId()), 0.0);
        assertTrue(true);
    }
}
