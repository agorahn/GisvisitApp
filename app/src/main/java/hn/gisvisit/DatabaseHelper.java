package hn.gisvisit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "gisvisit.db";
    private static final int DATABASE_VERSION = 6;
    public Cursor cursor;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL(DATABASE_CREATE);
        //database.execSQL(Campaña.DATABASE_CREATE);
        database.execSQL(Visita.DATABASE_CREATE);
        database.execSQL(Usuario.DATABASE_CREATE);
        database.execSQL(Cliente.DATABASE_CREATE);
        database.execSQL(Evento.DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + Visita.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Cliente.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Usuario.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Evento.TABLE_NAME);

        onCreate(db);
    }

    public long insertUsuario(String co,String u, String c,String s, String e,String et,String auto,String login) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Usuario.COLUMN_CODIGO,co);
        values.put(Usuario.COLUMN_USUARIO, u);
        values.put(Usuario.COLUMN_CLAVE, c);
        values.put(Usuario.COLUMN_SEGUNDOS, s);
        values.put(Usuario.COLUMN_ESTADO, e);
        values.put(Usuario.COLUMN_ESTADO_TIPO, et);
        values.put(Usuario.COLUMN_AUTOGUARDO, auto);
        values.put(Usuario.COLUMN_LOGIN, login);


        // insert row
        long id = db.insert(Usuario.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public int getUsuarioCount() {
        String countQuery = "SELECT  id FROM " + Usuario.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    public int getUsuarioMax() {

        int count=0;
        String countQuery = "SELECT  MAX(id) as max FROM " + Usuario.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        count=cursor.getInt(cursor.getColumnIndex("max"));

        // close the db connection
        cursor.close();

        // return count
        return count;
    }
    public void deleteUsuario(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Usuario.TABLE_NAME, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note)});
        db.close();
    }

    public Usuario getUsuario() {
        List<Usuario> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT id,codigo,usuario,clave,segundos,estado,estadoTipo,estado_auto,estado_login FROM " + Usuario.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Usuario note = new Usuario();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                note.setId(cursor.getInt(cursor.getColumnIndex(Usuario.COLUMN_ID)));
                note.setCodigo(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_CODIGO)));
                note.setNombre(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_USUARIO)));
                note.setClave(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_CLAVE)));
                note.setSegundos(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_SEGUNDOS)));
                note.setEstado(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ESTADO)));
                note.setEstado_tipo(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ESTADO_TIPO)));
                note.setEstado_autoguardado(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_AUTOGUARDO)));
                note.setEstado_login(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_LOGIN)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return note;
    }

    public int updateUsuario(String id,String codigo,String usuario,String clave,String estado, String estadoTipo,String auto,String login) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Usuario.COLUMN_ID, id);
        values.put(Usuario.COLUMN_CODIGO, codigo);
        values.put(Usuario.COLUMN_USUARIO, usuario);
        values.put(Usuario.COLUMN_CLAVE, clave);
        values.put(Usuario.COLUMN_ESTADO, estado);
        values.put(Usuario.COLUMN_ESTADO_TIPO, estadoTipo);
        values.put(Usuario.COLUMN_AUTOGUARDO, auto);
        values.put(Usuario.COLUMN_LOGIN, login);

        // updating row
        return db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateUsuarioLogOut(String id,String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Usuario.COLUMN_ID, id);
        values.put(Usuario.COLUMN_ESTADO, estado);
        values.put(Usuario.COLUMN_LOGIN, estado);


        // updating row
        return db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateUsuarioLogIn(String id,String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Usuario.COLUMN_ID, id);
        values.put(Usuario.COLUMN_LOGIN, estado);


        // updating row
        return db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public int updateUsuarioServicio(String id,String segundos, String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Usuario.COLUMN_ID, id);
        values.put(Usuario.COLUMN_SEGUNDOS, segundos);
        values.put(Usuario.COLUMN_ESTADO, estado);

        // updating row
        return db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }


    ////// clientes //////////////******************************************

    public int getClienteCount() {
        String countQuery = "SELECT  id FROM " + Cliente.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getClienteMax() {

        int count=0;
        String countQuery = "SELECT  MAX(id) as max FROM " + Cliente.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        count=cursor.getInt(cursor.getColumnIndex("max"));

        // close the db connection
        cursor.close();

        // return count
        return count;
    }
    public int getClienteFaltaSincronizar() {

        int count=0;
        String countQuery = "SELECT  count(id) as cuenta FROM " + Cliente.TABLE_NAME+" where estado_sincronizado='0'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        count=cursor.getInt(cursor.getColumnIndex("cuenta"));

        // close the db connection
        cursor.close();

        // return count
        return count;
    }



    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Cliente.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cliente cliente = new Cliente();
                cliente.setId(cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID)));
                cliente.setCodigo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CODIGO)));
                cliente.setCodigo_c_empresa(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_C_EMPRESA)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOMBRE)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DIRECCION)));
                cliente.setIndentidad(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_IDENTIDAD)));
                cliente.setCorreo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CORREO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_TELEFONO)));
                cliente.setCelular(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CELULAR)));
                cliente.setFoto1(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO1)));
                cliente.setFoto2(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO2)));
                cliente.setFoto3(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO3)));
                cliente.setLatitude(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LAT)));
                cliente.setLongitud(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LON)));
                cliente.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_LOCALIZACION)));
                cliente.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_SINCRONIZADO)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO)));
                clientes.add(cliente);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return clientes;
    }
    public List<Cliente> getAllClientes_toSinc() {
        List<Cliente> clientes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Cliente.TABLE_NAME+" where estado_sincronizado='0'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cliente cliente = new Cliente();
                cliente.setId(cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID)));
                cliente.setCodigo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CODIGO)));
                cliente.setCodigo_c_empresa(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_C_EMPRESA)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOMBRE)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DIRECCION)));
                cliente.setIndentidad(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_IDENTIDAD)));
                cliente.setCorreo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CORREO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_TELEFONO)));
                cliente.setCelular(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CELULAR)));
                cliente.setFoto1(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO1)));
                cliente.setFoto2(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO2)));
                cliente.setFoto3(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO3)));
                cliente.setLatitude(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LAT)));
                cliente.setLongitud(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LON)));
                cliente.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_LOCALIZACION)));
                cliente.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_SINCRONIZADO)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO)));
                clientes.add(cliente);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return clientes;
    }

    public Cliente getCliente_nombre(String codigo) {


        // Select All Query
        String selectQuery = "SELECT * FROM " + Cliente.TABLE_NAME +" where nombre='"+codigo+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Cliente cliente = new Cliente();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                cliente.setId(cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID)));
                cliente.setCodigo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CODIGO)));
                cliente.setCodigo_c_empresa(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_C_EMPRESA)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOMBRE)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DIRECCION)));
                cliente.setIndentidad(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_IDENTIDAD)));
                cliente.setCorreo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CORREO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_TELEFONO)));
                cliente.setCelular(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CELULAR)));
                cliente.setFoto1(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO1)));
                cliente.setFoto2(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO2)));
                cliente.setFoto3(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO3)));
                cliente.setLatitude(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LAT)));
                cliente.setLongitud(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LON)));
                cliente.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_LOCALIZACION)));
                cliente.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_SINCRONIZADO)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return cliente;
    }
    public Cliente getCliente_codigo(String codigo) {


        // Select All Query
        String selectQuery = "SELECT * FROM " + Cliente.TABLE_NAME +" where "+Cliente.COLUMN_CODIGO+"="+codigo;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Cliente cliente = new Cliente();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                cliente.setId(cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID)));
                cliente.setCodigo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CODIGO)));
                cliente.setCodigo_c_empresa(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_C_EMPRESA)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOMBRE)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DIRECCION)));
                cliente.setIndentidad(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_IDENTIDAD)));
                cliente.setCorreo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CORREO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_TELEFONO)));
                cliente.setCelular(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CELULAR)));
                cliente.setFoto1(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO1)));
                cliente.setFoto2(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO2)));
                cliente.setFoto3(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO3)));
                cliente.setLatitude(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LAT)));
                cliente.setLongitud(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LON)));
                cliente.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_LOCALIZACION)));
                cliente.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_SINCRONIZADO)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return cliente;
    }
    public Cliente getCliente(String codigo) {


        // Select All Query
        String selectQuery = "SELECT * FROM " + Cliente.TABLE_NAME +" where id="+codigo;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Cliente cliente = new Cliente();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                cliente.setId(cursor.getInt(cursor.getColumnIndex(Cliente.COLUMN_ID)));
                cliente.setCodigo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CODIGO)));
                cliente.setCodigo_c_empresa(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_C_EMPRESA)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_NOMBRE)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_DIRECCION)));
                cliente.setIndentidad(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_IDENTIDAD)));
                cliente.setCorreo(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CORREO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_TELEFONO)));
                cliente.setCelular(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_CELULAR)));
                cliente.setFoto1(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO1)));
                cliente.setFoto2(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO2)));
                cliente.setFoto3(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_FOTO3)));
                cliente.setLatitude(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LAT)));
                cliente.setLongitud(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_LON)));
                cliente.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_LOCALIZACION)));
                cliente.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO_SINCRONIZADO)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndex(Cliente.COLUMN_ESTADO)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return cliente;
    }

    public long insertCliente(String codigo,String codigo_emp,String nombre, String direccion,String identidad, String correo,String telefono,String celular,String foto1,String foto2,String foto3,
                              String lat,String lon,String estado_loc,String estado_sincro,String estado) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_C_EMPRESA,codigo_emp);
        values.put(Cliente.COLUMN_NOMBRE, nombre);
        values.put(Cliente.COLUMN_DIRECCION, direccion);
        values.put(Cliente.COLUMN_IDENTIDAD, identidad);
        values.put(Cliente.COLUMN_CORREO, correo);
        values.put(Cliente.COLUMN_TELEFONO, telefono);
        values.put(Cliente.COLUMN_CELULAR, celular);
        values.put(Cliente.COLUMN_FOTO1, foto1);
        values.put(Cliente.COLUMN_FOTO2, foto2);
        values.put(Cliente.COLUMN_FOTO3, foto3);
        values.put(Cliente.COLUMN_LAT, lat);
        values.put(Cliente.COLUMN_LON, lon);
        values.put(Cliente.COLUMN_ESTADO_LOCALIZACION, estado_loc);
        values.put(Cliente.COLUMN_ESTADO_SINCRONIZADO, estado_sincro);
        values.put(Cliente.COLUMN_ESTADO, estado);

        // insert row
        long id = db.insert(Cliente.TABLE_NAME, null, values);


        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public int updateCliente(String codigo,String codigo_emp,String nombre, String direccion,String identidad, String correo,String telefono,String celular,String foto1,String foto2,String foto3,
                             String lat,String lon,String estado_loc,String estado_sincro,String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_C_EMPRESA,codigo_emp);
        values.put(Cliente.COLUMN_NOMBRE, nombre);
        values.put(Cliente.COLUMN_DIRECCION, direccion);
        values.put(Cliente.COLUMN_IDENTIDAD, identidad);
        values.put(Cliente.COLUMN_CORREO, correo);
        values.put(Cliente.COLUMN_TELEFONO, telefono);
        values.put(Cliente.COLUMN_CELULAR, celular);
        values.put(Cliente.COLUMN_FOTO1, foto1);
        values.put(Cliente.COLUMN_FOTO2, foto2);
        values.put(Cliente.COLUMN_FOTO3, foto3);
        values.put(Cliente.COLUMN_LAT, lat);
        values.put(Cliente.COLUMN_LON, lon);
        values.put(Cliente.COLUMN_ESTADO_LOCALIZACION, estado_loc);
        values.put(Cliente.COLUMN_ESTADO_SINCRONIZADO, estado_sincro);
        values.put(Cliente.COLUMN_ESTADO, estado);


        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_CODIGO + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateCliente_sinc(String id,String estado_sincro,String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_ESTADO,estado);
        values.put(Cliente.COLUMN_ESTADO_SINCRONIZADO, estado_sincro);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateCliente(String id,String estado_sincro) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(Cliente.COLUMN_ESTADO_SINCRONIZADO, estado_sincro);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateClienteEstado(String codigo,String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_ESTADO, estado);


        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_CODIGO + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateCliente_afterSinc(String id,String codigo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateClienteGPS(String codigo,String lat,String lon) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_LAT, lat);
        values.put(Cliente.COLUMN_LON, lon);
        values.put(Cliente.COLUMN_ESTADO_LOCALIZACION, "1");

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_CODIGO + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateClienteFoto1_load(String id,String foto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_ID,id);
        values.put(Cliente.COLUMN_FOTO1, foto);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateClienteFoto2_load(String id,String foto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_ID,id);
        values.put(Cliente.COLUMN_FOTO2, foto);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateClienteFoto3_load(String id,String foto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_ID,id);
        values.put(Cliente.COLUMN_FOTO3, foto);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateClienteFoto1(String codigo,String foto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_FOTO1, foto);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_CODIGO + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateClienteFoto2(String codigo,String foto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_FOTO2, foto);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_CODIGO + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateClienteFoto3(String codigo,String foto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cliente.COLUMN_CODIGO,codigo);
        values.put(Cliente.COLUMN_FOTO3, foto);

        // updating row
        return db.update(Cliente.TABLE_NAME, values, Cliente.COLUMN_CODIGO + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public void deleteAllCliente() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Cliente.TABLE_NAME, Cliente.COLUMN_ID + " != ?",
                new String[]{String.valueOf("0")});
        db.close();
    }
    public void deleteCliente(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Cliente.TABLE_NAME, Cliente.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note)});
        db.close();
    }

    //********************VISITAS**************************

    public int getVisitaCount() {
        String countQuery = "SELECT  id FROM " + Visita.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getVisitaMax() {

        int count=0;
        String countQuery = "SELECT  MAX(id) as max FROM " + Visita.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        count=cursor.getInt(cursor.getColumnIndex("max"));

        // close the db connection
        cursor.close();

        // return count
        return count;
    }
    public List<Visita> getAllVisitas_toSinc() {
        List<Visita> visitas = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Visita.TABLE_NAME+" where estado_sincronizado='0'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Visita visita = new Visita();
                visita.setId(cursor.getInt(cursor.getColumnIndex(Visita.COLUMN_ID)));
                visita.setCodigo_c_cliente(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ID_CLIENTE)));
                visita.setRazon_visita(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_VISIT_REASON)));
                visita.setFecha(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FECHA)));
                visita.setObservacion(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_OBSERVACION)));
                visita.setFecha_proxima(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_PROXIMA_VISITA)));
                visita.setFoto1(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO1)));
                visita.setFoto2(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO2)));
                visita.setFoto3(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO3)));
                visita.setLatitude(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_LAT)));
                visita.setLongitud(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_LON)));
                visita.setCodigo_evento(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_CODIGO_EVENTO)));
                visita.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ESTADO_SINCRONIZADO)));
                visita.setEstado(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ESTADO)));
                visitas.add(visita);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return visitas;
    }
    public List<Visita> getAllVisitas() {
        List<Visita> visitas = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Visita.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Visita visita = new Visita();
                visita.setId(cursor.getInt(cursor.getColumnIndex(Visita.COLUMN_ID)));
                visita.setCodigo_c_cliente(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ID_CLIENTE)));
                visita.setRazon_visita(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_VISIT_REASON)));
                visita.setFecha(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FECHA)));
                visita.setObservacion(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_OBSERVACION)));
                visita.setFecha_proxima(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_PROXIMA_VISITA)));
                visita.setFoto1(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO1)));
                visita.setFoto2(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO2)));
                visita.setFoto3(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO3)));
                visita.setLatitude(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_LAT)));
                visita.setLongitud(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_LON)));
                visita.setCodigo_evento(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_CODIGO_EVENTO)));
                visita.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ESTADO_SINCRONIZADO)));
                visita.setEstado(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ESTADO)));
                visitas.add(visita);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return visitas;
    }

    public Visita getVisita(String codigo) {


        // Select All Query
        String selectQuery = "SELECT * FROM " + Visita.TABLE_NAME +" where id="+codigo;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Visita visita = new Visita();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                visita.setId(cursor.getInt(cursor.getColumnIndex(Visita.COLUMN_ID)));
                visita.setCodigo_c_cliente(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ID_CLIENTE)));
                visita.setRazon_visita(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_VISIT_REASON)));
                visita.setFecha(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FECHA)));
                visita.setObservacion(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_OBSERVACION)));
                visita.setFecha_proxima(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_PROXIMA_VISITA)));
                visita.setFoto1(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO1)));
                visita.setFoto2(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO2)));
                visita.setFoto3(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_FOTO3)));
                visita.setLatitude(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_LAT)));
                visita.setLongitud(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_LON)));
                visita.setCodigo_evento(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_CODIGO_EVENTO)));
                visita.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ESTADO_SINCRONIZADO)));
                visita.setEstado(cursor.getString(cursor.getColumnIndex(Visita.COLUMN_ESTADO)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return visita;
    }

    public long insertVisita(String c_c,String razon, String observacion,String fecha,String foto1,String foto2,
                              String foto3,String latitud,String longitud,String fecha_proxima,String c_evento,String es_sin,String es) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Visita.COLUMN_ID_CLIENTE,c_c);
        values.put(Visita.COLUMN_VISIT_REASON,razon);
        values.put(Visita.COLUMN_OBSERVACION, observacion);
        values.put(Visita.COLUMN_FECHA, fecha);
        values.put(Visita.COLUMN_FOTO1, foto1);
        values.put(Visita.COLUMN_FOTO2, foto2);
        values.put(Visita.COLUMN_FOTO3, foto3);
        values.put(Visita.COLUMN_LAT, latitud);
        values.put(Visita.COLUMN_LON, longitud);
        values.put(Visita.COLUMN_PROXIMA_VISITA, fecha_proxima);
        values.put(Visita.COLUMN_CODIGO_EVENTO, c_evento);
        values.put(Visita.COLUMN_ESTADO_SINCRONIZADO, es_sin);
        values.put(Visita.COLUMN_ESTADO, es);

        // insert row
        long id = db.insert(Visita.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public int updateVisita(String codigo,String c_c,String razon, String observacion,String fecha,String foto1,String foto2,
                             String foto3,String latitud,String longitud,String fecha_proxima,String c_evento,String es_sin,String es) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Visita.COLUMN_ID,codigo);
        values.put(Visita.COLUMN_ID_CLIENTE,c_c);
        values.put(Visita.COLUMN_VISIT_REASON,razon);
        values.put(Visita.COLUMN_OBSERVACION, observacion);
        values.put(Visita.COLUMN_FECHA, fecha);
        values.put(Visita.COLUMN_FOTO1, foto1);
        values.put(Visita.COLUMN_FOTO2, foto2);
        values.put(Visita.COLUMN_FOTO3, foto3);
        values.put(Visita.COLUMN_LAT, latitud);
        values.put(Visita.COLUMN_LON, longitud);
        values.put(Visita.COLUMN_PROXIMA_VISITA, fecha_proxima);
        values.put(Visita.COLUMN_CODIGO_EVENTO, c_evento);
        values.put(Visita.COLUMN_ESTADO_SINCRONIZADO, es_sin);
        values.put(Visita.COLUMN_ESTADO, es);

        // updating row
        return db.update(Visita.TABLE_NAME, values, Visita.COLUMN_ID + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateVisita_sinc(String id,String es_sin,String es) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(Visita.COLUMN_ID,codigo);
        values.put(Visita.COLUMN_ESTADO_SINCRONIZADO, es_sin);
        values.put(Visita.COLUMN_ESTADO, es);

        // updating row
        return db.update(Visita.TABLE_NAME, values, Visita.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public int updateVisita_afterSinc(String id,String codigo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Visita.COLUMN_CODIGO_EVENTO,codigo);


        // updating row
        return db.update(Visita.TABLE_NAME, values, Visita.COLUMN_ID + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public int updateEvent_estado(String id,String estado) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Evento.COLUMN_ESTADO,estado);


        // updating row
        return db.update(Evento.TABLE_NAME, values, Evento.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
    public void deleteAllVisita() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Visita.TABLE_NAME, Visita.COLUMN_ID + " != ?",
                new String[]{String.valueOf("0")});
        db.close();
    }
    public void deleteVisita(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Visita.TABLE_NAME, Visita.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note)});
        db.close();
    }



    //********************VISITAS**************************
    //********************EVENTOS*************************

    public int getEventoCount() {
        String countQuery = "SELECT  id FROM " + Evento.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getEventoMax() {

        int count=0;
        String countQuery = "SELECT  MAX(id) as max FROM " + Evento.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        count=cursor.getInt(cursor.getColumnIndex("max"));

        // close the db connection
        cursor.close();

        // return count
        return count;
    }
    public List<Evento> getAllEvento(String fecha) {
        List<Evento> eventos = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Evento.TABLE_NAME+" where "+Evento.COLUMN_FECHA_PROGRAMACION+"='"+fecha+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Evento evento = new Evento();
                evento.setId(cursor.getInt(cursor.getColumnIndex(Evento.COLUMN_ID)));
                evento.setCodigo_c_cliente(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ID_CLIENTE)));
                evento.setCodigo_c_evento(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ID_EVENTO)));
                evento.setVisit_reason(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_VISIT_REASON)));
                evento.setTipo_visit(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_TIPO_VISIT)));
                evento.setFecha_programacion(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_FECHA_PROGRAMACION)));
                evento.setFecha_realizada(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_FECHA_REALIZADA)));
                evento.setLatitude(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_LAT)));
                evento.setLongitud(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_LON)));
                evento.setEstado(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ESTADO)));
                evento.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ESTADO_SINCRONIZADO)));
                evento.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ESTADO_LOCALIZACION)));

                //Log.e("FECHA","SELECT  * FROM " + Evento.TABLE_NAME+" where CAST("+Evento.COLUMN_FECHA_PROGRAMACION+" as date)=cast('"+fecha+"' as date)");
                Log.e("FECHA",cursor.getString(cursor.getColumnIndex(Evento.COLUMN_FECHA_PROGRAMACION)));

                eventos.add(evento);
            } while (cursor.moveToNext());
        }

        Log.e("FECHA","SELECT  * FROM " + Evento.TABLE_NAME+" where "+Evento.COLUMN_FECHA_PROGRAMACION+"='"+fecha+"'");

        // close db connection
        db.close();

        // return notes list
        return eventos;
    }

    public Evento getEvento(String codigo) {

        // Select All Query
        String selectQuery = "SELECT * FROM " + Evento.TABLE_NAME +" where id="+codigo;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Evento evento = new Evento();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                evento.setId(cursor.getInt(cursor.getColumnIndex(Evento.COLUMN_ID)));
                evento.setCodigo_c_cliente(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ID_CLIENTE)));
                evento.setCodigo_c_evento(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ID_EVENTO)));
                evento.setVisit_reason(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_VISIT_REASON)));
                evento.setTipo_visit(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_TIPO_VISIT)));
                evento.setFecha_programacion(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_FECHA_PROGRAMACION)));
                evento.setFecha_realizada(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_FECHA_REALIZADA)));
                evento.setLatitude(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_LAT)));
                evento.setLongitud(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_LON)));
                evento.setEstado(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ESTADO)));
                evento.setEstado_sincronizar(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ESTADO_SINCRONIZADO)));
                evento.setEstado_localizacion(cursor.getString(cursor.getColumnIndex(Evento.COLUMN_ESTADO_LOCALIZACION)));

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return evento;
    }

    public long insertEvento(String codigo_cliente,String codigo_evento,String tipo_visita, String razon,String fecha_proga,String fecha_realizado,
                             String latitud,String longitud,String es_loc,String es_sic,String es) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Evento.COLUMN_ID_CLIENTE,codigo_cliente);
        values.put(Evento.COLUMN_ID_EVENTO, codigo_evento);
        values.put(Evento.COLUMN_VISIT_REASON,razon);
        values.put(Evento.COLUMN_TIPO_VISIT,tipo_visita);
        values.put(Evento.COLUMN_FECHA_PROGRAMACION, fecha_proga);
        values.put(Evento.COLUMN_FECHA_REALIZADA, fecha_realizado);
        values.put(Evento.COLUMN_LAT, latitud);
        values.put(Evento.COLUMN_LON, longitud);
        values.put(Evento.COLUMN_ESTADO_SINCRONIZADO, es_sic);
        values.put(Evento.COLUMN_ESTADO_LOCALIZACION, es_loc);
        values.put(Evento.COLUMN_ESTADO, es);


        // insert row
        long id = db.insert(Evento.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public int updateCliente(String codigo,String codigo_cliente,String codigo_evento, String razon,String fecha_proga,String fecha_realizado,
                             String latitud,String longitud,String es_loc,String es) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Evento.COLUMN_ID,codigo);
        values.put(Evento.COLUMN_ID_CLIENTE,codigo_cliente);
        values.put(Evento.COLUMN_ID_EVENTO, codigo_evento);
        values.put(Evento.COLUMN_VISIT_REASON,razon);
        values.put(Evento.COLUMN_FECHA_PROGRAMACION, fecha_proga);
        values.put(Evento.COLUMN_FECHA_REALIZADA, fecha_realizado);
        values.put(Evento.COLUMN_LAT, latitud);
        values.put(Evento.COLUMN_LON, longitud);
        values.put(Evento.COLUMN_ESTADO_SINCRONIZADO, es_loc);
        values.put(Evento.COLUMN_ESTADO, es);

        // updating row
        return db.update(Evento.TABLE_NAME, values, Evento.COLUMN_ID + " = ?",
                new String[]{String.valueOf(codigo)});

    }
    public void deleteAllEvento() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Evento.TABLE_NAME, Evento.COLUMN_ID + " != ?",
                new String[]{String.valueOf("0")});
        db.close();
    }
    public void deleteEvento(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Evento.TABLE_NAME, Evento.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note)});
        db.close();
    }

    //********************EVENTOS*************************

}
