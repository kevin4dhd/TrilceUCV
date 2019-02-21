package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas;

/**
 * Created by KevinPiazzoli on 01/11/2016.
 */

public class Subdatos_Asignaturas_Matriculadas {
    private String Curso;
    private String Docente;
    private String Ciclo;
    private String CodigoDelCurso;
    private String Dia;
    private String Hora;
    private String Aula;
    private String FechaProx;
    private String WeekendDay;
    private boolean Header = false;
    private boolean Feriado = false;

    public Subdatos_Asignaturas_Matriculadas() {}

    String getCurso() {
        return Curso;
    }

    public void setCurso(String curso) {
        Curso = curso;
    }

    String getDocente() {
        return Docente;
    }

    public void setDocente(String docente) {
        Docente = docente;
    }

    String getCiclo() {
        return Ciclo;
    }

    public void setCiclo(String ciclo) {
        Ciclo = ciclo;
    }

    String getCodigoDelCurso() {
        return CodigoDelCurso;
    }

    public void setCodigoDelCurso(String codigoDelCurso) {
        CodigoDelCurso = codigoDelCurso;
    }

    String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        String HoraTransformada;
        String HoraAuxi[] = hora.split("-");
        String HoraNoMinuteC[] = HoraAuxi[0].split(":");
        String HoraNoMinuteF[] = HoraAuxi[1].split(":");

        int HoraC = Integer.parseInt(HoraNoMinuteC[0].replaceAll(" ", ""));

        if(HoraC - 12 >= 0 ) if(HoraC!=12) HoraTransformada = (HoraC - 12) + ":" + HoraNoMinuteC[1] + " PM";
                             else HoraTransformada = HoraC + ":" +HoraNoMinuteC[1] + " PM";
        else HoraTransformada = HoraAuxi[0] + " AM";

        int HoraF = Integer.parseInt(HoraNoMinuteF[0].replaceAll(" ", ""));
        if( HoraF- 12 >= 0 ) if(HoraF!=12) HoraTransformada = HoraTransformada + " - " + (HoraF-12) + ":" +HoraNoMinuteF[1] + " PM";
                             else HoraTransformada = HoraTransformada + " - " + HoraF + ":" +HoraNoMinuteF[1] + " PM";
        else HoraTransformada = HoraTransformada + " - " +  HoraAuxi[1] + " AM";

        Hora = HoraTransformada;
    }

    String getAula() {
        return Aula;
    }

    public void setAula(String aula) {
        Aula = aula;
    }

    boolean isHeader() {
        return Header;
    }

    void setHeader(boolean header) {
        Header = header;
    }

    String getFechaProx() {
        return FechaProx;
    }

    void setFechaProx(String fechaProx) {
        FechaProx = fechaProx;
    }

    public boolean isFeriado() {
        return Feriado;
    }

    public void setFeriado(boolean feriado) {
        Feriado = feriado;
    }

    public String getWeekendDay() {
        return WeekendDay;
    }

    public void setWeekendDay(String weekendDay) {
        WeekendDay = weekendDay;
    }
}
