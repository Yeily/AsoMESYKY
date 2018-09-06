package asomesyky.webhostapp.com.Actividades;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import asomesyky.webhostapp.com.Entidades.Resumen;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

import lib.graficos.com.charts.PieChart;
import lib.graficos.com.components.Description;
import lib.graficos.com.components.Legend;
import lib.graficos.com.data.PieData;
import lib.graficos.com.data.PieDataSet;
import lib.graficos.com.data.PieEntry;

public class MayorActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private ArrayList<PieEntry> valores = new ArrayList<>();
    private ArrayList<Integer> colores = new ArrayList<>();

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;

    private void InicializarComponentes() {
        respuesta = Volley.newRequestQueue(this);

        String url = Global.URL+"?c=11";

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    private void CrearGrafico() {
        PieChart pieChart = (PieChart) findViewById(R.id.idGrafico);

        Description desc = new Description();
        desc.setText("Distribución de capital (%)");
        desc.setTextSize(12f);

        //definimos algunos atributos
        pieChart.setHoleRadius(40f);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);
        pieChart.setDescription(desc);
        pieChart.setCenterText("AsoMESYKY");
        pieChart.setCenterTextSize(12);

        //creamos una lista para los valores X
        /*ArrayList<String> valsX = new ArrayList<String>();
        valsX.add("Varones");
        valsX.add("Mujeres");
        valsX.add("Otros");*/

        //creamos una lista de colores
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Global.getColor());
        colors.add(Global.getColor());
        colors.add(Global.getColor());

        //seteamos los valores de Y y los colores
        PieDataSet set1 = new PieDataSet(valores,"Socios");
        set1.setValueTextSize(12);
        set1.setSliceSpace(1);
        set1.setColors(colors);

        //seteamos los valores de X
        PieData data = new PieData(set1);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor);

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
                ArrayList<Resumen> totales = new ArrayList<>();
                Double acumulado = 0.00;

                for(int i = 0; i < datos.length(); i++) {
                    totales.add(new Resumen(datos.getJSONObject(i).getString("Socio").toString(),
                            datos.getJSONObject(i).getString("Nombre").toString(),
                            Double.parseDouble(datos.getJSONObject(i).getString("Total").toString())));

                    acumulado += Double.parseDouble(datos.getJSONObject(i).getString("Total").toString());
                }

                for(int i = 0; i < totales.size(); i++) {
                    Float porcentaje = Float.parseFloat(String.valueOf(Double.parseDouble(totales.get(i).getTotal().toString()) / acumulado * 100));

                    valores.add(new PieEntry(porcentaje, totales.get(i).getNombre()));
                    colores.add(Global.getColor());
                }

                CrearGrafico();
            } else {
                Toast.makeText(this, response.getString("resultado"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}