package asomesyky.webhostapp.com.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import asomesyky.webhostapp.com.R;

public class MenuActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private TextView lblNombre;
    private TextView lblSaldo;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    private void InicializarComponentes() {
        lblNombre = (TextView) findViewById(R.id.lblNombre);
        lblSaldo = (TextView) findViewById(R.id.lblSaldo);

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

    private void RefescarInformacion() {
        String url = Global.URL+"?c=3&socio="+ Global.usuarioActual.getCodigo();

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    public void btnInversiones_Click(View view) {
        Intent i = new Intent(this, InversionesActivity.class);
        startActivity(i);
    }

    public void btnMayor_Click(View view) {
        Intent i = new Intent(this, MayorActivity.class);
        startActivity(i);
    }
}
