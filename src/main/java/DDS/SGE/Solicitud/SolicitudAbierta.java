package DDS.SGE.Solicitud;

import DDS.SGE.Dispositivo.DispositivoDeCatalogo;
import DDS.SGE.Usuarie.Administrador;
import DDS.SGE.Usuarie.Cliente;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SolicitudAbierta extends Solicitud {
    protected SolicitudAbierta() {
    }

    public SolicitudAbierta(Cliente cliente, DispositivoDeCatalogo dispositivo) {
        this.cliente = cliente;
        this.dispositivo = dispositivo;
        this.fechaCreacion = LocalDateTime.now();
    }

    public SolicitudCerrada aceptar(Administrador administrador) {
        cliente.agregarDispositivo(dispositivo.construir());
        return new SolicitudCerrada(this.cliente, administrador, this.fechaCreacion, this.dispositivo, EstadoDeSolicitud.aceptada);
    }

    public SolicitudCerrada rechazar(Administrador administrador) {
        return new SolicitudCerrada(this.cliente, administrador, this.fechaCreacion, this.dispositivo, EstadoDeSolicitud.rechazada);
    }
}