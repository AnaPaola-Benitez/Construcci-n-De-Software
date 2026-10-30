
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// TECNICA UTILIZADA: Introduce Parameter Object
// REFACTOR: Se creo SeleccionAsientos para agrupar los asientos solicitados en
// un solo objeto, en lugar de pasar los arreglos paralelos char[] e int[].
public class SeleccionAsientos {
    private final List<String> codigos;

    public SeleccionAsientos(String... asientos) {
        if (asientos == null || asientos.length == 0) {
            throw new IllegalArgumentException("Debe seleccionar al menos un asiento");
        }

        Set<String> unicos = new LinkedHashSet<>();

        for (String asiento : asientos) {
            if (asiento == null || !asiento.matches("[A-Z][1-9][0-9]*")) {
                throw new IllegalArgumentException("Código de asiento inválido: " + asiento);
            }

            if (!unicos.add(asiento)) {
                throw new IllegalArgumentException("Asiento repetido en la solicitud: " + asiento);
            }
        }

        codigos = Collections.unmodifiableList(new ArrayList<>(unicos));
    }

    public List<String> getCodigos() {
        return codigos;
    }
}
