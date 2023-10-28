package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView choiceText;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choiceText = findViewById(R.id.choiceText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            choice = extras.getString("choice");
        }
        choiceText.setText(choice);
        switch (choice) {
            case "Dog":
                break;
            case "Movie":
                break;
            case "Game":
                new FetchGamesTask().execute();
                break;
        }
    }

    private class FetchGamesTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                String apiKey = "1c8158864e824bf181081be6d97b1504";
                int totalItems = 64;
                int pageSize = 40;
                int totalPages = (int) Math.ceil((double) totalItems / pageSize);

                List<String> games = new ArrayList<>();

                for (int page = 1; page <= totalPages; page++) {
                    String apiUrl = "https://api.rawg.io/api/games?ordering=popular&page=" + page + "&page_size=" + pageSize + "&key=" + apiKey;

                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the JSON data and extract the names of the games for this page
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray resultsArray = jsonResponse.getJSONArray("results");

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject gameObject = resultsArray.getJSONObject(i);
                        String gameName = gameObject.getString("name");
                        String background_image = gameObject.getString("background_image");
                        games.add("Game Name: " + gameName + "\nBackground Image: " + background_image);
                    }
                }

                return games;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> results) {
            super.onPostExecute(results);
            if (results != null) {
                Log.d("RESULTS", results.size() + " results downloaded");
                for (String result : results) {
                    System.out.println(result);
                }
            } else {
                // Handle the case where the request failed.
            }
        }
    }
}