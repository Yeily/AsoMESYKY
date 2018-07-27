package asomesyky.webhostapp.com.Globales;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Convertir {
    public static Date ToFecha(String formato) {
        Date result = null;

        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            result = formatoFecha.parse(formato);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
