package es.udc.psi.view.activities.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import es.udc.psi.R;
import es.udc.psi.model.Reserve;

public class ReservesAdapter2 extends RecyclerView.Adapter<ReservesAdapter2.reserveViewHolder> {

    private ArrayList<Reserve> mDataset;

    public ReservesAdapter2(){

        mDataset = new ArrayList<>();
    }


    public ReservesAdapter2(ArrayList<Reserve> myDataset){

        mDataset = myDataset;
    }


    public static class reserveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNameReservation, tvNameCourt, tvStrDate, tvNameSport, tv_newplayer;
        public LinearLayout layoutPlayersTeam1, layoutPlayersTeam2, layoutOnePlayer, layoutTeams;
        public CardView cardView_img_player;
        public ImageView imageView_newplayer;


        public reserveViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNameReservation = itemView.findViewById(R.id.tv_nameReservation);
            tvNameCourt = itemView.findViewById(R.id.tv_nameCourt);
            tvStrDate = itemView.findViewById(R.id.tv_nameDate);
            tvNameSport = itemView.findViewById(R.id.tv_nameSport);

            layoutPlayersTeam1=itemView.findViewById(R.id.ll_team1players);
            layoutPlayersTeam2=itemView.findViewById(R.id.ll_team2players);
            layoutTeams=itemView.findViewById(R.id.ll_players);
        }


        public void bind(Reserve reserve) {

            tvNameReservation.setText(reserve.getId()+" - "+reserve.getPista());
            tvNameCourt.setText(reserve.getPista());
            //tvStrDate.setText(reserve.getFecha());
            tvNameSport.setText(reserve.getDeporte());

            layoutPlayersTeam1.removeAllViews();
            layoutPlayersTeam2.removeAllViews();

            if ((layoutPlayersTeam1 != null) && (layoutPlayersTeam2 != null)) {
                //if ((layoutPlayers!= null) && (layoutPlayers.getChildCount()>0)){

                int size_list_players = reserve.getPlayerList().size();     //int size_list_players = reservation.getNumPlayers(); //TODO:???????????
                int num_players_team = size_list_players / 2;

                //Add team 1
                for (int i = 0; i < num_players_team; i++) {
                    settingsForNewPlayer(layoutPlayersTeam1);
                    addNewPlayerTeam(reserve, i, layoutPlayersTeam1);

                }

                if (size_list_players > 4) {                            //ll_players en horizontal (2 ll_teams en horizontal)
                    layoutTeams.setOrientation(LinearLayout.VERTICAL);
                }

                //Add team2
                for (int i = num_players_team; i < size_list_players; i++) {
                    settingsForNewPlayer(layoutPlayersTeam2);
                    addNewPlayerTeam(reserve, i, layoutPlayersTeam2);
                }
            }
        }

        private void settingsForNewPlayer(LinearLayout layoutPlayersTeam) {

            layoutOnePlayer = new LinearLayout(layoutPlayersTeam.getContext());
            tv_newplayer = new TextView(layoutPlayersTeam.getContext());
            cardView_img_player = new CardView(layoutPlayersTeam.getContext());
            imageView_newplayer = new ImageView(layoutPlayersTeam.getContext());
        }

        private void addNewPlayerTeam(Reserve reserve, int position, LinearLayout layoutTeamPlayers){

            tv_newplayer.setId(100+position);;
            tv_newplayer.setTag("tv_player_"+position);
            tv_newplayer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            tv_newplayer.setText(reserve.getPlayerList().get(position).getNombre());
            tv_newplayer.setTextSize(14);
            tv_newplayer.setGravity(Gravity.CENTER);

            cardView_img_player.setId(200+position);
            cardView_img_player.setCardElevation(10);
            cardView_img_player.setRadius(250);
            cardView_img_player.setLayoutParams(new LinearLayout.LayoutParams(150,150,1));
            cardView_img_player.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);

            imageView_newplayer.setId(300+position);
            imageView_newplayer.setTag("image_player_"+position);
            imageView_newplayer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,1));
            Glide.with(imageView_newplayer)
                    .load(reserve.getPlayerList().get(position).getUriAvatar())
                    .placeholder(android.R.drawable.ic_input_add)   //TODO: Cambiar icono??
                    .into(imageView_newplayer);

            layoutOnePlayer.setId(position);
            layoutOnePlayer.setTag("layoutplayer_"+position);
            layoutOnePlayer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            layoutOnePlayer.setOrientation(LinearLayout.VERTICAL);
            layoutOnePlayer.setGravity(Gravity.CENTER);
            layoutOnePlayer.setOnClickListener(this);

            cardView_img_player.addView(imageView_newplayer);
            layoutOnePlayer.addView(cardView_img_player);
            layoutOnePlayer.addView(tv_newplayer);

            layoutTeamPlayers.addView(layoutOnePlayer);

        }


        @Override
        public void onClick(View v) {

            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public ReservesAdapter2.reserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_tile, parent, false);
        return new reserveViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull reserveViewHolder holder,
                                 int position) {

        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {

        return mDataset.size();
    }

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    private static OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener itemClickListener) {

        //this.clickListener = itemClickListener;
        clickListener = itemClickListener;
    }


    public void setReserveList(ArrayList<Reserve> reserveList) {

        mDataset = reserveList;
        notifyDataSetChanged();
    }



    public void addReserve(Reserve reserve) {

        mDataset.add(reserve);
        //notifyItemChanged(mDataset.size()-1);
        notifyItemInserted(mDataset.size() - 1);
    }


    public void deleteReserve(int position) {

        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    public Reserve getReserve(int position){

        return mDataset.get(position);
    }


    public void setReserve(int position, Reserve reserve) {

        mDataset.set(position,reserve);
        //notifyDataSetChanged();
        notifyItemChanged(position);
    }


    public void updateReserve(Reserve reserve,
                           int position) {

        mDataset.add(position, reserve);
        notifyItemChanged(position);
    }

}
