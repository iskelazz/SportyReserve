package es.udc.psi.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import es.udc.psi.R;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.view.activities.MainActivity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public interface OnUserExpelledListener {
        void onUserExpelled();
    }
    private OnUserExpelledListener listener;
    private List<User> userList;
    private Reserve reserve;
    private Context context;
    private String currentUserId;


    public UserAdapter(List<User> userList, Reserve reserve, Context context){

        this.userList = userList;
        this.reserve = reserve;
        this.context = context;
        UserController userController = new UserControllerImpl();
        this.currentUserId = userController.getCurrentUserId();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }
    public void setOnUserExpelledListener(OnUserExpelledListener listener) {
        this.listener = listener;
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

            if (user.getId().equals(reserve.getAnfitrion())) {
                btnExpulsar.setVisibility(View.GONE);
                btnHacerAnfitron.setVisibility(View.GONE);
                textHost.setVisibility(View.VISIBLE);
            } else {
                // Solo muestra los botones si el usuario actual es el anfitrión.
                if (currentUserId.equals(reserve.getAnfitrion())) {
                    btnExpulsar.setVisibility(View.VISIBLE);
                    btnHacerAnfitron.setVisibility(View.VISIBLE);
                } else {
                    btnExpulsar.setVisibility(View.GONE);
                    btnHacerAnfitron.setVisibility(View.GONE);
                }
                textHost.setVisibility(View.GONE);
            }

            // Handle click events for the buttons
            btnExpulsar.setOnClickListener(v -> {
                BookRepository bookRepository = new BookRepositoryImpl();
                userList.remove(user);
                bookRepository.replaceUserListWithNew(reserve.getId(),userList, new BookRepositoryImpl.OnUserListUpdatedListener() {
                    @Override
                    public void onSuccess() {
                        // Muestra un Toast cuando el usuario se haya eliminado con éxito
                        Toast.makeText(context.getApplicationContext(), "Usuario expulsado con éxito.", Toast.LENGTH_SHORT).show();
                        // Actualizar la vista aquí. Por ejemplo, podrías querer actualizar una lista de usuarios.
                        if(listener != null) {
                            listener.onUserExpelled();
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Muestra un Toast cuando haya un error
                        Toast.makeText(context.getApplicationContext(), "Error al expulsar al usuario: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            });


            btnHacerAnfitron.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Hacer Anfitrion")
                        .setMessage("Si haces a otro jugador anfitrion, perderas ese rol. ¿Estás seguro de querer continuar?")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            BookRepository bookRepository = new BookRepositoryImpl();
                            reserve.setAnfitrion(user.getId());
                            bookRepository.updateReserve(reserve, new BookRepositoryImpl.OnReserveUpdatedListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context.getApplicationContext(), "Usuario promovido a anfitrion con éxito.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(context.getApplicationContext(), "Error al promover al usuario a anfitrion: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });
        }
    }
}
