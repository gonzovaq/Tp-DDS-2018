package DDS.SGE.Web.Controllers;

import DDS.SGE.Dispositivo.DispositivoBuilder;
import DDS.SGE.Dispositivo.DispositivoDeCatalogo;
import DDS.SGE.Dispositivo.MetodoDeCreacion;
import DDS.SGE.Exceptions.UnauthorizedAccessException;
import DDS.SGE.Repositorios.RepositorioAdministradores;
import DDS.SGE.Repositorios.RepositorioCatalogo;
import DDS.SGE.Repositorios.RepositorioClientes;
import DDS.SGE.Repositorios.RepositorioSolicitudes;
import DDS.SGE.Solicitud.SolicitudAbierta;
import DDS.SGE.Usuarie.Cliente;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

import static DDS.SGE.Web.Controllers.Routes.*;

public class CatalogoController extends Controller {
    public ModelAndView mostrar(Request req, Response res) {
        String page = req.params(":page");
        int pageNumber;

        if (page == null || Integer.parseInt(page) == 1) {
            pageNumber = 1;
        } else {
            pageNumber = Integer.parseInt(page);
        }

        HashMap<String, Object> viewModel = new HashMap<>();

        List<DispositivoDeCatalogo> dispos = RepositorioCatalogo.getInstance().listarPagina(pageNumber);

        viewModel.put("dispositivos", dispos);
        viewModel.put("previousPage", pageNumber - 1);
        viewModel.put("hayAnterior", pageNumber > 1);
        viewModel.put("nextPage", pageNumber + 1);
        viewModel.put("haySiguiente", pageNumber < RepositorioCatalogo.getInstance().cantidadDePaginas());

        if (req.session().attribute(ADMIN) == "si") {
            viewModel = this.rellenarAdministrador(viewModel, req.session().attribute(SESSION_NAME));
            return new ModelAndView(viewModel, "catalogo-admin.hbs");
        }

        viewModel = this.rellenarCliente(viewModel, req.session().attribute(SESSION_NAME));
        return new ModelAndView(viewModel, "catalogo-user.hbs");
    }

    public ModelAndView solicitar(Request req, Response res) {
        String id_dispositivo = req.params(":id");
        Long id_dispositivo_posta = Long.parseLong(id_dispositivo);

        String id_cliente = req.session().attribute(SESSION_NAME);
        Long id_cliente_posta = Long.parseLong(id_cliente);

        Cliente cliente = RepositorioClientes.getInstance().findByID(id_cliente_posta);
        DispositivoDeCatalogo dispositivo = RepositorioCatalogo.getInstance().findByID(id_dispositivo_posta);

        SolicitudAbierta nuevaSolicitud = new SolicitudAbierta(cliente, dispositivo);

        withTransaction(() -> {
            RepositorioSolicitudes.getInstance().saveOrUpdate(nuevaSolicitud);
            RepositorioAdministradores.getInstance().listar().forEach(a -> a.setTieneNotificaciones(true));
        });

        res.redirect(SOLICITUDES);
        return new SolicitudesController().mostrar(req, res);

    }

    public ModelAndView mostrarFormularioInteligente(Request req, Response res) {
        if (req.session().attribute(ADMIN) != "si") {
            throw new UnauthorizedAccessException(Long.parseLong(req.session().attribute(SESSION_NAME)), req);
        }

        HashMap<String, Object> viewModel = this.rellenarAdministrador(null, req.session().attribute(SESSION_NAME));
        viewModel.put("tipo", "inteligente");

        return new ModelAndView(viewModel, "crear-inteligente.hbs");
    }

    public ModelAndView mostrarFormularioEstandar(Request req, Response res) {
        if (req.session().attribute(ADMIN) != "si") {
            throw new UnauthorizedAccessException(Long.parseLong(req.session().attribute(SESSION_NAME)), req);
        }

        HashMap<String, Object> viewModel = this.rellenarAdministrador(null, req.session().attribute(SESSION_NAME));
        viewModel.put("tipo", "estandar");

        return new ModelAndView(viewModel, "crear-estandar.hbs");
    }

