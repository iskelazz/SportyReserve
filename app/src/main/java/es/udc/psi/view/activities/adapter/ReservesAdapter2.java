package es.udc.psi.view.activities.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import es.udc.psi.R;
import es.udc.psi.model.Reserve;
import es.udc.psi.utils.CommonThings;

public class ReservesAdapter2 extends RecyclerView.Adapter<ReservesAdapter2.reserveViewHolder> {

    private ArrayList<Reserve> mDataset;

    public ReservesAdapter2(){

        mDataset = new ArrayList<>();
    }


    public ReservesAdapter2(ArrayList<Reserve> myDataset){

        mDataset = myDataset;
    }


    public static class reserveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNameReservation, tvNameCourt, tvStrTime, tvStrDate, tvNameSport, tv_newplayer, tvNumPlayers, tvNameTeam2;
        public LinearLayout layoutPlayersTeam1, layoutPlayersTeam2, layoutOnePlayer, layoutTeams;
        public CardView cardView_img_player;
        public ImageView imageView_newplayer, imageView_privateReserve;


        public reserveViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNameReservation = itemView.findViewById(R.id.tv_nameReservation);
            tvNameCourt = itemView.findViewById(R.id.tv_nameCourt);
            tvStrTime = itemView.findViewById(R.id.tv_nameTime);
            tvStrDate = itemView.findViewById(R.id.tv_nameDate);
            tvNumPlayers = itemView.findViewById(R.id.tv_numberPlayers);
            tvNameSport = itemView.findViewById(R.id.tv_nameSport);
            tvNameTeam2 = itemView.findViewById(R.id.tv_nameTeam2);
            imageView_privateReserve = itemView.findViewById(R.id.iv_privateReserve);

