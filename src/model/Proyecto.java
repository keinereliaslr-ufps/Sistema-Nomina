package model;

import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private String nombre;
    private String tipo; // investigación / extensión
    private List<Producto> productos;

    public Proyecto(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        if (p != null && p.validarPuntos()) {
            productos.add(p);
        }
    }

    public int calcularPuntosProyecto() {
        int total = 0;
        for (Producto p : productos) {
            total += p.getPuntos();
        }
        return total;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public List<Producto> getProductos() { return productos; }

    @Override
    public String toString() {
        return "Proyecto{" +
                "nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", productos=" + productos +
                ", puntosProyecto=" + calcularPuntosProyecto() +
                '}';
    }
}
