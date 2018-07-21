package asomesyky.webhostapp.com.Entidades;

import java.util.Date;

public class Aporte {
    private Integer pId;
    private String pSocio;
    private Date pFecha;
    private Double pMonto;
    private String pPeriodo;
    private Integer pAño;

    public Aporte() {}

    //gets
    public Integer getId() {
        return pId;
    }
    public String getSocio() {
        return pSocio;
    }
    public Date getFecha() {
        return pFecha;
    }
    public Double getMonto() {
        return pMonto;
    }
    public String getPeriodo() {
        return pPeriodo;
    }
    public Integer getAño() {
        return pAño;
    }

    //sets
    public void setSocio(String socio) {
        pSocio = socio;
    }
    public void setFecha(Date fecha ) {
        pFecha = fecha;
    }
    public void setMonto(Double monto) {
        pMonto = monto;
    }
    public void setPeriodo(String periodo) {
        pPeriodo = periodo;
    }
    public void setAño(Integer año) {
        pAño = año;
    }
}
