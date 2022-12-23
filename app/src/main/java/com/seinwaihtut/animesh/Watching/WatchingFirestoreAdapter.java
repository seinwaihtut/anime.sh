package com.seinwaihtut.animesh.Watching;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.seinwaihtut.animesh.DB.Anime;
import com.seinwaihtut.animesh.R;

public class WatchingFirestoreAdapter extends FirestoreRecyclerAdapter<Anime, WatchingFirestoreAdapter.AnimeHolder> {
    private OnItemClickListener  listener;
    private static final String LOGTAG = "WATCHINGFIRESTOREADAPTER";

    public WatchingFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Anime> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnimeHolder holder, int position, @NonNull Anime model) {
        ImageView imageView = holder.poster;
        Glide.with(imageView).load(model.getImage_url()).placeholder(R.drawable.placeholder).into(imageView);
        holder.title.setText(model.getTitle());
        holder.aired_string.setText("Aired From: "+ model.getAired_string());
        holder.type.setText("Type: "+model.getType());
        holder.source.setText("Type: "+model.getSource());
        holder.broadcast.setText(model.getBroadcast_string());
        holder.genres.setText(model.getGenres());
    }

    @NonNull
    @Override
    public AnimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_preview_watching,
                parent,
                false
        );

        return new AnimeHolder(v);
    }


    class AnimeHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView poster;
        TextView aired_string;
        TextView type;
        TextView source;
        TextView broadcast;
        TextView genres;
        public AnimeHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.iv_item_preview_watching_image);
            title = itemView.findViewById(R.id.tv_item_preview_watching_title);
            aired_string = itemView.findViewById(R.id.tv_item_preview_watching_aired_string);
            type = itemView.findViewById(R.id.tv_item_preview_watching_type);
            source = itemView.findViewById(R.id.tv_item_preview_watching_source);
            broadcast = itemView.findViewById(R.id.tv_item_preview_watching_broadcast_string);
            genres = itemView.findViewById(R.id.tv_item_preview_watching_genres);

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION && listener !=null){
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            }
        });



        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener  listener){
        this.listener = listener;
    }
}
