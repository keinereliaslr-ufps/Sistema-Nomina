package model;

import java.time.LocalDate;
import java.time.Period;

public abstract class Empleado {
    private String identificacion;
    private String nombres;
    private String apellidos;
    private LocalDate fechaIngreso;
    private String cargo;
    private double salarioBase;

    public Empleado(String identificacion, String nombres, String apellidos,
                    LocalDate fechaIngreso, String cargo, double salarioBase) {
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaIngreso = fechaIngreso;
        this.cargo = cargo;
        this.salarioBase = salarioBase;
    }

    public int calcularAntiguedad() {
        if (fechaIngreso == null) return 0;
        return Period.between(fechaIngreso, LocalDate.now()).getYears();
    }

    public abstract double calcularSalarioTotal();

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    @Override
    public String toString() {
        return "Empleado{" +
                "id='" + identificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", cargo='" + cargo + '\'' +
                ", salarioBase=" + salarioBase +
                ", antiguedad=" + calcularAntiguedad() +
                ", salarioTotal=" + calcularSalarioTotal() +
                '}';
    }
}
