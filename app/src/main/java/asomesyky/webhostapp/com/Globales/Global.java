package asomesyky.webhostapp.com.Globales;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

import asomesyky.webhostapp.com.Entidades.Socio;

public class Global {

    public static String URL = "https://asomesyky.000webhostapp.com/consultas.php";
    public static String RUTA_RECIBOS = android.os.Environment.getExternalStorageDirectory().toString()+"/"+Environment.DIRECTORY_DOWNLOADS+"/Recibos_AsoMESYKY";

    public static Socio usuarioActual;

    public static void setFecha(Context contexto, final TextView control) {
        final Calendar calendario = Calendar.getInstance();
        final int año = calendario.get(calendario.YEAR);
        final int mes = calendario.get(calendario.MONTH);
        final int dia = calendario.get(calendario.DAY_OF_MONTH);

        DatePickerDialog fecha = new DatePickerDialog(contexto, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int a, int m, int d) {
                final int mesActual = m+1;
                String diaFormateado = (d < 10)? "0"+String.valueOf(d) : String.valueOf(d);
                String mesFormateado = (mesActual < 10)? "0"+String.valueOf(mesActual) : String.valueOf(mesActual);

                control.setText(a +"-"+ mesFormateado +"-"+ diaFormateado);
            }
        }, año, mes, dia);

        fecha.show();
    }

    public static int getColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
