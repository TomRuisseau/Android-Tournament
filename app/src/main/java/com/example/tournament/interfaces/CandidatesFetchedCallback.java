package com.example.tournament.interfaces;

import com.example.tournament.dataClasses.Candidate;

import java.util.List;

public interface CandidatesFetchedCallback {
    void onCandidatesFetched(List<Candidate> candidates);
}
