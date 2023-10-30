package com.example.tournament;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutBtn;
    FirebaseUser user;

    Button showBtn;
    Button movieBtn;
    Button gameBtn;

    Button favoritesBtn;
    Button candidatesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Initialize variables

        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        showBtn = findViewById(R.id.btn_show);
        movieBtn = findViewById(R.id.btn_movie);
        gameBtn = findViewById(R.id.btn_game);
        favoritesBtn = findViewById(R.id.btn_favorites);
        candidatesBtn = findViewById(R.id.btn_candidates);

        //if no user connected, go back to login activity
        if(user == null){
            Intent intent= new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent= new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseActivity("TV Show");
            }
        });

        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseActivity("Movie");
            }
        });

        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseActivity("Game");
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), Favorites.class);
                startActivity(intent);
            }
        });

        candidatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), Candidates.class);
                startActivity(intent);
            }
        });

    }

    private void chooseActivity(String activity){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("choice", activity);
        startActivity(intent);
    }
}