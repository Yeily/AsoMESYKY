package asomesyky.webhostapp.com.Actividades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
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
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asomesyky.webhostapp.com.Entidades.Socio;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;
import harmony.java.awt.Color;

public class AporteActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private final static String DIR_RECIBOS = "Recibos_AsoMESYKY";
    private final static String NOMBRE_RECIBO = "Recibo ";
    private final static String ETIQUETA_ERROR = "Error:";

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

    /*private void GenerarRecibo() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument pdf = new PdfDocument();
            PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(1, 1, 1).create();
            PdfDocument.Page pag = pdf.startPage(info);
            // draw something on the page
            View content = this.btnFecha;
            content.draw(pag.getCanvas());

            // finish the page
            pdf.finishPage(pag);
            // write the document content
            OutputStream stream = new OutputStream() {
                @Override
                public void write(int i) throws IOException {

                }
            };
            pdf.writeTo(stream);

            // close the document
            pdf.close();
        }
    }*/

    private void GenerarRecibo() {
        Document doc = new Document();
        Font tipoFuente1 = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.RED);
        Font tipoFuente2 = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD, Color.GRAY);

        try {
            File archivo = CrearArchivo(NOMBRE_RECIBO+txtFecha.getText().toString()+"_"+txtCodigo.getText().toString()+".pdf");
            FileOutputStream archivoPDF = new FileOutputStream(archivo.getAbsolutePath());
            PdfWriter pdf = PdfWriter.getInstance(doc, archivoPDF);
            HeaderFooter cabecera = new HeaderFooter(new Phrase("Recibo de aporte de ahorro"), false);
            HeaderFooter pie = new HeaderFooter(new Phrase("Guardese bien!"), false);

            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.inversion);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            logo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = Image.getInstance(stream.toByteArray());

            Table tabla = new Table(5);
            for(int i = 0; i < 15; i++) {
                tabla.addCell("Celda "+i);
            }

            doc.setHeader(cabecera);
            doc.setFooter(pie);
            doc.open();
            doc.add(new Paragraph("Título"));
            doc.add(new Paragraph("Título personalizado", tipoFuente1));
            doc.add(img);
            doc.add(tabla);

            ColumnText.showTextAligned(pdf.getDirectContent(), Element.ALIGN_CENTER, new Paragraph("AsoMESYKY", tipoFuente2), 297.5f, 421,pdf.getPageNumber() % 2== 1 ? 45: -45);
        }
        catch(DocumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            doc.close();
        }
    }

    private File CrearArchivo(String nombreArchivo) {
        File archivo = null;

        try {
            File ruta = getRuta();


            if(ruta != null) {
                archivo = new File(ruta, nombreArchivo);
            }
        }
        catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return archivo;
    }

    private File getRuta() {
        File ruta = null;

        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        DIR_RECIBOS);

                if (ruta != null) {
                    if (!ruta.mkdirs()) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return ruta;
    }

    private void Limpiar() {
        txtCodigo.setText("");
        txtFecha.setText("");
        txtMonto.setText("");
        txtAño.setText("");
    }

    public void btnDoc_Click(View view) {
        GenerarRecibo();
    }
}
