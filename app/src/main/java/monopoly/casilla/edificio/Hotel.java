package monopoly.casilla.edificio;

import monopoly.casilla.propiedad.Solar;

public class Hotel extends Edificio {
    // ·············
    // Constructores
    // ·············

    public Hotel(Solar casilla) {
        super(casilla, "Hotel", "󰋡");
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        return String.format("hotel - propietario: %s - casilla: %s - grupo: %s - coste: %.2f\n",
                super.get_casilla().get_propietario().get_nombre(), super.get_casilla().get_nombre(),
                super.get_casilla().get_grupo().get_nombre(),
                coste());
    }

    @Override
    public float coste() {
        return super.coste() * 2;
    }
}
