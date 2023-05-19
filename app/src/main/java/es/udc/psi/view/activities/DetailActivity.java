package es.udc.psi.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import es.udc.psi.R;
import es.udc.psi.model.Reserve;

public class DetailActivity extends AppCompatActivity {
    private MaterialButton btnCancel;
    private MaterialButton btnEdit;
    private TextView textViewReserveId;
    private TextView textViewHost;
    private TextView textViewPista;
    private TextView textViewCapacidadMax;
    private TextView textViewDeporte;
    private TextView textViewNumPlayers;
    private TextView textViewFecha;
    private TextView textViewDuracion;
    private RecyclerView rvPlayerList;
    private static final String EXTRA_RESERVE = "extra_reserve";
    //private UserAdapter adapter;

    public static Intent newIntent(Context context, Reserve reserve) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_RESERVE, reserve);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Reserve reserve = getIntent().getParcelableExtra(EXTRA_RESERVE);

        textViewReserveId = findViewById(R.id.text_view_reserve_id);
        textViewReserveId.setText(reserve.getId());
/*
        textViewPista = findViewById(R.id.tvPista);
        textViewPista.setText(reserve.getPista());

        textViewCapacidadMax = findViewById(R.id.tvCapacidadMax);
        textViewCapacidadMax.setText(String.valueOf(reserve.getCapacidadMax()));

        textViewDeporte = findViewById(R.id.tvDeporte);
        textViewDeporte.setText(reserve.getDeporte());

        textViewNumPlayers = findViewById(R.id.tvNumPlayers);
        textViewNumPlayers.setText(String.valueOf(reserve.getNumPlayers()));

        textViewFecha = findViewById(R.id.tvFecha);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        textViewFecha.setText(df.format(reserve.getFecha()));

        textViewDuracion = findViewById(R.id.tvDuracion);
        textViewDuracion.setText(String.valueOf(reserve.getDuracion()));
*/
        SetupCancelButton();
        SetupEditButton();

        //RecyclerView rvPlayerList = findViewById(R.id.rvPlayerList);
        /*adapter = new UserAdapter();
        rvPlayerList.setAdapter(adapter);
        if (reserve.getPlayerList() != null) {
            adapter.submitList(reserve.getPlayerList());
        }*/
    }

    private void SetupCancelButton() {
        // Initialize cancel button
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            // Handle click event here
            // For example, cancel the reservation
        });
    }

    private void SetupEditButton() {
        // Initialize edit button
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(view -> {
            // Handle click event here
            // For example, edit the reservation
        });
    }
}
