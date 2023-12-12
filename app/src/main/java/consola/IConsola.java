package consola;

public interface IConsola {
    public void imprimir(String s, Object... args);

    public String leer();

    public void limpiar_pantalla();

    public void limpiar_resultado();

    public boolean entrada();
}
