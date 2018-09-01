package asomesyky.webhostapp.com.Entidades;

public class Resumen {
    private String pSocio;
    private String pNombre;
    private Double pTotal;

    public Resumen(){ }

    public Resumen(String socio, String nombre, Double total) {
        pSocio = socio;
        pNombre = nombre;
        pTotal = total;
    }

    public String getSocio() { return pSocio; }
    public String getNombre() { return pNombre; }
    public Double getTotal() { return pTotal; }

    public void setSocio(String socio) { pSocio = socio; }
    public void setNombre(String nombre) { pNombre = nombre; }
    public void setTotal(Double total) { pTotal = total; }
}
