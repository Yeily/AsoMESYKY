package asomesyky.webhostapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    private void RefescarInformacion() {
        String url = "";


    }
}
