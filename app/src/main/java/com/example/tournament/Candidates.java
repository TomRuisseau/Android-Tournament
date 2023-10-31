package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.executors.RetrieveCandidatesExecutor;
import com.example.tournament.interfaces.CandidatesFetchedCallback;
import com.example.tournament.interfaces.TypeFetchedCallback;

import java.util.ArrayList;
import java.util.List;

public class Candidates extends AppCompatActivity implements TypeFetchedCallback {

    List <Candidate> games;
    List <Candidate> movies;
    List <Candidate> shows;

    RetrieveCandidatesExecutor retrieveCandidatesExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);

        games = new ArrayList<>();
        movies = new ArrayList<>();
        shows = new ArrayList<>();
        retrieveCandidatesExecutor = new RetrieveCandidatesExecutor();
        retrieveCandidatesExecutor.getCandidates(this, "games");
        retrieveCandidatesExecutor.getCandidates(this, "movies");
        retrieveCandidatesExecutor.getCandidates(this, "tv shows");
    }




    @Override
    public void onCandidatesFetched(List<Candidate> candidates, String type) {
        switch (type){
            case "games":
                games = candidates;
                break;
            case "movies":
                movies = candidates;
                break;
            case "tv shows":
                shows = candidates;
                break;
        }
    }
}