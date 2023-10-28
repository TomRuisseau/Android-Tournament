package com.example.tournament.executors;

import android.os.Handler;
import android.os.Looper;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.interfaces.GamesFetchedCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetrieveGamesExecutor {
    private ExecutorService mExecutorService;
    private Handler handler;

    public RetrieveGamesExecutor() {
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }


    public  void getGames(GamesFetchedCallback callback){
        mExecutorService.execute(() -> {
            try {
                String apiKey = "1c8158864e824bf181081be6d97b1504";
                int totalItems = 32;

                List<Candidate> games = new ArrayList<>();

                String apiUrl = "https://api.rawg.io/api/games?ordering=popular&page=1&page_size=" + totalItems + "&key=" + apiKey;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON data and extract the names of the games for this page
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray resultsArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject gameObject = resultsArray.getJSONObject(i);
                    String gameName = gameObject.getString("name");
                    String background_image = gameObject.getString("background_image");
                    Candidate game = new Candidate(gameName, background_image);
                    games.add(game);
                }

                handler.post(() -> {
                    callback.onGamesFetched(games);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
