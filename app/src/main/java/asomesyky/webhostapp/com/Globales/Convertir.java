package asomesyky.webhostapp.com.Globales;

import java.util.Date;

public class Convertir {
    public static Date ToFecha(String strFecha, Boolean isLocal) {
        Date result = new Date();
        Integer año = 0;
        Integer mes = 0;
        Integer dia = 0;

        if(isLocal) {
            for(Integer i = 0; i < strFecha.length(); i++) {
                dia = Integer.parseInt(strFecha.substring(0, 2));
                mes = Integer.parseInt(strFecha.substring(3, 2));
                año = Integer.parseInt(strFecha.substring(6));
            }
        } else {
            for(Integer i = 0; i < strFecha.length(); i++) {
                dia = Integer.parseInt(strFecha.substring(0, 2));
                mes = Integer.parseInt(strFecha.substring(3, 2));
                año = Integer.parseInt(strFecha.substring(6));
            }
        }

        result.setYear(año);
        result.setMonth(mes);
        return result;
    }
}
