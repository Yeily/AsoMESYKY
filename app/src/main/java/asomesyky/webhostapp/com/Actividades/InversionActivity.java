package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

public class InversionActivity extends AppCompatActivity {
    private EditText txtDocumento;
    private EditText txtComprobante;
    private EditText txtEntidad;
    private EditText txtPlan;
    private EditText txtFechaInicio;
    private EditText txtFechaVencimiento;
    private EditText txtMonto;
    private EditText txtInteres;
    private EditText txtImpuesto;
    private EditText txtGanancia;
    private Spinner cmbPeriodo;
    private EditText txtAño;

    private RequestQueue respuesta;
    private StringRequest strJSON;

    private void InicializarComponentes() {
        txtDocumento = (EditText) findViewById(R.id.txtDocumento);
        txtComprobante = (EditText) findViewById(R.id.txtComprobante);
        txtEntidad = (EditText) findViewById(R.id.txtEntidad);
        txtPlan = (EditText) findViewById(R.id.txtPlan);
        txtFechaInicio = (EditText) findViewById(R.id.txtFechaInicio);
        txtFechaVencimiento = (EditText) findViewById(R.id.txtFechaVencimiento);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        txtInteres = (EditText) findViewById(R.id.txtInteresAnual);
        txtImpuesto = (EditText) findViewById(R.id.txtImpuesto);
        txtGanancia = (EditText) findViewById(R.id.txtGanancia);
        cmbPeriodo = (Spinner) findViewById(R.id.cmbPeriodo);
        txtAño = (EditText) findViewById(R.id.txtAño);

        respuesta = Volley.newRequestQueue(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inversion);

        InicializarComponentes();
    }

    public void btnFechaVencimiento_Click(View view) {
        Global.setFecha(this, txtFechaVencimiento);
    }

    public void btnFechaInicio_Click(View view) {
        Global.setFecha(this, txtFechaInicio);
    }

    public void btnGuardar_Click(View view) {
        strJSON = new StringRequest(Request.Method.POST, Global.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuestaJSON = new JSONObject(response);
                    String estado = respuestaJSON.getString("resultado");

                    if(estado.equals("OK")) {
                        Limpiar();
                        Toast.makeText(getApplication(), "Inversión guardada correctamente!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException ex) {
                    Toast.makeText(getApplication(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("ERROR", error.toString());
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("c", "7");
                params.put("doc", txtDocumento.getText().toString());
                params.put("comp", txtComprobante.getText().toString());
                params.put("enti", txtEntidad.getText().toString());
                params.put("plan", txtPlan.getText().toString());
                params.put("ini", txtFechaInicio.getText().toString());
                params.put("ven", txtFechaVencimiento.getText().toString());
                params.put("mon", txtMonto.getText().toString());
                params.put("int", txtInteres.getText().toString());
                params.put("imp", txtImpuesto.getText().toString());
                params.put("gan", txtGanancia.getText().toString());
                params.put("peri", cmbPeriodo.getSelectedItem().toString());
                params.put("año", txtAño.getText().toString());

                return params;
            }
        };

        respuesta.add(strJSON);
    }

    private void Limpiar() {
        txtDocumento.setText("");
        txtComprobante.setText("");
        txtEntidad.setText("");
        txtPlan.setText("");
        txtFechaInicio.setText("");
        txtFechaVencimiento.setText("");
        txtMonto.setText("");
        txtInteres.setText("");
        txtImpuesto.setText("");
        txtGanancia.setText("");
        txtAño.setText("");
    }
}
