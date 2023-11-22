package consola.excepciones;


public class ComprarException extends ConsolaException {
    public ComprarException(String msg) {
        super(msg);
    }

    public ComprarException(String msg, Throwable err) {
        super(msg, err);
    }
}