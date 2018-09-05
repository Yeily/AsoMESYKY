package asomesyky.webhostapp.com.Globales;

import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Correo  extends Authenticator {
    private String pUsuario = "yeilycalderonmarin@gmail.com";
    private String pPass = "Shiraimi1411";
    private Context pContexto;

    //private String host = ;
    private Session sesion;

    static { Security.addProvider(new YProvider()); }

    public Correo(Context contexto) {
        pContexto = contexto;

        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.googlemail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        /*props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        //props.setProperty("mail.smtp.starttls.port", "587");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.user", pUsuario);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");*/

        sesion = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(pUsuario, pPass);
            }
        });
    }

    public synchronized void enviar(String para, String asunto, String cuerpo) {
        try {
            if(sesion != null) {
                Message mensaje = new MimeMessage(sesion);
                mensaje.setFrom(new InternetAddress(pUsuario));
                mensaje.setSubject(asunto);
                mensaje.setContent(cuerpo, "text/html; charset=utf-8");

                if (para.indexOf(';') > 0) {
                    mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
                } else {
                    mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
                }

                Transport.send(mensaje);
            }
        } catch(Exception ex) {
            Toast.makeText(pContexto, "Error: "+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public synchronized void enviar(String para, String asunto, String cuerpo, String rutaAdjunto) {
        int pos = rutaAdjunto.lastIndexOf("/");
        String nombreAdjunto = rutaAdjunto.substring(pos + 1);

        try {
            BodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);

            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaAdjunto)));
            adjunto.setFileName(nombreAdjunto);

            Multipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);

            MimeMessage mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(pUsuario));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(para));
            mensaje.setSubject(asunto);
            mensaje.setContent(multiParte);

            Transport t = sesion.getTransport("smtp");
            t.connect(pUsuario,pPass);
            t.sendMessage(mensaje,mensaje.getAllRecipients());
            t.close();
        } catch(Exception ex) {
            Toast.makeText(pContexto, "Error: "+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] pDatos;
        private String pTipo;

        public ByteArrayDataSource(byte[] datos) {
            super();
            pDatos = datos;
        }

        public ByteArrayDataSource(byte[] datos, String tipo) {
            super();
            pDatos = datos;
            pTipo = tipo;
        }

        public void setTipo(String tipo) {
            pTipo = tipo;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(pDatos);
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }

        @Override
        public String getContentType() {
            if(pTipo == null) {
                return "application/octet-stream";
            }else{
                return pTipo;
            }
        }

        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}