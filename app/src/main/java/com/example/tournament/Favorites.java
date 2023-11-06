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
import com.example.tournament.executors.RetrieveFavoritesExecutor;
import com.example.tournament.interfaces.FavoritesFetchedCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Favorites extends AppCompatActivity implements FavoritesFetchedCallback {

    TextView textViewShow, textViewMovie, textViewGame;
    ImageView imageViewShow, imageViewMovie, imageViewGame;

    Button buttonChoicesFavorite;

    ProgressBar progressBarFavorites;

    RetrieveFavoritesExecutor retrieveFavoritesExecutor = new RetrieveFavoritesExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Initialize variables
        textViewShow = findViewById(R.id.textViewShow);
        textViewMovie = findViewById(R.id.textViewMovie);
        textViewGame = findViewById(R.id.textViewGame);
        imageViewShow = findViewById(R.id.imageViewShow);
        imageViewMovie = findViewById(R.id.imageViewMovie);
        imageViewGame = findViewById(R.id.imageViewGame);
        buttonChoicesFavorite = findViewById(R.id.buttonChoicesFavorite);
        progressBarFavorites = findViewById(R.id.progressBarFavorites);

        buttonChoicesFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });


        retrieveFavoritesExecutor.getFavorites(this::onFavoritesFetched);
    }

    @Override
    public void onFavoritesFetched(List<Candidate> favorites) {
        Candidate game = favorites.get(0);
        Candidate movie = favorites.get(1);
        Candidate show = favorites.get(2);
        progressBarFavorites.setVisibility(View.GONE);

        int posterSizeX = 1800;
        int posterSizeY = 2800;


        if (game.getName() != null) {
            textViewGame.setText(game.getName());
            Picasso.get()
                    .load(game.getImageUrl())
                    .resize(1920, 1080)
                    .centerCrop()
                    .into(imageViewGame);
        }
        else{
            textViewGame.setText(R.string.yet_to_be_determined);
        }
        if (movie.getName() != null) {
            textViewMovie.setText(movie.getName());
            Picasso.get()
                    .load(movie.getImageUrl())
                    .resize(posterSizeX, posterSizeY)
                    .centerCrop()
                    .into(imageViewMovie);
        }
        else{
            textViewMovie.setText(R.string.yet_to_be_determined);
        }
        if (show.getName() != null) {
            textViewShow.setText(show.getName());
            Picasso.get()
                    .load(show.getImageUrl())
                    .resize(posterSizeX, posterSizeY)
                    .centerCrop()
                    .into(imageViewShow);
        }
        else{
            textViewShow.setText(R.string.yet_to_be_determined);
        }
    }
}