    public ModelAndView nuevoInteligente(Request req, Response res) {
        if (req.session().attribute(ADMIN) != "si") {
            throw new UnauthorizedAccessException(Long.parseLong(req.session().attribute(SESSION_NAME)), req);
        }

        try {
            String nombre = req.queryParams("nombre");

            double consumo = Double.parseDouble(req.queryParams("consumo"));
            MetodoDeCreacion metodoDeCreacion = MetodoDeCreacion.parse(req.queryParams("fabricante"));
            boolean bajoConsumo;

            bajoConsumo = req.queryParams("bajoConsumo").equals("Sí");

            DispositivoBuilder db = new DispositivoBuilder();
            DispositivoDeCatalogo dispositivo = db.construirDispositivoDeCatalogo(nombre, consumo, bajoConsumo, true, metodoDeCreacion);
            withTransaction(() -> RepositorioCatalogo.getInstance().saveOrUpdate(dispositivo));

            res.redirect(DISPOSITIVOS);
            return new CatalogoController().mostrar(req, res);
        } catch (RuntimeException e) {
            HashMap<String, Object> viewModel = this.rellenarAdministrador(null, req.session().attribute(SESSION_NAME));
            viewModel.put("error", "<div> <p class=\"error\">{{errorMessage}}</p> </div>");
            viewModel.put("errorMessage", e.getMessage());

            return new ModelAndView(viewModel, "crear-inteligente.hbs");
        }

    }

    public ModelAndView nuevoEstandar(Request req, Response res) {
        if (req.session().attribute(ADMIN) != "si") {
            throw new UnauthorizedAccessException(Long.parseLong(req.session().attribute(SESSION_NAME)), req);
        }

        try {
            String nombre = req.queryParams("nombre");
            MetodoDeCreacion metodoDeCreacion = MetodoDeCreacion.parse(req.queryParams("fabricante"));
            double consumo = Double.parseDouble(req.queryParams("consumo"));
            long usoEstimadoDiario = Long.parseLong(req.queryParams("uso"));
            boolean bajoConsumo;

            bajoConsumo = req.queryParams("bajoConsumo").equals("Sí");

            DispositivoBuilder db = new DispositivoBuilder();
            DispositivoDeCatalogo dispositivo = db.construirDispositivoDeCatalogo(nombre, consumo, bajoConsumo, false, metodoDeCreacion);

            withTransaction(() -> RepositorioCatalogo.getInstance().saveOrUpdate(dispositivo));

            res.redirect(DISPOSITIVOS);
            return new PanelDeAdministradorController().mostrar(req, res);
        } catch (RuntimeException e) {
            HashMap<String, Object> viewModel = this.rellenarAdministrador(null, req.session().attribute(SESSION_NAME));
            viewModel.put("error", "<div> <p class=\"error\">{{errorMessage}}</p> </div>");
            viewModel.put("errorMessage", e.getMessage());

            return new ModelAndView(viewModel, "crear-estandar.hbs");
        }
    }

    public ModelAndView mostrarFichaTecnica(Request req, Response res) {
        String id = req.params(":id");
        Long id_posta = Long.parseLong(id);

        DispositivoDeCatalogo dispositivo = RepositorioCatalogo.getInstance().findByID(id_posta);

        HashMap<String, Object> viewModel = this.rellenarCliente(null, req.session().attribute(SESSION_NAME));
        viewModel.put("dispositivo", dispositivo);

        if (req.session().attribute(ADMIN) == "si") {
            viewModel = this.rellenarAdministrador(viewModel, req.session().attribute(SESSION_NAME));

            return new ModelAndView(viewModel, "ficha-tecnica-administrador.hbs");
        }


        return new ModelAndView(viewModel, "ficha-tecnica-user.hbs");
    }
}
