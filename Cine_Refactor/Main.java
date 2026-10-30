import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        SistemaReservas s = new SistemaReservas();
        Funcion f = new Funcion("Interestelar", 3, "20:30", 12.0);

        // TECNICA UTILIZADA: Preserve Whole Object
        // REFACTOR: Se entrega el objeto Cliente completo, no sus tres campos sueltos.
        // TECNICA UTILIZADA: Introduce Parameter Object
        // REFACTOR: Los asientos F5 y F6 forman una sola SeleccionAsientos.
        // TECNICA UTILIZADA: Remove Parameter
        // REFACTOR: Las llamadas ya no incluyen el boolean imprimir, que no tenia uso.
        int r1 = s.reservarAsientos(new Cliente("Ana", "ana@mail.com", true), f,
                new SeleccionAsientos("F5", "F6"), "App");
        System.out.println("Reserva Ana: " + r1);

        // TECNICA UTILIZADA: Replace Error Code with Exception
        // REFACTOR: Se captura y se muestra la excepcion de la reserva de Luis
        // en vez de imprimir el codigo de error -3.
        try {
            int r2 = s.reservarAsientos(new Cliente("Luis", "luis@mail.com", false), f,
                    new SeleccionAsientos("F7", "F6"), "Taquilla");
            System.out.println("Reserva Luis: " + r2);
        } catch (IllegalStateException e) {
            System.out.println("Reserva Luis: " + e.getMessage());
        }
        System.out.println("Ocupados: " + f.getOcupados());

        // TECNICA UTILIZADA: Encapsulate Downcast
        // REFACTOR: buscar() devuelve Reserva, se elimino (Reserva) en Main.
        Reserva ana = s.buscar(r1);
        System.out.println("Precio Ana (socio): " + ana.precio);

        s.cancelar(r1);
        System.out.println("Cancelar 1: correcto");

        // TECNICA UTILIZADA: Replace Error Code with Exception
        // REFACTOR: Se captura NoSuchElementException para informar del error
        // de una reserva inexistente sin devolver ni mostrar un numero negativo.
        try {
            s.cancelar(99);
            System.out.println("Cancelar 99: correcto");
        } catch (NoSuchElementException e) {
            System.out.println("Cancelar 99: error controlado - " + e.getMessage());
        }
    }
}
