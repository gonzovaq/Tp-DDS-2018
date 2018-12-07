package DDS.SGE.Utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import DDS.SGE.Optimizador.Optimizador;
import DDS.SGE.Repositorios.RepositorioClientes;
import DDS.SGE.Usuarie.Cliente;

public class ControladorDeDispositivos {
    public static void main(String args[]) {
        Optimizador optimizador = new Optimizador();
        Timer timer = new Timer();

        TimerTask tarea = new TimerTask() {

            @Override
            public void run() {
                System.out.println("Controlando dispositivos...");
                List<Cliente> clientes = RepositorioClientes.getInstance().getAllClients();
                clientes.forEach(c -> {
                    optimizador.simplex(c);
                    optimizador.accionarSobreDispositivos(
                            optimizador.obtenerDispositivosInfractores(c.getDispositivos()));
                });

                System.out.println("Dispositivos controlados.");
                System.out.println("Revisando de nuevo en un minuto.");
            }
        };

        timer.schedule(tarea, 0, 60000);
    }

}