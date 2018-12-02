package DDS.SGE.Persistencia;

import DDS.SGE.*;
import DDS.SGE.Dispositivo.Dispositivo;
import DDS.SGE.Dispositivo.DispositivoEstandar;
import DDS.SGE.Dispositivo.DispositivoInteligente;
import DDS.SGE.Dispositivo.Estado.Apagado;
import DDS.SGE.Repositorios.RepositorioAdministradores;
import DDS.SGE.Repositorios.RepositorioClientes;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import DDS.SGE.Repositorios.RepositorioDispositivos;
import Fabricante.AireAcondicionado;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCliente {
    Cliente cliente;
    Dispositivo dispositivoSencillo;
    Dispositivo noSencillo;
    DispositivoInteligente inteligente;
    DispositivoEstandar estandar;
    EntityManager em;

    @Before
    public void initialize() {
        estandar = new DispositivoEstandar(10, 20);
        dispositivoSencillo = new Dispositivo(estandar);
        inteligente = new DispositivoInteligente(new Apagado(), new AireAcondicionado(20));
        noSencillo = new Dispositivo(inteligente);

        cliente = new Cliente("Un nombre", "Un apellido", Cliente.TipoDni.DNI, "1111", "4444", "Un domicilio",
                LocalDateTime.now(), new ArrayList<Dispositivo>());
        em = EntityManagerHelper.entityManager();
    }

    @After
    public void after() {
        EntityManagerHelper.rollback();
    }

    @Test
    public void testPersistirAUnClienteDesdeElRepositorioYDespuesTraerlo() {
        RepositorioClientes.agregarCliente(cliente);
        Cliente persistido = em.find(Cliente.class, cliente.getId());

        assertEquals(persistido, cliente);
    }

    @Test
    public void testPersistirUnClienteYTraerloPorUsername() {
        Cliente otroCliente = new Cliente("Usuario", "pass");
        RepositorioClientes.agregarCliente(otroCliente);

        Cliente persistido = RepositorioClientes.findByUsername(otroCliente.getUsername()).get();
        assertEquals(persistido, otroCliente);

    }

    @Test
    public void testPersistirUnAdministrador() {
        Administrador administrador = new Administrador("Admin", "admin");
        RepositorioAdministradores.agregarAdministrador(administrador);
        Administrador persistido = RepositorioAdministradores.findByID(administrador.getId());

        assertEquals(administrador, persistido);
    }

    @Test
    public void testPersistirUnClienteYLevantarSusDispositivos() {
        Cliente otroCliente = new Cliente("Mati", "Cash", Cliente.TipoDni.DNI, "121", "1212", "121", LocalDateTime.now(), Arrays.asList(dispositivoSencillo, noSencillo));
        RepositorioClientes.agregarCliente(otroCliente);

        List<Dispositivo> dispositivos = RepositorioClientes.findByID(otroCliente.getId()).getDispositivos().collect(Collectors.toList());
        assertEquals(Arrays.asList(dispositivoSencillo, noSencillo), dispositivos);
    }

}
