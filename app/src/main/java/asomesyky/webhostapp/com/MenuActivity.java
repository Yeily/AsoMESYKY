package asomesyky.webhostapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import asomesyky.webhostapp.com.Globales.Global;

public class MenuActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Toolbar barNavegacion;
    private TextView lblNombre;
    private TextView lblSaldo;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    private void InicializarComponentes() {
        lblNombre = (TextView) findViewById(R.id.lblNombre);
        lblSaldo = (TextView) findViewById(R.id.lblSaldo);
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            barNavegacion = (Toolbar) findViewById(R.id.barNavegacion);

            barNavegacion.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }*/

        respuesta = Volley.newRequestQueue(this);

        RefescarInformacion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        InicializarComponentes();
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
                JSONArray datosJSON = response.optJSONArray("datos");
                JSONObject datos = datosJSON.getJSONObject(0);

                String nombre = datos.optString("Nombre");
                Double saldo = Double.parseDouble(datos.optString("Saldo"));
                DecimalFormat formato = new DecimalFormat("#,##0.00");

                lblNombre.setText(nombre);
                lblSaldo.setText(formato.format(saldo));
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

    private void RefescarInformacion() {
        String url = "https://asomesyky.000webhostapp.com/consultas.php?c=3&socio="+ Global.usuarioActual.getCodigo();

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }
}
