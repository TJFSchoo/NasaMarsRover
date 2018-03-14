package com.s2032257.tomschoonbeek.nasamarsrover.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.s2032257.tomschoonbeek.nasamarsrover.R;
import com.s2032257.tomschoonbeek.nasamarsrover.application_logic.OnItemClickListener;
import com.s2032257.tomschoonbeek.nasamarsrover.application_logic.PhotoAdapter;
import com.s2032257.tomschoonbeek.nasamarsrover.data_access.ApiAsyncTask;
import com.s2032257.tomschoonbeek.nasamarsrover.data_access.OnTaskCompleted;
import com.s2032257.tomschoonbeek.nasamarsrover.data_access.local_database.PhotoDatabase;
import com.s2032257.tomschoonbeek.nasamarsrover.domain.MarsRoverPhoto;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, OnTaskCompleted {

    // Final waardes
    private final static String TAG = "MainActivity";
    private final static String API_KEY = "YuFTWcDAd6vmTI62dHn5NNYIosgdsdL7ZXZ3qCiL";
        // De regel hieronder non-private in verband met call uit DetailActivity (Intent)
    final static String CLICKED_PHOTO = "clickedPhoto";

    private RecyclerView recyclerView;
    private Spinner cameraSelection;

        // De drie regels hieronder alvast geïnitieerd ivm gebruik in Spinner methodes
    private ArrayList<MarsRoverPhoto> photoList = new ArrayList<MarsRoverPhoto>();
    private PhotoAdapter photoAdapter = new PhotoAdapter(MainActivity.this, photoList);
    private PhotoDatabase database = new PhotoDatabase(this);
    private String currentSpinnerSelection = "All cameras";

        // De regel hieronder voor onCreate() geïnitieerd ivm performance. Onnodig initiëren bij wisselen tussen MainActivity en DetailActivity voorkomen
    private ApiAsyncTask asyncTask = new ApiAsyncTask(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() aangeroepen.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner voor camera selectie (Bonuspunten)
        cameraSelection = (Spinner) findViewById(R.id.cameraSelectionSpinner);
        String[] cameraOptions = new String[]{"All cameras", "FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM"};

        // Adapter voor Spinner met mogelijke camera's
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cameraOptions);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        cameraSelection.setAdapter(spinnerAdapter);

        // Lijst van reeds gedownloade foto's inladen bij opstarten van de applicatie
        photoList.clear();
        photoList.addAll(database.getAllPhotos());

        // Toevoegen van functionaliteit van selectie Spinner
        cameraSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // Bij selectie van Spinner-item
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "In Spinner selectie " + parent.getItemAtPosition(position) + " gekozen.");
                currentSpinnerSelection = "" + parent.getItemAtPosition(position);

                photoList.clear();
                if(currentSpinnerSelection.equals("All cameras")){
                    photoList.addAll(database.getAllPhotos());
                } else {
                    photoList.addAll(database.getAllPhotosOfSpecificCamera(currentSpinnerSelection));
                }
                // Aantal foto's als toast
                Toast.makeText(MainActivity.this,photoList.size() + " photographs found.",Toast.LENGTH_SHORT).show();
                photoAdapter.notifyDataSetChanged();
            }

            // Bij geen selectie (normaalgesproken onmogelijk maar toch vereist ivm AdapterView.OnItemSelectedListener())
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "Geen selectie in Spinner gevonden.");
            }
        });

        // Randomizer toegepast op selectie van welke sol (Mars-dag) foto's opgehaald moeten worden
        Random random = new Random();
        Log.i(TAG, "Geselecteerde camera: " + currentSpinnerSelection);
        String addToQuery = "&camera=" + currentSpinnerSelection + "&";
        String url = "";

        if(currentSpinnerSelection.equals("All cameras")){
            url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + random.nextInt(1000)+ "?page=1&api_key=" + API_KEY;
        } else {
            url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + random.nextInt(1000) + addToQuery + "?page=1&api_key=" + API_KEY;
        }

        Log.i(TAG, url);
        String[] urls = {url};

        // RecyclerView met lijst van foto's die opgehaald worden uit lokale database
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        photoList.addAll(database.getAllPhotos());

        // Starten van de asynchrone taak
        try {
            asyncTask.execute(urls);
            Log.i(TAG, "Asynchrone taak gestart.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: Kan ApiAsyncTask.execute() niet uitvoeren vanuit MainActivity.");
        }
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(MainActivity.this);
    }

    @Override
    public void onItemClick(int position) {
        Log.i(TAG, "onItemClick() aangeroepen.");
        Intent detailIntent = new Intent(this, DetailActivity.class);
        MarsRoverPhoto clickedPhoto = photoList.get(position);
        detailIntent.putExtra(CLICKED_PHOTO, clickedPhoto);
        startActivity(detailIntent);
        Log.i(TAG, "DetailActivity gestart.");
    }

    public void onTaskCompleted(MarsRoverPhoto photo) {
        Log.i(TAG, "onTaskCompleted() aangeroepen vanuit asynchrone taak.");
        database.addPhoto(photo);
        photoList.add(photo);
        Log.i(TAG, "Foto met ID " + photo.getId() + " toegevoegd aan database.");
        photoAdapter.notifyDataSetChanged();
    }
}
