package consola.excepciones;

public class ComprarCasillaException extends ComprarException {
    public ComprarCasillaException(String msg) {
        super(msg);
    }

    public ComprarCasillaException(String msg, Throwable err) {
        super(msg, err);
    }
}
