package asomesyky.webhostapp.com.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txtCodigo;

    private RequestQueue respuesta;
    private StringRequest strJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        InicializarComponentes();
    }

    private void InicializarComponentes() {
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);

        respuesta = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = null;

        switch(view.getId()) {
            case R.id.btnNuevoAporte:
                i = new Intent(this, AporteActivity.class);
                break;
            case R.id.btnNuevoSocio:
                i = new Intent(this, RegistroActivity.class);
                break;
            case R.id.btnNuevaInversion:
                i = new Intent(this, InversionActivity.class);
                break;
            case R.id.btnGanancia:
                i = new Intent(this, LiquidaInvActivity.class);
                break;
            case R.id.btnGasto:
                i = new Intent(this, GastoActivity.class);
                break;
            case R.id.btnResetPass:
                if(txtCodigo.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Debe especificar el código al que le desea resetear la contraseña.", Toast.LENGTH_LONG).show();
                } else {
                    CambiarClave();
                }
                break;
        }

        if(i != null) {
            startActivity(i);
        }
    }

    public void barNavegacion_Click(View view) {
        finish();
    }

    private void CambiarClave() {
        strJSON = new StringRequest(Request.Method.POST, Global.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuestaJSON = new JSONObject(response);
                    String estado = respuestaJSON.getString("resultado");

                    if (estado.equals("OK")) {
                        Toast.makeText(getApplication(), "La contraseña se ha reseteado correctamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(getApplication(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.i("ERROR", volleyError.toString());
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("c", "6");
                params.put("socio", txtCodigo.getText().toString());
                params.put("pass", txtCodigo.getText().toString());

                return params;
            }
        };

        respuesta.add(strJSON);
    }
}
