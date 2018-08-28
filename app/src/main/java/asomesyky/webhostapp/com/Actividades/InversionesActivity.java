package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import asomesyky.webhostapp.com.R;

public class InversionesActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inversiones);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    public void barNavegacion_Click(View view) { finish(); }
}
