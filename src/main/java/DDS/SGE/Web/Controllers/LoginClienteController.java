package DDS.SGE.Web.Controllers;

import DDS.SGE.Cliente;
import DDS.SGE.Repositorios.RepositorioClientes;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import static DDS.SGE.Web.Controllers.Routes.*;

public class LoginClienteController extends LoginController {
    public static ModelAndView mostrar(Request req, Response res) {
        // Obtener la direccion correspondiente del hbs, ruta comienza en
        // main/resources/templates/
        return new ModelAndView(null, "login.hbs");
    }

    public static ModelAndView loginCliente(Request req, Response res) {
        String username = req.queryParams("username");
        String password = req.queryParams("password");

        try {
            Cliente usuario = RepositorioClientes.findByUsername(username).get();

            revisarUsuario(usuario, password);

            String id = Long.toString(usuario.getId());
            res.redirect(HOME);

            req.session().attribute(SESSION_NAME, id);
            req.session().attribute(ADMIN, false);

            return new ModelAndView(null, "login.hbs");
        } catch (Exception e) {
            return error(req, res);
        }

    }

}