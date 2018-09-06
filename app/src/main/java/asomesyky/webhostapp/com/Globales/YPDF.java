package asomesyky.webhostapp.com.Globales;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class YPDF {
    private Context pContexto;

    private File pdf;
    private Document doc;
    private PdfWriter escritor;

    private Font FUENTE_TITULO = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font FUENTE_SUBTITULO = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font FUENTE_TEXTO = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private Font FUENTE_RESALTADO = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);

    public YPDF(Context contexto) {
        pContexto = contexto;
    }

    private void crearPDF(String nombrePDF) {
        File folder = new File(Global.RUTA_RECIBOS);

        if(!folder.exists()) {
            folder.mkdirs();
        }

        try {
            pdf = new File(folder, nombrePDF);
            pdf.createNewFile();
        } catch(IOException ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void abrirPDF(String nombrePDF) {
        crearPDF(nombrePDF);

        try{
            doc = new Document(PageSize.A4);
            escritor = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
            doc.open();
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void cerrarPDF() {
        doc.close();
    }

    public void agregarMetaDatos(String titulo, String tema, String autor) {
        agregarMetaDatos(titulo, tema, autor, "");
    }

    public void agregarMetaDatos(String titulo, String tema, String autor, String creador) {
        doc.addTitle(titulo);
        doc.addSubject(tema);
        doc.addAuthor(autor);
        doc.addCreator(creador);
    }

    public void agregarTitulos(String titulo, String subtitulo, String fecha) {
        try {
            Paragraph titulos = new Paragraph();
            Paragraph tit = new Paragraph(titulo, FUENTE_TITULO);
            tit.setAlignment(Element.ALIGN_CENTER);
            titulos.add(tit);
            titulos.add(new Paragraph(subtitulo, FUENTE_SUBTITULO));
            titulos.add(new Paragraph("Generado: "+fecha, FUENTE_RESALTADO));
            titulos.setSpacingAfter(30);

            doc.add(titulos);
        } catch(Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void agregarTexto(String texto) {
        try {
            Paragraph parrafo = new Paragraph(texto);
            parrafo.setSpacingAfter(5);
            parrafo.setSpacingBefore(5);
            doc.add(parrafo);
        } catch(Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void agregarTexto(Paragraph texto) {
        try {
            doc.add(texto);
        } catch(Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void agregarTabla(String[] encabezados, ArrayList<String[]> filas, BaseColor colorFondo) {
        PdfPCell celda;
        int col = 0;

        try {
            Paragraph parrafo = new Paragraph();
            parrafo.setFont(FUENTE_TEXTO);
            PdfPTable tabla = new PdfPTable(encabezados.length);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(20);

            while (col < encabezados.length) {
                celda = new PdfPCell(new Phrase(encabezados[col++], FUENTE_SUBTITULO));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                if(colorFondo != null) celda.setBackgroundColor(colorFondo);
                tabla.addCell(celda);
            }

            for (int f = 0; f < filas.size(); f++) {
                String[] fila = filas.get(f);

                for (col = 0; col < encabezados.length; col++) {
                    celda = new PdfPCell(new Phrase(fila[col]));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setFixedHeight(30);
                    celda.setBorder(0);
                    tabla.addCell(celda);
                }
            }

            parrafo.add(tabla);
            doc.add(parrafo);
        } catch(Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void agregarTabla(PdfPCell[] encabezados, ArrayList<PdfPCell[]> filas) {
        try {
            Paragraph parrafo = new Paragraph();
            parrafo.setFont(FUENTE_SUBTITULO);
            PdfPTable tabla = new PdfPTable(encabezados.length);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(20);

            for(int c = 0; c < encabezados.length; c++) {
                tabla.addCell(encabezados[c]);
            }

            for (int f = 0; f < filas.size(); f++) {
                PdfPCell[] fila = filas.get(f);

                for (int c = 0; c < encabezados.length; c++) {
                    fila[c].setFixedHeight(20);
                    tabla.addCell(fila[c]);
                }
            }

            parrafo.add(tabla);
            doc.add(parrafo);
        } catch(Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void agregarImagen(Image imagen, float posX, float posY) {
        try {
            imagen.setAbsolutePosition(posX, posY);
            doc.add(imagen);
        } catch(Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void visualizarPDF(Activity actividad) {
        if(pdf.exists()){
            Uri path = Uri.fromFile(pdf);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path,"application/pdf");

            try {

            } catch(ActivityNotFoundException ex) {
                actividad.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                Toast.makeText(actividad.getApplicationContext(), "Debes instalar un aplicacion para vizualizar PDFs", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(actividad.getApplicationContext(), "El archivo ''"+pdf+"'' no existe", Toast.LENGTH_LONG).show();
        }
    }

    public String rutaPDF() {
        return pdf.getPath();
    }
}
