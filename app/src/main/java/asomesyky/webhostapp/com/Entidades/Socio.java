package asomesyky.webhostapp.com.Entidades;

import java.util.Date;

public class Socio {
    private String pCodigo;
    private String pNombre;
    private String pPass;
    private Date pFechaIngreso;
    private Boolean pActivo;
    private String pTelefono;
    private String pCorreo;

    public Socio() {}

    //Métodos get
    public String getCodigo() {
        return pCodigo;
    }
    public String getNombre() {
        return pNombre;
    }
    public String getPass() {
        return pPass;
    }
    public Date getFechaIngreso() {
        return pFechaIngreso;
    }
    public Boolean getActivo() {
        return pActivo;
    }
    public String getTelefono() {
        return pTelefono;
    }
    public String getCorreo() {
        return pCorreo;
    }

    //Métodos set
    public void setCodigo(String codigo) {
        this.pCodigo = codigo;
    }
    public void setNombre(String nombre) {
        this.pNombre = nombre;
    }
    public void setPass(String pass) {
        this.pPass = pass;
    }
    public void setFechaIngreso(Date fechaIngreso) {
        this.pFechaIngreso = fechaIngreso;
    }
    public void setActivo(Boolean activo) {
        this.pActivo = activo;
    }
    public void setTelefono(String telefono) {
        this.pTelefono = telefono;
    }
    public void setCorreo(String correo) {
        this.pCorreo = correo;
    }
}
