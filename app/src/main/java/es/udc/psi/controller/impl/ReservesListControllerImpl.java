package es.udc.psi.controller.impl;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import es.udc.psi.controller.interfaces.ReservesListController;
import es.udc.psi.model.Reserve;
import es.udc.psi.repository.impl.BookRepositoryImpl;
import es.udc.psi.view.interfaces.ReservesListView;

public class ReservesListControllerImpl implements ReservesListController {

    private ReservesListView mReserveListView;

    private FirebaseAuth mAuth;

    private BookRepositoryImpl mBookRepository;

    public ReservesListControllerImpl(ReservesListView reservesListView) {

        mReserveListView = reservesListView;
        mAuth = FirebaseAuth.getInstance();
        mBookRepository = new BookRepositoryImpl();
    }

    @Override
    public void initFlow() {

        ArrayList<Reserve> reservesList = mBookRepository.getReservesList();
        mReserveListView.showReservesList(reservesList);
    }

    @Override
    public void onClickReserve() {

    }

    @Override
    public void onClickPlayer() {

    }

    @Override
    public void onClickAddNewPlayer() {

    }
}
