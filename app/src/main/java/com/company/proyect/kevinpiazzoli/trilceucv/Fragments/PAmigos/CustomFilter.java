package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

import android.widget.Filter;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter{

    SearchAdapter adapterSearch;
    List<List<String>> NuevosAmigos;
    ArrayList<DatosAmigos> Datos;

    CustomFilter(List<List<String>> NuevosAmigos, SearchAdapter adapter,ArrayList<DatosAmigos> Datos) {
        this.adapterSearch=adapter;
        this.NuevosAmigos=NuevosAmigos;
        this.Datos=Datos;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            String[] PalabrasEditText = (constraint.toString() + " ").split(" ");
            ArrayList<DatosAmigos> filteredPlayers=new ArrayList<>();
            for (int i=0;i<NuevosAmigos.size();i++) {
                if (NuevosAmigos.get(i).get(3).compareTo("No_Amigo")==0){
                    for (int x = 0; x < PalabrasEditText.length; x++) {
                        String UsuarioNombres[] = (NuevosAmigos.get(i).get(1) + " " + NuevosAmigos.get(i).get(2) + " ").split(" ");
                        for (String UsuarioNombre : UsuarioNombres) {
                            if (remove1(UsuarioNombre).toUpperCase().contains(PalabrasEditText[x])) {
                                DatosAmigos auxiAmigos = new DatosAmigos();
                                auxiAmigos.setFoto(R.drawable.ic_contact_icon);
                                auxiAmigos.setUser(NuevosAmigos.get(i).get(0));
                                auxiAmigos.setNombre(NuevosAmigos.get(i).get(1));
                                auxiAmigos.setApellido(NuevosAmigos.get(i).get(2));
                                auxiAmigos.setAmigo(NuevosAmigos.get(i).get(3));
                                filteredPlayers.add(auxiAmigos);
                                x = PalabrasEditText.length;
                                break;
                            }
                        }
                    }
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else {
            results.count=Datos.size();
            results.values=Datos;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
            adapterSearch.Datos = (ArrayList<DatosAmigos>) results.values;
            adapterSearch.Actualizar();
    }

    private String remove1(String input) {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUcC";
        String output = input;
        for (int i=0; i< original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }

    public void ActualizarFiltro(List<List<String>> NuevosAmigos, SearchAdapter adapter,ArrayList<DatosAmigos> Datos){
        this.adapterSearch=adapter;
        this.NuevosAmigos=NuevosAmigos;
        this.Datos=Datos;
    }

}
