package asomesyky.webhostapp.com.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import asomesyky.webhostapp.com.R;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    @Override
    public void onClick(View view) {
        Intent i = null;

        switch(view.getId()) {
            case R.id.btnNuevoAporte:
                i = new Intent(this, AporteActivity.class);
                break;
            case R.id.btnNuevoSocio:
                i = new Intent(this, RegistroActivity.class);
                break;
            case R.id.btnNuevaInversion:
                i = new Intent(this, InversionActivity.class);
                break;
            case R.id.btnGanancia:
                i = new Intent(this, AporteActivity.class);
                break;
            case R.id.btnGasto:
                i = new Intent(this, AporteActivity.class);
                break;
            case R.id.btnResetPass:
                i = new Intent(this, AporteActivity.class);
                break;
        }

        if(i != null) {
            startActivity(i);
        }
    }

    public void barNavegacion_Click(View view) {
        finish();
    }
}
