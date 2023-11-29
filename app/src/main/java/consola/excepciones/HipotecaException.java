package consola.excepciones;

public class HipotecaException extends ConsolaException{
    public HipotecaException(String msg) {
        super(msg);
    }

    public HipotecaException(String msg, Throwable err) {
        super(msg, err);
    }
}
