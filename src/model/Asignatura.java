package model;

public class Asignatura {
    private String codigo;
    private String nombre;
    private int creditos;
    private int horas;

    public Asignatura(String codigo, String nombre, int creditos, int horas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horas = horas;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }

    @Override
    public String toString() {
        return "Asignatura{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", creditos=" + creditos +
                ", horas=" + horas +
                '}';
    }
}
