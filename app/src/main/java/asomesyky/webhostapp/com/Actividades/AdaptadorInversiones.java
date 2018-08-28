package asomesyky.webhostapp.com.Actividades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import asomesyky.webhostapp.com.Entidades.Inversion;
import asomesyky.webhostapp.com.Globales.Convertir;
import asomesyky.webhostapp.com.R;

public class AdaptadorInversiones extends RecyclerView.Adapter<AdaptadorInversiones.ViewHolderInversiones> {
    private ArrayList<Inversion> inversiones;

    public AdaptadorInversiones(ArrayList<Inversion> lista) {
        inversiones = lista;
    }

    @Override
    public AdaptadorInversiones.ViewHolderInversiones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_inversiones, null, false);
        return new ViewHolderInversiones(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInversiones.ViewHolderInversiones holder, int position) {
        holder.txtDocumento.setText(inversiones.get(position).getDocumento());
        holder.txtFechaInicio.setText(Convertir.toFecha(inversiones.get(position).getFechaInicial()));
        holder.txtFechaVencimiento.setText(Convertir.toFecha(inversiones.get(position).getFechaVencimiento()));
        holder.txtMonto.setText(inversiones.get(position).getMonto().toString());
        holder.txtInteres.setText(inversiones.get(position).getInteresAnual().toString());
        holder.txtGanancia.setText(inversiones.get(position).getGanancia().toString());
    }

    @Override
    public int getItemCount() {
        return inversiones.size();
    }

    public class ViewHolderInversiones extends RecyclerView.ViewHolder {
        private TextView txtDocumento;
        private TextView txtFechaInicio;
        private TextView txtFechaVencimiento;
        private TextView txtMonto;
        private TextView txtInteres;
        private TextView txtGanancia;

        public ViewHolderInversiones(@NonNull View itemView) {
            super(itemView);

            txtDocumento = (TextView) itemView.findViewById(R.id.lblDoc);
            txtFechaInicio = (TextView) itemView.findViewById(R.id.lblFechaInicio);
            txtFechaVencimiento = (TextView) itemView.findViewById(R.id.lblFechaFinal);
            txtMonto = (TextView) itemView.findViewById(R.id.lblMonto);
            txtInteres = (TextView) itemView.findViewById(R.id.lblInteres);
            txtGanancia = (TextView) itemView.findViewById(R.id.lblGanancia);
        }
    }
}
