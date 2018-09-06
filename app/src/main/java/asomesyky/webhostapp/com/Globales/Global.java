package asomesyky.webhostapp.com.Globales;

import android.app.AppComponentFactory;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import asomesyky.webhostapp.com.Actividades.PasswordActivity;
import asomesyky.webhostapp.com.Entidades.Socio;
import org.apache.commons.codec.binary.Base64;

public class Global {
    private static final String CLAVE = "Yeily";
    private static final String ALGORITMO = "DESede";

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

    public static String Encriptar(String texto)  throws Exception {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pass = md.digest(CLAVE.getBytes("utf-8"));
            byte[] passBytes = Arrays.copyOf(pass, 24);

            SecretKey key = new SecretKeySpec(passBytes, ALGORITMO);
            Cipher cifrado = Cipher.getInstance(ALGORITMO);
            cifrado.init(Cipher.ENCRYPT_MODE, key);

            byte[] textoBytes = texto.getBytes("utf-8");
            byte[] bufer = cifrado.doFinal(textoBytes);
            byte[] resultBytes = Base64.encodeBase64(bufer);
            result = new String(resultBytes);
        } catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }

        return result;
    }

    public static String Desencriptar(String textoEncriptado) throws Exception {
        String result = "";

        try {
            byte[] mensaje = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pass = md.digest(CLAVE.getBytes("utf-8"));
            byte[] passBytes = Arrays.copyOf(pass, 24);
            SecretKey key = new SecretKeySpec(passBytes, ALGORITMO);

            Cipher decifrado = Cipher.getInstance(ALGORITMO);
            decifrado.init(Cipher.DECRYPT_MODE, key);

            byte[] textoBytes = decifrado.doFinal(mensaje);
            result = new String(textoBytes, "UTF-8");
        } catch(Exception ex) {
            throw  new Exception(ex.getMessage());
        }

        return result;
    }
}
