package consola.excepciones;

public class CarcelException extends ConsolaException{
    public CarcelException(String msg) {
        super(msg);
    }

    public CarcelException(String msg, Throwable err) {
        super(msg, err);
    }
}
