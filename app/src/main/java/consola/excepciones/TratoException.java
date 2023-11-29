package consola.excepciones;

public class TratoException extends ConsolaException {
    public TratoException(String msg) {
        super(msg);
    }

    public TratoException(String msg, Throwable err) {
        super(msg, err);
    }
}
