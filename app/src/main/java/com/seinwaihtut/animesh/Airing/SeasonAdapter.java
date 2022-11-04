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
    private List<Anime> seasonNowList = new ArrayList<>();
    private List<Anime> seasonNowListFull = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_season, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonAdapter.ViewHolder holder, int position) {
        if (seasonNowList.size() != 0) {
            Anime anime = seasonNowList.get(position);

            ImageView imageView = holder.getPoster();
            Glide.with(imageView).load(anime.getImage_url()).placeholder(R.drawable.placeholder).into(imageView);
            holder.getTitle().setText(anime.getTitle());
            if(anime.getSeason().isEmpty()||anime.getYear().equals(0)){

            }else{
                holder.getSeason().setText("Season: "+anime.getSeason()+" "+anime.getYear().toString());
            }

            holder.getScore().setText("Score: "+anime.getScore().toString());
            holder.getGenres().setText("Genres: "+anime.getGenres());

            holder.getSynopsis().setText("Synopsis: "+seasonNowList.get(position).getSynopsis());
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
        private TextView season;
        private TextView score;
        private TextView genres;
        private TextView synopsis;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            poster = itemView.findViewById(R.id.iv_item_preview_season_image);
            title = itemView.findViewById(R.id.tv_item_preview_season_title);
            score = itemView.findViewById(R.id.tv_item_preview_season_score);
            synopsis = itemView.findViewById(R.id.tv_item_preview_season_synopsis);
            season = itemView.findViewById(R.id.tv_preview_item_season_season_year);
            genres = itemView.findViewById(R.id.tv_item_preview_season_genres);



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

        public TextView getScore(){
            return score;
        }

        public TextView getSynopsis(){
            return synopsis;
        }

        public TextView getSeason() {
            return season;
        }

        public TextView getGenres() {
            return genres;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Anime anime);
    }


}
