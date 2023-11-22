package consola.excepciones;

public class PropiedadException extends ConsolaException{
    public PropiedadException(String msg) {
        super(msg);
    }

    public PropiedadException(String msg, Throwable err) {
        super(msg, err);
    }
}
