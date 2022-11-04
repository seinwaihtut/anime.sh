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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WatchingAdapter extends RecyclerView.Adapter<WatchingAdapter.ViewHolder> implements Filterable {

    private List<Anime> watchingList = new ArrayList<>();
    private List<Anime> watchingListFull = new ArrayList<>();
    private static ClickListener clickListener;

    private static WatchingAdapter instance = null;

    public static WatchingAdapter getInstance(){
        if (instance == null){
            instance = new WatchingAdapter();
        }
        return instance;
    }

    public Anime getAnimeAtPosition(int position) {
        return watchingList.get(position);
    }


    @NonNull
    @Override
    public WatchingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_watching, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchingAdapter.ViewHolder holder, int position) {
        if (watchingList != null) {
            Anime current = watchingList.get(position);
            ImageView imageView = holder.getPoster();
            Glide.with(imageView).load(current.getImage_url()).placeholder(R.drawable.placeholder).into(imageView);
            holder.getTitle().setText(current.getTitle());
            holder.getAired_string().setText("Aired From: "+ current.getAired_string());
            holder.getType().setText("Type: "+current.getType());
            holder.getSource().setText("Type: "+current.getSource());
            holder.getBroadcast().setText(current.getBroadcast_string());
            holder.getGenres().setText(current.getGenres());


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
        private final ImageView poster;
        private final TextView aired_string;
        private final TextView type;
        private final TextView source;
        private final TextView broadcast;
        private final TextView genres;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.iv_item_preview_watching_image);
            title = itemView.findViewById(R.id.tv_item_preview_watching_title);
            aired_string = itemView.findViewById(R.id.tv_item_preview_watching_aired_string);
            type = itemView.findViewById(R.id.tv_item_preview_watching_type);
            source = itemView.findViewById(R.id.tv_item_preview_watching_source);
            broadcast = itemView.findViewById(R.id.tv_item_preview_watching_broadcast_string);
            genres = itemView.findViewById(R.id.tv_item_preview_watching_genres);
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

        public TextView getTitle() {
            return title;
        }

        public ImageView getPoster() {
            return poster;
        }

        public TextView getAired_string() {
            return aired_string;
        }

        public TextView getType() {
            return type;
        }

        public TextView getSource() {
            return source;
        }

        public TextView getBroadcast() {
            return broadcast;
        }

        public TextView getGenres() {
            return genres;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WatchingAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(Anime anime);
    }
}
