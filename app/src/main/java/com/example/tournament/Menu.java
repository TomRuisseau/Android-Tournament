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
    TextView textView;
    FirebaseUser user;

    Button dogBtn;
    Button movieBtn;
    Button gameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Initialize variables

        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logout);
        textView = findViewById(R.id.choiceText);
        user = auth.getCurrentUser();
        dogBtn = findViewById(R.id.btn_dog);
        movieBtn = findViewById(R.id.btn_movie);
        gameBtn = findViewById(R.id.btn_game);

        //if no user connected, go back to login activity
        if(user == null){
            Intent intent= new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail());
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

        dogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseActivity("Dog");
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

    }

    private void chooseActivity(String activity){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("choice", activity);
        startActivity(intent);
    }
}