package model;

import java.time.LocalDate;

public class ProfesorOcasional extends Profesor {
    public ProfesorOcasional(String identificacion, String nombres, String apellidos,
                             LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        super(identificacion, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
    }
}
