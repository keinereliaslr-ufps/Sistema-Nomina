package model;

import java.time.LocalDate;

public class Administrativo extends Empleado {

    public Administrativo(String identificacion, String nombres, String apellidos,
                          LocalDate fechaIngreso, String cargo, double salarioBase) {
        super(identificacion, nombres, apellidos, fechaIngreso, cargo, salarioBase);
    }

    @Override
    public double calcularSalarioTotal() {
        // Enunciado no define bonificación para administrativos.
        return getSalarioBase();
    }
}
