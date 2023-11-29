package consola.excepciones;

public class LanzarDadosException extends ConsolaException {
    public LanzarDadosException(String msg) {
        super(msg);
    }

    public LanzarDadosException(String msg, Throwable err) {
        super(msg, err);
    }
}
