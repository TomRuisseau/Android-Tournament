package com.example.tournament.executors;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.interfaces.CandidatesFetchedCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetrieveFavoritesExecutor {
    private ExecutorService mExecutorService;
    private Handler handler;

    FirebaseFirestore db;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    List<Candidate> favorites = new ArrayList<>();

    public RetrieveFavoritesExecutor() {
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.db = FirebaseFirestore.getInstance();
    }

    public void getFavorites(CandidatesFetchedCallback callback){
        mExecutorService.execute(() -> {
            try {
                // Get a reference to the users collection.
                CollectionReference usersCollection = db.collection("users");

                // Get a reference to the current user's document.
                DocumentReference userDocumentReference = usersCollection.document(currentUser.getUid());

                // Get a Task object for the query.
                Task<DocumentSnapshot> userDocumentTask = userDocumentReference.get();

                // Add a listener to the Task object that will be called when the task is complete and the document is successfully retrieved.
                userDocumentTask.addOnSuccessListener(userDocumentSnapshot -> {
                    // Get the data from the document snapshot.
                    String gameName = userDocumentSnapshot.getString("Game_name");
                    String gameImage = userDocumentSnapshot.getString("Game_image");
                    String movieName = userDocumentSnapshot.getString("Movie_name");
                    String movieImage = userDocumentSnapshot.getString("Movie_image");
                    String showName = userDocumentSnapshot.getString("TV Show_name");
                    String showImage = userDocumentSnapshot.getString("TV Show_image");

                    Candidate game = new Candidate(gameName, gameImage, "game");
                    Candidate movie = new Candidate(movieName, movieImage, "movie");
                    Candidate show = new Candidate(showName, showImage, "show");

                    favorites.add(game);
                    favorites.add(movie);
                    favorites.add(show);

                    handler.post(() -> {
                        callback.onCandidatesFetched(favorites);
                    });
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        mExecutorService.shutdown();
    }
}
