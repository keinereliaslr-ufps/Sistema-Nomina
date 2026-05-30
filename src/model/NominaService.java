package model;

import java.util.ArrayList;
import java.util.List;

public class NominaService {
    private List<Empleado> empleados;

    public NominaService() {
        this.empleados = new ArrayList<>();
    }

    public void registrarEmpleado(Empleado e) {
        if (e != null) empleados.add(e);
    }

    public List<Empleado> listarEmpleados() {
        return empleados;
    }

    public Empleado buscarEmpleadoPorId(String id) {
        for (Empleado e : empleados) {
            if (e.getIdentificacion().equals(id)) return e;
        }
        return null;
    }

    public double calcularNominaTotal() {
        double total = 0;
        for (Empleado e : empleados) {
            total += e.calcularSalarioTotal();
        }
        return total;
    }
}
