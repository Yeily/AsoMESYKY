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
import com.itextpdf.text.Font;
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
        Font FORMATO = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        PdfPCell[] celdas = new PdfPCell[COLS];

        try {
            for (Integer i = 0; i < COLS; i++) {
                celdas[i] = new PdfPCell(new Phrase(i.toString(), FORMATO));
                celdas[i].setBorder(0);
                celdas[i].setBorderWidthBottom(1f);
                celdas[i].setHorizontalAlignment(Element.ALIGN_CENTER);
                celdas[i].setBackgroundColor(BaseColor.LIGHT_GRAY);

                /*PdfPCell celda = new PdfPCell(new Phrase(i.toString(), FORMATO));
                celda.setBorder(0);
                celda.setBorderWidthBottom(1f);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celdas[i] = celda;*/
            }
        }catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return celdas;
    }

    private PdfPCell[][] getDatos() {
        Font FORMATO = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        PdfPCell[][] filas = new PdfPCell[11][COLS];
        /*PdfPCell[] celdas0 = new PdfPCell[COLS];
        PdfPCell[] celdas1 = new PdfPCell[COLS];
        PdfPCell[] celdas2 = new PdfPCell[COLS];
        PdfPCell[] celdas3 = new PdfPCell[COLS];
        PdfPCell[] celdas4 = new PdfPCell[COLS];
        PdfPCell[] celdas5 = new PdfPCell[COLS];
        PdfPCell[] celdas6 = new PdfPCell[COLS];
        PdfPCell[] celdas7 = new PdfPCell[COLS];
        PdfPCell[] celdas8 = new PdfPCell[COLS];
        PdfPCell[] celdas9 = new PdfPCell[COLS];*/
        /*PdfPCell celda0; //= new PdfPCell();
        PdfPCell celda1; //= new PdfPCell();
        PdfPCell celda2; //= new PdfPCell();
        PdfPCell celda3; //= new PdfPCell();
        PdfPCell celda4; //= new PdfPCell();
        PdfPCell celda5; //= new PdfPCell();*/

        try {
            filas[0][0] = new PdfPCell(new Phrase("1", FORMATO));
            filas[0][1] = new PdfPCell(new Phrase("2", FORMATO));
            filas[0][2] = new PdfPCell(new Phrase("3", FORMATO));
            filas[0][3] = new PdfPCell(new Phrase("4", FORMATO));
            filas[0][4] = new PdfPCell(new Phrase("5", FORMATO));
            filas[0][5] = new PdfPCell(new Phrase("6", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas0);
            ////////////////////////////////////////////////
            filas[1][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[1][1] = new PdfPCell(new Phrase("Fecha: ", FORMATO));
            filas[1][1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            filas[1][2] = new PdfPCell(new Phrase(txtFecha.getText().toString(), FORMATO));
            filas[1][2].setHorizontalAlignment(Element.ALIGN_LEFT);
            filas[1][2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            filas[1][3] = new PdfPCell(new Phrase("", FORMATO));
            filas[1][4] = new PdfPCell(new Phrase("", FORMATO));
            filas[1][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas1);
            ////////////////////////////////////////////////
            filas[2][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[2][1] = new PdfPCell(new Phrase("Periodo: ", FORMATO));
            filas[2][1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            filas[2][2] = new PdfPCell(new Phrase(cmbPeriodo.getSelectedItem().toString()));
            filas[2][2].setHorizontalAlignment(Element.ALIGN_CENTER);
            filas[2][2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            filas[2][3] = new PdfPCell(new Phrase("Año: ", FORMATO));
            filas[2][3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            filas[2][4] = new PdfPCell(new Phrase(txtAño.getText().toString(), FORMATO));
            filas[2][4].setHorizontalAlignment(Element.ALIGN_CENTER);
            filas[2][4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            filas[2][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas2);
            ////////////////////////////////////////////////
            filas[3][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[3][1] = new PdfPCell(new Phrase("", FORMATO));
            filas[3][2] = new PdfPCell(new Phrase("", FORMATO));
            filas[3][3] = new PdfPCell(new Phrase("", FORMATO));
            filas[3][4] = new PdfPCell(new Phrase("", FORMATO));
            filas[3][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas3);
            ////////////////////////////////////////////////
            filas[4][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[4][1] = new PdfPCell(new Phrase("Socio: ", FORMATO));
            filas[4][1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            filas[4][2] = new PdfPCell(new Phrase(txtCodigo.getText().toString(), FORMATO));
            filas[4][2].setHorizontalAlignment(Element.ALIGN_LEFT);
            filas[4][2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            filas[4][3] = new PdfPCell(new Phrase("Nombre: ", FORMATO));
            filas[4][3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            filas[4][4] = new PdfPCell(new Phrase(cmbNombre.getSelectedItem().toString(), FORMATO));
            filas[4][4].setHorizontalAlignment(Element.ALIGN_LEFT);
            filas[4][4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            filas[4][4].setColspan(2);

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;*/
            //filas.add(celdas4);
            ////////////////////////////////////////////////
            filas[5][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[5][1] = new PdfPCell(new Phrase("", FORMATO));
            filas[5][2] = new PdfPCell(new Phrase("", FORMATO));
            filas[5][3] = new PdfPCell(new Phrase("", FORMATO));
            filas[5][4] = new PdfPCell(new Phrase("", FORMATO));
            filas[5][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas5);
            ////////////////////////////////////////////////
            filas[6][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[6][1] = new PdfPCell(new Phrase("", FORMATO));
            filas[6][2] = new PdfPCell(new Phrase("", FORMATO));
            filas[6][3] = new PdfPCell(new Phrase("Monto: "));
            filas[6][3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            filas[6][4] = new PdfPCell(new Phrase(txtMonto.getText().toString(), FORMATO));
            filas[6][4].setHorizontalAlignment(Element.ALIGN_LEFT);
            filas[6][4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            filas[6][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas6);
            ////////////////////////////////////////////////
            filas[7][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[7][1] = new PdfPCell(new Phrase("", FORMATO));
            filas[7][2] = new PdfPCell(new Phrase("", FORMATO));
            filas[7][3] = new PdfPCell(new Phrase("", FORMATO));
            filas[7][4] = new PdfPCell(new Phrase("", FORMATO));
            filas[7][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas7);
            //filas.add(celdas7);
            //filas.add(celdas7);
            ////////////////////////////////////////////////
            filas[8][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[8][1] = new PdfPCell(new Phrase("", FORMATO));
            filas[8][2] = new PdfPCell(new Phrase("Firma recibido", FORMATO));
            filas[8][2].setColspan(3);
            filas[8][2].setBorderWidthTop(2);
            filas[8][3] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;*/
            //filas.add(celdas8);
            ////////////////////////////////////////////////
            filas[9][0] = new PdfPCell(new Phrase("", FORMATO));
            filas[9][1] = new PdfPCell(new Phrase("", FORMATO));
            filas[9][2] = new PdfPCell(new Phrase("", FORMATO));
            filas[9][3] = new PdfPCell(new Phrase("", FORMATO));
            filas[9][4] = new PdfPCell(new Phrase("", FORMATO));
            filas[9][5] = new PdfPCell(new Phrase("", FORMATO));

            /*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*/
            //filas.add(celdas9);
            //filas.add(celdas9);
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return filas;
    }

    /*private ArrayList<PdfPCell[]> getDatos() {
        Font FORMATO = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        ArrayList<PdfPCell[]> filas = new ArrayList<>();
        PdfPCell[] celdas0 = new PdfPCell[COLS];
        PdfPCell[] celdas1 = new PdfPCell[COLS];
        PdfPCell[] celdas2 = new PdfPCell[COLS];
        PdfPCell[] celdas3 = new PdfPCell[COLS];
        PdfPCell[] celdas4 = new PdfPCell[COLS];
        PdfPCell[] celdas5 = new PdfPCell[COLS];
        PdfPCell[] celdas6 = new PdfPCell[COLS];
        PdfPCell[] celdas7 = new PdfPCell[COLS];
        PdfPCell[] celdas8 = new PdfPCell[COLS];
        PdfPCell[] celdas9 = new PdfPCell[COLS];
        *//*PdfPCell celda0; //= new PdfPCell();
        PdfPCell celda1; //= new PdfPCell();
        PdfPCell celda2; //= new PdfPCell();
        PdfPCell celda3; //= new PdfPCell();
        PdfPCell celda4; //= new PdfPCell();
        PdfPCell celda5; //= new PdfPCell();*//*

        try {
            celdas0[0] = new PdfPCell(new Phrase("1", FORMATO));
            celdas0[1] = new PdfPCell(new Phrase("2", FORMATO));
            celdas0[2] = new PdfPCell(new Phrase("3", FORMATO));
            celdas0[3] = new PdfPCell(new Phrase("4", FORMATO));
            celdas0[4] = new PdfPCell(new Phrase("5", FORMATO));
            celdas0[5] = new PdfPCell(new Phrase("6", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas0);
            ////////////////////////////////////////////////
            celdas1[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas1[1] = new PdfPCell(new Phrase("Fecha: ", FORMATO));
            celdas1[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas1[2] = new PdfPCell(new Phrase(txtFecha.getText().toString(), FORMATO));
            celdas1[2].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas1[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas1[3] = new PdfPCell(new Phrase("", FORMATO));
            celdas1[4] = new PdfPCell(new Phrase("", FORMATO));
            celdas1[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas1);
            ////////////////////////////////////////////////
            celdas2[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas2[1] = new PdfPCell(new Phrase("Periodo: ", FORMATO));
            celdas2[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas2[2] = new PdfPCell(new Phrase(cmbPeriodo.getSelectedItem().toString()));
            celdas2[2].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas2[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas2[3] = new PdfPCell(new Phrase("Año: ", FORMATO));
            celdas2[3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas2[4] = new PdfPCell(new Phrase(txtAño.getText().toString(), FORMATO));
            celdas2[4].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas2[4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas2[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas2);
            ////////////////////////////////////////////////
            celdas3[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas3[1] = new PdfPCell(new Phrase("", FORMATO));
            celdas3[2] = new PdfPCell(new Phrase("", FORMATO));
            celdas3[3] = new PdfPCell(new Phrase("", FORMATO));
            celdas3[4] = new PdfPCell(new Phrase("", FORMATO));
            celdas3[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas3);
            ////////////////////////////////////////////////
            celdas4[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas4[1] = new PdfPCell(new Phrase("Socio: ", FORMATO));
            celdas4[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas4[2] = new PdfPCell(new Phrase(txtCodigo.getText().toString(), FORMATO));
            celdas4[2].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas4[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas4[3] = new PdfPCell(new Phrase("Nombre: ", FORMATO));
            celdas4[3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas4[4] = new PdfPCell(new Phrase(cmbNombre.getSelectedItem().toString(), FORMATO));
            celdas4[4].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas4[4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas4[4].setColspan(2);

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;*//*
            filas.add(celdas4);
            ////////////////////////////////////////////////
            celdas5[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas5[1] = new PdfPCell(new Phrase("", FORMATO));
            celdas5[2] = new PdfPCell(new Phrase("", FORMATO));
            celdas5[3] = new PdfPCell(new Phrase("", FORMATO));
            celdas5[4] = new PdfPCell(new Phrase("", FORMATO));
            celdas5[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas5);
            ////////////////////////////////////////////////
            celdas6[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas6[1] = new PdfPCell(new Phrase("", FORMATO));
            celdas6[2] = new PdfPCell(new Phrase("", FORMATO));
            celdas6[3] = new PdfPCell(new Phrase("Monto: "));
            celdas6[3].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas6[4] = new PdfPCell(new Phrase(txtMonto.getText().toString(), FORMATO));
            celdas6[4].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas6[4].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas6[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas6);
            ////////////////////////////////////////////////
            celdas7[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas7[1] = new PdfPCell(new Phrase("", FORMATO));
            celdas7[2] = new PdfPCell(new Phrase("", FORMATO));
            celdas7[3] = new PdfPCell(new Phrase("", FORMATO));
            celdas7[4] = new PdfPCell(new Phrase("", FORMATO));
            celdas7[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas7);
            filas.add(celdas7);
            filas.add(celdas7);
            ////////////////////////////////////////////////
            celdas8[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas8[1] = new PdfPCell(new Phrase("", FORMATO));
            celdas8[2] = new PdfPCell(new Phrase("Firma recibido", FORMATO));
            celdas8[2].setColspan(3);
            celdas8[2].setBorderWidthTop(2);
            celdas8[3] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;*//*
            filas.add(celdas8);
            ////////////////////////////////////////////////
            celdas9[0] = new PdfPCell(new Phrase("", FORMATO));
            celdas9[1] = new PdfPCell(new Phrase("", FORMATO));
            celdas9[2] = new PdfPCell(new Phrase("", FORMATO));
            celdas9[3] = new PdfPCell(new Phrase("", FORMATO));
            celdas9[4] = new PdfPCell(new Phrase("", FORMATO));
            celdas9[5] = new PdfPCell(new Phrase("", FORMATO));

            *//*celdas[0] = celda0;
            celdas[1] = celda1;
            celdas[2] = celda2;
            celdas[3] = celda3;
            celdas[4] = celda4;
            celdas[5] = celda5;*//*
            filas.add(celdas9);
            filas.add(celdas9);
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return filas;
    }*/

    private void Limpiar() {
        txtCodigo.setText("");
        txtFecha.setText("");
        txtMonto.setText("");
        txtAño.setText("");
    }

    public void btnDoc_Click(View view) {
        GenerarRecibo();
        EnviarCorreo();
        Limpiar();
    }
}
