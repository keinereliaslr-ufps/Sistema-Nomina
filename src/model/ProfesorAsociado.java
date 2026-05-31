package model;

import java.time.LocalDate;

public class ProfesorAsociado extends Profesor {
    public ProfesorAsociado(String identificacion, String nombres, String apellidos,
                            LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        super(identificacion, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
    }
}
