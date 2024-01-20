package es.riberadeltajo.tarea_4_raul_rivas_2;

public class Contacto {

    public int id;
    public String nombre;
    public String apellidos;
    public String tlf;

    public Contacto(int id, String nombre, String apellidos, String tlf) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tlf = tlf;
    }
    public Contacto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", tieneTlf=" + tlf +
                '}';
    }
}
