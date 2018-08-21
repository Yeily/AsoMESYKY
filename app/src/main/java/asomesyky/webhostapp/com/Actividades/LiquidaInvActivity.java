package asomesyky.webhostapp.com.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import asomesyky.webhostapp.com.Entidades.Inversion;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.R;

public class LiquidaInvActivity extends AppCompatActivity {
    private ArrayList<Inversion> inversiones;
    private RecyclerView rvInversones;

    private void InicializarComponentes(){
        inversiones = new ArrayList<>();
        rvInversones = (RecyclerView) findViewById(R.id.rcyLista);
        rvInversones.setLayoutManager(new LinearLayoutManager(this));

        LlenarInversiones();
        AdaptadorLiquidaInv adp = new AdaptadorLiquidaInv(inversiones);
        rvInversones.setAdapter(adp);
    }

    private void LlenarInversiones(){
        inversiones.add(new Inversion("65465464", "fdsfs", "dsgsd", "jhgjhg",
                Convertir.toFecha("2018-08-01"), Convertir.toFecha("2018-11-08"), 100.20,
                10f, 101.00, 200.00, "hkhkjh", 5, new Character('N')));
        inversiones.add(new Inversion("65465464", "fdsfs", "dsgsd", "jhgjhg",
                Convertir.toFecha("2018-08-01"), Convertir.toFecha("2018-11-08"), 100.20,
                10f, 101.00, 200.00, "hkhkjh", 5, new Character('N')));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidainv);

        InicializarComponentes();
    }

    public void barNavegacion_Click(View view) { finish(); }

    public void btnLiquidar_Click(View view) {
        switch(view.getId()){
            case R.id.btnLiquidar:
                Toast.makeText(this, "Liquidando inversión... " + view.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}
