package model;

import java.time.LocalDate;

public class ProfesorCatedratico extends Profesor {
    public ProfesorCatedratico(String identificacion, String nombres, String apellidos,
                               LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        super(identificacion, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
    }
}
