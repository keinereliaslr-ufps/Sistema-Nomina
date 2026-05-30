# Sistema-Nomina (MVC - NetBeans Ant)

Proyecto académico (POO1) para gestionar empleados universitarios y calcular nómina.

## Estructura (MVC)
- `src/model`    → clases del dominio (Empleado, Profesor, etc.)
- `src/controller` → controlador (`NominaController`)
- `src/view`     → vista Swing (`NominaFrame`) con salida en `JTextArea`
- `src/app`      → arranque (`Main`) que conecta todo

## Requisitos
- JDK 8+ (recomendado JDK 8/11)
- NetBeans (proyecto tipo Ant)

## Cómo ejecutar en NetBeans (Ant)
1. Abre NetBeans.
2. `File > Open Project...` y selecciona la carpeta del repo.
3. Ejecuta el proyecto (`Run Project`).

## Notas
- No hay persistencia: todo se guarda en memoria usando `ArrayList`.
- La vista no calcula nómina; solo captura datos y llama al controlador.
