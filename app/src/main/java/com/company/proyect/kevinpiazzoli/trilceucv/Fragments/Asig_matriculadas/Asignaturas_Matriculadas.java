package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Asignaturas_Matriculadas extends Fragment {

    private List<Subdatos_Asignaturas_Matriculadas> DatosAsignaturas;
    private BaseDeDatosUCV UCVdb;
    private Asignaturas_Adapter adaptador;
    private RecyclerView rv;

    public Asignaturas_Matriculadas() {
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UCVdb = new BaseDeDatosUCV(getActivity());
        rv = (RecyclerView) getActivity().findViewById(R.id.recicleView);
        DatosAsignaturas = UCVdb.ObtenerAsignaturasAdapter(0);

        DatosAsignaturas = OrdenarPorDiasDeLaSemana();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        adaptador = new Asignaturas_Adapter(DatosAsignaturas, getContext());
        rv.setAdapter(adaptador);
        adaptador.Actualizar();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asignaturas__matriculadas, container, false);
    }

    List<Subdatos_Asignaturas_Matriculadas> OrdenarPorDiasDeLaSemana(){

        List<String> DiasDeLaSemanaLocal= Arrays.asList(
                getString(R.string.domingo),
                getString(R.string.lunes),
                getString(R.string.martes),
                getString(R.string.miercoles),
                getString(R.string.jueves),
                getString(R.string.viernes),
                getString(R.string.sabado));

        List<String> DiasDeLaSemana= Arrays.asList(
                "Domingo",
                "Lunes",
                "Martes",
                "Miercoles",
                "Jueves",
                "Viernes",
                "Sabado");

        List<String> Meses = Arrays.asList(
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
        );

        List<Subdatos_Asignaturas_Matriculadas> ListaAuxiliar = new ArrayList<>();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd MM yyy", new Locale("es_ES"));
        boolean YaSeAgrego = false;
        for(int j=0;j<DiasDeLaSemana.size();j++){
            for(int i=0;i<DatosAsignaturas.size();i++){
                if(DatosAsignaturas.get(i).getDia().toLowerCase().compareTo(DiasDeLaSemana.get(j).toLowerCase())==0) {
                    Calendar currentCal = RetornarCalendarioOrdenado(j,DiasDeLaSemana);
                    if(!YaSeAgrego){
                        String toDate = dateFormat.format(currentCal.getTime());
                        String FechaArray[] = toDate.split(" ");
                        Subdatos_Asignaturas_Matriculadas AuxiliarDatos = new Subdatos_Asignaturas_Matriculadas();
                        AuxiliarDatos.setDia(DiasDeLaSemanaLocal.get(j));
                        AuxiliarDatos.setHeader(true);
                        AuxiliarDatos.setFechaProx(FechaArray[0] + " de " + Meses.get(Integer.parseInt(FechaArray[1])-1) + " del " + FechaArray[2]);
                        if(Integer.parseInt(FechaArray[0]) == 13 || Integer.parseInt(FechaArray[0]) == 14) AuxiliarDatos.setFeriado(true);
                        ListaAuxiliar.add(AuxiliarDatos);
                        YaSeAgrego=true;
                    }
                    Subdatos_Asignaturas_Matriculadas AuxiliarWeek = DatosAsignaturas.get(i);
                    AuxiliarWeek.setWeekendDay(""+currentCal.get(Calendar.DAY_OF_WEEK));
                    ListaAuxiliar.add(AuxiliarWeek);
                }
            }
            YaSeAgrego = false;
        }
        return ListaAuxiliar;
    }

    public Calendar RetornarCalendarioOrdenado(int dayWeekend, List<String> DiasDeLaSemana){
        Calendar currentCal = Calendar.getInstance();
        for(int i=0;i<DiasDeLaSemana.size();i++){
            if(dayWeekend != currentCal.get(Calendar.DAY_OF_WEEK) - 1) currentCal.add(Calendar.DATE, 1);
            else return currentCal;
        }
        return currentCal;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
