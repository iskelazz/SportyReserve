package es.udc.psi.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.udc.psi.R;
import es.udc.psi.controller.impl.UserControllerImpl;
import es.udc.psi.controller.interfaces.UserController;
import es.udc.psi.model.Reserve;
import es.udc.psi.model.User;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.repository.interfaces.BookRepository;
import es.udc.psi.view.activities.MainActivity;
import es.udc.psi.databinding.UserItemBinding;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    public interface OnUserExpelledListener {
        void onUserExpelled();
    }

    private OnUserExpelledListener listener;
    private List<User> userList;
    private Reserve reserve;
    private Context context;
    private String currentUserId;

    public UserAdapter(List<User> userList, Reserve reserve, Context context) {
        this.userList = userList;
        this.reserve = reserve;
        this.context = context;
        UserController userController = new UserControllerImpl();
        this.currentUserId = userController.getCurrentUserId();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
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

        private UserItemBinding binding;

        UserViewHolder(@NonNull UserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(User user) {
            binding.textViewSurname.setText(String.format("%s,", user.getApellidos()));
            binding.textViewName.setText(user.getNombre());

            if (user.getId().equals(reserve.getAnfitrion())) {
                binding.btnExpulsar.setVisibility(View.GONE);
                binding.btnHacerAnfitron.setVisibility(View.GONE);
                binding.textViewAnfitrion.setVisibility(View.VISIBLE);
            } else {
                if (currentUserId.equals(reserve.getAnfitrion())) {
                    binding.btnExpulsar.setVisibility(View.VISIBLE);
                    binding.btnHacerAnfitron.setVisibility(View.VISIBLE);
                } else {
                    binding.btnExpulsar.setVisibility(View.GONE);
                    binding.btnHacerAnfitron.setVisibility(View.GONE);
                }
                binding.textViewAnfitrion.setVisibility(View.GONE);
            }

            binding.btnExpulsar.setOnClickListener(v -> {
                BookRepository bookRepository = new BookRepositoryImpl();
                userList.remove(user);
                bookRepository.replaceUserListWithNew(reserve.getId(),userList, new BookRepositoryImpl.OnUserListUpdatedListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context.getApplicationContext(), "Usuario expulsado con éxito.", Toast.LENGTH_SHORT).show();
                        if(listener != null) {
                            listener.onUserExpelled();
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(context.getApplicationContext(), "Error al expulsar al usuario: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            binding.btnHacerAnfitron.setOnClickListener(v -> {
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
