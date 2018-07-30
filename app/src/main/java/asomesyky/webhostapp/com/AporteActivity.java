package asomesyky.webhostapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AporteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aporte);
    }

    public void barNavegacion_Click(View view) {
        finish();
    }
}
