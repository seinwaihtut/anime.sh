package com.seinwaihtut.animesh.Watching;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.R;

import java.util.ArrayList;
import java.util.List;

public class WatchingAdapter extends RecyclerView.Adapter<WatchingAdapter.ViewHolder> implements Filterable {

    private List<Anime> watchingList;
    private List<Anime> watchingListFull;
    private static ClickListener clickListener;

    public Anime getAnimeAtPosition(int position) {
        return watchingList.get(position);
    }


    @NonNull
    @Override
    public WatchingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_preview_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchingAdapter.ViewHolder holder, int position) {
        if (watchingList != null) {
            Anime current = watchingList.get(position);

            holder.title.setText(current.getTitle());
            Log.i("WatchingAdapter:title", current.getTitle());

            ImageView imageView = holder.poster;
            Glide.with(imageView).load(current.getImage_url()).placeholder(R.drawable.placeholder).override(337, 477).into(imageView);

        } else {
            holder.title.setText("id");
            holder.title.setText("No anime");
        }
    }

    void setAnimes(List<Anime> animes) {
        watchingList = animes;
        watchingListFull = new ArrayList<>(animes);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (watchingList != null) {
            return watchingList.size();
        } else return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Anime> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(watchingListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Anime anime: watchingListFull){
                    if (anime.getTitle().toLowerCase().trim().contains(filterPattern)){
                        filteredList.add(anime);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            watchingList.clear();
            watchingList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

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

                    int position = getAdapterPosition();
                    if (clickListener != null && position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(watchingList.get(position));
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WatchingAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Anime anime);
    }
}
