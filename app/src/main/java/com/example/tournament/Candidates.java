package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

    CandidatesRecyclerViewAdapter candidatesRecyclerViewAdapter;
    int countTypeFetched = 0;
    RetrieveCandidatesExecutor retrieveCandidatesExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);

        games = new ArrayList<>();
        movies = new ArrayList<>();
        shows = new ArrayList<>();
        current = new ArrayList<>();

        candidatesRecyclerView = findViewById(R.id.candidatesRecyclerView);
        candidatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        candidatesRecyclerViewAdapter = new CandidatesRecyclerViewAdapter(this, current);
        candidatesRecyclerView.setAdapter(candidatesRecyclerViewAdapter);


        retrieveCandidatesExecutor = new RetrieveCandidatesExecutor();
        retrieveCandidatesExecutor.getCandidates(this, "games");
        retrieveCandidatesExecutor.getCandidates(this, "movies");
        retrieveCandidatesExecutor.getCandidates(this, "tv shows");
    }

    private void setGamesRecyclerView(){
        //TODO : use only one adapter and change the list
        current.clear();
        current.addAll(games);
        for (Candidate candidate : games){
            Log.d("Set", candidate.getImageUrl());
        }
        candidatesRecyclerViewAdapter.notifyDataSetChanged();
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
            setGamesRecyclerView();
        }
    }

}