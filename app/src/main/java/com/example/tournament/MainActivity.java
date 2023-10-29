package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.executors.RetrieveGamesExecutor;
import com.example.tournament.interfaces.CandidatesFetchedCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CandidatesFetchedCallback {
    String choice;
    TextView choiceText;
    ProgressBar progressBar;

    ImageView imageViewTop, imageViewBottom;

    TextView textViewTop, textViewBottom;

    enum position {
        TOP,
        BOTTOM
    }

    RetrieveGamesExecutor retrieveGamesExecutor = new RetrieveGamesExecutor();
    List <Candidate> candidates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBarCandidates);
        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewBottom = findViewById(R.id.imageViewBottom);
        textViewTop = findViewById(R.id.textViewTop);
        textViewBottom = findViewById(R.id.textViewBottom);

        imageViewTop.setVisibility(ImageView.INVISIBLE);
        imageViewBottom.setVisibility(ImageView.INVISIBLE);
        textViewTop.setVisibility(TextView.INVISIBLE);
        textViewBottom.setVisibility(TextView.INVISIBLE);

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
        this.candidates = candidates;
        for (Candidate candidate : this.candidates) {
            Log.d("RESULTS", candidate.getName() + " " + candidate.getImageUrl());
        }
        retrieveGamesExecutor.shutdown();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        startTournament();
    }

    private void startTournament() {
        imageViewTop.setVisibility(ImageView.VISIBLE);
        imageViewBottom.setVisibility(ImageView.VISIBLE);
        textViewTop.setVisibility(TextView.VISIBLE);
        textViewBottom.setVisibility(TextView.VISIBLE);


        textViewTop.setText(candidates.get(0).getName());
        textViewBottom.setText(candidates.get(1).getName());

        insertImage(position.TOP, candidates.get(0).getImageUrl());
        insertImage(position.BOTTOM, candidates.get(1).getImageUrl());
    }

    private void insertImage(position pos, String url) {
        switch (choice){
            case "Movie":
                Picasso.get()
                        .load(url)
                        .resize(2000, 3000)
                        .centerCrop()
                        .into(pos == position.TOP ? imageViewTop : imageViewBottom);
                break;
            case "Game":
                Picasso.get()
                        .load(url)
                        .resize(1920, 1080)
                        .centerCrop()
                        .into(pos == position.TOP ? imageViewTop : imageViewBottom);
                break;

        }
    }
}

