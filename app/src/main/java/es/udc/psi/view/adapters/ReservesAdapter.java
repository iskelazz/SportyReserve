package es.udc.psi.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.udc.psi.R;
import es.udc.psi.model.Reserve;
import es.udc.psi.view.activities.DetailActivity;

public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {

        private List<Reserve> reservas;

        public ReservesAdapter(List<Reserve> reservas) {
            this.reservas = reservas;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserva_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Reserve reserva = reservas.get(position);
            holder.textViewNombrePista.setText(reserva.getPista());
            holder.textViewHoraInicio.setText(reserva.getFecha().toString());
            holder.textViewHoraFin.setText(reserva.getDeporte());
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
            public final TextView textViewNombrePista;
            public final TextView textViewHoraInicio;
            public final TextView textViewHoraFin;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewNombrePista = itemView.findViewById(R.id.nombre_pista);
                textViewHoraInicio = itemView.findViewById(R.id.hora_inicio);
                textViewHoraFin = itemView.findViewById(R.id.hora_fin);
            }
        }
    }
