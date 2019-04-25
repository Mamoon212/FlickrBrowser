package com.mo2a.example.flickrjava;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> photosList;
    private Context context;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photosList) {
        this.photosList = photosList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int pos) {
        if(photosList == null || photosList.size() == 0){
            holder.thumbnail.setImageResource(R.drawable.baseline_image_black_48dp);
            holder.title.setText(context.getString(R.string.empty_search_result));
        }else{
            Photo photoItem= photosList.get(pos);
            Log.d(TAG, "onBindViewHolder: "+ photoItem.getTitle() + "-"+ pos);
            Picasso.get().load(photoItem.getImage())
                    .error(R.drawable.baseline_image_black_48dp)
                    .placeholder(R.drawable.baseline_image_black_48dp)
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return ((photosList != null) && (photosList.size() != 0) ? photosList.size() : 1);
    }

    void loadNewData(List<Photo> newPhotos){
        photosList= newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int pos){
        return ((photosList != null) && (photosList.size() != 0) ? photosList.get(pos) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail= null;
        TextView title= null;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: called");
            this.thumbnail= itemView.findViewById(R.id.thumbnail);
            this.title= itemView.findViewById(R.id.title);
        }
    }
}
