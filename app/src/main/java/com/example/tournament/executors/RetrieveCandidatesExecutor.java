package com.example.tournament.executors;

import android.os.Handler;
import android.os.Looper;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.interfaces.TypeFetchedCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetrieveCandidatesExecutor {
    private ExecutorService mExecutorService;
    private Handler handler;

    FirebaseFirestore db;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    List<Candidate> candidates = new ArrayList<>();

    public RetrieveCandidatesExecutor() {
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.db = FirebaseFirestore.getInstance();
    }

    public void getCandidates(TypeFetchedCallback callback, String type){
        mExecutorService.execute(() -> {
            try {
                // Get a reference to the users collection.
                CollectionReference mCollection = db.collection(type);

                // Get a Task object for the query.
                Task<QuerySnapshot> mDocumentTask = mCollection.get();

                // Add a listener to the Task object that will be called when the task is complete and the document is successfully retrieved.
                mDocumentTask.addOnSuccessListener(gamesDocumentSnapshot -> {
                    // Get the data from the document snapshot.
                    for (DocumentSnapshot document : gamesDocumentSnapshot.getDocuments()) {
                        String candidateName = document.getString("name");
                        String candidateImage = document.getString("image");
                        int candidateCount = document.getLong("count").intValue();
                        Candidate candidate = new Candidate(candidateName, candidateImage, type);
                        candidate.setCount(candidateCount);
                        candidates.add(candidate);
                    }
                    callback.onCandidatesFetched(candidates, type);
                    candidates.clear();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown(){
        mExecutorService.shutdown();
    }
}
