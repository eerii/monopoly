package monopoly.casilla.edificio;

import monopoly.casilla.propiedad.Solar;

public class Termas extends Edificio {
    // ·············
    // Constructores
    // ·············

    public Termas(Solar casilla) {
        super(casilla, "Termas", "󰘆");
    }

    // ·········
    // Overrides
    // ·········

    @Override
    public String toString() {
        return String.format("terma - propietario: %s - casilla: %s - grupo: %s - coste: %.2f\n",
                super.get_casilla().get_propietario().get_nombre(), super.get_casilla().get_nombre(),
                super.get_casilla().get_grupo().get_nombre(),
                coste());
    }

    @Override
    public float coste() {
        return super.coste() * 3;
    }
}
