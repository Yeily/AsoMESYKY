package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import asomesyky.webhostapp.com.Entidades.Inversion;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

public class LiquidaInvActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private ArrayList<Inversion> inversiones;
    private RecyclerView rvInversones;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    private void InicializarComponentes(){
        inversiones = new ArrayList<>();
        rvInversones = (RecyclerView) findViewById(R.id.rcyLista);
        rvInversones.setLayoutManager(new LinearLayoutManager(this));

        respuesta = Volley.newRequestQueue(this);

        GetInversiones();
        AdaptadorLiquidaInv adp = new AdaptadorLiquidaInv(inversiones);
        rvInversones.setAdapter(adp);
    }

    private void GetInversiones(){
        String url = Global.URL+"?c=10";

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidainv);

        InicializarComponentes();
    }

    public void barNavegacion_Click(View view) { finish(); }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        try {
            String estado = response.getString("resultado");

            if(estado.equals("OK")) {
                JSONArray datos = response.optJSONArray("datos");

                for(int i = 0; i < datos.length(); i++) {
                    /*JSONObject dato = datos.getJSONObject(i);
                    Inversion inv = new Inversion();

                    inv.setDocumento(dato.getString("Documento"));
                    Toast.makeText(this, inv.getDocumento(), Toast.LENGTH_SHORT).show();
                    inv.setComprobante(dato.getString("Comprobante"));
                    inv.setEntidad(dato.getString("Entidad"));
                    inv.setPlan(dato.getString("Plan"));
                    inv.setFechaInicial(Convertir.toFecha(dato.getString("FechaInicial")));
                    inv.setFechaVencimiento(Convertir.toFecha(dato.getString("FechaVencimiento")));
                    inv.setMonto(Double.parseDouble(dato.getString("Monto")));
                    inv.setInteresAnual(Float.parseFloat(dato.getString("InteresAnual")));
                    inv.setImpuestoRenta(Double.parseDouble(dato.getString("ImpuestoRenta")));
                    inv.setGanancia(Double.parseDouble(dato.getString("Ganancia")));
                    inv.setPeriodo(dato.getString("Periodos"));
                    inv.setAño(Integer.parseInt(dato.getString("Ano")));
                    inv.setLiquidada(dato.getString("Liquidada").toCharArray()[0]);

                    //Toast.makeText(this, datos.getJSONObject(i).toString(), Toast.LENGTH_SHORT).show();
                    //inversiones.add(inv);
                    inversiones.add(new Inversion(dato.getString("Documento"), dato.getString("Comprobante"), dato.getString("Entidad"),
                            dato.getString("Plan"), Convertir.toFecha(dato.getString("FechaInicial")), Convertir.toFecha(dato.getString("FechaVencimiento")),
                            Double.parseDouble(dato.getString("Monto")), Float.parseFloat(dato.getString("InteresAnual")), Double.parseDouble(dato.getString("ImpuestoRenta")),
                            Double.parseDouble(dato.getString("Ganancia")), dato.getString("Periodos"), Integer.parseInt(dato.getString("Ano")), dato.getString("Liquidada").toCharArray()[0]));*/
                    inversiones.add(new Inversion("Documento", "Comprobante", "Entidad", "Plan", Convertir.toFecha("2018-01-01"),
                            Convertir.toFecha("2018-05-01"), 100.00, Float.parseFloat("5"), Double.parseDouble("200"), Double.parseDouble("300"),
                            "Periodos", 2018, "N".toCharArray()[0]));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
