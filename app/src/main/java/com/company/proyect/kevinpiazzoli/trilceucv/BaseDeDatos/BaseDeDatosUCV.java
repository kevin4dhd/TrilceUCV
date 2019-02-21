package com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas.Subdatos_Asignaturas_Matriculadas;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas.ArreglosdeArreglos;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Notificaciones.RetornarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Asistencias.Asistencias;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos.Recursos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by KevinPiazzoli on 28/10/2016.
 */

public class BaseDeDatosUCV extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "basededatos.db";

    public static final String TABLA_DATOS_BASICOS = "TablaDaBa";
    public static final String COLUMNA_ID = "key_id";
    public static final String COLUMNA_NOMBRE = "key_nombre";
    public static final String COLUMNA_DATOS = "key_datos";
    public static final String COLUMNA_NOTIFICACION = "key_notificaciones";

    private static final String SQL_CREAR = "create table "
            + TABLA_DATOS_BASICOS + "(" + COLUMNA_ID
            + " integer primary key, " + COLUMNA_NOMBRE
            + " text not null, "+ COLUMNA_DATOS
            + " text not null, "+ COLUMNA_NOTIFICACION
            + " VARCHAR(10000));";

    public BaseDeDatosUCV(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + TABLA_DATOS_BASICOS + " ADD COLUMN " + COLUMNA_NOTIFICACION + " VARCHAR(10000)");
        }
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

    public String obtenerDatosBasicos(int id){

        String Datos = "";
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            Datos = datos.getString("datos_basicos");
        } catch (JSONException e) {
            //bd.close();
        }
        return Datos;
    }

    public String[] ObtenerNombresyApellidos(int id){
        String Nombre[] = {"Error al cargar nombre","Error al cargar carrera"};
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            Nombre[0] = datos.getJSONObject("datos_basicos").getString("nombres")+ " "+datos.getJSONObject("datos_basicos").getString("apellidos") ;
            Nombre[1] = datos.getJSONObject("datos_basicos").getString("escuela") ;
        } catch (JSONException e) {
            //bd.close();
        }
        return Nombre;
    }

    public RetornarDatos ObtenerNombresyCarrera(int id){

        String Nombre[][] = new String[0][3];
        String NombreCadena = "";
        RetornarDatos retornarDatos;
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            JSONArray CadenadeTokens = datos.getJSONArray("Tokens");
            Nombre = new String[CadenadeTokens.length()][3];
            for (int i=0;i<CadenadeTokens.length();i++){
                Nombre[i][0] = datos.getJSONObject("Usuarios"+i).getString("nombres")+ " "+datos.getJSONObject("datos_basicos").getString("apellidos");
                Nombre[i][1] = datos.getJSONObject("Usuarios"+i).getString("nombres");
                Nombre[i][2] = datos.getJSONObject("Usuarios"+i).getString("escuela");
            }
            NombreCadena = datos.getJSONObject("datos_basicos").getString("nombres");
        } catch (JSONException e) {
            //bd.close();
        }
        retornarDatos= new RetornarDatos(Nombre,NombreCadena);
        return retornarDatos;
    }

    public List<ArreglosdeArreglos> obtenerCursosyCodeyNotas(int id) {

        String CursosArray[];
        String NotasArray[][];
        List<ArreglosdeArreglos> ListDeVectores =  new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
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

    public List<Recursos> ObtenerRecursos(int id, String Curso, Context context) {
        List<Recursos> ListDeVectores =  new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            for (int i = 0; i < 16; i++) {
                String Recursos = datos.getJSONObject("Recursos"+Curso).getString("semana"+(i+1));
                String SepararRecursos[] = Recursos.split("-");
                for(int j=0;j<SepararRecursos.length;j++) {
                    String SepararDatosRecursos[] = SepararRecursos[j].split("_");
                    if(SepararDatosRecursos.length>1)ListDeVectores.add(new Recursos(
                            SepararDatosRecursos[0],
                            SepararDatosRecursos[1],
                            SepararDatosRecursos[2],
                            context.getString(R.string.semana)+(i+1),
                            "Semana "+(i+1)));
                }
            }
        } catch (JSONException ignored) {
        }
        return ListDeVectores;
    }

    public String[] obtenerCodigos(int id){
        String Codigos[]=null;
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            String CadenaDeCursos = datos.getJSONObject("Cursos").getString("Cursos");
            Codigos = CadenaDeCursos.split(" ");
        } catch (JSONException ignored) {
        }
        return Codigos;
    }

    public List<Subdatos_Asignaturas_Matriculadas> ObtenerAsignaturasAdapter(int id){
        ArrayList<Subdatos_Asignaturas_Matriculadas> ArrayAuxiliar= new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            String CadenaDeCursos = datos.getJSONObject("Cursos").getString("Cursos");
            String[] Codigos_volley = CadenaDeCursos.split(" ");
            for(int i=0;i<Codigos_volley.length;i++){
                Subdatos_Asignaturas_Matriculadas Auxi = new Subdatos_Asignaturas_Matriculadas();
                Auxi.setCurso(datos.getJSONObject(Codigos_volley[i]).getString("asignatura"));
                Auxi.setDocente(datos.getJSONObject(Codigos_volley[i]).getString("docente"));
                Auxi.setCiclo(datos.getJSONObject(Codigos_volley[i]).getString("ciclo"));
                Auxi.setDia(datos.getJSONObject(Codigos_volley[i]).getString("dia"));
                Auxi.setHora(datos.getJSONObject(Codigos_volley[i]).getString("horas"));
                Auxi.setAula(datos.getJSONObject(Codigos_volley[i]).getString("ambiente"));
                Auxi.setCodigoDelCurso(Codigos_volley[i]);
                ArrayAuxiliar.add(Auxi);
            }
        } catch (JSONException ignored) {
        }
        return ArrayAuxiliar;
    }

    public List<Asistencias> ObtenerAsistencias(String Curso, String HoraIngreso, String HoraSalida, int DayWekend, Context context){
        ArrayList<Asistencias> ArrayAuxiliar= new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        Calendar currentCal = new GregorianCalendar(2016,7,28);

        try {
            JSONObject datos = new JSONObject(getCursor(0));
            JSONArray AsistenciasArray = datos.getJSONArray("Asistencias");
            for(int i=0;i<AsistenciasArray.length();i++){
                String idCurso = AsistenciasArray.getJSONObject(i).getString("id");
                if(Curso.compareTo(idCurso)==0) {
                    currentCal.add(Calendar.DATE, DayWekend-1);
                    for(int k=0;k<16;k++) {
                        Asistencias Auxi = new Asistencias();
                        Auxi.setSemana(context.getString(R.string.semana)+(k+1));
                        String EstadoAsistencia = AsistenciasArray.getJSONObject(i).getString("semana"+(k+1));
                        if(EstadoAsistencia.compareTo("A")==0) Auxi.setHoraDeLlegada(HoraIngreso);
                        else Auxi.setHoraDeLlegada("xx:xx");
                        Auxi.setEstado(EstadoAsistencia);
                        Auxi.setHoraDelCurso(HoraIngreso + " - " + HoraSalida);
                        Auxi.setFecha(dateFormat.format(currentCal.getTime()));
                        ArrayAuxiliar.add(Auxi);
                        currentCal.add(Calendar.DATE, 7);
                    }
                    break;
                }
            }
        } catch (JSONException ignored) {
        }
        return ArrayAuxiliar;
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
        try {
            JSONObject datos = new JSONObject(getCursor(0));
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

    public List<List<String>> ObtenerAmigos(int id){
        List<List<String>> Amigos = new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            JSONArray CadenaDeAmigos = datos.getJSONArray("Amigos");
            for(int i=0;i<CadenaDeAmigos.length();i++){
                List<String> AmigosAuxiliares = new ArrayList<>();
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("id"));
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("nombres"));
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("apellidos"));
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("genero"));
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("escuela"));
                AmigosAuxiliares.add("true");
                Amigos.add(AmigosAuxiliares);
            }
        } catch (JSONException ignored) {
        }
        return Amigos;
    }

    public List<List<String>> ObtenerEstadoAmigos(int id){
        List<List<String>> Amigos = new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            JSONArray CadenaDeAmigos = datos.getJSONArray("Estado_Amigos");
            for(int i=0;i<CadenaDeAmigos.length();i++){
                List<String> AmigosAuxiliares = new ArrayList<>();
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("id"));
                AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("estado"));
                Amigos.add(AmigosAuxiliares);
            }
        } catch (JSONException ignored) {
        }
        return Amigos;
    }

    public List<List<String>> ObtenerBuscadorDeAmigosSearch(List<List<String>> EstadoAmigos , String UsuarioPrincipal){
        List<List<String>> Amigos = new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            JSONArray CadenaDeAmigos = datos.getJSONArray("all_users");
            for(int i=0;i<CadenaDeAmigos.length();i++){
                List<String> AmigosAuxiliares = new ArrayList<>();
                String idUser = CadenaDeAmigos.getJSONObject(i).getString("id");
                if(UsuarioPrincipal.compareTo(idUser)!=0) {
                    AmigosAuxiliares.add(idUser);
                    AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("nombres"));
                    AmigosAuxiliares.add(CadenaDeAmigos.getJSONObject(i).getString("apellidos"));
                    for(int u=0;u<EstadoAmigos.size();u++){
                        if(idUser.compareTo(EstadoAmigos.get(u).get(0))==0)
                            AmigosAuxiliares.add(EstadoAmigos.get(u).get(1));
                    }
                    if (AmigosAuxiliares.size() != 4) AmigosAuxiliares.add("No_Amigo");
                    if(AmigosAuxiliares.get(3).compareTo("Amigos")!=0)
                    Amigos.add(AmigosAuxiliares);
                }
            }
        } catch (JSONException ignored) {}
        return Amigos;
    }

    public List<List<String>> ObtenerMensajes(int id, String User){
        List<List<String>> Mensajes = new ArrayList<>();
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            String MensajesCadena = datos.getString("Mensajes");
            JSONObject JsonDatos = new JSONObject(MensajesCadena);
            JSONArray ArrayMensajes = JsonDatos.getJSONArray(User);
            for(int i=0;i<ArrayMensajes.length();i++){
                List<String> MensajesAuxiliares = new ArrayList<>();
                MensajesAuxiliares.add(ArrayMensajes.getJSONObject(i).getString("id"));
                MensajesAuxiliares.add(ArrayMensajes.getJSONObject(i).getString("mensaje"));
                MensajesAuxiliares.add(ArrayMensajes.getJSONObject(i).getString("tipo_mensaje"));
                MensajesAuxiliares.add(ArrayMensajes.getJSONObject(i).getString("hora_del_mensaje"));
                Mensajes.add(MensajesAuxiliares);
            }
        } catch (JSONException ignored) {
        }
        return Mensajes;
    }

    public Boolean ActualizarMensajes(int id,String User, String NuevoMensaje){
        List<List<String>> Mensajes = new ArrayList<>();
        try {
            //GET
            JSONObject datos = new JSONObject(getCursor(0));
            String MensajesCadena = datos.getString("Mensajes");
            JSONObject JsonDatos = new JSONObject(MensajesCadena);
            JSONArray ArrayMensajes = JsonDatos.getJSONArray(User);

            //Update
            JSONObject UpdateJSON = new JSONObject(NuevoMensaje);
            ArrayMensajes.put(UpdateJSON);
            JsonDatos.put(User,ArrayMensajes);
            datos.put("Mensajes",JsonDatos);

            //UpdateBD
            actualizar(0,datos.toString());
        } catch (JSONException ignored) {
            return false;
        }
        return true;
    }

    public String getNameUserPrincipal(int id){
        String Nombre = "Example";
        try {
            JSONObject datos = new JSONObject(getCursor(0));
            Nombre = datos.getJSONObject("datos_basicos").getString("nombres");
        }catch (JSONException e) {
                e.printStackTrace();
            }
        return Nombre;
    }

    private String getCursor(int id){
        String DatosJSON = "";
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
        if (cursor != null) {
            cursor.moveToFirst();
            DatosJSON = cursor.getString(1);
            cursor.close();
            db.close();
        }
        return DatosJSON;
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

}
