package hn.gisvisit;

public class Visita {

    public static final String TABLE_NAME = "visita";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_CLIENTE = "codigo_cliente";
    public static final String COLUMN_VISIT_REASON = "razon_visita";
    public static final String COLUMN_OBSERVACION = "observacion";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_FOTO1 = "foto1";
    public static final String COLUMN_FOTO2 = "foto2";
    public static final String COLUMN_FOTO3 = "foto3";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitud";
    public static final String COLUMN_PROXIMA_VISITA = "fecha_proxima";
    public static final String COLUMN_CODIGO_EVENTO = "codigo_evento"; //estado 0=evento nuevo ?=otro numero es el id del evento
    public static final String COLUMN_ESTADO = "estado"; //estado 0=visita cliente 1=visita cliente nuevo
    public static final String COLUMN_ESTADO_SINCRONIZADO = "estado_sincronizado"; //0=sin sincronicar 1=sincronizado


    public static final  String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_ID_CLIENTE + " text not null,"
            + COLUMN_VISIT_REASON + " text not null,"
            + COLUMN_OBSERVACION + " text not null,"
            + COLUMN_FECHA + " text not null,"
            + COLUMN_FOTO1 + " text not null,"
            + COLUMN_FOTO2 + " text not null,"
            + COLUMN_FOTO3 + " text not null,"
            + COLUMN_LAT + " text not null,"
            + COLUMN_LON + " text not null,"
            + COLUMN_PROXIMA_VISITA + " text not null,"
            + COLUMN_ESTADO_SINCRONIZADO + " text not null,"
            + COLUMN_CODIGO_EVENTO + " text not null,"
            + COLUMN_ESTADO + " text not null"
            +");";

    private int id;
    private String codigo_c_cliente;
    private String razon_visita;
    private String observacion;
    private String fecha;
    private String foto1;
    private String foto2;
    private String foto3;
    private String latitude;
    private String longitud;
    private String fecha_proxima;
    private String codigo_evento;
    private String estado_sincronizar;
    private String estado;

    public Visita()
    {

    }

    public Visita(int i,String c_c,String r, String o,String f,String f1,String f2,String f3,String lt,String ln,String fp,String es_loc,String c_evento,String es_sin,String es) {

        this.id=i;
        this.codigo_c_cliente=c_c;
        this.razon_visita=r;
        this.observacion=o;
        this.fecha=f;
        this.foto1=f1;
        this.foto2=f2;
        this.foto3=f3;
        this.latitude=lt;
        this.longitud=ln;
        this.fecha_proxima=fp;
        this.estado_sincronizar=es_loc;
        this.codigo_evento=c_evento;
        this.estado=es;

    }
    public void setCodigo_evento(String codigo_evento) {
        this.codigo_evento = codigo_evento;
    }
    public String getCodigo_evento() {
        return codigo_evento;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getCodigo_c_cliente() {
        return codigo_c_cliente;
    }
    public void setCodigo_c_cliente(String codigo_c_cliente) {
        this.codigo_c_cliente = codigo_c_cliente;
    }
    public String getRazon_visita() {
        return razon_visita;
    }
    public void setRazon_visita(String razon_visita) {
        this.razon_visita = razon_visita;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setEstado_sincronizar(String estado_sincronizar) {
        this.estado_sincronizar = estado_sincronizar;
    }

    public String getEstado_sincronizar() {
        return estado_sincronizar;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto1() {
        return foto1;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha_proxima() {
        return fecha_proxima;
    }

    public void setFecha_proxima(String fecha_proxima) {
        this.fecha_proxima = fecha_proxima;
    }
}
