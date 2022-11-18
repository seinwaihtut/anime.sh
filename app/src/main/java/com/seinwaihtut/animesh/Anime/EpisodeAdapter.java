package com.seinwaihtut.animesh.Anime;

import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.seinwaihtut.animesh.DB.EpisodePOJO;
import com.seinwaihtut.animesh.R;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    private List<EpisodePOJO> adapterEpisodes = new ArrayList<>();
    private static ClickListener clickListener;
    private static EpisodeAdapter instance = null;

    public static EpisodeAdapter getInstance(){
        if (instance==null){
            instance = new EpisodeAdapter();
        }
        return instance;
    }
    public EpisodePOJO getItemAtPosition(Integer position){
        return adapterEpisodes.get(position);
    }

    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);
        return new EpisodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {
        holder.getUpload_name().setText(adapterEpisodes.get(position).getUpload_title());
        holder.getSize().setText(adapterEpisodes.get(position).getSize());
        holder.getUpload_date().setText(adapterEpisodes.get(position).getUpload_time());
        holder.getSeeders().setText(adapterEpisodes.get(position).getSeeders().toString());
        holder.getLeechers().setText(adapterEpisodes.get(position).getLeechers().toString());
    }

    @Override
    public int getItemCount() {
        return adapterEpisodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private final TextView upload_name;
        private final TextView upload_date;
        private final TextView size;
        private final TextView seeders;
        private final TextView leechers;
        private final CardView episode_card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upload_name = itemView.findViewById(R.id.episode_upload_name);
            upload_date = itemView.findViewById(R.id.episode_upload_date);
            size = itemView.findViewById(R.id.episode_size);
            seeders = itemView.findViewById(R.id.episode_seeders);
            leechers = itemView.findViewById(R.id.episode_leechers);
            episode_card_view = itemView.findViewById(R.id.episode_card_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
            episode_card_view.setOnCreateContextMenuListener(this);

        }

        public TextView getUpload_name() {
            return upload_name;
        }

        public TextView getUpload_date() {
            return upload_date;
        }

        public TextView getSize() {
            return size;
        }

        public TextView getSeeders() {
            return seeders;
        }

        public TextView getLeechers() {
            return leechers;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Choose"); //Set title for floating context menu
            menu.add(getAdapterPosition(), 3001, 0, "Open in a Web Browser");
            menu.add(getAdapterPosition(), 3002, 1, "Open Magnet Link");
            menu.add(getAdapterPosition(), 3003, 2, "Download Torrent File");
        }
    }

    public void setData(List<EpisodePOJO> episodes) {
        adapterEpisodes = episodes;
        notifyDataSetChanged();
    }

    public EpisodeAdapter() {
    }

    public void setOnItemClickListener(EpisodeAdapter.ClickListener clickListener) {
        EpisodeAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }




}
