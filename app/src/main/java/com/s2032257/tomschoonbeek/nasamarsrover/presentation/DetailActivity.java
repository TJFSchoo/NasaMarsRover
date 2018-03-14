package com.s2032257.tomschoonbeek.nasamarsrover.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.s2032257.tomschoonbeek.nasamarsrover.R;

import com.s2032257.tomschoonbeek.nasamarsrover.domain.MarsRoverPhoto;
import com.squareup.picasso.Picasso;

import static com.s2032257.tomschoonbeek.nasamarsrover.presentation.MainActivity.CLICKED_PHOTO;

public class DetailActivity extends AppCompatActivity {
    private final static String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Gestart vanuit MainActivity.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Doorgegeven data uit MainActivity
        Intent intent = getIntent();
        MarsRoverPhoto clickedPhoto = (MarsRoverPhoto) intent.getSerializableExtra(CLICKED_PHOTO);
        Log.i(TAG,"Foto met ID " + clickedPhoto.getId() + " opgehaald uit doorgegeven Intent.");

        String imageUrl = clickedPhoto.getImageUrl();
        String camera = clickedPhoto.getCameraFullName();

        ImageView detailedImageView = findViewById(R.id.detailedImageView);
        TextView detailedTextView = findViewById(R.id.detailedTextView);

        // Het omzetten van de imageUrl naar daadwerkelijke afbeelding
        Picasso.with(this).load(imageUrl).fit().centerInside().into(detailedImageView);

        // Invullen van camera-naam
        detailedTextView.setText(camera);
    }
}
