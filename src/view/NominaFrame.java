package view;

import controller.NominaController;
import model.*;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class NominaFrame extends javax.swing.JFrame {

    private final NominaController controller;

    public NominaFrame(NominaController controller) {
        this.controller = controller;
        initComponents();

        setTitle("Sistema de Nómina - Universidad");
        setSize(800, 500);
        setLocationRelativeTo(null);

        actualizarEstadoCamposProfesor();
        imprimirLinea("Sistema listo. Usa las pestañas para registrar y gestionar.");
    }

    private void imprimirLinea(String msg) {
        txaSalida.append(msg + "\n");
    }

    private void limpiarSalida() {
        txaSalida.setText("");
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private LocalDate leerFecha(String texto) {
        return LocalDate.parse(texto.trim());
    }

    private int leerEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser un entero.");
        }
    }

    private double leerDouble(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser un número (double).");
        }
    }

    private boolean estaVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void actualizarEstadoCamposProfesor() {
        String tipo = (String) cmbTipoEmpleado.getSelectedItem();
        boolean esProfesor = tipo != null && tipo.startsWith("Profesor");

        lblPerfil.setEnabled(esProfesor);
        txtPerfil.setEnabled(esProfesor);

        if (!esProfesor) {
            txtPerfil.setText("");
        }
    }

    private void listarEmpleadosEnSalida() {
        limpiarSalida();
        List<Empleado> lista = controller.listarEmpleados();

        if (lista.isEmpty()) {
            imprimirLinea("No hay empleados registrados.");
            return;
        }

        imprimirLinea("=== LISTA DE EMPLEADOS ===");
        for (Empleado e : lista) {
            imprimirLinea(e.toString());
            imprimirLinea("----------------------------------------");
        }
    }

    private void cmbTipoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarEstadoCamposProfesor();
    }

    private void btnRegistrarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String tipo = (String) cmbTipoEmpleado.getSelectedItem();

            String id = txtId.getText();
            String nombres = txtNombres.getText();
            String apellidos = txtApellidos.getText();
            String cargo = txtCargo.getText();
            LocalDate fechaIngreso = leerFecha(txtFechaIngreso.getText());
            double salarioBase = leerDouble(txtSalarioBase.getText(), "Salario Base");

            if (estaVacio(id) || estaVacio(nombres) || estaVacio(apellidos) || estaVacio(cargo)) {
                throw new IllegalArgumentException("Completa ID, Nombres, Apellidos y Cargo.");
            }
            if (salarioBase < 0) {
                throw new IllegalArgumentException("Salario Base no puede ser negativo.");
            }

            if ("Administrativo".equals(tipo)) {
                controller.crearAdministrativo(id, nombres, apellidos, fechaIngreso, cargo, salarioBase);
                mostrarInfo("Administrativo registrado.");
            } else {
                String perfil = txtPerfil.getText();
                if (estaVacio(perfil)) {
                    throw new IllegalArgumentException("Para profesor debes ingresar el Perfil.");
                }

                if ("ProfesorCatedratico".equals(tipo)) {
                    controller.crearProfesorCatedratico(id, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
                } else if ("ProfesorOcasional".equals(tipo)) {
                    controller.crearProfesorOcasional(id, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
                } else if ("ProfesorAsociado".equals(tipo)) {
                    controller.crearProfesorAsociado(id, nombres, apellidos, fechaIngreso, cargo, salarioBase, perfil);
                } else {
                    throw new IllegalArgumentException("Tipo de empleado inválido.");
                }

                mostrarInfo("Profesor registrado.");
            }

            imprimirLinea("Registrado: " + id + " (" + tipo + ")");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnListarEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {
        listarEmpleadosEnSalida();
    }

    private void btnBuscarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String id = txtBuscarId.getText();
            if (estaVacio(id)) throw new IllegalArgumentException("Ingresa un ID para buscar.");

            Empleado e = controller.buscarEmpleado(id);
            limpiarSalida();

            if (e == null) {
                imprimirLinea("No se encontró empleado con ID: " + id);
                return;
            }

            imprimirLinea("=== EMPLEADO ENCONTRADO ===");
            imprimirLinea(e.toString());
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnAsignarAsignaturaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String profesorId = txtProfesorIdAsig.getText();
            if (estaVacio(profesorId)) throw new IllegalArgumentException("Ingresa el ID del profesor.");

            String cod = txtAsigCodigo.getText();
            String nom = txtAsigNombre.getText();
            int cred = leerEntero(txtAsigCreditos.getText(), "Créditos");
            int horas = leerEntero(txtAsigHoras.getText(), "Horas");

            if (estaVacio(cod) || estaVacio(nom)) {
                throw new IllegalArgumentException("Completa código y nombre de la asignatura.");
            }

            Asignatura a = new Asignatura(cod, nom, cred, horas);
            boolean ok = controller.asignarAsignatura(profesorId, a);

            if (!ok) {
                throw new IllegalArgumentException("No se pudo asignar: el ID no existe o no es profesor.");
            }

            imprimirLinea("Asignatura asignada a profesor " + profesorId + ": " + a);
            mostrarInfo("Asignatura asignada.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnAgregarProyectoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String profesorId = txtProfesorIdProy.getText();
            if (estaVacio(profesorId)) throw new IllegalArgumentException("Ingresa el ID del profesor.");

            String nombre = txtProyectoNombre.getText();
            String tipo = txtProyectoTipo.getText();

            if (estaVacio(nombre) || estaVacio(tipo)) {
                throw new IllegalArgumentException("Completa nombre y tipo del proyecto.");
            }

            Proyecto p = new Proyecto(nombre, tipo);
            boolean ok = controller.agregarProyecto(profesorId, p);

            if (!ok) {
                throw new IllegalArgumentException("No se pudo agregar proyecto: el ID no existe o no es profesor.");
            }

            imprimirLinea("Proyecto agregado a profesor " + profesorId + ": " + p);
            mostrarInfo("Proyecto agregado.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String profesorId = txtProfesorIdProd.getText();
            String nombreProyecto = txtProdProyecto.getText();

            if (estaVacio(profesorId) || estaVacio(nombreProyecto)) {
                throw new IllegalArgumentException("Ingresa ID del profesor y nombre del proyecto.");
            }

            String nombreProd = txtProdNombre.getText();
            String tipoProd = txtProdTipo.getText();
            int puntos = leerEntero(txtProdPuntos.getText(), "Puntos");

            if (estaVacio(nombreProd) || estaVacio(tipoProd)) {
                throw new IllegalArgumentException("Completa nombre y tipo del producto.");
            }
            if (puntos < 0) {
                throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
            }

            Producto prod = new Producto(nombreProd, tipoProd, puntos);
            boolean ok = controller.agregarProductoAProyecto(profesorId, nombreProyecto, prod);

            if (!ok) {
                throw new IllegalArgumentException("No se pudo agregar producto: revisa ID profesor o nombre del proyecto.");
            }

            imprimirLinea("Producto agregado a proyecto '" + nombreProyecto + "' del profesor " + profesorId + ": " + prod);
            mostrarInfo("Producto agregado.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnTotalNominaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            double total = controller.calcularNominaTotal();
            imprimirLinea("TOTAL NÓMINA = " + total);
            mostrarInfo("Total nómina: " + total);
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void btnLimpiarSalidaActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarSalida();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        tabPrincipal = new javax.swing.JTabbedPane();
        pnlEmpleados = new javax.swing.JPanel();
        lblTipo = new javax.swing.JLabel();
        cmbTipoEmpleado = new javax.swing.JComboBox<>();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblNombres = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        lblApellidos = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblFechaIngreso = new javax.swing.JLabel();
        txtFechaIngreso = new javax.swing.JTextField();
        lblCargo = new javax.swing.JLabel();
        txtCargo = new javax.swing.JTextField();
        lblSalarioBase = new javax.swing.JLabel();
        txtSalarioBase = new javax.swing.JTextField();
        lblPerfil = new javax.swing.JLabel();
        txtPerfil = new javax.swing.JTextField();
        btnRegistrarEmpleado = new javax.swing.JButton();
        sep1 = new javax.swing.JSeparator();
        lblBuscar = new javax.swing.JLabel();
        txtBuscarId = new javax.swing.JTextField();
        btnBuscarEmpleado = new javax.swing.JButton();
        btnListarEmpleados = new javax.swing.JButton();
        btnTotalNomina = new javax.swing.JButton();

        pnlAsignaturas = new javax.swing.JPanel();
        lblProfesorIdAsig = new javax.swing.JLabel();
        txtProfesorIdAsig = new javax.swing.JTextField();
        lblAsigCodigo = new javax.swing.JLabel();
        txtAsigCodigo = new javax.swing.JTextField();
        lblAsigNombre = new javax.swing.JLabel();
        txtAsigNombre = new javax.swing.JTextField();
        lblAsigCreditos = new javax.swing.JLabel();
        txtAsigCreditos = new javax.swing.JTextField();
        lblAsigHoras = new javax.swing.JLabel();
        txtAsigHoras = new javax.swing.JTextField();
        btnAsignarAsignatura = new javax.swing.JButton();

        pnlProyectos = new javax.swing.JPanel();
        lblProfesorIdProy = new javax.swing.JLabel();
        txtProfesorIdProy = new javax.swing.JTextField();
        lblProyectoNombre = new javax.swing.JLabel();
        txtProyectoNombre = new javax.swing.JTextField();
        lblProyectoTipo = new javax.swing.JLabel();
        txtProyectoTipo = new javax.swing.JTextField();
        btnAgregarProyecto = new javax.swing.JButton();
        sep2 = new javax.swing.JSeparator();
        lblProfesorIdProd = new javax.swing.JLabel();
        txtProfesorIdProd = new javax.swing.JTextField();
        lblProdProyecto = new javax.swing.JLabel();
        txtProdProyecto = new javax.swing.JTextField();
        lblProdNombre = new javax.swing.JLabel();
        txtProdNombre = new javax.swing.JTextField();
        lblProdTipo = new javax.swing.JLabel();
        txtProdTipo = new javax.swing.JTextField();
        lblProdPuntos = new javax.swing.JLabel();
        txtProdPuntos = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();

        pnlSalida = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaSalida = new javax.swing.JTextArea();
        btnLimpiarSalida = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTipo.setText("Tipo:");

        cmbTipoEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Administrativo", "ProfesorCatedratico", "ProfesorOcasional", "ProfesorAsociado" }
        ));
        cmbTipoEmpleado.addActionListener(this::cmbTipoEmpleadoActionPerformed);

        lblId.setText("ID:");
        lblNombres.setText("Nombres:");
        lblApellidos.setText("Apellidos:");
        lblFechaIngreso.setText("Fecha Ingreso (yyyy-mm-dd):");
        lblCargo.setText("Cargo:");
        lblSalarioBase.setText("Salario Base:");
        lblPerfil.setText("Perfil (solo profesor):");

        btnRegistrarEmpleado.setText("Registrar");
        btnRegistrarEmpleado.addActionListener(this::btnRegistrarEmpleadoActionPerformed);

        lblBuscar.setText("Buscar por ID:");
        btnBuscarEmpleado.setText("Buscar");
        btnBuscarEmpleado.addActionListener(this::btnBuscarEmpleadoActionPerformed);

        btnListarEmpleados.setText("Listar");
        btnListarEmpleados.addActionListener(this::btnListarEmpleadosActionPerformed);

        btnTotalNomina.setText("Total Nómina");
        btnTotalNomina.addActionListener(this::btnTotalNominaActionPerformed);

        javax.swing.GroupLayout pnlEmpleadosLayout = new javax.swing.GroupLayout(pnlEmpleados);
        pnlEmpleados.setLayout(pnlEmpleadosLayout);
        pnlEmpleadosLayout.setHorizontalGroup(
            pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sep1)
                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                        .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblTipo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbTipoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblId)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtId))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblNombres)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNombres))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblApellidos)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtApellidos))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblFechaIngreso)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblCargo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCargo))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblSalarioBase)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtSalarioBase, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                        .addComponent(lblPerfil)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPerfil)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnRegistrarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                                .addComponent(lblBuscar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnListarEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTotalNomina, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 105, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlEmpleadosLayout.setVerticalGroup(
            pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmpleadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipo)
                    .addComponent(cmbTipoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombres)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidos)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaIngreso)
                    .addComponent(txtFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCargo)
                    .addComponent(txtCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalarioBase)
                    .addComponent(txtSalarioBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPerfil)
                    .addComponent(txtPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sep1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlEmpleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(txtBuscarId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnListarEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTotalNomina, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );

        lblProfesorIdAsig.setText("ID Profesor:");
        lblAsigCodigo.setText("Código:");
        lblAsigNombre.setText("Nombre:");
        lblAsigCreditos.setText("Créditos:");
        lblAsigHoras.setText("Horas:");

        btnAsignarAsignatura.setText("Asignar Asignatura");
        btnAsignarAsignatura.addActionListener(this::btnAsignarAsignaturaActionPerformed);

        javax.swing.GroupLayout pnlAsignaturasLayout = new javax.swing.GroupLayout(pnlAsignaturas);
        pnlAsignaturas.setLayout(pnlAsignaturasLayout);
        pnlAsignaturasLayout.setHorizontalGroup(
            pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                        .addComponent(lblProfesorIdAsig)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProfesorIdAsig, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                        .addComponent(lblAsigCodigo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAsigCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                        .addComponent(lblAsigNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAsigNombre))
                    .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                        .addComponent(lblAsigCreditos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAsigCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                        .addComponent(lblAsigHoras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAsigHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAsignarAsignatura, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(495, Short.MAX_VALUE))
        );
        pnlAsignaturasLayout.setVerticalGroup(
            pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAsignaturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProfesorIdAsig)
                    .addComponent(txtProfesorIdAsig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAsigCodigo)
                    .addComponent(txtAsigCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAsigNombre)
                    .addComponent(txtAsigNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAsigCreditos)
                    .addComponent(txtAsigCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlAsignaturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAsigHoras)
                    .addComponent(txtAsigHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAsignarAsignatura, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(292, Short.MAX_VALUE))
        );

        lblProfesorIdProy.setText("ID Profesor:");
        lblProyectoNombre.setText("Nombre Proyecto:");
        lblProyectoTipo.setText("Tipo Proyecto:");

        btnAgregarProyecto.setText("Agregar Proyecto");
        btnAgregarProyecto.addActionListener(this::btnAgregarProyectoActionPerformed);

        lblProfesorIdProd.setText("ID Profesor:");
        lblProdProyecto.setText("Proyecto (nombre):");
        lblProdNombre.setText("Nombre Producto:");
        lblProdTipo.setText("Tipo Producto:");
        lblProdPuntos.setText("Puntos:");

        btnAgregarProducto.setText("Agregar Producto");
        btnAgregarProducto.addActionListener(this::btnAgregarProductoActionPerformed);

        javax.swing.GroupLayout pnlProyectosLayout = new javax.swing.GroupLayout(pnlProyectos);
        pnlProyectos.setLayout(pnlProyectosLayout);
        pnlProyectosLayout.setHorizontalGroup(
            pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProyectosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProfesorIdProy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProfesorIdProy, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProyectoNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProyectoNombre))
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProyectoTipo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProyectoTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAgregarProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sep2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProfesorIdProd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProfesorIdProd, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProdProyecto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProdProyecto))
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProdNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProdNombre))
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProdTipo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProdTipo))
                    .addGroup(pnlProyectosLayout.createSequentialGroup()
                        .addComponent(lblProdPuntos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProdPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(354, Short.MAX_VALUE))
        );
        pnlProyectosLayout.setVerticalGroup(
            pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProyectosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProfesorIdProy)
                    .addComponent(txtProfesorIdProy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProyectoNombre)
                    .addComponent(txtProyectoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProyectoTipo)
                    .addComponent(txtProyectoTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sep2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProfesorIdProd)
                    .addComponent(txtProfesorIdProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProdProyecto)
                    .addComponent(txtProdProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProdNombre)
                    .addComponent(txtProdNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProdTipo)
                    .addComponent(txtProdTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlProyectosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProdPuntos)
                    .addComponent(txtProdPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        pnlSalida.setBorder(javax.swing.BorderFactory.createTitledBorder("Salida"));

        txaSalida.setColumns(20);
        txaSalida.setRows(5);
        txaSalida.setEditable(false);
        jScrollPane1.setViewportView(txaSalida);

        btnLimpiarSalida.setText("Limpiar");
        btnLimpiarSalida.addActionListener(this::btnLimpiarSalidaActionPerformed);

        javax.swing.GroupLayout pnlSalidaLayout = new javax.swing.GroupLayout(pnlSalida);
        pnlSalida.setLayout(pnlSalidaLayout);
        pnlSalidaLayout.setHorizontalGroup(
            pnlSalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSalidaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(pnlSalidaLayout.createSequentialGroup()
                        .addComponent(btnLimpiarSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSalidaLayout.setVerticalGroup(
            pnlSalidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSalidaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiarSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabPrincipal.addTab("Empleados", pnlEmpleados);
        tabPrincipal.addTab("Asignaturas", pnlAsignaturas);
        tabPrincipal.addTab("Proyectos/Productos", pnlProyectos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPrincipal)
            .addComponent(pnlSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnAgregarProyecto;
    private javax.swing.JButton btnAsignarAsignatura;
    private javax.swing.JButton btnBuscarEmpleado;
    private javax.swing.JButton btnLimpiarSalida;
    private javax.swing.JButton btnListarEmpleados;
    private javax.swing.JButton btnRegistrarEmpleado;
    private javax.swing.JButton btnTotalNomina;
    private javax.swing.JComboBox<String> cmbTipoEmpleado;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblAsigCodigo;
    private javax.swing.JLabel lblAsigCreditos;
    private javax.swing.JLabel lblAsigHoras;
    private javax.swing.JLabel lblAsigNombre;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblFechaIngreso;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblPerfil;
    private javax.swing.JLabel lblProdNombre;
    private javax.swing.JLabel lblProdProyecto;
    private javax.swing.JLabel lblProdPuntos;
    private javax.swing.JLabel lblProdTipo;
    private javax.swing.JLabel lblProfesorIdAsig;
    private javax.swing.JLabel lblProfesorIdProd;
    private javax.swing.JLabel lblProfesorIdProy;
    private javax.swing.JLabel lblProyectoNombre;
    private javax.swing.JLabel lblProyectoTipo;
    private javax.swing.JLabel lblSalarioBase;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JPanel pnlAsignaturas;
    private javax.swing.JPanel pnlEmpleados;
    private javax.swing.JPanel pnlProyectos;
    private javax.swing.JPanel pnlSalida;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTextArea txaSalida;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtAsigCodigo;
    private javax.swing.JTextField txtAsigCreditos;
    private javax.swing.JTextField txtAsigHoras;
    private javax.swing.JTextField txtAsigNombre;
    private javax.swing.JTextField txtBuscarId;
    private javax.swing.JTextField txtCargo;
    private javax.swing.JTextField txtFechaIngreso;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtPerfil;
    private javax.swing.JTextField txtProdNombre;
    private javax.swing.JTextField txtProdProyecto;
    private javax.swing.JTextField txtProdPuntos;
    private javax.swing.JTextField txtProdTipo;
    private javax.swing.JTextField txtProfesorIdAsig;
    private javax.swing.JTextField txtProfesorIdProd;
    private javax.swing.JTextField txtProfesorIdProy;
    private javax.swing.JTextField txtProyectoNombre;
    private javax.swing.JTextField txtProyectoTipo;
    private javax.swing.JTextField txtSalarioBase;
}
