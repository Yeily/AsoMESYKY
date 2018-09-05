package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import asomesyky.webhostapp.com.Entidades.Socio;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.Globales.Correo;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.Globales.YPDF;
import asomesyky.webhostapp.com.R;

public class AporteActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private int COLS = 6;

    private Spinner cmbNombre;
    private EditText txtCodigo;
    private EditText txtFecha;
    private ImageButton btnFecha;
    private EditText txtMonto;
    private Spinner cmbPeriodo;
    private EditText txtAño;
    private Button btnGuardar;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;
    private StringRequest strJSON;
    private ArrayList<Socio> listaSocios = new ArrayList<>();

    private void InicializarComponentes() {
        cmbNombre = (Spinner) findViewById(R.id.cmbNombre);
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        btnFecha = (ImageButton) findViewById(R.id.btnFecha);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        cmbPeriodo = (Spinner) findViewById(R.id.cmbPeriodo);
        txtAño = (EditText) findViewById(R.id.txtAño);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        respuesta = Volley.newRequestQueue(this);

        cmbNombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtCodigo.setText(listaSocios.get(i).getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txtCodigo.setText("");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aporte);

        InicializarComponentes();
        LlenarDatos();
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

            if (estado.equals("OK")) {
                CargarSpinner(response);
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

    public void btnFecha_Click(View view) {
        Global.setFecha(this, txtFecha);
    }

    public void btnGuardar_Click(View view) {
        strJSON = new StringRequest(Request.Method.POST, Global.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuestaJSON = new JSONObject(response);
                    String estado = respuestaJSON.getString("resultado");

                    if (estado.equals("OK")) {
                        //GenerarRecibo();
                        Limpiar();
                        Toast.makeText(getApplication(), "Transacción guardada correctamente!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorResponse(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("c", "5");
                params.put("socio", txtCodigo.getText().toString());
                params.put("fecha", txtFecha.getText().toString());
                params.put("monto", txtMonto.getText().toString());
                params.put("periodo", cmbPeriodo.getSelectedItem().toString());
                params.put("año", txtAño.getText().toString());

                return params;
            }
        };

        respuesta.add(strJSON);
    }

    private void LlenarDatos() {
        String url = Global.URL+"?c=1";

        objJSON = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        respuesta.add(objJSON);
    }

    private void CargarSpinner(JSONObject respuestaJSON) {
        try {
            JSONArray datosJSON = respuestaJSON.optJSONArray("datos");
            JSONObject datos;

            for(int i = 0; i < datosJSON.length(); i++) {
                datos = datosJSON.getJSONObject(i);
                Socio s = new Socio();
                s.setCodigo(datos.optString("Socio"));
                s.setNombre(datos.optString("Nombre"));
                listaSocios.add(s);
            }

            ArrayAdapter<Socio> adp = new ArrayAdapter<Socio>(this, android.R.layout.simple_spinner_dropdown_item, listaSocios);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cmbNombre.setAdapter(adp);
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void GenerarRecibo() {
        YPDF pdf = new YPDF(getApplicationContext());
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date fecha = new Date();

        String strFecha = formato.format(fecha);

        try {
            /*ArrayList<String[]> titulo = new ArrayList<>();
            ArrayList<String[]> datos = new ArrayList<>();
            titulo.add(new String[]{ "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });
            datos.add(new String[]{ "", "Fecha:", txtFecha.getText().toString(), "", "", "", "" });
            datos.add(new String[]{ "", "Periodo:", cmbPeriodo.getSelectedItem().toString(), "Año:", txtAño.getText().toString(), "", "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });
            datos.add(new String[]{ "", "Socio:", txtCodigo.getText().toString(), "Nombre:", cmbNombre.getSelectedItem().toString(), "", "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });
            datos.add(new String[]{ "", "", "", "Monto:", txtMonto.getText().toString(), "", "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });
            datos.add(new String[]{ "", "", "", "Firma", "", "", "" });
            datos.add(new String[]{ "", "", "", "", "", "", "" });*/


            pdf.abrirPDF("Recibo_"+txtFecha.getText()+"_"+txtCodigo.getText()+".pdf");
            pdf.agregarMetaDatos("Recibo", "AsoMESYKY", "Administrador", "Yeily Calderon Marin");
            pdf.agregarTitulos("Recibo de aporte de ahorro", "", strFecha);
            pdf.agregarTabla(getEncabezados(), getDatos());
            pdf.agregarTexto("Guardese bien!");


            /*pdf.abrirPDF("Recibo_"+txtFecha.getText()+"_"+txtCodigo.getText()+".pdf");
            pdf.agregarMetaDatos("AsoMESYKY", "Recibo", "Administrador", "Yeily Calderon Marin");
            pdf.agregarTitulos("Comprobante de recibo de AsoMESYKY", "", txtFecha.getText().toString());
            pdf.agregarTabla(getEncabezados(), getDatos());
            pdf.agregarTexto("Guardese cuidadosamente");

            pdf.visualizarPDF(this);*/
        }
        catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            pdf.cerrarPDF();
        }
    }

    private void EnviarCorreo() {
        Correo mail = new Correo(this);

        try {
            mail.enviar("yeilycalderon@yahoo.es", "Recibo", "Este es el cuerpo del correo.", Global.RUTA_RECIBOS+"/Recibo_"+txtFecha.getText()+"_"+txtCodigo.getText()+".pdf");
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private PdfPCell[] getEncabezados() {
        PdfPCell[] celdas = new PdfPCell[COLS];

        try {
            for (int i = 0; i < COLS; i++) {
                PdfPCell celda = new PdfPCell();
                celda.setBorder(0);
                celda.setBorderWidthBottom(2f);
                celdas[i] = celda;
            }
        }catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return celdas;
    }

    private ArrayList<PdfPCell[]> getDatos() {
        ArrayList<PdfPCell[]> filas = new ArrayList<>();
        PdfPCell[] celdas = new PdfPCell[COLS];
        /*PdfPCell celda0; //= new PdfPCell();
        PdfPCell celda1; //= new PdfPCell();
        PdfPCell celda2; //= new PdfPCell();
        PdfPCell celda3; //= new PdfPCell();
        PdfPCell celda4; //= new PdfPCell();
        PdfPCell celda5; //= new PdfPCell();*/

        try {
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase(""));
            celdas[3] = new PdfPCell(new Phrase(""));
            celdas[4] = new PdfPCell(new Phrase(""));
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase("Fecha: "));
            celdas[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas[2] = new PdfPCell(new Phrase(txtFecha.getText().toString()));
            celdas[2].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas[3] = new PdfPCell(new Phrase(""));
            celdas[4] = new PdfPCell(new Phrase(""));
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase("Periodo: "));
            celdas[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas[2] = new PdfPCell(new Phrase(cmbPeriodo.getSelectedItem().toString()));
            celdas[2].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas[3] = new PdfPCell(new Phrase("Año: "));
            celdas[3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas[4] = new PdfPCell(new Phrase(txtAño.getText().toString()));
            celdas[4].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas[4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase(""));
            celdas[3] = new PdfPCell(new Phrase(""));
            celdas[4] = new PdfPCell(new Phrase(""));
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase("Socio: "));
            celdas[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas[2] = new PdfPCell(new Phrase(txtCodigo.getText().toString()));
            celdas[2].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas[3] = new PdfPCell(new Phrase("Nombre: "));
            celdas[3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas[4] = new PdfPCell(new Phrase(cmbNombre.getSelectedItem().toString()));
            celdas[4].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas[4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas[4].setColspan(2);

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase(""));
            celdas[3] = new PdfPCell(new Phrase(""));
            celdas[4] = new PdfPCell(new Phrase(""));
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase(""));
            celdas[3] = new PdfPCell(new Phrase("Monto: "));
            celdas[3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas[4] = new PdfPCell(new Phrase(txtMonto.getText().toString()));
            celdas[4].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas[4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase(""));
            celdas[3] = new PdfPCell(new Phrase(""));
            celdas[4] = new PdfPCell(new Phrase(""));
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            filas.add(celdas);
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase("Firma recibido"));
            celdas[2].setColspan(3);
            celdas[2].setBorderWidthTop(2);
            celdas[3] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;*/
            filas.add(celdas);
            ////////////////////////////////////////////////
            celdas[0] = new PdfPCell(new Phrase(""));
            celdas[1] = new PdfPCell(new Phrase(""));
            celdas[2] = new PdfPCell(new Phrase(""));
            celdas[3] = new PdfPCell(new Phrase(""));
            celdas[4] = new PdfPCell(new Phrase(""));
            celdas[5] = new PdfPCell(new Phrase(""));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            filas.add(celdas);
            filas.add(celdas);
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return filas;
    }

    private void Limpiar() {
        txtCodigo.setText("");
        txtFecha.setText("");
        txtMonto.setText("");
        txtAño.setText("");
    }

    public void btnDoc_Click(View view) {
        Toast.makeText(this, "Reporte", Toast.LENGTH_LONG).show();
        GenerarRecibo();
        Toast.makeText(this, "Correo", Toast.LENGTH_LONG).show();
        EnviarCorreo();
        Limpiar();
    }
}
