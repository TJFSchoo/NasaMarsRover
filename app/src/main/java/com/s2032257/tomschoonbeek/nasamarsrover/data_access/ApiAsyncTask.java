package com.s2032257.tomschoonbeek.nasamarsrover.data_access;

import android.os.AsyncTask;
import android.util.Log;

import com.s2032257.tomschoonbeek.nasamarsrover.domain.MarsRoverPhoto;
import com.s2032257.tomschoonbeek.nasamarsrover.presentation.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiAsyncTask extends AsyncTask<String, Void, String> {
    private OnTaskCompleted listener = null;
    private final static String TAG = "ApiAsyncTask";

    // Constructor
    public ApiAsyncTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    // De main asynchrone taak
    @Override
    protected String doInBackground(String... urls) {
        Log.i(TAG, "doInBackground() aangeroepen.");

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            int checkIfConnected = urlConnection.getResponseCode();

            if (checkIfConnected == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "Goede verbinding tijdens uitvoeren van doInBackground().");
                InputStream in = urlConnection.getInputStream();
                result = getInputData(in);
            } else {
                Log.e(TAG, "ERROR: Geen goede verbinding tijdens uitvoeren van doInBackground().");
            }

            Log.i(TAG, "Resultaat: " + result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: doInBackground() gefaald.");
            return null;
        }
    }

    // Wordt uitgevoerd na doInBackground()
    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, "onPostExecute() aangeroepen.");
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("photos");

            // Limiet ingesteld op maximaal 50 foto's
            int lengthLimit = 50;
            if (jsonArray.length() < lengthLimit) {
                lengthLimit = jsonArray.length();
            }

            for (int i = 0; i < lengthLimit; i++) {
                JSONObject photo = jsonArray.getJSONObject(i);

                int id = photo.getInt("id");
                int sol = photo.getInt("sol");
                String earthDate = photo.getString("earth_date");
                String imageUrl = photo.getString("img_src");

                JSONObject cameraInfo = photo.getJSONObject("camera");
                String cameraName = cameraInfo.getString("name");
                String cameraFullName = cameraInfo.getString("full_name");

                MarsRoverPhoto photoToAdd = new MarsRoverPhoto(id, sol, earthDate, imageUrl, cameraName, cameraFullName);
                listener.onTaskCompleted(photoToAdd);
                Log.i(TAG, "Foto met ID " + photoToAdd.getId() + " succesvol doorgegeven aan MainActivity en aan database toegevoegd. Camera van deze foto: " + photoToAdd.getCameraName() + ".");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: Fout bij het aanmaken van JSONObject in methode onPostExecute().");
        }
    }

    private static String getInputData(InputStream in) {
        Log.i(TAG, "getInputData() aangeroepen.");

        BufferedReader buffer = null;
        StringBuilder stringBuilder = new StringBuilder();
        String temp = "";
        String result = "";

        try {
            buffer = new BufferedReader(new InputStreamReader(in));
            while ((temp = buffer.readLine()) != null) {
                stringBuilder.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: Probleem met ophalen van API data in methode getInputData().");
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        result = stringBuilder.toString();
        return result;

    }
}
