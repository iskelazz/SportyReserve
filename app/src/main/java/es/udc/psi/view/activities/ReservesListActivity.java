package es.udc.psi.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import es.udc.psi.R;
import es.udc.psi.controller.impl.ReservesListControllerImpl;
import es.udc.psi.controller.interfaces.ReservesListController;
import es.udc.psi.databinding.ActivityReservesListBinding;
import es.udc.psi.model.Reserve;
import es.udc.psi.view.activities.adapter.ReservesAdapter2;
import es.udc.psi.view.interfaces.ReservesListView;

public class ReservesListActivity extends AppCompatActivity implements ReservesListView {

    private ActivityReservesListBinding binding;
    private ReservesListController mReserveListController;
    private ReservesAdapter2 mReserveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityReservesListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setUI();

        mReserveListController = new ReservesListControllerImpl(this);
        mReserveListController.initFlow();

    }

    @Override
    public void showReservesList(ArrayList<Reserve> reservesList) {
        mReserveAdapter.setReserveList(reservesList);
        binding.tvReservesEmptyList.setVisibility(View.GONE);
        binding.rvReserves.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {

        Toast.makeText(this, getResources().getText(R.string.str_error_msg).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

        binding.tvReservesEmptyList.setVisibility(View.VISIBLE);
        binding.rvReserves.setVisibility(View.GONE);

    }

    @Override
    public void updateReserve(Reserve reserve,
                              int position) {

    }

    private void setUI() {

        binding.rvReserves.setHasFixedSize(true);
        mReserveAdapter = new ReservesAdapter2();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvReserves.setLayoutManager(linearLayoutManager);
        binding.rvReserves.setAdapter(mReserveAdapter);

        mReserveAdapter.setClickListener(new ReservesAdapter2.OnItemClickListener() {

            @Override
            public void onClick(View view,
                                int position) {
                //TODO: check if player exist or add new player
                mReserveListController.onClickPlayer();
                mReserveListController.onClickAddNewPlayer();

            }
        });

    }

}