package asomesyky.webhostapp.com.Actividades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import asomesyky.webhostapp.com.Entidades.Inversion;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.Globales.Global;
import asomesyky.webhostapp.com.R;

public class AdaptadorLiquidaInv extends RecyclerView.Adapter<AdaptadorLiquidaInv.ViewHolderLiquidaInv> {
    private ArrayList<Inversion> inversiones;

    public AdaptadorLiquidaInv(ArrayList<Inversion> lista){
        inversiones = lista;
    }

    public class ViewHolderLiquidaInv extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDocumento;
        private TextView txtFechaInicio;
        private TextView txtFechaVencimiento;
        private TextView txtMonto;
        private TextView txtInteres;
        private TextView txtGanancia;
        private ImageView btnLiquidar;

        private RequestQueue respuesta;
        private StringRequest strJSON;

        public ViewHolderLiquidaInv(@NonNull View itemView) {
            super(itemView);
            txtDocumento = (TextView) itemView.findViewById(R.id.lblDoc);
            txtFechaInicio = (TextView) itemView.findViewById(R.id.lblFechaInicio);
            txtFechaVencimiento = (TextView) itemView.findViewById(R.id.lblFechaFinal);
            txtMonto = (TextView) itemView.findViewById(R.id.lblMonto);
            txtInteres = (TextView) itemView.findViewById(R.id.lblInteres);
            txtGanancia = (TextView) itemView.findViewById(R.id.lblGanancia);
            btnLiquidar = (ImageView) itemView.findViewById(R.id.btnLiquidar);

            btnLiquidar.setOnClickListener(this);

            respuesta = Volley.newRequestQueue(itemView.getContext());
        }

        @Override
        public void onClick(final View view) {
            if(view.getId() == btnLiquidar.getId()){
                strJSON = new StringRequest(Request.Method.POST, Global.URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respuestaJSON = new JSONObject(response);
                            String estado = respuestaJSON.getString("resultado");

                            if(estado.equals("OK")) {
                                Toast.makeText(view.getContext(), "Inversión ''"+txtDocumento.getText().toString()+"'' liquidada correctamente.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(view.getContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(view.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("ERROR", error.toString());
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("c", "8");
                        params.put("documento", txtDocumento.getText().toString());

                        return params;
                    }
                };

                respuesta.add(strJSON);
            }
        }
    }

    @Override
    public AdaptadorLiquidaInv.ViewHolderLiquidaInv onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_liquidainv, null, false);
        return new ViewHolderLiquidaInv(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLiquidaInv.ViewHolderLiquidaInv holder, int position) {
        holder.txtDocumento.setText(inversiones.get(position).getDocumento());
        holder.txtFechaInicio.setText(Convertir.toFecha(inversiones.get(position).getFechaInicial()));
        holder.txtFechaVencimiento.setText(Convertir.toFecha(inversiones.get(position).getFechaVencimiento()));
        holder.txtMonto.setText(inversiones.get(position).getMonto().toString());
        holder.txtInteres.setText(inversiones.get(position).getInteresAnual().toString());
        holder.txtGanancia.setText(inversiones.get(position).getGanancia().toString());
        holder.btnLiquidar.setImageResource(R.drawable.borrar);
    }

    @Override
    public int getItemCount() {
        return inversiones.size();
    }
}
