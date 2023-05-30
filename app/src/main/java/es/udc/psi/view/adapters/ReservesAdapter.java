package es.udc.psi.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.udc.psi.model.Reserve;
import es.udc.psi.view.activities.DetailActivity;
import es.udc.psi.databinding.ReservaItemBinding;

public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {

    private List<Reserve> reservas;

    public ReservesAdapter(List<Reserve> reservas) {
        this.reservas = reservas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReservaItemBinding binding = ReservaItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserve reserva = reservas.get(position);
        holder.binding.nombrePista.setText(reserva.getPista());
        holder.binding.horaInicio.setText(reserva.getFecha().toString());
        holder.binding.horaFin.setText(reserva.getDeporte());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(DetailActivity.newIntent(v.getContext(), reserva));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public void updateData(List<Reserve> newReserves) {
        this.reservas = newReserves;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ReservaItemBinding binding;

        public ViewHolder(@NonNull ReservaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
