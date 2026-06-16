import java.io.Serializable;

public class Cliente implements Serializable {
    private String clienteId;
    private String nombre;
    private String telefono;
    private String email;

    public Cliente(String clienteId, String nombre, String telefono, String email){
        this.clienteId=clienteId;
        this.nombre=nombre;
        this.telefono=telefono;
        this.email=email;
    }
    public String getClienteId() { return clienteId; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono(){return telefono;}
    public String toString() {
        return "ID: " + clienteId + ", Nombre: " + nombre + ", Email: " + email+", Telefono:"+telefono;
    }
}
