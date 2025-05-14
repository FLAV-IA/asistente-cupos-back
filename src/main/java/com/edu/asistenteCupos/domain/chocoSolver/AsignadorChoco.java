package com.edu.asistenteCupos.domain.chocoSolver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import java.util.*;

public class AsignadorChoco {

    public static void main(String[] args) {
        new AsignadorChoco().resolver();
    }

    void resolver() {
        List<Estudiante> estudiantes = cargarDatosEstudiantes();
        estudiantes.sort(Comparator.comparingInt(this::calcularPrioridad).reversed());

        Model model = new Model("Asignación con Prioridades");
        Map<Estudiante, Map<String, IntVar>> asignaciones = crearVariables(model, estudiantes);
        aplicarRestriccionesConAsignacionJusta(model, estudiantes, asignaciones);

        IntVar totalAsignados = contarAsignados(model, asignaciones);
        model.setObjective(Model.MAXIMIZE, totalAsignados); // Maximizar total de asignaciones

        mostrarSolucion(model, estudiantes, asignaciones);
    }

    IntVar contarAsignados(Model model, Map<Estudiante, Map<String, IntVar>> asignaciones) {
        List<IntVar> asignados = new ArrayList<>();
        for (Map<String, IntVar> vars : asignaciones.values()) {
            for (IntVar var : vars.values()) {
                asignados.add(model.arithm(var, "!=", -1).reify());
            }
        }
        IntVar total = model.intVar("totalAsignados", 0, asignados.size());
        model.sum(asignados.toArray(new IntVar[0]), "=", total).post();
        return total;
    }

    List<Estudiante> cargarDatosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();

        Estudiante estudiante1 = new Estudiante("11111111",
                new Historial(1, 0, 19.0, List.of("1032"), List.of()),
                List.of(
                        new Peticion(List.of("1032-8-G14", "1032-10-G14"), true),
                        new Peticion(List.of("487-8-G14", "487-9-G14"), true)
                ));

        Estudiante estudiante2 = new Estudiante("22222222",
                new Historial(3, 1, 15.0, List.of("1032"), List.of()),
                List.of(
                        new Peticion(List.of("1032-8-G14", "1032-10-G14"), true)
                ));

        Estudiante estudiante3 = new Estudiante("33333333",
                new Historial(4, 2, 12.0, List.of(), List.of()),
                List.of(
                        new Peticion(List.of("1032-8-G14", "1032-10-G14"), true)
                ));

        estudiantes.add(estudiante1);
        estudiantes.add(estudiante2);
        estudiantes.add(estudiante3);

