package asomesyky.webhostapp.com.Globales;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Convertir {
    public static Date toFecha(String fecha) {
        Date result = null;

        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            result = formatoFecha.parse(fecha);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static String toFecha(Date fecha) {
        String result = "";

        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            result = formato.format(fecha);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
