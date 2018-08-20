package asomesyky.webhostapp.com.Actividades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import asomesyky.webhostapp.com.Entidades.Inversion;
import asomesyky.webhostapp.com.R;

public class AdaptadorLiquidaInv extends RecyclerView.Adapter<AdaptadorLiquidaInv.ViewHolderLiquidaInv> {
    private ArrayList<Inversion> inversiones;

    public AdaptadorLiquidaInv(ArrayList<Inversion> lista){
        inversiones = lista;
    }

    public class ViewHolderLiquidaInv extends RecyclerView.ViewHolder{
        private TextView txtDocumento;
        private TextView txtFechaInicio;
        private TextView txtFechaVencimiento;
        private TextView txtMonto;
        private TextView txtInteres;
        private TextView txtGanancia;
        private ImageView btnLiquidar;


        public ViewHolderLiquidaInv(@NonNull View itemView) {
            super(itemView);
            txtDocumento = (TextView) itemView.findViewById(R.id.txtDocumento);
            txtFechaInicio = (TextView) itemView.findViewById(R.id.txtFechaInicio);
            txtFechaVencimiento = (TextView) itemView.findViewById(R.id.txtFechaVencimiento);
            txtMonto = (TextView) itemView.findViewById(R.id.txtMonto);
            txtInteres = (TextView) itemView.findViewById(R.id.txtInteresAnual);
            txtGanancia = (TextView) itemView.findViewById(R.id.txtGanancia);
            btnLiquidar = (ImageView) itemView.findViewById(R.id.btnLiquidar);
        }
    }

    @Override
    public AdaptadorLiquidaInv.ViewHolderLiquidaInv onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_inversiones, null, false);
        return new ViewHolderLiquidaInv(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLiquidaInv.ViewHolderLiquidaInv holder, int position) {
        holder.txtDocumento.setText(inversiones.get(position).getDocumento());
        holder.txtFechaInicio.setText(inversiones.get(position).getFechaInicial().toString());
        holder.txtFechaVencimiento.setText(inversiones.get(position).getFechaVencimiento().toString());
        holder.txtMonto.setText(inversiones.get(position).getMonto().toString());
        holder.txtInteres.setText(inversiones.get(position).getInteresAnual().toString());
        holder.txtGanancia.setText(inversiones.get(position).getGanancia().toString());
    }

    @Override
    public int getItemCount() {
        return inversiones.size();
    }
}