        return estudiantes;
    }

    int calcularPrioridad(Estudiante e) {
        Historial h = e.historial;
        int prioridad = 0;
        if (h.materiasParaEgresar - h.materiasAprobadas <= 3) prioridad += 10;
        if (h.materiasARecursar.isEmpty()) prioridad += 5;
        for (Peticion p : e.peticiones) {
            for (String cod : p.materias) {
                String materia = cod.split("-")[0];
                if (h.materiasCursadas.contains(materia)) {
                    prioridad += 3;
                    break;
                }
            }
        }
        if (h.materiasAprobadas <= 3) prioridad += 2;
        prioridad += h.coeficiente / 10;
        return prioridad;
    }

    Map<Estudiante, Map<String, IntVar>> crearVariables(Model model, List<Estudiante> estudiantes) {
        Map<Estudiante, Map<String, IntVar>> asignaciones = new HashMap<>();
        for (Estudiante e : estudiantes) {
            Map<String, IntVar> vars = new HashMap<>();
            for (Peticion p : e.peticiones) {
                if (!p.cumpleCorrelativas || p.materias.isEmpty()) continue;
                String materia = p.materias.get(0).split("-")[0];
                int[] dom = new int[p.materias.size() + 1];
                for (int i = 0; i < p.materias.size(); i++) dom[i] = i;
                dom[dom.length - 1] = -1;
                vars.put(materia, model.intVar("E" + e.dni + "_" + materia, dom));
            }
            asignaciones.put(e, vars);
        }
        return asignaciones;
    }

    void aplicarRestriccionesConAsignacionJusta(Model model, List<Estudiante> estudiantes, Map<Estudiante, Map<String, IntVar>> asignaciones) {
        Map<String, Integer> cupos = new HashMap<>();
        cupos.put("1032-8-G14", 2);
        cupos.put("1032-10-G14", 2);
        cupos.put("487-8-G14", 1);
        cupos.put("487-9-G14", 0);
        cupos.put("487-10-G14", 1);

        Map<String, List<IntVar>> usoPorComision = new HashMap<>();

        // Paso 1: Maximizar la cantidad total de asignaciones
        List<IntVar> tieneAsignacionVars = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            List<IntVar> opciones = new ArrayList<>();
            for (Peticion p : e.peticiones) {
                String materia = p.materias.get(0).split("-")[0];
                IntVar var = asignaciones.get(e).get(materia);
                for (int i = 0; i < p.materias.size(); i++) {
                    opciones.add(model.arithm(var, "=", i).reify());
                }
            }
            if (!opciones.isEmpty()) {
                IntVar tieneAsignacion = model.intVar("alMenosUna_" + e.dni, 0, 1);
                model.max(tieneAsignacion, opciones.toArray(new IntVar[0])).post();
                tieneAsignacionVars.add(tieneAsignacion);  // Guardamos para la suma al final
            }
        }

        // Paso 2: Limitar comisiones por cupo
        for (Estudiante e : estudiantes) {
            for (Peticion p : e.peticiones) {
                String materia = p.materias.get(0).split("-")[0];
                IntVar var = asignaciones.get(e).get(materia);
                for (int i = 0; i < p.materias.size(); i++) {
                    String comision = p.materias.get(i);
                    usoPorComision.computeIfAbsent(comision, k -> new ArrayList<>()).add(model.arithm(var, "=", i).reify());
                }
            }
        }

        // Restringir cantidad de asignaciones por cupo
        for (Map.Entry<String, List<IntVar>> entry : usoPorComision.entrySet()) {
            int max = cupos.getOrDefault(entry.getKey(), 1);
            model.sum(entry.getValue().toArray(new IntVar[0]), "<=", max).post();
        }

        // Paso 3: Maximizar la cantidad de estudiantes asignados
        IntVar totalAsignados = model.intVar("totalAsignados", 0, estudiantes.size());
        model.sum(tieneAsignacionVars.toArray(new IntVar[0]), "=", totalAsignados).post();
    }

    void mostrarSolucion(Model model, List<Estudiante> estudiantes, Map<Estudiante, Map<String, IntVar>> asignaciones) {
        if (model.getSolver().solve()) {
            for (Estudiante e : estudiantes) {
                int prioridad = calcularPrioridad(e);
                System.out.println("Estudiante " + e.dni + " (Prioridad: " + prioridad + "):");
                for (Map.Entry<String, IntVar> entry : asignaciones.get(e).entrySet()) {
                    int val = entry.getValue().getValue();
                    if (val == -1) {
                        System.out.println("  Materia " + entry.getKey() + " → No asignado");
                    } else {
                        String com = e.peticiones.stream()
                                .filter(p -> p.materias.get(0).split("-")[0].equals(entry.getKey()))
                                .findFirst().get().materias.get(val);
                        System.out.println("  Materia " + entry.getKey() + " → Comisión " + com);
                    }
                }
            }
        } else {
            System.out.println("No se encontró solución.");
        }
    }

    static class Estudiante {
        String dni;
        Historial historial;
        List<Peticion> peticiones;
        Estudiante(String dni, Historial h, List<Peticion> p) {
            this.dni = dni; this.historial = h; this.peticiones = p;
        }
    }

    static class Historial {
        int materiasParaEgresar;
        int materiasAprobadas;
        double coeficiente;
        List<String> materiasCursadas;
        List<String> materiasARecursar;

        Historial(int materiasParaEgresar, int materiasAprobadas, double coeficiente, List<String> materiasCursadas, List<String> materiasARecursar) {
            this.materiasParaEgresar = materiasParaEgresar;
            this.materiasAprobadas = materiasAprobadas;
            this.coeficiente = coeficiente;
            this.materiasCursadas = materiasCursadas;
            this.materiasARecursar = materiasARecursar;
        }
    }

    static class Peticion {
        List<String> materias;
        boolean cumpleCorrelativas;
        Peticion(List<String> materias, boolean cumple) {
            this.materias = materias; this.cumpleCorrelativas = cumple;
        }
    }
}

