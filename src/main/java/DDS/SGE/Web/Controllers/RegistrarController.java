package DDS.SGE.Web.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import DDS.SGE.Exceptions.UserUnavailableException;
import DDS.SGE.Geoposicionamiento.Transformador;
import DDS.SGE.Repositorios.RepositorioTransformadores;
import DDS.SGE.Usuarie.Cliente;
import DDS.SGE.Usuarie.ClienteBuilder;
import DDS.SGE.Repositorios.RepositorioClientes;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import static DDS.SGE.Web.Controllers.Routes.*;

public class RegistrarController extends Controller {
    public ModelAndView mostrar(Request req, Response res) {
        return new ModelAndView(null, "registro.hbs");
    }

    public ModelAndView registrar(Request req, Response res) {

        String username = req.queryParams("username");
        String password = req.queryParams("password");

        String nombre = req.queryParams("nombre");
        String apellido = req.queryParams("apellido");
        String codigoArea = req.queryParams("codigoTelefono");
        String telefono = req.queryParams("telefono");
        String tipoDni = req.queryParams("tipoDni");
        String numeroDni = req.queryParams("numeroDni");
        String direccion = req.queryParams("direccion");

        ClienteBuilder cb = new ClienteBuilder();
        cb.especificarTipoDocumento(tipoDni);
        cb.especificarDireccion(direccion);

        try {
            if (RepositorioClientes.getInstance().findByUsername(username).isPresent()) {
                throw new UserUnavailableException(username);
            }

            Cliente cliente = cb.crearCliente(nombre, apellido, numeroDni, codigoArea + telefono, username, password);
            List<Transformador> transformadores = RepositorioTransformadores.getInstance().listar();

            //Transformador transformador = Transformador.parse(direccion)
            //SE SE Segui soñando pelotudo

            Transformador transformador = transformadores.get(new Random().nextInt(transformadores.size()));
            transformador.agregarCliente(cliente);

            withTransaction(() -> {
                RepositorioTransformadores.getInstance().saveOrUpdate(transformador);
                //RepositorioClientes.getInstance().registrarCliente(cliente);
            });

            res.redirect(LOGIN);
            System.out.println("persisti bien");
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            HashMap<String, Object> viewModel = new HashMap<>();
            viewModel.put("username", req.queryParams("username"));
            viewModel.put("password", req.queryParams("password"));
            viewModel.put("nombre", req.queryParams("nombre"));
            viewModel.put("apellido", req.queryParams("apellido"));
            viewModel.put("codigoTelefono", req.queryParams("codigoTelefono"));
            viewModel.put("telefono", req.queryParams("telefono"));
            viewModel.put("numeroDni", req.queryParams("numeroDni"));
            viewModel.put("direccion", req.queryParams("direccion"));
            viewModel.put("errorMessage", ex.getMessage());

            res.redirect(SIGNUP);
            return new ModelAndView(viewModel, "registro.hbs");
        }

        return new LoginClienteController().mostrar(req, res);

    }
}
