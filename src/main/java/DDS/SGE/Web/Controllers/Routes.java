package DDS.SGE.Web.Controllers;

public class Routes {
    public static final String HOME = "/";

    public static final String LOGIN = "/login";

    public static final String HOGAR = "/hogar";

    public static final String OPTIMIZADOR = "/optimizador";

    public static final String ADMINISTRADOR = "/administrador";
    public static final String ADMINISTRADOR_LOGIN = ADMINISTRADOR + "/login";
    public static final String ADMINISTRADOR_HOGARES = ADMINISTRADOR + "/hogares";

    public static final String DISPOSITIVOS = "/dispositivos";
    public static final String DISPOSITIVOS_ID_ACQUIRE = DISPOSITIVOS + "/:id/acquire";
    public static final String DISPOSITIVOS_ID_INFO = DISPOSITIVOS + "/:id/info";

    private static final String DISPOSITIVOS_NEW = DISPOSITIVOS + "/new";
    public static final String DISPOSITIVOS_NEW_INTELIGENTE = DISPOSITIVOS_NEW + "/inteligente";
    public static final String DISPOSITIVOS_NEW_ESTANDAR = DISPOSITIVOS_NEW + "/estandar";

    public static final String TRANSFORMADOR = "/transformador";

    public static final String USER = "/me";
    public static final String USER_EDIT = USER + "/edit";

    public static final String SIGNUP = "/signup";

    public static final String CONSUMO = "/consumo";
    public static final String CONSUMO_OBTENER = CONSUMO + "/obtener";

    public static final String LOGOUT = "/logout";

    public static final String LIFE = "/42";

    public static final String GLITCH = "*";
}
