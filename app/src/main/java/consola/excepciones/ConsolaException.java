package consola.excepciones;

public class ConsolaException extends Exception {
    public ConsolaException(String msg) {
        super(msg);
    }

    public ConsolaException(String msg, Throwable err) {
        super(msg, err);
    }
}

