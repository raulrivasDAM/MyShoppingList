package es.riberadeltajo.tarea_4_raul_rivas_2;

import java.util.ArrayList;

import es.riberadeltajo.tarea_4_raul_rivas_2.Producto;

public class ProductosListas {
    public String nombreLista;
    public  static ArrayList<Producto> miListaProductos = new ArrayList<>();

    public int id;
    public ProductosListas(int id, String nombreLista, ArrayList<Producto> miListaProductos) {
        this.id = id;
        this.nombreLista = nombreLista;
        this.miListaProductos = miListaProductos;
    }

    public ProductosListas() {

    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public ArrayList<Producto> getMiListaProductos() {
        return miListaProductos;
    }

    public void setMiListaProductos(ArrayList<Producto> miListaProductos) {
        this.miListaProductos = miListaProductos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductosListas{" +
                "nombreLista='" + nombreLista + '\'' +
                ", id=" + id +
                '}';
    }
}
