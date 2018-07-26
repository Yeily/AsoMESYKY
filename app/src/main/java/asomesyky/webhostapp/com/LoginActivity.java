package asomesyky.webhostapp.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import asomesyky.webhostapp.com.Entidades.Socio;

public class LoginActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText txtCodigo;
    private EditText txtPass;
    private Button btnIngresar;
    private ProgressDialog barProgreso;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    private static String USUARIO;

    private void InicializarComponentes() {
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        respuesta = Volley.newRequestQueue(this);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USUARIO = txtCodigo.getText().toString();
                Loguear();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InicializarComponentes();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        barProgreso.hide();
        Toast.makeText(this, "Error al consultar socio: "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        barProgreso.hide();

        Socio user = new Socio();
        JSONArray datosJSON = response.optJSONArray("datos");
        JSONObject datos = null;

        try {
            String estado = response.getString("resultado");

            if(estado.equals("OK")) {
                datos = datosJSON.getJSONObject(0);
                user.setCodigo(datos.optString("Socio"));
                user.setNombre(datos.optString("Nombre"));
                user.setPass(datos.optString("Pass"));
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String strFecha = datos.optString("FechaIngreso");
                Date fecha = formato.parse(strFecha);
                user.setFechaIngreso(fecha);
                user.setActivo(Boolean.parseBoolean(datos.optString("Activo")));
                user.setTelefono(datos.optString("Telefono"));
                user.setCorreo(datos.optString("Correo"));

                try {
                    String e = response.getString("resultado");
                    Toast.makeText(this, e, Toast.LENGTH_SHORT).show();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                Toast.makeText(this, USUARIO, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, txtPass.getText().toString(), Toast.LENGTH_SHORT).show();
                if(txtPass.getText().toString().equals(user.getPass())) {
                    if(USUARIO.equals("00-000")) {
                        startActivity(new Intent(this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(this, MenuActivity.class));
                    }
                } else {
                    Toast.makeText(this, "La contraseña es incorrecta.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "El código ''"+USUARIO+"'' no existe.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void Loguear() {
        String url = "https://asomesyky.000webhostapp.com/consultas.php?c=2&socio="+USUARIO;

        barProgreso = new ProgressDialog(this);
        barProgreso.setMessage("Verificando...");
        barProgreso.show();

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

}
