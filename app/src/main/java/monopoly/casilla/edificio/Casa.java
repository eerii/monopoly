package monopoly.casilla.edificio;

import monopoly.casilla.*;

public class Casa extends Edificio {


    public Casa(Solar casilla) {
        super(casilla, "Casa", "");
    }

    @Override
    public String toString() {
        return String.format("casa - propietario: %s - casilla: %s - grupo: %s - coste: %.2f\n", super.getCasilla().get_propietario().get_nombre(), super.getCasilla().get_nombre(), super.getCasilla().get_grupo().get_nombre(),
                coste());
    }

    @Override
    public float coste() {
        return super.coste();
    }
}


