package DDS.SGE.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("No se encontró la combinación de usuario y contraseña");
    }
}
