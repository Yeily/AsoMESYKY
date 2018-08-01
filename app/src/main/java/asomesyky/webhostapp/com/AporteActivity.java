package asomesyky.webhostapp.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import asomesyky.webhostapp.com.Entidades.Socio;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.Globales.Global;

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

            if(estado.equals("OK")) {
                CargarSpinner(response);
                Toast.makeText(this, "onResponse", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, response.getString("resultado"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void LlenarDatos() {
        String url = Global.URL+"c=1";

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    private void CargarSpinner(JSONObject respuestaJSON) {
        ArrayList<String> listaSocios = new ArrayList<String>();

        try {
            JSONArray datosJSON = respuestaJSON.optJSONArray("datos");
            JSONObject datos;

            for(int i = 0; i < datosJSON.length(); i++) {
                datos = datosJSON.getJSONObject(i);
                Socio s = new Socio();
                s.setNombre(datos.optString("Nombre"));
                listaSocios.add(s.getNombre());
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaSocios);
            cmbNombre.setAdapter(adp);
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void barNavegacion_Click(View view) {
        finish();
    }

    public void btnFecha_Click(View view) {

    }

    public void btnGuardar_Click(View view) {

    }
}
