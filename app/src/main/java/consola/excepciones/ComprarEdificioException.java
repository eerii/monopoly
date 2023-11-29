package consola.excepciones;

public class ComprarEdificioException extends ComprarException{
    public ComprarEdificioException(String msg) {
        super(msg);
    }

    public ComprarEdificioException(String msg, Throwable err) {
        super(msg, err);
    }
}
