package asomesyky.webhostapp.com.Entidades;

import java.util.Date;

public class Inversion {
    private String pDocumento;
    private String pComprobante;
    private String pEntidad;
    private String pPlan;
    private Date pFechaInicial;
    private Date pFechaVencimiento;
    private Double pMonto;
    private Float pInteresAnual;
    private String pPeriodo;
    private Integer pAño;

    public Inversion() {}

    //gets
    public String getDocumento() { return pDocumento; }
    public String getComprobante() { return pComprobante; }
    public String getEntidad() { return pEntidad; }
    public String getPlan() { return pPlan; }
    public Date getFechaInicial() { return pFechaInicial; }
    public Date getFechaVencimiento() { return pFechaVencimiento; }
    public Double getMonto() { return pMonto; }
    public Float getInteresAnual() { return pInteresAnual; }
    public String getPeriodo() { return pPeriodo; }
    public Integer getAño() { return pAño; }

    //sets
    public void setDocumento(String documento) { pDocumento = documento; }
    public void setComprobante(String comprobante) { pComprobante = comprobante; }
    public void setEntidad(String entidad) { pEntidad = entidad; }
    public void setPlan(String plan) { pPlan = plan; }
    public void setFechaInicial(Date fecha) { pFechaInicial = fecha ; }
    public void setFechaVencimiento(Date fecha) { pFechaVencimiento = fecha; }
    public void setMonto(Double monto) { pMonto = monto; }
    public void setInteresAnual(Float porcentaje) { pInteresAnual = porcentaje; }
    public void setPeriodo(String periodo) { pPeriodo = periodo; }
    public void setAño(Integer año) { pAño = año; }
}
