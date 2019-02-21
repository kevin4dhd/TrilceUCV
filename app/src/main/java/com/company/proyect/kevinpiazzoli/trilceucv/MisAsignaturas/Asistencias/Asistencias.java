package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Asistencias;

/**
 * Created by KevinPiazzoli on 03/12/2016.
 */

public class Asistencias {

    private String semana;
    private String Fecha;
    private String HoraDeLlegada;
    private String HoraDelCurso;
    private String Estado;

    public Asistencias() {
    }

    public String getSemana() {
        return semana;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHoraDeLlegada() {
        return HoraDeLlegada;
    }

    public void setHoraDeLlegada(String horaDeLlegada) {
        HoraDeLlegada = horaDeLlegada;
    }

    public String getHoraDelCurso() {
        return HoraDelCurso;
    }

    public void setHoraDelCurso(String horaDelCurso) {
        HoraDelCurso = horaDelCurso;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
