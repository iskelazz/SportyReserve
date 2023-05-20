package es.udc.psi.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import es.udc.psi.R;
import es.udc.psi.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private String anfitrion;

    public UserAdapter(List<User> userList, String anfitrion) {

        this.userList = userList;
        this.anfitrion = anfitrion;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewSurname;
        private TextView textHost;
        private MaterialButton btnExpulsar;
        private MaterialButton btnHacerAnfitron;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSurname = itemView.findViewById(R.id.textViewSurname);
            textHost = itemView.findViewById(R.id.textViewAnfitrion);
            btnExpulsar = itemView.findViewById(R.id.btnExpulsar);
            btnHacerAnfitron = itemView.findViewById(R.id.btnHacerAnfitron);
        }

        void bind(User user) {
            textViewSurname.setText(String.format("%s,", user.getApellidos()));
            textViewName.setText(user.getNombre());
            if (user.getId().equals(anfitrion)) {
                btnExpulsar.setVisibility(View.GONE);
                btnHacerAnfitron.setVisibility(View.GONE);
                textHost.setVisibility(View.VISIBLE);
            } else {
                btnExpulsar.setVisibility(View.VISIBLE);
                btnHacerAnfitron.setVisibility(View.VISIBLE);
                textHost.setVisibility(View.GONE);
            }

            // Handle click events for the buttons
            btnExpulsar.setOnClickListener(v -> {
                // Handle the click event for the "Expulsar" button
            });

            btnHacerAnfitron.setOnClickListener(v -> {
                // Handle the click event for the "Hacer Anfitron" button
            });
        }
    }
}