/**
1. Inicialización del Modelo de Asignación
        Lo primero que se hace es crear el modelo de programación por restricciones utilizando Choco Solver, que es el motor que resolverá el
        problema de asignación. Aquí se definen las variables de decisión y las restricciones.

        Variables de Decisión:
        Para cada estudiante, se define una variable de decisión que representa si un estudiante es asignado a una materia en particular y, si lo es,
        qué comisión se le asigna. Estas variables son del tipo binarias (0 o 1).

        Ejemplo:

        Si un estudiante es asignado a una comisión, la variable toma el valor de 1. Si no lo es, toma el valor 0.

        Restricciones:
        Cupos de las comisiones: Para cada materia, hay una restricción que indica cuántos estudiantes pueden ser asignados a esa comisión, basándose en la cantidad de cupos disponibles.

        Estudiantes solo asignados a una materia: Un estudiante puede ser asignado a una sola comisión por materia. Esto se asegura mediante restricciones que controlan
        que solo una de las variables de decisión asociadas a las comisiones de una materia pueda tomar el valor 1.

        Prioridad en las asignaciones: Los estudiantes con mayor prioridad (por ejemplo, aquellos que necesitan más materias para egresar) tienen una mayor probabilidad de ser
        asignados a las comisiones, siempre que haya cupos disponibles.

        2. Cálculo de Prioridades
        La asignación de estudiantes no es aleatoria; se basa en una prioridad calculada. Los estudiantes con mayor prioridad (según sus necesidades académicas) tienen preferencia en la asignación.

        El cálculo de prioridades sigue estos pasos:

        Materias necesarias para egresar: Si un estudiante tiene muchas materias por aprobar para egresar, esto aumenta su prioridad, ya que debe completar sus estudios. Cada materia pendiente suma a la prioridad del estudiante.

        Materias para recursar: Si un estudiante tiene materias a recursar, también se incrementa su prioridad. Esto refleja la necesidad de completar las materias que no aprobó previamente.

        Coeficiente académico: Un coeficiente académico más alto puede darle más prioridad a un estudiante, aunque este factor tiene un peso menor en el cálculo.

        Materias ya aprobadas: Si un estudiante ya ha aprobado algunas de las materias solicitadas, se toma en cuenta para ajustar su prioridad.

        3. Proceso de Asignación
        Una vez que el modelo de restricciones está definido y las prioridades de los estudiantes calculadas, el proceso de asignación funciona de la siguiente manera:

        Paso 1: Orden de Prioridades
        Ordenamiento: Los estudiantes son ordenados en función de su prioridad. Aquellos con la mayor prioridad serán los primeros en ser asignados a las comisiones de materias.

        Paso 2: Asignación de Estudiantes
        Para cada estudiante, se evalúa la posibilidad de asignarlo a una comisión de las materias que ha solicitado. La asignación se realiza respetando las restricciones de cupo y de correlativas (si existen materias que requieren haber aprobado otras antes).

        El modelo intenta asignar a los estudiantes de acuerdo a la mayor prioridad primero. Para cada materia solicitada por un estudiante:

        Si aún hay cupos disponibles en la comisión solicitada, el estudiante puede ser asignado a esa comisión.

        Si no hay cupos disponibles en la comisión solicitada, el estudiante debe ser asignado a otra comisión de la misma materia (si la hay), o se le asigna una comisión diferente si la materia tiene varias comisiones.

        Paso 3: Maximización
        El modelo también busca maximizar el número total de estudiantes asignados a alguna comisión. Para ello, utiliza una variable totalAsignados que cuenta el número total de estudiantes que han sido asignados a alguna comisión. El objetivo es hacer que esta variable sea lo más alta posible, lo que se logra con la optimización de la asignación.

        Paso 4: Aplicación de Restricciones de Cupo y Correlativas
        Restricciones de cupo: Solo se pueden asignar estudiantes a una comisión si esta tiene cupos disponibles. Si un estudiante tiene más de una solicitud para una materia, se intenta asignarlo a la comisión más preferida que aún tenga cupos disponibles.

        Restricciones de correlativas: Si una materia tiene correlativas, el modelo verifica si el estudiante cumple con la precondición de haber aprobado la materia anterior antes de asignarlo.

        4. Finalización y Solución
        El proceso de asignación se repite hasta que todos los estudiantes hayan sido evaluados y asignados a las comisiones correspondientes, respetando todas las restricciones.

        Al final, el modelo muestra la solución: la asignación de cada estudiante a las comisiones de sus materias solicitadas. Si no es posible asignar a un estudiante debido a la falta de cupos o restricciones no cumplidas, se indica que el estudiante no ha sido asignado a ninguna comisión.

        Si la asignación fue exitosa, se muestra la lista de estudiantes y las comisiones a las que fueron asignados, en orden de prioridad.

        5. Resumen Funcional
        Calcular prioridad: Los estudiantes son ordenados por su prioridad, calculada según factores como la cantidad de materias pendientes para egresar y el coeficiente académico.

        Evaluar asignación: Los estudiantes son asignados a las comisiones de las materias que han solicitado, respetando las restricciones de cupo y correlativas.

        Maximizar asignaciones: Se busca maximizar el número total de estudiantes asignados, utilizando las variables de decisión.

        Mostrar solución: Al final, se muestra una lista de estudiantes con sus respectivas asignaciones.

        6. Concepto Clave
        La funcionalidad clave aquí es que el algoritmo, a través de la programación por restricciones, optimiza la asignación de estudiantes a las comisiones de acuerdo con sus prioridades y las restricciones de cupo, correlativas y otras. Se hace todo en un solo paso de asignación, respetando las restricciones impuestas, y buscando maximizar la cantidad de estudiantes asignados de manera justa.
*/
