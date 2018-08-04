package asomesyky.webhostapp.com.Actividades;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

public class RegistroActivity extends AppCompatActivity {
    private EditText txtCodigo;
    private EditText txtNombre;
    private EditText txtFecha;
    private EditText txtTelefono;
    private EditText txtCorreo;

    private RequestQueue respuesta;
    private StringRequest strJSON;

    private void InicializarComponentes() {
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);

        respuesta = Volley.newRequestQueue(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        InicializarComponentes();
    }

    public void barNavegacion_Click(View view) { finish(); }

    public void btnFecha_Click(View view) {
        final Calendar calendario = Calendar.getInstance();
        final int año = calendario.get(calendario.YEAR);
        final int mes = calendario.get(calendario.MONTH);
        final int dia = calendario.get(calendario.DAY_OF_MONTH);

        DatePickerDialog fecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int a, int m, int d) {
                final int mesActual = m+1;
                String diaFormateado = (d < 10)? "0"+String.valueOf(d) : String.valueOf(d);
                String mesFormateado = (mesActual < 10)? "0"+String.valueOf(mesActual) : String.valueOf(mesActual);

                txtFecha.setText(a +"-"+ mesFormateado +"-"+ diaFormateado);
            }
        }, año, mes, dia);

        fecha.show();
    }

    public void btnGuardar_Click(View view) {
        strJSON = new StringRequest(Request.Method.POST, Global.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuestaJSON = new JSONObject(response);
                    String estado = respuestaJSON.getString("resultado");

                    if (estado.equals("OK")) {
                        Limpiar();
                        Toast.makeText(getApplication(), "Socio registrado correctamente!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("c", "4");
                params.put("socio", txtCodigo.getText().toString());
                params.put("nombre", txtNombre.getText().toString());
                params.put("fecha", txtFecha.getText().toString());
                params.put("telefono", txtTelefono.getText().toString());
                params.put("correo", txtCorreo.getText().toString());

                return params;
            }
        };

        respuesta.add(strJSON);
    }

    private void Limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtFecha.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
    }
}
