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

public class GastoActivity extends AppCompatActivity {
    private EditText txtDocumento;
    private EditText txtFecha;
    private EditText txtMonto;
    private EditText txtPeriodos;
    private EditText txtAño;
    private EditText txtNota;

    private RequestQueue respuesta;
    private StringRequest strJSON;

    private void InicializarComponentes() {
        txtDocumento = (EditText) findViewById(R.id.txtDocumento);
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        txtPeriodos = (EditText) findViewById(R.id.txtPeriodos);
        txtAño = (EditText) findViewById(R.id.txtAño);
        txtNota = (EditText) findViewById(R.id.txtNota);

        respuesta = Volley.newRequestQueue(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        InicializarComponentes();
    }

    public void barNavegacion_Click(View view) { finish(); }

    public void btnFecha_Click(View view) {
        Global.setFecha(this, txtFecha);
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
                        Toast.makeText(getApplication(), "Gasto guardado correctamente!", Toast.LENGTH_SHORT).show();
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
                params.put("c", "12");
                params.put("doc", txtDocumento.getText().toString());
                params.put("fec", txtFecha.getText().toString());
                params.put("mon", txtMonto.getText().toString());
                params.put("per", txtPeriodos.getText().toString());
                params.put("año", txtAño.getText().toString());
                params.put("not", txtNota.getText().toString());

                return params;
            }
        };

        respuesta.add(strJSON);
    }

    private void Limpiar() {
        txtDocumento.setText("");
        txtFecha.setText("");
        txtMonto.setText("");
        txtPeriodos.setText("");
        txtAño.setText("");
        txtNota.setText("");
    }
}
