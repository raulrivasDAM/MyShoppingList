package es.riberadeltajo.tarea_4_raul_rivas_2;

import android.graphics.Bitmap;

public class Producto {
    public Bitmap img;
    public String nombreProducto;
    public Double precio;
    public String descripcion;

    public double cantidad;
    public int cod;


    public Producto(Bitmap img, String nombreProducto, Double precio, String descripcion) {
        this.img = img;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.descripcion = descripcion;
    }
    public Producto(Bitmap img, String nombreProducto, Double precio, String descripcion, double cantidad) {
        this.img = img;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.descripcion = descripcion;
        this.cantidad = cantidad;

    }
    public Producto(Bitmap img, String nombreProducto,  String descripcion) {
        this.img = img;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;

    }
    public Producto(Bitmap img, String nombreProducto,  String descripcion, int cod) {
        this.img = img;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.cod = cod;

    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "img=" + img +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidad=" + precio +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
