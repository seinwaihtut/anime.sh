package com.seinwaihtut.animesh.Anime;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.seinwaihtut.animesh.R;

import java.util.ArrayList;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> localEpisodeList;
    private static ClickListener clickListener;

    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);
        return new EpisodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {
            holder.getUpload_name().setText(localEpisodeList.get(position).get(0));
            holder.getSize().setText(localEpisodeList.get(position).get(2));
            holder.getUpload_date().setText(localEpisodeList.get(position).get(3));
            holder.getSeeders().setText(localEpisodeList.get(position).get(4));
            holder.getLeechers().setText(localEpisodeList.get(position).get(5));

    }

    @Override
    public int getItemCount() {
        return localEpisodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView upload_name;
        private final TextView upload_date;
        private final TextView size;
        private final TextView seeders;
        private final TextView leechers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upload_name = itemView.findViewById(R.id.episode_upload_name);
            upload_date = itemView.findViewById(R.id.episode_upload_date);
            size = itemView.findViewById(R.id.episode_size);
            seeders = itemView.findViewById(R.id.episode_seeders);
            leechers = itemView.findViewById(R.id.episode_leechers);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }
        public TextView getUpload_name(){return upload_name;}

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
    }

    public EpisodeAdapter(ArrayList<ArrayList<String>> episodeList){
        localEpisodeList = episodeList;
    }
    public void setOnItemClickListener(EpisodeAdapter.ClickListener clickListener) {
        EpisodeAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
    public void openMagnet(String url) {
        Uri magnet = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, magnet);
        
    }
}
