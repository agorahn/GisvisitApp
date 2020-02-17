package hn.gisvisit;

public class Evento {

    public static final String TABLE_NAME = "evento";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_EVENTO = "id_evento";
    public static final String COLUMN_ID_CLIENTE = "codigo_cliente";
    public static final String COLUMN_VISIT_REASON = "razon_visita";
    public static final String COLUMN_TIPO_VISIT = "tipo_visita";
    public static final String COLUMN_FECHA_PROGRAMACION = "fecha_programada";
    public static final String COLUMN_FECHA_REALIZADA = "fecha_realizada";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitud";
    public static final String COLUMN_ESTADO_LOCALIZACION = "estado_localizacion";
    public static final String COLUMN_ESTADO = "estado"; //estado 0=No visitado 1=visitado 2=creado desde app
    public static final String COLUMN_ESTADO_SINCRONIZADO= "estado_sincronizado"; //0=sin sincronicar 1=sincronizado


    public static final  String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_ID_EVENTO + " text not null,"
            + COLUMN_ID_CLIENTE + " text not null,"
            + COLUMN_VISIT_REASON + " text not null,"
            + COLUMN_TIPO_VISIT + " text not null,"
            + COLUMN_FECHA_PROGRAMACION + " text not null,"
            + COLUMN_FECHA_REALIZADA + " text not null,"
            + COLUMN_LAT + " text not null,"
            + COLUMN_LON + " text not null,"
            + COLUMN_ESTADO_LOCALIZACION + " text not null,"
            + COLUMN_ESTADO_SINCRONIZADO + " text not null,"
            + COLUMN_ESTADO + " text not null"
            +");";

    private int id;
    private String codigo_c_cliente;
    private String codigo_c_evento;
    private String visit_reason;
    private String tipo_visit;
    private String fecha_programacion;
    private String fecha_realizada;
    private String latitude;
    private String longitud;
    private String estado_localizacion;
    private String estado_sincronizar;
    private String estado;

    public Evento()
    {

    }

    public Evento(int i,String c_c,String c_e, String v,String tv,String fp,String fr,String lt,String ln,String es_loc,String es_sic,String es) {

        this.id=i;
        this.codigo_c_cliente=c_c;
        this.codigo_c_evento=c_e;
        this.visit_reason=v;
        this.tipo_visit=tv;
        this.fecha_programacion=fp;
        this.fecha_realizada=fr;
        this.latitude=lt;
        this.longitud=ln;
        this.estado_sincronizar=es_sic;
        this.estado_localizacion=es_loc;
        this.estado=es;
    }

    public String getTipo_visit() {
        return tipo_visit;
    }

    public void setTipo_visit(String tipo_visit) {
        this.tipo_visit = tipo_visit;
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

    public void setCodigo_c_cliente(String codigo_c_cliente) {
        this.codigo_c_cliente = codigo_c_cliente;
    }

    public String getCodigo_c_cliente() {
        return codigo_c_cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo_c_evento() {
        return codigo_c_evento;
    }

    public String getFecha_programacion() {
        return fecha_programacion;
    }

    public String getFecha_realizada() {
        return fecha_realizada;
    }

    public String getVisit_reason() {
        return visit_reason;
    }

    public void setCodigo_c_evento(String codigo_c_evento) {
        this.codigo_c_evento = codigo_c_evento;
    }

    public void setFecha_programacion(String fecha_programacion) {
        this.fecha_programacion = fecha_programacion;
    }

    public void setFecha_realizada(String fecha_realizada) {
        this.fecha_realizada = fecha_realizada;
    }

    public void setVisit_reason(String visit_reason) {
        this.visit_reason = visit_reason;
    }

    public void setEstado_localizacion(String estado_localizacion) {
        this.estado_localizacion = estado_localizacion;
    }

    public String getEstado_localizacion() {
        return estado_localizacion;
    }


}
