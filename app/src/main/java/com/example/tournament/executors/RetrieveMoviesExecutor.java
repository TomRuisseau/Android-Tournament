package com.example.tournament.executors;

import android.os.Handler;
import android.os.Looper;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.interfaces.CandidatesFetchedCallback;

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

public class RetrieveMoviesExecutor {
    private ExecutorService mExecutorService;
    private Handler handler;

    public RetrieveMoviesExecutor() {
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }


    public void getMovies(CandidatesFetchedCallback callback) {
        mExecutorService.execute(() -> {
            try {
                String apiKey = "aaa5b44cb7ae1451b0040edd47bf5c69";
                int totalItems = 32;
                int itemsPerPage = 20;

                List<Candidate> movies = new ArrayList<>();

                int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

                for (int page = 1; page <= totalPages; page++) {
                    String apiUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + apiKey + "&language=en-US&page=" + page;
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

                    // Parse the JSON data and extract the names of the movies for this page
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray resultsArray = jsonResponse.getJSONArray("results");

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject movieObject = resultsArray.getJSONObject(i);
                        String movieName = movieObject.getString("title");
                        String poster_path = movieObject.getString("poster_path");
                        poster_path = "https://image.tmdb.org/t/p/original" + poster_path;
                        Candidate movie = new Candidate(movieName, poster_path, "movie");
                        movies.add(movie);

                        // Check if we have collected enough items
                        if (movies.size() >= totalItems) {
                            break;
                        }
                    }
                }

                handler.post(() -> {
                    callback.onCandidatesFetched(movies);
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
