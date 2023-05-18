package es.udc.psi.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.udc.psi.R;
import es.udc.psi.model.Reserve;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_RESERVE = "extra_reserve";

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

        TextView textViewReserveId = findViewById(R.id.text_view_reserve_id);
        textViewReserveId.setText(reserve.getId());

        TextView textViewHost = findViewById(R.id.text_view_host);
        textViewHost.setText(reserve.getAnfitrion());
    }
}
