package monopoly.casilla.edificio;

import monopoly.casilla.*;

public class Pabellon extends Edificio {


    public Pabellon(Solar casilla) {
        super(casilla, "Pabellon", "󰶠");
    }

    @Override
    public String toString() {
        return String.format("pabellon - propietario: %s - casilla: %s - grupo: %s - coste: %.2f\n", super.getCasilla().get_propietario().get_nombre(), super.getCasilla().get_nombre(), super.getCasilla().get_grupo().get_nombre(),
                coste());
    }

    @Override
    public float coste() {
        return super.coste() * 4;
    }
}

