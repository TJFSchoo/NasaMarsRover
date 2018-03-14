package com.s2032257.tomschoonbeek.nasamarsrover.data_access.local_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.s2032257.tomschoonbeek.nasamarsrover.domain.MarsRoverPhoto;

import java.util.ArrayList;

public class PhotoDatabase extends SQLiteOpenHelper{
    private final static String TAG = "PhotoDatabase";

    // Database info variabelen
    private final static String DATABASE_NAME = "Photo.db";
    private final static String TABLE_NAME = "Photo";
    private final static int DATABASE_VERSION = 1;

    // Tabel "Photo" content variabelen
    private final static String ID = "id";
    private final static String SOL = "sol";
    private final static String EARTH_DATE = "earthDate";
    private final static String IMAGE_URL = "imageUrl";
    private final static String CAMERA_NAME = "cameraName";
    private final static String CAMERA_FULL_NAME = "cameraFullName";

    // Constructor maakt gebruik van final waarden
    public PhotoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG,"onCreate() aangeroepen.");

        // Maakt nieuwe database tabel met onderstaande gegevens
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                " (" +
                ID +                " INTEGER PRIMARY KEY," +
                SOL +               " INTEGER NOT NULL," +
                EARTH_DATE +        " TEXT NOT NULL," +
                IMAGE_URL +         " TEXT NOT NULL," +
                CAMERA_NAME +       " TEXT NOT NULL," +
                CAMERA_FULL_NAME +  " TEXT NOT NULL);"
        );

        Log.i(TAG,"Database " + DATABASE_NAME + " met daarin tabel " + TABLE_NAME + " aangemaakt.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG,"onUpgrade() aangeroepen.");

        // Dropt oude database tabel
        String query = "DROP TABLE " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(query,null);

        // Maakt nieuwe database tabel
        this.onCreate(sqLiteDatabase);
    }

    public void addPhoto(MarsRoverPhoto photo){
        Log.i(TAG,"addPhoto() aangeroepen voor foto met ID " + photo.getId() + ".");

        // Waardes toevoegen aan tabel
        ContentValues toBeAdded = new ContentValues();
        toBeAdded.put(ID, photo.getId());
        toBeAdded.put(SOL, photo.getSol());
        toBeAdded.put(EARTH_DATE, photo.getEarthDate());
        toBeAdded.put(IMAGE_URL, photo.getImageUrl());
        toBeAdded.put(CAMERA_NAME, photo.getCameraName());
        toBeAdded.put(CAMERA_FULL_NAME, photo.getCameraFullName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, toBeAdded);
        Log.i(TAG,"Foto succesvol toegevoegd aan database.");
        db.close();
    }

    public ArrayList<MarsRoverPhoto> getAllPhotos(){
        Log.i(TAG,"getAllPhotos() aangeroepen.");
        ArrayList<MarsRoverPhoto> photos = new ArrayList<>();

        // Eerste stap van het lezen van de personen uit de database: Query uitvoeren en cursor aanmaken en op juiste positie klaarzetten
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        // Tweede stap van het lezen: Stapsgewijs door alle content heen met de cursor
        while(!cursor.isAfterLast()){

            // Lezen van gegevens van elke foto
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            int sol = cursor.getInt(cursor.getColumnIndex(SOL));
            String earthDate = cursor.getString(cursor.getColumnIndex(EARTH_DATE));
            String imageUrl = cursor.getString(cursor.getColumnIndex(IMAGE_URL));
            String cameraName = cursor.getString(cursor.getColumnIndex(CAMERA_NAME));
            String cameraFullName = cursor.getString(cursor.getColumnIndex(CAMERA_FULL_NAME));

            // Omzetten van gelezen gegevens naar MarsRoverPhoto instantie
            MarsRoverPhoto photoToAdd = new MarsRoverPhoto(id,sol,earthDate,imageUrl,cameraName,cameraFullName);

            // Toevoegen aan lijst van terug te geven fotos
            Log.i(TAG,"Foto met ID " + photoToAdd.getId() + " gevonden in lokale opslag en doorgegeven aan MainActivity.");
            photos.add(photoToAdd);

            // Naar volgende foto
            cursor.moveToNext();
        }
        Log.i(TAG,"Aantal terug te geven foto's: " + photos.size());

        return photos;
    }

    public ArrayList<MarsRoverPhoto> getAllPhotosOfSpecificCamera(String spinnerSelection){

        Log.i(TAG,"getAllPhotosOfSpecificCamera() aangeroepen. Geselecteerde camera: " + spinnerSelection);
        ArrayList<MarsRoverPhoto> photos = new ArrayList<>();

        // Eerste stap van het lezen van de personen uit de database: Query uitvoeren en cursor aanmaken en op juiste positie klaarzetten
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CAMERA_NAME + "='" + spinnerSelection + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        // Tweede stap van het lezen: Stapsgewijs door alle content heen met de cursor
        while(!cursor.isAfterLast()){

            // Lezen van gegevens van elke foto
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            int sol = cursor.getInt(cursor.getColumnIndex(SOL));
            String earthDate = cursor.getString(cursor.getColumnIndex(EARTH_DATE));
            String imageUrl = cursor.getString(cursor.getColumnIndex(IMAGE_URL));
            String cameraName = cursor.getString(cursor.getColumnIndex(CAMERA_NAME));
            String cameraFullName = cursor.getString(cursor.getColumnIndex(CAMERA_FULL_NAME));

            // Omzetten van gelezen gegevens naar MarsRoverPhoto instantie
            MarsRoverPhoto photoToAdd = new MarsRoverPhoto(id,sol,earthDate,imageUrl,cameraName,cameraFullName);

            // Toevoegen aan lijst van terug te geven fotos
            Log.i(TAG,"Tijdens zoeken op camera naam " + spinnerSelection + ", foto met ID " + photoToAdd.getId() + " gevonden in lokale opslag en doorgegeven aan MainActivity.");
            photos.add(photoToAdd);

            // Naar volgende foto
            cursor.moveToNext();
        }
        Log.i(TAG,"Aantal terug te geven foto's voor camera " + spinnerSelection + ": " + photos.size());

        return photos;
    }
}