            layoutPlayersTeam1=itemView.findViewById(R.id.ll_team1players);
            layoutPlayersTeam2=itemView.findViewById(R.id.ll_team2players);
            layoutTeams=itemView.findViewById(R.id.ll_players);
        }


        @SuppressLint("SetTextI18n")
        public void bind(@NonNull Reserve reserve) {


            tvNameReservation.setText(reserve.getName());
            if (!reserve.isPublic()){imageView_privateReserve.setVisibility(View.VISIBLE);} else {imageView_privateReserve.setVisibility(View.GONE);}
            tvNameCourt.setText(reserve.getPista());
            tvNameSport.setText(reserve.getDeporte());
            if (reserve.getPlayerList()!=null){
                tvNumPlayers.setText(Integer.toString((reserve.getCapacidadMax()-reserve.getPlayerList().size())));
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reserve.getFecha());
            calendar.add(Calendar.MINUTE, reserve.getDuracion());
            Date endTime = calendar.getTime();
            DateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String stringDate = df.format(reserve.getFecha())+" - "+df.format(endTime);
            tvStrTime.setText(stringDate);
            DateFormat dfi = new SimpleDateFormat("dd/MM", Locale.getDefault());
            tvStrDate.setText(dfi.format(reserve.getFecha()));

            layoutPlayersTeam1.removeAllViews();
            layoutPlayersTeam2.removeAllViews();

            if ((layoutPlayersTeam1 != null) && (layoutPlayersTeam2 != null) && (reserve.getPlayerList()!=null) && (!reserve.getPlayerList().isEmpty())) {

                int size_list_players = reserve.getPlayerList().size();
                //int size_list_players = reserve.getNumPlayers();
                int num_max_players = reserve.getCapacidadMax();
                int num_players_team = num_max_players / 2;

                // Orientación de los layouts de los equipos
                if (num_max_players > 4) {                            //ll_players en horizontal (2 ll_teams en horizontal)
                    layoutTeams.setOrientation(LinearLayout.VERTICAL);
                } else {
                    layoutTeams.setOrientation(LinearLayout.HORIZONTAL);
                }

                //Añadir los jugadores de la playerList
                if (size_list_players > num_players_team) {
                    //Add team 1
                    for (int i = 0; i < num_players_team; i++) {
                        settingsForNewPlayer(layoutPlayersTeam1);
                        addNewPlayerTeam(reserve, i, layoutPlayersTeam1);

                    }

                    //Add team2
                    for (int i = num_players_team; i < size_list_players; i++) {
                        settingsForNewPlayer(layoutPlayersTeam2);
                        addNewPlayerTeam(reserve, i, layoutPlayersTeam2);
                    }

                } else {
                    //Add team 1
                    for (int i = 0; i < size_list_players; i++) {
                        settingsForNewPlayer(layoutPlayersTeam1);
                        addNewPlayerTeam(reserve, i, layoutPlayersTeam1);

                    }
                }

                //Añadir avatar para añadir jugador
                tvNameTeam2.setVisibility(View.VISIBLE);
                if (size_list_players < num_max_players) {
                    if (size_list_players < num_players_team) {
                        settingsForNewPlayer(layoutPlayersTeam1);
                        addNewAvatarPlayer(size_list_players, layoutPlayersTeam1);
                        tvNameTeam2.setVisibility(View.GONE);
                    } else {
                        settingsForNewPlayer(layoutPlayersTeam2);
                        addNewAvatarPlayer(size_list_players, layoutPlayersTeam2);
                    }
                }

            }
        }

        private void settingsForNewPlayer(@NonNull LinearLayout layoutPlayersTeam) {

            layoutOnePlayer = new LinearLayout(layoutPlayersTeam.getContext());
            tv_newplayer = new TextView(layoutPlayersTeam.getContext());
            cardView_img_player = new CardView(layoutPlayersTeam.getContext());
            imageView_newplayer = new ImageView(layoutPlayersTeam.getContext());
        }


        private void addNewPlayerTeam(@NonNull Reserve reserve, int position, @NonNull LinearLayout layoutTeamPlayers){

            int sizeAvatar = (position>10)? CommonThings.SIZE_AVATAR_SMALL : CommonThings.SIZE_AVATAR_BIG;

            tv_newplayer.setId(100+position);;
            tv_newplayer.setTag("tv_player_"+position);
            tv_newplayer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            tv_newplayer.setText(reserve.getPlayerList().get(position).getNombre());
            tv_newplayer.setTextSize(14);
            tv_newplayer.setGravity(Gravity.CENTER);
            tv_newplayer.setTextColor(Color.parseColor("#777777"));

            cardView_img_player.setId(200+position);
            cardView_img_player.setCardElevation(10);
            cardView_img_player.setRadius(250);
            cardView_img_player.setLayoutParams(new LinearLayout.LayoutParams(sizeAvatar,sizeAvatar,1));
            cardView_img_player.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);

            imageView_newplayer.setId(300+position);
            imageView_newplayer.setTag("image_player_"+position);
            imageView_newplayer.setLayoutParams(new LinearLayout.LayoutParams(sizeAvatar,sizeAvatar,1));
            imageView_newplayer.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(imageView_newplayer)
                    .load(reserve.getPlayerList().get(position).getUriAvatar())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .skipMemoryCache(true)
                    .signature(new ObjectKey(UUID.randomUUID().toString()))
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

        private void addNewAvatarPlayer(int position, @NonNull LinearLayout layoutTeamPlayers) {

            int sizeAvatar = (position>10)? CommonThings.SIZE_AVATAR_SMALL : CommonThings.SIZE_AVATAR_BIG;

            tv_newplayer.setId(100+position);
            tv_newplayer.setTag("tv_player_"+position);
            tv_newplayer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            tv_newplayer.setText(R.string.str_NewPlayer);
            tv_newplayer.setTextSize(14);
            tv_newplayer.setGravity(Gravity.CENTER);
            tv_newplayer.setTextColor(Color.parseColor("#777777"));

            imageView_newplayer.setId(300+position);
            imageView_newplayer.setTag("image_player_"+position);
            imageView_newplayer.setLayoutParams(new LinearLayout.LayoutParams(sizeAvatar,sizeAvatar,1));
            imageView_newplayer.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView_newplayer.setImageResource(R.drawable.round_add_player_24);

            layoutOnePlayer.setId(position);
            layoutOnePlayer.setTag("layoutplayer_"+position);
            layoutOnePlayer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            layoutOnePlayer.setOrientation(LinearLayout.VERTICAL);
            layoutOnePlayer.setGravity(Gravity.CENTER);
            layoutOnePlayer.setOnClickListener(this);

            layoutOnePlayer.addView(imageView_newplayer);
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
        notifyItemInserted(mDataset.size());
    }


    public void deleteReserve(int position) {

        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    public Reserve getReserve(int position){

        return mDataset.get(position);
    }


    public void setReserve(Reserve reserve, int position) {

        mDataset.set(position,reserve);
        //notifyDataSetChanged();
        notifyItemChanged(position);
    }


    public void updateReserve(Reserve reserve,
                           int position) {

        notifyDataSetChanged();
        //mDataset.add(position, reserve);
        //notifyItemChanged(position);
    }

}
