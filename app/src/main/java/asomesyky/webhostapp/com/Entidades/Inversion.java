package asomesyky.webhostapp.com.Entidades;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;

import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.Globales.Global;

public class Inversion {
    private String pDocumento;
    private String pComprobante;
    private String pEntidad;
    private String pPlan;
    private Date pFechaInicial;
    private Date pFechaVencimiento;
    private Double pMonto;
    private Float pInteresAnual;
    private Double pImpuestoRenta;
    private Double pGanancia;
    private String pPeriodo;
    private Integer pAño;
    private Character pLiquidada;

    public Inversion() {}

    public Inversion(JSONObject json) {
        setInversion(json);
    }

    public Inversion(String documento, String comprobante, String entidad, String plan,
                     Date fechaInicial, Date fechaVencimiento, Double monto, Float interesAnual,
                     Double impuestoRenta, Double ganancia, String periodo, Integer año, Character liquidada) {
        pDocumento = documento;
        pComprobante = comprobante;
        pEntidad = entidad;
        pPlan = plan;
        pFechaInicial = fechaInicial;
        pFechaVencimiento = fechaVencimiento;
        pMonto = monto;
        pInteresAnual = interesAnual;
        pImpuestoRenta = impuestoRenta;
        pGanancia = ganancia;
        pPeriodo = periodo;
        pAño = año;
        pLiquidada = liquidada;
    }

    //gets
    public String getDocumento() { return pDocumento; }
    public String getComprobante() { return pComprobante; }
    public String getEntidad() { return pEntidad; }
    public String getPlan() { return pPlan; }
    public Date getFechaInicial() { return pFechaInicial; }
    public Date getFechaVencimiento() { return pFechaVencimiento; }
    public Double getMonto() { return pMonto; }
    public Float getInteresAnual() { return pInteresAnual; }
    public Double getImpuestoRenta() { return pImpuestoRenta; }
    public Double getGanancia() { return pGanancia; }
    public String getPeriodo() { return pPeriodo; }
    public Integer getAño() { return pAño; }
    public Character getLiquidada() { return pLiquidada; }

    //sets
    public void setDocumento(String documento) { pDocumento = documento; }
    public void setComprobante(String comprobante) { pComprobante = comprobante; }
    public void setEntidad(String entidad) { pEntidad = entidad; }
    public void setPlan(String plan) { pPlan = plan; }
    public void setFechaInicial(Date fecha) { pFechaInicial = fecha ; }
    public void setFechaInicial(String fecha) { pFechaInicial = Convertir.toFecha(fecha); }
    public void setFechaVencimiento(Date fecha) { pFechaVencimiento = fecha; }
    public void setFechaVencimiento(String fecha) { pFechaVencimiento = Convertir.toFecha(fecha); }
    public void setMonto(Double monto) { pMonto = monto; }
    public void setMonto(String monto) { pMonto = Double.parseDouble(monto); }
    public void setInteresAnual(Float porcentaje) { pInteresAnual = porcentaje; }
    public void setInteresAnual(String porcentaje) { pInteresAnual = Float.parseFloat(porcentaje); }
    public void setImpuestoRenta(Double impuestoRenta) { pImpuestoRenta = impuestoRenta; }
    public void setImpuestoRenta(String impuestoRenta) { pImpuestoRenta = Double.parseDouble(impuestoRenta); }
    public void setGanancia(Double ganancia) { pGanancia = ganancia; }
    public void setGanancia(String ganancia) { pGanancia = Double.parseDouble(ganancia); }
    public void setPeriodo(String periodo) { pPeriodo = periodo; }
    public void setAño(Integer año) { pAño = año; }
    public void setAño(String año) { pAño = Integer.parseInt(año); }
    public void setLiquidada(Character liquidada) { pLiquidada = liquidada; }
    public void setLiquidada(String liquidada) { pLiquidada = liquidada.toCharArray()[0]; }

    //Métodos
    public void setInversion(JSONObject json) {
        try {
            pDocumento = json.getString("Documento");
            pComprobante = json.getString("Comprobante");
            pEntidad = json.getString("Entidad");
            pPlan = json.getString("Plan");
            pFechaInicial = Convertir.toFecha(json.getString("FechaInicial"));
            pFechaVencimiento = Convertir.toFecha(json.getString("FechaVencimiento"));
            pMonto = Double.parseDouble(json.getString("Monto"));
            pInteresAnual = Float.parseFloat(json.getString("InteresAnual"));
            pImpuestoRenta = Double.parseDouble(json.getString("ImpuestoRenta"));
            pGanancia = Double.parseDouble(json.getString("Ganancia"));
            pPeriodo = json.getString("Periodos");
            pAño = Integer.parseInt(json.getString("Año"));
            pLiquidada = json.getString("Liquidada").toCharArray()[0];
        } catch(Exception ex) {
            Log.i("ERROR", ex.getMessage());
        }
    }
}
