package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.executors.RetrieveCandidatesExecutor;
import com.example.tournament.interfaces.CandidatesFetchedCallback;
import com.example.tournament.interfaces.TypeFetchedCallback;
import com.example.tournament.recyclers.CandidatesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Candidates extends AppCompatActivity implements TypeFetchedCallback {

    List <Candidate> games;
    List <Candidate> movies;
    List <Candidate> shows;

    List <Candidate> current;

    RecyclerView candidatesRecyclerView;

    Button btnCandidatesGames, btnCandidatesMovies, btnCandidatesShows;

    CandidatesRecyclerViewAdapter candidatesRecyclerViewAdapter;
    int countTypeFetched = 0;
    String type;
    RetrieveCandidatesExecutor retrieveCandidatesExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);

        //Initialize the lists
        games = new ArrayList<>();
        movies = new ArrayList<>();
        shows = new ArrayList<>();
        current = new ArrayList<>();

        //Retrieve the views and buttons
        candidatesRecyclerView = findViewById(R.id.candidatesRecyclerView);
        btnCandidatesGames = findViewById(R.id.btnCandidatesGames);
        btnCandidatesMovies = findViewById(R.id.btnCandidatesMovies);
        btnCandidatesShows = findViewById(R.id.btnCandidatesShows);

        //Set the buttons listeners
        btnCandidatesGames.setOnClickListener(v -> setGamesRecyclerView());
        btnCandidatesMovies.setOnClickListener(v -> setMoviesRecyclerView());
        btnCandidatesShows.setOnClickListener(v -> setShowsRecyclerView());

        //Setup the recycler view (empty at first)
        candidatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        candidatesRecyclerViewAdapter = new CandidatesRecyclerViewAdapter(this, current);
        candidatesRecyclerView.setAdapter(candidatesRecyclerViewAdapter);

        //Retrieve all the candidates
        retrieveCandidatesExecutor = new RetrieveCandidatesExecutor();
        retrieveCandidatesExecutor.getCandidates(this, "games");
        retrieveCandidatesExecutor.getCandidates(this, "movies");
        retrieveCandidatesExecutor.getCandidates(this, "tv shows");
    }

    private void setGamesRecyclerView(){
        if (type != "games"){
            type = "games";
            current.clear();
            current.addAll(games);
            candidatesRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    private void setMoviesRecyclerView(){
        if (type != "movies"){
            type = "movies";
            current.clear();
            current.addAll(movies);
            candidatesRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void setShowsRecyclerView(){
        if (type != "tv shows"){
            type = "tv shows";
            current.clear();
            current.addAll(shows);
            candidatesRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCandidatesFetched(List<Candidate> candidates, String type) {
        countTypeFetched++;
        switch (type){
            case "games":
                games.addAll(candidates);
                break;
            case "movies":
                movies.addAll(candidates);
                break;
            case "tv shows":
                shows.addAll(candidates);
                break;
        }
        if(countTypeFetched == 3){
            retrieveCandidatesExecutor.shutdown();
        }
    }

}