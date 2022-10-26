package com.seinwaihtut.animesh.Watching;

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

import java.util.List;

public class WatchingAdapter extends RecyclerView.Adapter<WatchingAdapter.ViewHolder> {

    private List<Anime> mAnimes;
    private static ClickListener clickListener;

    public Anime getAnimeAtPosition(int position) {
        return mAnimes.get(position);
    }

    @NonNull
    @Override
    public WatchingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_preview_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchingAdapter.ViewHolder holder, int position) {
        if (mAnimes != null) {
            Anime current = mAnimes.get(position);

            holder.title.setText(current.getTitle());



            ImageView imageView = holder.poster;
            Glide.with(imageView).load(current.getImage_url()).placeholder(R.drawable.placeholder).override(337, 477).into(imageView);

        } else {
            holder.title.setText("id");
            holder.title.setText("No anime");
        }
    }

    void setAnimes(List<Anime> animes) {
        mAnimes = animes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mAnimes != null) {
            return mAnimes.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView score;
        private final TextView day_time;
        private final ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.anime_preview_score);
            title = itemView.findViewById(R.id.anime_preview_title);
            day_time = itemView.findViewById(R.id.anime_preview_day_time);
            poster = itemView.findViewById(R.id.anime_preview_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WatchingAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}
