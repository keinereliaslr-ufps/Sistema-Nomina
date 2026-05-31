package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Profesor extends Empleado {
    private String perfil;
    private List<Asignatura> asignaturas;
    private List<Proyecto> proyectos;

    public Profesor(String identificacion, String nombres, String apellidos,
                    LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        super(identificacion, nombres, apellidos, fechaIngreso, cargo, salarioBase);
        this.perfil = perfil;
        this.asignaturas = new ArrayList<>();
        this.proyectos = new ArrayList<>();
    }

    public void agregarAsignatura(Asignatura a) {
        if (a != null) asignaturas.add(a);
    }

    public void agregarProyecto(Proyecto p) {
        if (p != null) proyectos.add(p);
    }

    public int calcularPuntos() {
        int total = 0;
        for (Proyecto p : proyectos) {
            total += p.calcularPuntosProyecto();
        }
        return total;
    }

    public double calcularBonificacion() {
        int puntos = calcularPuntos();
        double base = getSalarioBase();

        if (puntos >= 1 && puntos <= 20) return base * 0.05;
        if (puntos >= 21 && puntos <= 40) return base * 0.10;
        if (puntos > 40) return base * 0.15;
        return 0.0;
    }

    @Override
    public double calcularSalarioTotal() {
        return getSalarioBase() + calcularBonificacion();
    }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public List<Asignatura> getAsignaturas() { return asignaturas; }
    public List<Proyecto> getProyectos() { return proyectos; }

    @Override
    public String toString() {
        return "Profesor{" +
                "id='" + getIdentificacion() + '\'' +
                ", nombres='" + getNombres() + " " + getApellidos() + '\'' +
                ", perfil='" + perfil + '\'' +
                ", antiguedad=" + calcularAntiguedad() +
                ", salarioBase=" + getSalarioBase() +
                ", puntos=" + calcularPuntos() +
                ", bonificacion=" + calcularBonificacion() +
                ", salarioTotal=" + calcularSalarioTotal() +
                ", asignaturas=" + asignaturas +
                ", proyectos=" + proyectos +
                '}';
    }
}
