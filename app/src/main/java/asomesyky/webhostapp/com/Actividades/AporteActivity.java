package asomesyky.webhostapp.com.Actividades;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Switch;
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
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import asomesyky.webhostapp.com.Entidades.Socio;
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
    private Switch swtEnviar;
    private Button btnGuardar;

    private RequestQueue respuesta;
    private JsonObjectRequest objJSON;
    private StringRequest strJSON;
    private String correo;
    private ArrayList<Socio> listaSocios = new ArrayList<>();

    private void InicializarComponentes() {
        cmbNombre = (Spinner) findViewById(R.id.cmbNombre);
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        btnFecha = (ImageButton) findViewById(R.id.btnFecha);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        cmbPeriodo = (Spinner) findViewById(R.id.cmbPeriodo);
        txtAño = (EditText) findViewById(R.id.txtAño);
        swtEnviar = (Switch) findViewById(R.id.swtEnviar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        respuesta = Volley.newRequestQueue(this);

        swtEnviar.setChecked(true);
        cmbNombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtCodigo.setText(listaSocios.get(i).getCodigo());
                correo = listaSocios.get(i).getCorreo();
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
                        GenerarRecibo();
                        if(swtEnviar.isChecked()) {
                            EnviarCorreo();
                        }
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
                s.setCorreo(datos.optString("Correo"));
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
        SimpleDateFormat formato = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date fecha = new Date();

        String strFecha = formato.format(fecha);

        try {
            pdf.abrirPDF("Recibo_"+txtFecha.getText()+"_"+txtCodigo.getText()+".pdf");
            pdf.agregarMetaDatos("Recibo", "AsoMESYKY", "Administrador", "Yeily Calderon Marin");
            pdf.agregarTitulos("Recibo de aporte de ahorro", "", strFecha);
            pdf.agregarTabla(getEncabezados(), getDatos());
            Paragraph texto = new Paragraph("Guardese bien!", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
            texto.setAlignment(Element.ALIGN_CENTER);
            pdf.agregarTexto(texto);

            Drawable rd = getResources().getDrawable(R.drawable.firma);
            BitmapDrawable bitM = ((BitmapDrawable) rd);
            Bitmap bmp = bitM.getBitmap();
            ByteArrayOutputStream st = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, st);
            Image img = Image.getInstance(st.toByteArray());
            pdf.agregarImagen(img, 290, 440);
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
            String cuerpo = "Estimad@ "+cmbNombre.getSelectedItem().toString()+
                    ":\nAdjunto encontrará su recibo por el aporte de ahorro de la asociación.\n\n\nSaludos,\nYeily Calderón Marín.";
            mail.enviar(correo, "Recibo AsoMESYKY", cuerpo, Global.RUTA_RECIBOS+"/Recibo_"+txtFecha.getText()+"_"+txtCodigo.getText()+".pdf");
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private PdfPCell[] getEncabezados() {
        Font FORMATO = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        PdfPCell[] celdas = new PdfPCell[COLS];

        try {
            for (Integer i = 0; i < COLS; i++) {
                PdfPCell celda = new PdfPCell();
                celda.setBorder(0);
                celda.setBorderWidthBottom(2f);
                celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdas[i] = celda;
            }
        }catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return celdas;
    }

    private ArrayList<PdfPCell[]> getDatos() {
        ArrayList<PdfPCell[]> result = new ArrayList<>();
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

        Font FORMATO = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);

        try {
            celdas0[0] = new PdfPCell(new Phrase(""));
            celdas0[1] = new PdfPCell(new Phrase(""));
            celdas0[2] = new PdfPCell(new Phrase(""));
            celdas0[3] = new PdfPCell(new Phrase(""));
            celdas0[4] = new PdfPCell(new Phrase(""));
            celdas0[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas0));
            ////////////////////////////////////////////////
            celdas1[0] = new PdfPCell(new Phrase("Fecha: "));
            celdas1[0].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas1[1] = new PdfPCell(new Phrase(txtFecha.getText().toString()));
            celdas1[1].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas1[1].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas1[2] = new PdfPCell(new Phrase(""));
            celdas1[3] = new PdfPCell(new Phrase(""));
            celdas1[4] = new PdfPCell(new Phrase(""));
            celdas1[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas1));
            ////////////////////////////////////////////////
            celdas2[0] = new PdfPCell(new Phrase("Periodo: "));
            celdas2[0].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas2[1] = new PdfPCell(new Phrase(cmbPeriodo.getSelectedItem().toString()));
            celdas2[1].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas2[1].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas2[2] = new PdfPCell(new Phrase("Año: "));
            celdas2[2].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas2[3] = new PdfPCell(new Phrase(txtAño.getText().toString()));
            celdas2[3].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas2[3].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas2[4] = new PdfPCell(new Phrase(""));
            celdas2[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas2));
            ////////////////////////////////////////////////
            celdas3[0] = new PdfPCell(new Phrase(""));
            celdas3[1] = new PdfPCell(new Phrase(""));
            celdas3[2] = new PdfPCell(new Phrase(""));
            celdas3[3] = new PdfPCell(new Phrase(""));
            celdas3[4] = new PdfPCell(new Phrase(""));
            celdas3[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas0));
            ////////////////////////////////////////////////
            celdas4[0] = new PdfPCell(new Phrase("Socio: "));
            celdas4[0].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas4[1] = new PdfPCell(new Phrase(txtCodigo.getText().toString()));
            celdas4[1].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas4[1].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas4[2] = new PdfPCell(new Phrase("Nombre: "));
            celdas4[2].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas4[3] = new PdfPCell(new Phrase(cmbNombre.getSelectedItem().toString()));
            celdas4[3].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas4[3].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas4[3].setColspan(2);
            celdas4[4] = new PdfPCell();
            celdas4[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas4));
            ////////////////////////////////////////////////
            celdas5[0] = new PdfPCell(new Phrase(""));
            celdas5[1] = new PdfPCell(new Phrase(""));
            celdas5[2] = new PdfPCell(new Phrase(""));
            celdas5[3] = new PdfPCell(new Phrase(""));
            celdas5[4] = new PdfPCell(new Phrase(""));
            celdas5[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas5));
            ////////////////////////////////////////////////
            celdas6[0] = new PdfPCell(new Phrase(""));
            celdas6[1] = new PdfPCell(new Phrase("Monto: "));
            celdas6[1].setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdas6[2] = new PdfPCell(new Phrase(txtMonto.getText().toString()));
            celdas6[2].setHorizontalAlignment(Element.ALIGN_LEFT);
            celdas6[2].setBackgroundColor(BaseColor.LIGHT_GRAY);
            celdas6[3] = new PdfPCell();
            celdas6[4] = new PdfPCell();
            celdas6[5] = new PdfPCell();

            result.add(QuitarBordes(celdas6));
            ////////////////////////////////////////////////
            celdas7[0] = new PdfPCell(new Phrase(""));
            celdas7[1] = new PdfPCell(new Phrase(""));
            celdas7[2] = new PdfPCell(new Phrase(""));
            celdas7[3] = new PdfPCell(new Phrase(""));
            celdas7[4] = new PdfPCell(new Phrase(""));
            celdas7[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas7));
            result.add(QuitarBordes(celdas7));
            result.add(QuitarBordes(celdas7));
            ////////////////////////////////////////////////
            celdas8[0] = new PdfPCell();
            celdas8[0].setBorder(0);
            celdas8[1] = new PdfPCell(new Phrase("Firma recibido"));
            celdas8[1].setHorizontalAlignment(Element.ALIGN_CENTER);
            celdas8[1].setColspan(3);
            celdas8[1].setBorder(0);
            celdas8[1].setBorderWidthTop(2);
            celdas8[2] = new PdfPCell();
            celdas8[2].setBorder(0);
            celdas8[3] = new PdfPCell();
            celdas8[3].setBorder(0);
            celdas8[4] = new PdfPCell();
            celdas8[4].setBorder(0);
            celdas8[5] = new PdfPCell();
            celdas8[5].setBorder(0);

            result.add(celdas8);
            ////////////////////////////////////////////////
            celdas9[0] = new PdfPCell(new Phrase(""));
            celdas9[1] = new PdfPCell(new Phrase(""));
            celdas9[2] = new PdfPCell(new Phrase(""));
            celdas9[3] = new PdfPCell(new Phrase(""));
            celdas9[4] = new PdfPCell(new Phrase(""));
            celdas9[5] = new PdfPCell(new Phrase(""));

            result.add(QuitarBordes(celdas9));
            result.add(QuitarBordes(celdas9));
        }catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return result;
    }

    private PdfPCell[] QuitarBordes(PdfPCell[] celdas) {
        for(Integer i = 0; i < celdas.length; i++) {
            celdas[i].setBorder(0);
        }

        return celdas;
    }

    private void Limpiar() {
        txtFecha.setText("");
        txtMonto.setText("");
        txtAño.setText("");
    }

}
