package consola.excepciones;

public class PruebaException extends Exception {
    public PruebaException(String msg) {
        super(msg);
    }

    public PruebaException(String msg, Throwable err) {
        super(msg, err);
    }
}
