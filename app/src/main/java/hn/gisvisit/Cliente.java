package hn.gisvisit;

public class Cliente {

    public static final String TABLE_NAME = "cliente";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_C_EMPRESA = "codigo_empresa";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DIRECCION = "direccion";
    public static final String COLUMN_IDENTIDAD = "identidad";
    public static final String COLUMN_CORREO = "correo";
    public static final String COLUMN_TELEFONO = "telefono";
    public static final String COLUMN_CELULAR = "celular";
    public static final String COLUMN_FOTO1 = "foto1";
    public static final String COLUMN_FOTO2 = "foto2";
    public static final String COLUMN_FOTO3 = "foto3";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitud";
    public static final String COLUMN_ESTADO_LOCALIZACION = "estado_localizacion";
    public static final String COLUMN_ESTADO = "estado"; //estado 0=cargado desde la base de datos 1=modificado 2=nuevo/nuevo modificado
    public static final String COLUMN_ESTADO_SINCRONIZADO = "estado_sincronizado"; //0=sin sincronicar 1=sincronizado


    public static final  String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_CODIGO + " text not null,"
            + COLUMN_C_EMPRESA + " text not null,"
            + COLUMN_NOMBRE + " text not null,"
            + COLUMN_DIRECCION + " text not null,"
            + COLUMN_IDENTIDAD + " text not null,"
            + COLUMN_CORREO + " text not null,"
            + COLUMN_TELEFONO + " text not null,"
            + COLUMN_CELULAR + " text not null,"
            + COLUMN_FOTO1 + " text not null,"
            + COLUMN_FOTO2 + " text not null,"
            + COLUMN_FOTO3 + " text not null,"
            + COLUMN_LAT + " text not null,"
            + COLUMN_LON + " text not null,"
            + COLUMN_ESTADO_LOCALIZACION + " text not null,"
            + COLUMN_ESTADO_SINCRONIZADO + " text not null,"
            + COLUMN_ESTADO + " text not null"
            +");";

    private int id;
    private String codigo;
    private String codigo_c_empresa;
    private String nombre;
    private String direccion;
    private String indentidad;
    private String correo;
    private String telefono;
    private String celular;
    private String foto1;
    private String foto2;
    private String foto3;
    private String latitude;
    private String longitud;
    private String estado_localizacion;
    private String estado_sincronizar;
    private String estado;

    public Cliente() {
    }

    public Cliente(int id,String c,String c_e,String n, String d,String i,String correo,String t,String cel,String f1,String f2,String f3,String lt,String ln,String es_loc,String es_sin,String estado) {
        this.id = id;
        this.codigo=c;
        this.codigo_c_empresa=c_e;
        this.nombre = n;
        this.direccion = d;
        this.correo=correo;
        this.indentidad=i;
        this.telefono = t;
        this.celular = cel;
        this.foto1=f1;
        this.foto2=f2;
        this.foto3=f3;
        this.latitude=lt;
        this.longitud=ln;
        this.estado_localizacion=es_loc;
        this.estado_sincronizar=es_sin;
        this.estado=estado;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getCodigo_c_empresa() {
        return codigo_c_empresa;
    }

    public void setCodigo_c_empresa(String codigo_c_empresa) {
        this.codigo_c_empresa = codigo_c_empresa;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado_sincronizar() {
        return estado_sincronizar;
    }

    public void setEstado_sincronizar(String estado_sincronizar) {
        this.estado_sincronizar = estado_sincronizar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }

    public String getIndentidad() {
        return indentidad;
    }

    public void setIndentidad(String indentidad) {
        this.indentidad = indentidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado_localizacion() {
        return estado_localizacion;
    }

    public void setEstado_localizacion(String estado_localizacion) {
        this.estado_localizacion = estado_localizacion;
    }
}
