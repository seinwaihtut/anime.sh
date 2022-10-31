package com.seinwaihtut.animesh.Airing;

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

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> implements Filterable {
    private List<Anime> seasonNowList;
    private List<Anime> seasonNowListFull;
    private static ClickListener listener;

    private static SeasonAdapter instance = null;

    public static SeasonAdapter getInstance(){
        if (instance == null){
            instance = new SeasonAdapter();
        }
        return instance;
    }


    public void setData(List<Anime> arrayList) {
        this.seasonNowList = arrayList;
        this.seasonNowListFull = new ArrayList<>(arrayList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeasonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime_preview_constraint_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonAdapter.ViewHolder holder, int position) {
        if (seasonNowList.size() != 0) {
            ImageView imageView = holder.getPoster();
            Glide.with(imageView).load(seasonNowList.get(position).getImage_url()).placeholder(R.drawable.placeholder).override(337, 477).into(imageView);
            holder.getTitle().setText(seasonNowList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (seasonNowList == null) {
            return 0;
        } else return seasonNowList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Anime> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(seasonNowListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Anime anime : seasonNowListFull) {
                    if (anime.getTitle().toLowerCase().trim().contains(filterPattern)) {
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
            seasonNowList.clear();
            seasonNowList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

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
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(seasonNowList.get(position));
                    }
                }
            });
        }

        public ImageView getPoster() {
            return poster;
        }

        public TextView getTitle() {
            return title;
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Anime anime);
    }


}
