package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.executors.RetrieveGamesExecutor;
import com.example.tournament.executors.RetrieveMoviesExecutor;
import com.example.tournament.executors.RetrieveShowsExecutor;
import com.example.tournament.interfaces.CandidatesFetchedCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CandidatesFetchedCallback {
    String choice;
    ProgressBar progressBar;

    Button buttonChoices;
    ImageView imageViewTop, imageViewBottom;

    TextView textViewTop, textViewBottom;

    TextView textViewRound;

    enum position {
        TOP,
        BOTTOM
    }

    RetrieveGamesExecutor retrieveGamesExecutor = new RetrieveGamesExecutor();
    RetrieveMoviesExecutor retrieveMoviesExecutor = new RetrieveMoviesExecutor();

    RetrieveShowsExecutor retrieveShowsExecutor = new RetrieveShowsExecutor();

    List <Candidate> candidates = new ArrayList<>();
    List <Candidate> nextCandidates = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    int currentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize variables
        progressBar = findViewById(R.id.progressBarCandidates);
        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewBottom = findViewById(R.id.imageViewBottom);
        textViewTop = findViewById(R.id.textViewTop);
        textViewBottom = findViewById(R.id.textViewBottom);
        textViewRound = findViewById(R.id.textViewRound);
        mAuth = FirebaseAuth.getInstance();

        //TODO: make these invisible from xml not here
        imageViewTop.setVisibility(ImageView.INVISIBLE);
        imageViewBottom.setVisibility(ImageView.INVISIBLE);
        textViewTop.setVisibility(TextView.INVISIBLE);
        textViewBottom.setVisibility(TextView.INVISIBLE);
        textViewRound.setVisibility(TextView.INVISIBLE);

        imageViewBottom.setOnClickListener(this::ClickCandidate);
        imageViewTop.setOnClickListener(this::ClickCandidate);

        buttonChoices = findViewById(R.id.buttonChoices);
        buttonChoices.setOnClickListener(view -> {
            GoToMenu();
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            choice = extras.getString("choice");
        }
        switch (choice) {
            case "TV Show":
                retrieveShowsExecutor.getShows(this::onCandidatesFetched);
                break;
            case "Movie":
                retrieveMoviesExecutor.getMovies(this::onCandidatesFetched);
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
        retrieveMoviesExecutor.shutdown();
        retrieveShowsExecutor.shutdown();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        startTournament();
    }

    private void startTournament() {
        imageViewTop.setVisibility(ImageView.VISIBLE);
        imageViewBottom.setVisibility(ImageView.VISIBLE);
        textViewTop.setVisibility(TextView.VISIBLE);
        textViewBottom.setVisibility(TextView.VISIBLE);
        textViewRound.setVisibility(TextView.VISIBLE);

        nextCandidates.addAll(candidates);
        NextRound();
    }

    private void NextRound() {
        candidates.clear();
        candidates.addAll(nextCandidates);
        nextCandidates.clear();
        String text = getString(R.string.round_of) + candidates.size();
        textViewRound.setText(text);
        currentId = -1; //because we increment it at the beginning of NextDuel()

        if (candidates.size() == 1) {
            declareWinner();
            return;
        }
        NextDuel();
    }

    private void NextDuel(){
        int candidateCount = candidates.size();

        currentId++;
        if (currentId >= candidateCount / 2) { //if we reached the end of the list
            NextRound();
            return;
        }
        //get next candidates in the list
        textViewTop.setText(candidates.get(currentId).getName());
        insertImage(position.TOP, candidates.get(currentId).getImageUrl());

        //get the symmetrical candidate from the end of the list
        textViewBottom.setText(candidates.get(candidateCount - 1 - currentId).getName());
        insertImage(position.BOTTOM, candidates.get(candidateCount - 1 - currentId).getImageUrl());
    }

    private void ClickCandidate(View view) {
        if (view.getId() == R.id.imageViewTop){
            nextCandidates.add(candidates.get(currentId));
        }
        else{
            nextCandidates.add(candidates.get(candidates.size() - 1 - currentId));
        }
        NextDuel();
    }

    private void insertImage(position pos, String url) {
        switch (choice){
            case "Movie":
            case "TV Show":
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

    private void declareWinner() {
        imageViewTop.setOnClickListener(null); //disable click listeners
        Candidate winner = candidates.get(0);
        textViewTop.setText(winner.getName());
        insertImage(position.TOP, winner.getImageUrl());
        String favorite = "Here is your favorite " + choice + " !";
        textViewRound.setText(favorite);
        textViewBottom.setVisibility(TextView.INVISIBLE);
        imageViewBottom.setVisibility(ImageView.INVISIBLE);


        //add to database
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Store winners info
        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put(choice + "_name", winner.getName());
        favoriteData.put(choice + "_image", winner.getImageUrl());

        assert currentUser != null;
        String userId = currentUser.getUid();
        // Add a new document with a generated ID
        // Check if the document exists before updating
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        // Document exists, update specific fields
                        db.collection("users").document(userId).update(favoriteData);
                    } else {
                        // Document does not exist, create a new document
                        db.collection("users").document(userId).set(favoriteData);
                    }
                });


        // Store winners info in the games/movies/shows collection
        Map<String, Object> winnerData = new HashMap<>();
        winnerData.put("name", winner.getName());
        winnerData.put("image", winner.getImageUrl());
        String collectionName = choice.toLowerCase() + "s";
        db.collection(collectionName).document(winner.getName()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        // Document exists, update specific fields
                        db.collection(collectionName).document(winner.getName()).update(winnerData);
                        db.collection(collectionName).document(winner.getName()).update("count", FieldValue.increment(1));
                    } else {
                        // Document does not exist, create a new document
                        winnerData.put("count", 1);
                        db.collection(collectionName).document(winner.getName()).set(winnerData);
                    }
                });
    }

    private void GoToMenu() {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
        finish();
    }
}

