import java.io.Serializable;

public class Habitacion implements Serializable {
    private String numero;
    private String tipo;
    private boolean disponible;
    private double precio;

    public Habitacion(String numero, String tipo, double precio){
        this.numero=numero;
        this.tipo=tipo;
        this.precio=precio;
        this.disponible=true;
    }
    public String getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public double getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String toString() {
        return "Habitación N°: " + numero + ", Tipo: " + tipo + ", Precio: $" + precio +
                ", Estado: " + (disponible ? "Disponible" : "Ocupada");
    }
}
