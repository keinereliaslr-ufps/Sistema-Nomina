package controller;

import model.*;

import java.time.LocalDate;
import java.util.List;

public class NominaController {
    private NominaService service;

    public NominaController(NominaService service) {
        this.service = service;
    }

    public void crearAdministrativo(String id, String nombres, String apellidos,
                                    LocalDate fechaIngreso, String cargo, double salarioBase) {
        Administrativo a = new Administrativo(id, nombres, apellidos, fechaIngreso, cargo, salarioBase);
        service.registrarEmpleado(a);
    }

    public void crearProfesorCatedratico(String id, String nombres, String apellidos,
                                         LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        ProfesorCatedratico p = new ProfesorCatedratico(id, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
        service.registrarEmpleado(p);
    }

    public void crearProfesorOcasional(String id, String nombres, String apellidos,
                                       LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        ProfesorOcasional p = new ProfesorOcasional(id, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
        service.registrarEmpleado(p);
    }

    public void crearProfesorAsociado(String id, String nombres, String apellidos,
                                      LocalDate fechaIngreso, String cargo, double salarioBase, String perfil) {
        ProfesorAsociado p = new ProfesorAsociado(id, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
        service.registrarEmpleado(p);
    }

    public boolean asignarAsignatura(String profesorId, Asignatura asignatura) {
        Empleado e = service.buscarEmpleadoPorId(profesorId);
        if (e instanceof Profesor) {
            ((Profesor) e).agregarAsignatura(asignatura);
            return true;
        }
        return false;
    }

    public boolean agregarProyecto(String profesorId, Proyecto proyecto) {
        Empleado e = service.buscarEmpleadoPorId(profesorId);
        if (e instanceof Profesor) {
            ((Profesor) e).agregarProyecto(proyecto);
            return true;
        }
        return false;
    }

    public boolean agregarProductoAProyecto(String profesorId, String nombreProyecto, Producto producto) {
        Empleado e = service.buscarEmpleadoPorId(profesorId);
        if (!(e instanceof Profesor)) return false;

        Profesor p = (Profesor) e;
        for (Proyecto proy : p.getProyectos()) {
            if (proy.getNombre().equalsIgnoreCase(nombreProyecto)) {
                proy.agregarProducto(producto);
                return true;
            }
        }
        return false;
    }

    public Empleado buscarEmpleado(String id) {
        return service.buscarEmpleadoPorId(id);
    }

    public List<Empleado> listarEmpleados() {
        return service.listarEmpleados();
    }

    public double calcularNominaTotal() {
        return service.calcularNominaTotal();
    }
}
