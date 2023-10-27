package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private class FetchGamesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                String apiKey = "1c8158864e824bf181081be6d97b1504";
                int pageSize = 64;  // The number of popular games you want to retrieve
                String apiUrl = "https://api.rawg.io/api/games?ordering=popular&page_size=" + pageSize + "&key=" + apiKey;

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

                // Now, 'response.toString()' contains the JSON data with the top 64 popular games.
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // Handle the result here, such as updating UI elements with the retrieved data.
                System.out.println(result);
            } else {
                // Handle the case where the request failed.
            }
        }
    }
}