package es.udc.psi.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.udc.psi.R;
import es.udc.psi.model.Reserva;

    public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {

        private List<Reserva> reservas;

        public ReservesAdapter(List<Reserva> reservas) {
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
            // Configura los elementos de la vista con los datos de la reserva
            Reserva reserva = reservas.get(position);

            // Por ejemplo, si tienes un TextView en reserva_item.xml para mostrar el nombre de la pista
            // holder.textViewNombrePista.setText(reserva.getNombrePista());
        }

        @Override
        public int getItemCount() {
            return reservas.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            // Por ejemplo, si tienes un TextView en reserva_item.xml para mostrar el nombre de la pista
            // public final TextView textViewNombrePista;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                // Por ejemplo, si tienes un TextView en reserva_item.xml para mostrar el nombre de la pista
                // textViewNombrePista =
                // itemView.findViewById(R.id.nombre_pista);
            }
        }
    }
