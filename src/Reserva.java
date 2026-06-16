import java.time.LocalDate;
import java.io.Serializable;

public class Reserva implements Serializable{
    private static int contadorReservas = 1000;
    private String codigoReserva;
    private Cliente cliente;
    private Habitacion habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double total;

    public Reserva(Cliente Cliente, Habitacion Habitacion, LocalDate fechaInicio, LocalDate fechaFin, double total) {
        this.codigoReserva = "RES-" + contadorReservas++;
        this.cliente = Cliente;
        this.habitacion = Habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.total = total;
    }

    public String getCodigoReserva() { return codigoReserva; }
    public Cliente getCliente() { return cliente; }
    public Habitacion getHabitacion() { return habitacion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public double getTotal() { return total; }

    public String toString() {
        return "Código: " + codigoReserva +
                "\n  Cliente: " + cliente.getNombre() + " (" + cliente.getClienteId() + ")" +
                "\n  Habitación: " + habitacion.getNumero() + " (" + habitacion.getTipo() + ")" +
                "\n  Fechas: " + fechaInicio + " al " + fechaFin +
                "\n  Total: $" + total;
    }
}
