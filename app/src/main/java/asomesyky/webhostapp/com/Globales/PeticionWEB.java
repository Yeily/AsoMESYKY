package asomesyky.webhostapp.com.Globales;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class PeticionWEB implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Context contexto;
    //private Response.Listener<JSONObject> listener;
    private JSONObject datosJSON = null;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    public PeticionWEB(Context contexto, String url) {
        //listener = invocador;
        this.contexto = contexto;

        respuesta = Volley.newRequestQueue(contexto);

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(contexto, error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            datosJSON = response;
            Toast.makeText(contexto, "onResponse", Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            Toast.makeText(contexto, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public JSONObject getDatosJSON(String key) {
        //JSONObject result = null;

        try {
            //JSONArray datos = datosJSON.optJSONArray(key);
            //result = datos.getJSONObject(0);
            Toast.makeText(contexto, "getDatosJSON", Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            Toast.makeText(contexto, ex.toString(), Toast.LENGTH_SHORT).show();
        }
        return datosJSON;
    }
}
