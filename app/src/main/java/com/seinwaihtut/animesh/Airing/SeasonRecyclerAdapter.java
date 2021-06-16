package com.seinwaihtut.animesh.Airing;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.R;

import java.util.ArrayList;

public class SeasonRecyclerAdapter extends RecyclerView.Adapter<SeasonRecyclerAdapter.ViewHolder> {
    private ArrayList<Anime> localAnimeObjectList = new ArrayList<>();
    private static ClickListener clickListener;

    @NonNull
    @Override
    public SeasonRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_preview_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonRecyclerAdapter.ViewHolder holder, int position) {
        if (localAnimeObjectList.size() != 0) {
            ImageView imageView = holder.getPoster();
            Glide.with(imageView).load(localAnimeObjectList.get(position).getImage_url()).placeholder(R.drawable.placeholder).override(337, 477).into(imageView);
            holder.getTitle().setText(localAnimeObjectList.get(position).getTitle());
            holder.getDate_time().setText(localAnimeObjectList.get(position).getAiring_start());
            holder.getScore().setText("Score : "+localAnimeObjectList.get(position).getScore());
        }
    }

    @Override
    public int getItemCount() {
        Log.i("ra", Integer.toString(localAnimeObjectList.size()));
        if (localAnimeObjectList == null) {
            return 0;
        } else return localAnimeObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView title;
        private TextView date_time;
        private TextView score;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            poster = itemView.findViewById(R.id.anime_preview_poster);
            title = itemView.findViewById(R.id.anime_preview_title);
            date_time = itemView.findViewById(R.id.anime_preview_day_time);
            score = itemView.findViewById(R.id.anime_preview_score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        public ImageView getPoster() {
            return poster;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDate_time() {
            return date_time;
        }

        public TextView getScore() {
            return score;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SeasonRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    public SeasonRecyclerAdapter(ArrayList<Anime> animeList) {
        localAnimeObjectList = animeList;
    }

}
