package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asomesyky.webhostapp.com.Entidades.Inversion;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

public class InversionesActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private ArrayList<Inversion> inversiones;
    private RecyclerView rvInversiones;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    private void InicializarComponentes() {
        inversiones = new ArrayList<>();
        rvInversiones = (RecyclerView) findViewById(R.id.rcyLista);
        rvInversiones.setLayoutManager(new LinearLayoutManager(this));

        respuesta = Volley.newRequestQueue(this);
        GetInversiones();
    }

    private void GetInversiones() {
        String url = Global.URL+"?c=10";

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inversiones);

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
                JSONArray datos = response.optJSONArray("datos");

                for(int i = 0; i < datos.length(); i++) {
                    inversiones.add(new Inversion(datos.getJSONObject(i)));
                }
            }

            AdaptadorInversiones adp = new AdaptadorInversiones(inversiones);
            rvInversiones.setAdapter(adp);
            adp.notifyDataSetChanged();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
