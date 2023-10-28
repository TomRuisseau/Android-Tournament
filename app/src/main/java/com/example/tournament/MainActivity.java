package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.executors.RetrieveGamesExecutor;
import com.example.tournament.interfaces.CandidatesFetchedCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CandidatesFetchedCallback {
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
                retrieveGamesExecutor.getGames(this::onCandidatesFetched);
                break;
        }
    }
    @Override
    public void onCandidatesFetched(List<Candidate> candidates) {
        for (Candidate candidate : candidates) {
            Log.d("RESULTS", candidate.getName() + " " + candidate.getImageUrl());
        }
        retrieveGamesExecutor.shutdown();
    }

}