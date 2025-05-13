package com.edu.asistenteCupos.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ValidadorHorario {

    public static boolean haySuperposicion(String horario1, String horario2) {
        List<RangoHorario> lista1 = parsearHorarios(horario1);
        List<RangoHorario> lista2 = parsearHorarios(horario2);

        for (RangoHorario r1 : lista1) {
            for (RangoHorario r2 : lista2) {
                if (r1.seSuperpone(r2)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static List<RangoHorario> parsearHorarios(String horarioStr) {
        List<RangoHorario> resultado = new ArrayList<>();
        String[] bloques = horarioStr.split(",");

        for (String bloque : bloques) {
            String[] partes = bloque.trim().split(" ");
            String dia = partes[0];
            LocalTime desde = LocalTime.parse(partes[1]);
            LocalTime hasta = LocalTime.parse(partes[3]);
            resultado.add(new RangoHorario(dia, desde, hasta));
        }

        return resultado;
    }

    static class RangoHorario {
        String dia;
        LocalTime inicio;
        LocalTime fin;

        RangoHorario(String dia, LocalTime inicio, LocalTime fin) {
            this.dia = dia;
            this.inicio = inicio;
            this.fin = fin;
        }

        boolean seSuperpone(RangoHorario otro) {
            return this.dia.equalsIgnoreCase(otro.dia) &&
                    !(this.fin.isBefore(otro.inicio) || this.inicio.isAfter(otro.fin));
        }
    }
}
