package asomesyky.webhostapp.com.Actividades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AdaptadorInversiones extends RecyclerView.Adapter<AdaptadorInversiones.ViewHolderInversiones> {
    public class ViewHolderInversiones extends RecyclerView.ViewHolder{

        public ViewHolderInversiones(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public AdaptadorInversiones.ViewHolderInversiones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInversiones.ViewHolderInversiones holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
