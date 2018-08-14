package asomesyky.webhostapp.com.Globales;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import asomesyky.webhostapp.com.Entidades.Socio;

public class Global {

    public static String URL = "https://asomesyky.000webhostapp.com/consultas.php";

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
}
