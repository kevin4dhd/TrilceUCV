package com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas.ArreglosdeArreglos;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos.Recursos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 28/10/2016.
 */

public class BaseDeDatosUCV extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "basededatos.db";

    public static final String TABLA_DATOS_BASICOS = "TablaDaBa";
    public static final String COLUMNA_ID = "key_id";
    public static final String COLUMNA_NOMBRE = "key_nombre";
    public static final String COLUMNA_DATOS = "key_datos";

    private static final String SQL_CREAR = "create table "
            + TABLA_DATOS_BASICOS + "(" + COLUMNA_ID
            + " integer primary key, " + COLUMNA_NOMBRE
            + " text not null, "+ COLUMNA_DATOS
            + " text not null);";

    public BaseDeDatosUCV(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    public boolean agregar(int id, String nombre, String datos){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COLUMNA_ID, id);
            values.put(COLUMNA_NOMBRE, nombre);
            values.put(COLUMNA_DATOS, datos);

            db.insert(TABLA_DATOS_BASICOS, null, values);
            db.close();
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    public void actualizar (int id, String datos){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNA_DATOS,datos);

        int i = db.update(TABLA_DATOS_BASICOS,
                values,
                " "+COLUMNA_ID+" = ?",
                new String[] { String.valueOf( id ) });
        db.close();
    }

    public boolean eliminar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLA_DATOS_BASICOS,
                    " _id = ?",
                    new String[] { String.valueOf (id ) });
            db.close();
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public String obtenerDatosBasicos(int id){

        String Datos = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " "+COLUMNA_ID+" = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);
        if (cursor != null)
            cursor.moveToFirst();
        String DatosInternet = cursor.getString(1);
        cursor.close();
        db.close();
        try {
            JSONObject datos = new JSONObject(DatosInternet);
            Datos = datos.getString("datos_basicos");
        } catch (JSONException e) {
            //bd.close();
        }
        return Datos;
    }

    public String[] ObtenerNombresyApellidos(int id){

        String Nombre[] = {"Error al cargar nombre","Error al cargar carrera"};
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " "+COLUMNA_ID+" = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);
        if (cursor != null)
            cursor.moveToFirst();
        String DatosInternet = cursor.getString(1);
        cursor.close();
        db.close();
        try {
            JSONObject datos = new JSONObject(DatosInternet);
            Nombre[0] = datos.getJSONObject("datos_basicos").getString("nombres")+ " "+datos.getJSONObject("datos_basicos").getString("apellidos") ;
            Nombre[1] = datos.getJSONObject("datos_basicos").getString("escuela") ;
        } catch (JSONException e) {
            //bd.close();
        }
        return Nombre;
    }

    public List<ArreglosdeArreglos> obtenerCursosyCodeyNotas(int id) {

        String CursosArray[];
        String NotasArray[][];

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " " + COLUMNA_ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();
        String Cursos = cursor.getString(1);
        cursor.close();
        db.close();

        List<ArreglosdeArreglos> ListDeVectores =  new ArrayList<>();


        try {
            JSONObject datos = new JSONObject(Cursos);
            String CadenaDeCursos = datos.getJSONObject("Cursos").getString("Cursos");
            String[] Codigos_volley = CadenaDeCursos.split(" ");

            CursosArray = new String[Codigos_volley.length];
            NotasArray = new String[Codigos_volley.length][];

            for (int i = 0; i < Codigos_volley.length; i++) {
                String CodetoStringCurso = datos.getJSONObject(Codigos_volley[i]).getString("asignatura");
                String CodetoStringCodigo = datos.getJSONObject(Codigos_volley[i]).getString("codigo");

                for (int j = 0; j < Codigos_volley.length; j++) {
                    String Notas = datos.getJSONObject("Notas").getString("Notas" + (j + 1));
                    NotasArray[i] = Notas.split(" ");
                    if (NotasArray[i][0].compareTo(CodetoStringCodigo) == 0) break;
                }
                CursosArray[i] = CodetoStringCurso;
            }
            ListDeVectores.add(new ArreglosdeArreglos(CursosArray,NotasArray));
        } catch (JSONException ignored) {
        }
        return ListDeVectores;
    }

    public List<Recursos> ObtenerRecursos(int id, String Curso) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " " + COLUMNA_ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();
        String Datos = cursor.getString(1);
        cursor.close();
        db.close();

        List<Recursos> ListDeVectores =  new ArrayList<>();


        try {
            JSONObject datos = new JSONObject(Datos);

            for (int i = 0; i < 16; i++) {
                String Recursos = datos.getJSONObject("Recursos"+Curso).getString("semana"+(i+1));
                String SepararRecursos[] = Recursos.split("-");
                for(int j=0;j<SepararRecursos.length;j++) {
                    String SepararDatosRecursos[] = SepararRecursos[j].split("_");
                    if(SepararDatosRecursos.length>1)ListDeVectores.add(new Recursos(
                            SepararDatosRecursos[0],
                            SepararDatosRecursos[1],
                            SepararDatosRecursos[2],
                            "semana"+(i+1),
                            "Semana "+(i+1)));
                }
            }
        } catch (JSONException ignored) {
        }
        return ListDeVectores;
    }

    public String[] obtenerCodigos(int id){
        String Codigos[]=null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " "+COLUMNA_ID+" = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();
        String Cursos = cursor.getString(1);
        cursor.close();
        db.close();
        try {
            JSONObject datos = new JSONObject(Cursos);
            String CadenaDeCursos = datos.getJSONObject("Cursos").getString("Cursos");
            Codigos = CadenaDeCursos.split(" ");
        } catch (JSONException ignored) {
        }
        return Codigos;
    }

    public String[][] ObtenerAsignaturasAdapter(int id){
        String Codigos[][] = new String[0][4];
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " "+COLUMNA_ID+" = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();
        String Cursos = cursor.getString(1);
        cursor.close();
        db.close();
        try {
            JSONObject datos = new JSONObject(Cursos);
            String CadenaDeCursos = datos.getJSONObject("Cursos").getString("Cursos");
            String[] Codigos_volley = CadenaDeCursos.split(" ");
            Codigos = new String[Codigos_volley.length][4];
            for(int i=0;i<Codigos_volley.length;i++){
                String CodetoStringCurso = datos.getJSONObject(Codigos_volley[i]).getString("asignatura");
                String CodetoStringDocente = datos.getJSONObject(Codigos_volley[i]).getString("docente");
                String CodetoStringCiclo = datos.getJSONObject(Codigos_volley[i]).getString("ciclo");
                String CodetoStringCodigo = Codigos_volley[i];
                Codigos[i][0]=CodetoStringCurso;
                Codigos[i][1]=CodetoStringDocente;
                Codigos[i][2]=CodetoStringCiclo;
                Codigos[i][3]=CodetoStringCodigo;
            }
        } catch (JSONException ignored) {
        }
        return Codigos;
    }

    public void actualizarDatos (int id, String datos){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNA_DATOS,datos);

        int i = db.update(TABLA_DATOS_BASICOS,
                values,
                " "+COLUMNA_ID+" = ?",
                new String[] { String.valueOf( id ) });
        db.close();
    }

    public String[][] ObtenerInformacionAsignaturaMatricuolada(int id, String codigo, String[] keys){
        String Informacion[][] = new String[0][2];
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_DATOS};

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        projection,
                        " "+COLUMNA_ID+" = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();
        String Datos = cursor.getString(1);
        cursor.close();
        db.close();
        try {
            JSONObject datos = new JSONObject(Datos);
            Informacion = new String[keys.length][3];
            for(int i=0;i<keys.length;i++){
                Informacion[i][0] = datos.getJSONObject(codigo).getString(keys[i]);
            }
        } catch (JSONException ignored) {
        }
        return Informacion;
    }

    public Cursor obtenerALL(){

        String dato="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLA_DATOS_BASICOS,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        return cursor;
    }

}
