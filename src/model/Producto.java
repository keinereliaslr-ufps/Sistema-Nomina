package model;

public class Producto {
    private String nombre;
    private String tipo;
    private int puntos;

    public Producto(String nombre, String tipo, int puntos) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.puntos = puntos;
    }

    public boolean validarPuntos() {
        return puntos >= 0;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", puntos=" + puntos +
                '}';
    }
}
