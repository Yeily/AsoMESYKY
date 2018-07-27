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

import asomesyky.webhostapp.com.Entidades.Socio;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.Globales.PeticionWEB;

//public class LoginActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
public class LoginActivity extends AppCompatActivity {

    private EditText txtCodigo;
    private EditText txtPass;
    private Button btnIngresar;
    private ProgressDialog barProgreso;

    //private RequestQueue respuesta;
    //private JsonObjectRequest objJSON;

    private static String USUARIO;

    private void InicializarComponentes() {
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        //respuesta = Volley.newRequestQueue(this);

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

    /*@Override
    public void onErrorResponse(VolleyError error) {
        barProgreso.hide();
        Toast.makeText(this, "Error al consultar socio: "+error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }*/


    private void Verificar(JSONObject response) {
        barProgreso.hide();
//        try {
            Toast.makeText(this, "Verificar", Toast.LENGTH_SHORT).show();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
        /*try {
            Socio user = new Socio();
            String estado = response.getString("resultado");

            if(estado.equals("OK")) {
                JSONArray datosJSON = response.optJSONArray("datos");
                JSONObject datos = datosJSON.getJSONObject(0);

                user.setCodigo(datos.optString("Socio"));
                user.setNombre(datos.optString("Nombre"));
                user.setPass(datos.optString("Pass"));
                user.setFechaIngreso(Convertir.ToFecha("yyyy-MM-dd"));
                user.setActivo(Boolean.parseBoolean(datos.optString("Activo")));
                user.setTelefono(datos.optString("Telefono"));
                user.setCorreo(datos.optString("Correo"));

                if(txtPass.getText().toString().equals(user.getPass())) {
                    Global.usuarioActual = user;

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
        }*/
    }

    private void Loguear() {
        String url = "https://asomesyky.000webhostapp.com/consultas.php?c=2&socio="+USUARIO;

        barProgreso = new ProgressDialog(this);
        barProgreso.setMessage("Verificando...");
        barProgreso.show();

        PeticionWEB peticion = new PeticionWEB(this, url);
        JSONObject respuesta = peticion.getDatosJSON("resultado");
        /*try {
            Toast.makeText(this, respuesta.getString("resultado"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }*/
        Verificar(respuesta);
        //objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //respuesta.add(objJSON);

    }

}
