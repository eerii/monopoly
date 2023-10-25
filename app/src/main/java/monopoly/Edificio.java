package monopoly;

public class Edificio {
    TipoEdificio tipo;

    public enum TipoEdificio {
        CASA,
        HOTEL,
        TERMAS,
        PABELLON;
    }

    public Edificio(TipoEdificio tipo) {
        this.tipo = tipo;
    }

    public TipoEdificio get_tipo() {
        return tipo;
    }
}
