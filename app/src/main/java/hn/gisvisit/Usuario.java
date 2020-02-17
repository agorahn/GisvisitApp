package hn.gisvisit;

public class Usuario {

    public static final String TABLE_NAME = "usuario";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_USUARIO = "usuario";
    public static final String COLUMN_CLAVE = "clave";
    public static final String COLUMN_SEGUNDOS = "segundos";
    public static final String COLUMN_ESTADO = "estado";//activo de usuario 0 no hay usuario, 1 is login
    public static final String COLUMN_ESTADO_TIPO = "estadoTipo";//es ventas o cobros
    public static final String COLUMN_AUTOGUARDO = "estado_auto";//auto guardado
    public static final String COLUMN_LOGIN = "estado_login";//esta logeado


    public static final  String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_CODIGO + " text not null,"
            + COLUMN_USUARIO + " text not null,"
            + COLUMN_CLAVE + " text not null,"
            + COLUMN_SEGUNDOS + " text not null,"
            + COLUMN_ESTADO + " text not null,"
            + COLUMN_ESTADO_TIPO + " text not null,"
            + COLUMN_AUTOGUARDO + " text not null,"
            + COLUMN_LOGIN + " text not null"
            +");";

    private int id;
    private String codigo;
    private String nombre;
    private String clave;
    private String segundos;
    private String estado;
    private String estado_tipo;
    private String estado_autoguardado;
    private String estado_login;



    public Usuario() {
    }

    public Usuario(int id,String codigo, String nombre, String clave,String s,String estado,String estado_tipo,String autoguardado,String login) {
        this.id = id;
        this.codigo=codigo;
        this.nombre = nombre;
        this.clave = clave;
        this.segundos=s;
        this.estado = estado;
        this.estado_tipo = estado_tipo;
        this.estado_autoguardado=autoguardado;
        this.estado_login=login;
    }

    public String getEstado_login() {
        return estado_login;
    }

    public void setEstado_login(String estado_login) {
        this.estado_login = estado_login;
    }
    public void setEstado_autoguardado(String estado_autoguardado) {
        this.estado_autoguardado = estado_autoguardado;
    }
    public String getEstado_autoguardado() {
        return estado_autoguardado;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public String getSegundos() {
        return segundos;
    }
    public void setSegundos(String segundos) {
        this.segundos = segundos;
    }
    public int getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String ultima) {
        this.clave = ultima;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstado(String tipo) {
        this.estado = tipo;
    }

    public void setEstado_tipo(String tipo_tipo) {
        this.estado_tipo = tipo_tipo;
    }

    public String getEstado_tipo() {
        return estado_tipo;
    }
    
}

