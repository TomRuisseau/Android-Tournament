package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.executors.RetrieveGamesExecutor;
import com.example.tournament.interfaces.GamesFetchedCallback;
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

public class MainActivity extends AppCompatActivity implements GamesFetchedCallback {
    TextView choiceText;
    String choice;

    RetrieveGamesExecutor retrieveGamesExecutor = new RetrieveGamesExecutor();
    List <Candidate> candidates = new ArrayList<>();

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
                retrieveGamesExecutor.getGames(this::onGamesFetched);

//                new FetchGamesTask().execute();
                break;
        }
    }
    @Override
    public void onGamesFetched(List<Candidate> games) {
        for (Candidate candidate : games) {
            Log.d("RESULTS", candidate.getName() + " " + candidate.getImageUrl());
        }
    }

}