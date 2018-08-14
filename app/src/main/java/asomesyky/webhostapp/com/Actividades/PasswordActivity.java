package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class PasswordActivity extends AppCompatActivity {
    private EditText txtSocio;
    private EditText txtNueva;
    private EditText txtConfirma;

    private RequestQueue respuesta;
    private StringRequest strJSON;

    private void InicializarComponentes() {
        txtSocio = (EditText) findViewById(R.id.txtSocio);
        txtNueva = (EditText) findViewById(R.id.txtNueva);
        txtConfirma = (EditText) findViewById(R.id.txtConfirma);

        txtSocio.setText(Global.usuarioActual.getCodigo());
        respuesta = Volley.newRequestQueue(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        InicializarComponentes();
    }

    public void barNavegacion_Click(View view) { finish(); }

    public void btnCambiar_Click(View view) {
        if(txtNueva.getText().toString().equals(txtConfirma.getText().toString())) {
            strJSON = new StringRequest(Request.Method.POST, Global.URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuestaJSON = new JSONObject(response);
                        String estado = respuestaJSON.getString("resultado");

                        if (estado.equals("OK")) {
                            Toast.makeText(getApplication(), "La contraseña se ha establecido correctamente.", Toast.LENGTH_SHORT).show();
                            finish();
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
                    params.put("c", "6");
                    params.put("socio", txtSocio.getText().toString());
                    params.put("pass", txtNueva.getText().toString());

                    return params;
                }
            };

            respuesta.add(strJSON);
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
        }
    }
}
