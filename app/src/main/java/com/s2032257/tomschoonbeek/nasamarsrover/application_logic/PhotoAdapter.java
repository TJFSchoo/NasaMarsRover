package com.s2032257.tomschoonbeek.nasamarsrover.application_logic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.s2032257.tomschoonbeek.nasamarsrover.R;
import com.s2032257.tomschoonbeek.nasamarsrover.domain.MarsRoverPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context mCtx;
    private ArrayList<MarsRoverPhoto> photoList;
    private OnItemClickListener listener;
    private final static String TAG = "PhotoAdapter";

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PhotoAdapter(Context mCtx, ArrayList<MarsRoverPhoto> photoList) {
        this.mCtx = mCtx;
        this.photoList = photoList;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG,"onCreateViewHolder() aangeroepen.");
        View v = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_row, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Log.i(TAG,"onBindViewHolder() aangeroepen.");
        MarsRoverPhoto photo = photoList.get(position);
        holder.id.setText("Image ID: " + photo.getId());
        String imageUrl = photo.getImageUrl();
        Picasso.with(mCtx).load(imageUrl).fit().centerInside().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    // Public vanwege implementatie door PhotoAdapter zelf
    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView id;

        private PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rowImageView);
            id = itemView.findViewById(R.id.rowTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
