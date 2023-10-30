package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.interfaces.FavoritesFetchedCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Favorites extends AppCompatActivity implements FavoritesFetchedCallback {

    TextView textViewShow, textViewMovie, textViewGame;
    ImageView imageViewShow, imageViewMovie, imageViewGame;

    FirebaseFirestore db;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Initialize variables
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        textViewShow = findViewById(R.id.textViewShow);
        textViewMovie = findViewById(R.id.textViewMovie);
        textViewGame = findViewById(R.id.textViewGame);
        imageViewShow = findViewById(R.id.imageViewShow);
        imageViewMovie = findViewById(R.id.imageViewMovie);
        imageViewGame = findViewById(R.id.imageViewGame);
    }

    @Override
    public void onFavoritesFetched(List<Candidate> favorites) {
        for (Candidate candidate : favorites) {
            Log.d("FAVORITES", candidate.getName());
        }
    }
}