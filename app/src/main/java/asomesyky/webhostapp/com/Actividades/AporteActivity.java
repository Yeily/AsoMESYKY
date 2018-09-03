package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asomesyky.webhostapp.com.Entidades.Socio;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.Globales.YPDF;
import asomesyky.webhostapp.com.R;

public class AporteActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Spinner cmbNombre;
    private EditText txtCodigo;
    private EditText txtFecha;
    private ImageButton btnFecha;
    private EditText txtMonto;
    private Spinner cmbPeriodo;
    private EditText txtAño;
    private Button btnGuardar;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;
    private StringRequest strJSON;
    private ArrayList<Socio> listaSocios = new ArrayList<>();

    private void InicializarComponentes() {
        cmbNombre = (Spinner) findViewById(R.id.cmbNombre);
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        btnFecha = (ImageButton) findViewById(R.id.btnFecha);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        cmbPeriodo = (Spinner) findViewById(R.id.cmbPeriodo);
        txtAño = (EditText) findViewById(R.id.txtAño);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        respuesta = Volley.newRequestQueue(this);

        cmbNombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtCodigo.setText(listaSocios.get(i).getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txtCodigo.setText("");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aporte);

        InicializarComponentes();
        LlenarDatos();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String estado = response.getString("resultado");

            if (estado.equals("OK")) {
                CargarSpinner(response);
            } else {
                Toast.makeText(this, response.getString("resultado"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void barNavegacion_Click(View view) {
        finish();
    }

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

                    if (estado.equals("OK")) {
                        //GenerarRecibo();
                        Limpiar();
                        Toast.makeText(getApplication(), "Transacción guardada correctamente!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorResponse(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("c", "5");
                params.put("socio", txtCodigo.getText().toString());
                params.put("fecha", txtFecha.getText().toString());
                params.put("monto", txtMonto.getText().toString());
                params.put("periodo", cmbPeriodo.getSelectedItem().toString());
                params.put("año", txtAño.getText().toString());

                return params;
            }
        };

        respuesta.add(strJSON);
    }

    private void LlenarDatos() {
        String url = Global.URL+"?c=1";

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    private void CargarSpinner(JSONObject respuestaJSON) {
        try {
            JSONArray datosJSON = respuestaJSON.optJSONArray("datos");
            JSONObject datos;

            for(int i = 0; i < datosJSON.length(); i++) {
                datos = datosJSON.getJSONObject(i);
                Socio s = new Socio();
                s.setCodigo(datos.optString("Socio"));
                s.setNombre(datos.optString("Nombre"));
                listaSocios.add(s);
            }

            ArrayAdapter<Socio> adp = new ArrayAdapter<Socio>(this, android.R.layout.simple_spinner_dropdown_item, listaSocios);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cmbNombre.setAdapter(adp);
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void GenerarRecibo() {
        YPDF pdf = new YPDF(getApplicationContext());

        try {
            pdf.abrirPDF("Recibo "+txtFecha.getText()+"_"+txtCodigo.getText());
            pdf.agregarMetaDatos("AsoMESYKY", "Recibo", "Administrador", "Yeily Calderon Marin");
            pdf.agregarTitulos("Comprobante de recibo de AsoMESYKY", "", txtFecha.getText().toString());
            pdf.agregarTabla(getEncabezados(), getDatos());
            pdf.agregarTexto("Guardese cuidadosamente");

            pdf.visualizarPDF(this);
        }
        catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            pdf.cerrarPDF();
        }
    }

    private String[] getEncabezados() {
        return new String[]{"Nombre", "Codigo", "Monto", "Otro"};
    }

    private ArrayList<String[]> getDatos() {
        ArrayList<String[]> filas = new ArrayList<>();

        try {
            filas.add(new String[]{"Kimber", "01-0004", "10000", "Otra cosa"});
            filas.add(new String[]{"Isis", "01-0005", "2000", "Otra cosa"});
            filas.add(new String[]{"Emilio", "01-0006", "5000", "Otra cosa"});
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return filas;
    }

    private void Limpiar() {
        txtCodigo.setText("");
        txtFecha.setText("");
        txtMonto.setText("");
        txtAño.setText("");
    }

    public void btnDoc_Click(View view) {
        GenerarRecibo();
    }
}
